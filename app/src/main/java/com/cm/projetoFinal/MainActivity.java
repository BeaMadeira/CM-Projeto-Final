package com.cm.projetoFinal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.cm.projetoFinal.ui.main.FirstFragment;
import com.cm.projetoFinal.ui.main.FirstFragmentCreateProfile;
import com.cm.projetoFinal.ui.main.MatchingFragment;
import com.cm.projetoFinal.ui.main.MultiPlayerFragment;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;
import com.cm.projetoFinal.ui.main.interfaces.MQTTInterface;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

// ARDUINO PROJECT: https://wokwi.com/projects/348786713380782674

// TODO: Refactor code to make it more modular and readable
public class MainActivity extends AppCompatActivity implements FragmentChanger, MQTTInterface {
    private MainViewModel mainViewModel;
    private MQTTHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = getViewModel(MainViewModel.class);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean("firstrun", true)) {
            Toast.makeText(getApplicationContext(), "Welcome to the app!", Toast.LENGTH_LONG).show();
            if (savedInstanceState == null) {
                addFragment(FirstFragmentCreateProfile.class, false);
            }
        } else {
            if (savedInstanceState == null)
                addFragment(FirstFragment.class, false);
        }

        String clientId = MqttClient.generateClientId();

        helper = new MQTTHelper(getApplicationContext(), clientId, "");

        helper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Toast.makeText(getApplicationContext(), R.string.connected, Toast.LENGTH_SHORT).show();

                // TODO Get user profile uid
                //String uid = mainViewModel.getProfile().getUid().toString();
                String uid = "user1";
                // Subscribe to topic tiktaktoe/<uid>
                subscribe(getResources().getString(R.string.tiktaktoe).concat("/").concat(uid));
            }

            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(getApplicationContext(), R.string.connection_lost, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                // TODO Get user profile uid
                //String uid = mainViewModel.getProfile().getUid().toString();
                String uid = "user1";
                if (topic.equals(getResources().getString(R.string.tiktaktoe).concat("/").concat(uid))) {
                    String content = new String(message.getPayload());
                    mainViewModel.setOponentTopic(content);
                    Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
                    popBackStack();
                    replaceFragment(MultiPlayerFragment.class, true);
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
        } else {
            Toast.makeText(getApplication(), R.string.connection_not_established, Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(getApplication(), R.string.connection_not_established, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.stop();
    }
}