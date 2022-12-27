package com.cm.projetoFinal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cm.projetoFinal.database.repositories.ProfileRepository;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallback;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel{
    private final ProfileRepository profileRepository;



    public MainViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
    }

}