package com.cm.projetoFinal.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cm.projetoFinal.MainViewModel;
import com.cm.projetoFinal.R;
//import com.cm.projetoFinal.database.entities.Profile;
import com.cm.projetoFinal.ui.main.interfaces.Authentication;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.util.Base64;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountFragment extends Fragment {
    private MainViewModel mainViewModel;
    private static final int PICK_IMAGE = 100;
    ImageButton image_button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu);
        Menu menu = toolbar.getMenu();

        MenuItem signOut = menu.findItem(R.id.sign_out);
        signOut.setOnMenuItemClickListener(item -> {
            ((Authentication) requireActivity()).signOut();
            return true;
        });

        /* butao save profile -->
            // sharedPreferences.edit().putBoolean("firstrun", false).apply();
        EditText username = view.findViewById(R.id.textView4);

        mainViewModel.getAllProfile(new TaskCallback() {
            @Override
            public <T> void onSuccess(T result) {
                List<Profile> profiles = (List<Profile>) result;
                if(profiles.size()==1){
                    mainViewModel.setProfile(profiles.get(0));
                    username.setText(mainViewModel.getProfile().getUsername());
                }
            }
        });




        Button saveProfile = view.findViewById(R.id.profile_button);
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sharedPreferences.edit().putBoolean("firstrun", false).apply();
                //SharedPreferences preferences = AccountFragment.this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
                //preferences.edit().putBoolean("firstrun", false).apply();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                String name=username.getText().toString();

                //when the result from all profiles arrives
                TaskCallback tc = new TaskCallback() {
                    @Override
                    public <T> void onSuccess(T result) {
                        List<Profile> profiles = (List<Profile>) result;
                        //we check if theres only one profile, if there is we update it
                        if(profiles.size()==1){
                            Profile profile = profiles.get(0);
                            mainViewModel.setProfile(profile);
                            profile.setUsername(name);
                            mainViewModel.updateProfile(new TaskCallback() {
                                @Override
                                public <T> void onSuccess(T result) {
                                    //we save the updated profile
                                    List<Profile> profiles = (List<Profile>) result;
                                    if(profiles.size()==1)
                                        mainViewModel.setProfile(profiles.get(0));
                                    Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                                }
                            }, profile);
                            //if there is no profiles we create a new one and save it
                        } else if(profiles.size()==0) {
                            Profile profile = new Profile();
                            profile.setUsername(name);
                            mainViewModel.createProfile(new TaskCallback() {
                                @Override
                                public <T> void onSuccess(T result) {
                                    Toast.makeText(getContext(), "Profile made", Toast.LENGTH_SHORT).show();
                                }
                            }, profile);
                        }
                    }
                };
                //we check for all profiles
                mainViewModel.getAllProfile(tc);

                sharedPreferences.edit().putBoolean("firstrun", false).apply();

                ((FragmentChanger) requireActivity()).replaceFragment(FirstFragment.class, true);

                //bd and check if id is there already


                }
        });*/

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());


        image_button = view.findViewById(R.id.imageButton);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = ((Authentication) requireActivity()).getCurrentUser();
        FirebaseStorage.getInstance().getReference("images/"+user.getUid()).getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        image_button.setImageBitmap(bitmap);
                    }
                });
/*

        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot!=null && documentSnapshot.exists()){
                            String imgstring =documentSnapshot.getString("ImageString");
                            if(imgstring!=null){
                                byte [] stringbyte = Base64.decode(imgstring, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(stringbyte,0,stringbyte.length);
                                image_button.setImageBitmap(bitmap);
                            }
                        }
                    }
                });
*/





        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity  = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {

                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), selectedImageUri);
                            ByteArrayOutputStream output= new ByteArrayOutputStream();
                            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG,100,output);
                            byte [] imagebyte = output.toByteArray();
                            String imagestring = Base64.encodeToString(imagebyte, Base64.DEFAULT);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            FirebaseUser user = ((Authentication) requireActivity()).getCurrentUser();
                            FirebaseStorage.getInstance().getReference("images/"+user.getUid()).putBytes(imagebyte)
                                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                Log.d("DEBUG","SUCCESS IN ADDING");

                                            }
                                        }
                                    });


                           /* SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("foto_perfil",imagestring);
                            editor.apply();*/
                            //FirebaseUser user = ((Authentication) requireActivity()).getCurrentUser();
                            /*String userid = user.getUid();

                            DocumentReference ref = FirebaseFirestore.getInstance().collection("user").document(userid);
                            Map<String,Object> updates = new HashMap<>();
                            updates.put("imageProfile",imagestring);
                            ref.update(updates);*/
                            //FirebaseUser user = ((Authentication) requireActivity()).getCurrentUser();

                          /*  Map<String, Object> updates = new HashMap<>();
                            updates.put("ImageString", imagestring);

                            db.collection("users").document(user.getUid())
                                    .set(updates, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // the update was successful
                                            Log.d("DEBUG","SUCCESS IN ADDING");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // the update failed
                                            Log.d("DEBUG","FAILURE IN ADDING");
                                        }
                                    });
*/

                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        image_button.setImageBitmap(selectedImageBitmap);
                    }
                }
            });
}