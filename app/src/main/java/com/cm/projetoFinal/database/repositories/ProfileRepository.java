package com.cm.projetoFinal.database.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.cm.projetoFinal.database.AppDatabase;
import com.cm.projetoFinal.database.dao.ProfileDao;
import com.cm.projetoFinal.database.entities.Profile;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallback;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// TODO Handler Post
public class ProfileRepository {
    private final ProfileDao profileDao;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public ProfileRepository(@NonNull Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        profileDao = appDatabase.profileDao();
    }

    public void getProfile(TaskCallback taskCallback) {
        executor.execute(() -> {
            Profile profiles = profileDao.getProfile();
            handler.post(() -> {
                taskCallback.onSuccess(profiles);
            });
        });
    }

    public void insertProfile(TaskCallback taskCallback, Profile profile) {
        executor.execute(() -> {
            Long uid = profileDao.insertProfile(profile);
            Profile resprofile = profileDao.getProfileById(uid);
            handler.post(() -> {
                taskCallback.onSuccess(resprofile);
            });
        });
    }


    public void updateProfile(TaskCallback taskCallback, Profile profile) {
        executor.execute(() -> {
            Integer n = profileDao.updateProfile(profile);
            Profile resprofile = profileDao.getProfile();
            handler.post(() -> {
                taskCallback.onSuccess(resprofile);
            });
        });
    }

}