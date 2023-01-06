package com.cm.projetoFinal.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallback;

import java.io.IOException;
import java.util.List;

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


        image_button = view.findViewById(R.id.imageButton);
        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
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
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        image_button.setImageBitmap(selectedImageBitmap);
                    }
                }
            });
}