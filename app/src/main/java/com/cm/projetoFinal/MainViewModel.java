package com.cm.projetoFinal;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cm.projetoFinal.database.entities.Profile;
import com.cm.projetoFinal.database.repositories.ProfileRepository;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallback;

import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.scheduling.Task;

public class MainViewModel extends AndroidViewModel{
    private final ProfileRepository profileRepository;
    private Profile profile;


    public MainViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
    }


    public void createProfile(TaskCallback tc, Profile profile){
        profileRepository.insertProfile(tc,profile);
    }

    public void setProfile(Profile profile){
        this.profile = profile;
    }

    public Profile getProfile(){
        return profile;
    }

    public Profile getAllProfile(TaskCallback tc){

        profileRepository.getProfile(tc);
        return getProfile();
    }

    public void updateProfile(TaskCallback tc, Profile profile) {

        profileRepository.updateProfile(tc,profile);
    }
}