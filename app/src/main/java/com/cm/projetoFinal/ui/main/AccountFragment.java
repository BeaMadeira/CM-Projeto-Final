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
import androidx.room.Database;

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

import com.bumptech.glide.Glide;
import com.cm.projetoFinal.MainViewModel;
import com.cm.projetoFinal.R;
//import com.cm.projetoFinal.database.entities.Profile;
import com.cm.projetoFinal.ui.main.interfaces.Authentication;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;
import com.cm.projetoFinal.ui.main.interfaces.RemoteDbInterface;
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
    private static final int PICK_IMAGE = 100;
    ImageButton image_button;
    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
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
                            ByteArrayOutputStream output = new ByteArrayOutputStream();
                            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);
                            byte[] imagebyte = output.toByteArray();
                            String imagestring = Base64.encodeToString(imagebyte, Base64.DEFAULT);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            FirebaseUser user = ((Authentication) requireActivity()).getCurrentUser();
                            FirebaseStorage.getInstance().getReference("images/" + user.getUid()).putBytes(imagebyte)
                                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("DEBUG", "SUCCESS IN ADDING");
                                            } else {
                                                Log.d("DEBUG", "FAILED IN ADDING");
                                            }
                                        }
                                    });


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Glide.with(this)
                                .asBitmap()
                                .load(selectedImageBitmap)
                                .into(image_button);
//                        image_button.setImageBitmap(selectedImageBitmap);
                    }
                }
            });
    private MainViewModel mainViewModel;

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
        EditText username = view.findViewById(R.id.username_edit);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu);
        Menu menu = toolbar.getMenu();


        MenuItem signOut = menu.findItem(R.id.sign_out);
        signOut.setOnMenuItemClickListener(item -> {
            ((Authentication) requireActivity()).signOut();
            return true;
        });

        FirebaseUser user = ((Authentication) requireActivity()).getCurrentUser();
        ((RemoteDbInterface) requireActivity()).getUsername(new TaskCallback() {
            @Override
            public <T> void onSuccess(T result) {
                username.setText((String) result);
            }
        });

        DocumentReference ref = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    username.setText(documentSnapshot.getString("username"));
                }
            }
        });


        Button saveProfile = view.findViewById(R.id.profile_button);
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = ((Authentication) requireActivity()).getCurrentUser();
                Map<String, Object> updates = new HashMap<>();
                updates.put("username", name);
                ref.update(updates);

                ref.set(updates, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                                Log.d("DEBUG","Username added");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // the update failed
                                Log.d("DEBUG", "FAILURE IN ADDING");
                            }
                        });
                ((FragmentChanger) requireActivity()).replaceFragment(FirstFragment.class, true);

            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());


        image_button = view.findViewById(R.id.imageButton);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage.getInstance().getReference("images/" + user.getUid()).getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Glide.with(AccountFragment.this)
                                .asBitmap()
                                .load(bitmap)
                                .into(image_button);
//                        image_button.setImageBitmap(bitmap);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        if(!task.isSuccessful())
                            Log.d("DEBUG", "FAILED IN GETTING");
                        else {
                            Log.d("DEBUG", "SUCCESS IN GETTING");
                        }
                    }
                });


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
}