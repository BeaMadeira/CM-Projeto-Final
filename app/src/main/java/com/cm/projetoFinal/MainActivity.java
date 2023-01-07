package com.cm.projetoFinal;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cm.projetoFinal.database.entities.Topic;
import com.cm.projetoFinal.tictactoe.Position;
import com.cm.projetoFinal.ui.main.FirstFragment;
import com.cm.projetoFinal.ui.main.LoginFragment;
import com.cm.projetoFinal.ui.main.MultiPlayerFragment;
import com.cm.projetoFinal.ui.main.interfaces.Authentication;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;
import com.cm.projetoFinal.ui.main.interfaces.MQTTInterface;
import com.cm.projetoFinal.ui.main.interfaces.RemoteDbInterface;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallback;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallbackTopic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

// ARDUINO PROJECT: https://wokwi.com/projects/348786713380782674

// TODO: Refactor code to make it more modular and readable
// TODO: Authentication using Firebase

public class MainActivity extends AppCompatActivity implements FragmentChanger, MQTTInterface, TaskCallbackTopic, Authentication, RemoteDbInterface {
    private MainViewModel mainViewModel;
    private MQTTHelper helper;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    private int numTopics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = getViewModel(MainViewModel.class);
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        /*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean("firstrun", true)) {
            Toast.makeText(getApplicationContext(), "Welcome to the app!", Toast.LENGTH_LONG).show();
            if (savedInstanceState == null) {
                addFragment(FirstFragmentCreateProfile.class, false);
            }
        } else {
            if (savedInstanceState == null)
                addFragment(FirstFragment.class, false);
        }*/

        if (isSignIn()) {
            if (savedInstanceState == null) {
                addFragment(FirstFragment.class, false);
            }
        }
        else {
            if (savedInstanceState == null) {
                addFragment(LoginFragment.class, false);
            }
        }

        String clientId = MqttClient.generateClientId();

        helper = new MQTTHelper(getApplicationContext(), clientId, "");

        helper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Toast.makeText(getApplicationContext(), R.string.connected, Toast.LENGTH_SHORT).show();

                // TODO Subscribe to topic tiktaktoe/<uid>
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();
                    mainViewModel.getAllTopics(MainActivity.this);
                    subscribe(getResources().getString(R.string.tiktaktoe).concat("/").concat(uid));
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(getApplicationContext(), R.string.connection_lost, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();
                    if (topic.equals(getResources().getString(R.string.tiktaktoe).concat("/").concat(uid))) {
                        if (mainViewModel.isPlaying()) {
                            // The content is a position on the board
                            Position position = SerializationUtils.deserialize(message.getPayload());
                            mainViewModel.playOpponent(position);
                            MultiPlayerFragment multiPlayerFragment = (MultiPlayerFragment) getSupportFragmentManager().findFragmentByTag("MULTIPLAYER");
                            if (multiPlayerFragment != null && multiPlayerFragment.isVisible()) {
                                multiPlayerFragment.updateBoard(mainViewModel.getBoard());
                                multiPlayerFragment.enableButtons();
                                multiPlayerFragment.verifyGameCondition();
                            }
                        }
                        else {
                            // The content the matching result
                            String content = new String(message.getPayload());
                            try {
                                JSONObject match = new JSONObject(content);
                                mainViewModel.setOponentTopic(match.getString("topic"));
                                mainViewModel.setSymbol(match.getString("symbol").charAt(0));
                                mainViewModel.setPlaying(true);
                                popBackStack();
                                replaceFragment(MultiPlayerFragment.class, "MULTIPLAYER", true);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                //
            }
        });
        helper.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSignIn();
    }

    @Override
    public void addFragment(Class<? extends Fragment> fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().add(R.id.container, fragment, null);
        if (addToBackStack) {
            transaction = transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void replaceFragment(Class<? extends Fragment> fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .replace(R.id.container, fragment, null);
        if (addToBackStack) {
            transaction = transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void addFragment(Class<? extends Fragment> fragment, String tag, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().add(R.id.container, fragment, null, tag);
        if (addToBackStack) {
            transaction = transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void replaceFragment(Class<? extends Fragment> fragment, String tag, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .replace(R.id.container, fragment, null, tag);
        if (addToBackStack) {
            transaction = transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public <T extends ViewModel> T getViewModel(Class<T> viewModel) {
        return new ViewModelProvider(this).get(viewModel);
    }

    @Override
    public void subscribe(String topic) {
        if (helper.mqttAndroidClient.isConnected()) {
            helper.subscribeToTopic(topic);
            mainViewModel.insertTopic(this, new Topic(topic));
        } else {
            Toast.makeText(getApplication(), R.string.connection_not_established, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void publish(String topic, Position position) {
        byte[] encodedPayload;
        if (!helper.mqttAndroidClient.isConnected()) {
            Toast.makeText(getApplication(), R.string.connection_not_established, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            encodedPayload = SerializationUtils.serialize(position);
            MqttMessage message = new MqttMessage(encodedPayload);
            helper.mqttAndroidClient.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publish(String topic, String message) {
        if (!helper.mqttAndroidClient.isConnected()) {
            Toast.makeText(getApplication(), R.string.connection_not_established, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            helper.mqttAndroidClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribe(String topic) {
        if (helper.mqttAndroidClient.isConnected()) {
            helper.unsubscribeToTopic(topic);
            mainViewModel.deleteTopicByName(this, topic);
        } else {
            Toast.makeText(getApplication(), R.string.connection_not_established, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.stop();
    }

    @Override
    public void createAccount(String email, String password) {
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void createAccount(String email, String password, Map<String, Object> data) {
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                db.collection("users").document(user.getUid())
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("DEBUG", "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("DEBUG", "Error writing document", e);
                                            }
                                        });
                                updateUI(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void sendEmailVerification() {
        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Email sent
                        }
                    });
        }
    }

    @Override
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void signOut() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // TODO Unsubscribe topic tiktaktoe/<uid>
            String uid = user.getUid();
            unsubscribe(getResources().getString(R.string.tiktaktoe).concat("/").concat(uid));
            mAuth.signOut();
            updateUI(null);
        }
    }

    @Override
    public void reload() {

    }

    @Override
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            replaceFragment(FirstFragment.class, true);
            // TODO Subscribe to topic tiktaktoe/<uid>
            String uid = user.getUid();
            subscribe(getResources().getString(R.string.tiktaktoe).concat("/").concat(uid));
        }
        else {
            replaceFragment(LoginFragment.class, false);
        }
    }

    @Override
    public void checkSignIn() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    @Override
    public boolean isSignIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    @Override
    public void addWin() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> data = new HashMap<>();
                                    Long win = document.getLong("win");
                                    if (win == null) {
                                        data.put("win", 1);
                                    }
                                    else {
                                        data.put("win", win + 1);
                                    }
                                    db.collection("users").document(user.getUid())
                                            .set(data, SetOptions.merge());
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void addLoss() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> data = new HashMap<>();
                                    Long loss = document.getLong("loss");
                                    if (loss == null) {
                                        data.put("loss", 1);
                                    }
                                    else {
                                        data.put("loss", loss + 1);
                                    }
                                    db.collection("users").document(user.getUid())
                                            .set(data, SetOptions.merge());
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void addDraw() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> data = new HashMap<>();
                                    Long draw = document.getLong("draw");
                                    if (draw == null) {
                                        data.put("draw", 1);
                                    }
                                    else {
                                        data.put("draw", draw + 1);
                                    }
                                    db.collection("users").document(user.getUid())
                                            .set(data, SetOptions.merge());
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void getUsername(TaskCallback taskCallback) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    taskCallback.onSuccess(document.getString("username"));
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public <T> void onCompletedGetAllTopics(List<T> result) {
        //TODO: Unchecked Cast
        List<Topic> tempList = (List<Topic>) result;
        for (Topic topic : tempList) {
            helper.subscribeToTopic(topic.getTopic());
        }
    }

    @Override
    public <T> void onCompletedGetTopics(List<T> result) {
        List<Topic> tempList = (List<Topic>) result;

    }

    @Override
    public <T> void onCompletedGetTopic(T result) {

    }

    @Override
    public <T> void onCompletedInsertTopics(List<T> result) {
        Toast.makeText(getApplication(), R.string.topic_subscribed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public <T> void onCompletedDeleteTopicByName(T result) {
        Toast.makeText(getApplication(), R.string.topic_unsubscribed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNullPointer() {
        Toast.makeText(getApplication(), R.string.topic_unsubscribed_unsbbed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSQLiteConstraintException() {
        Toast.makeText(getApplication(), R.string.topic_subscribed_sbbed, Toast.LENGTH_SHORT).show();
    }
}