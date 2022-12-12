package com.cm.challenge03.database.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.cm.challenge03.database.AppDatabase;
import com.cm.challenge03.database.dao.TemperatureDao;
import com.cm.challenge03.database.entities.Temperature;
import com.cm.challenge03.ui.main.interfaces.TaskCallback;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// TODO Handler Post
public class TemperatureRepository {
    private final TemperatureDao temperatureDao;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public TemperatureRepository(@NonNull Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        temperatureDao = appDatabase.temperatureDao();
    }

    public void getTemperatures(TaskCallback taskCallback) {
        executor.execute(() -> {
            List<Temperature> temperatureList = temperatureDao.getTemperatures();
            handler.post(() -> {
                taskCallback.onCompletedGetTemperatures(temperatureList);
            });
        });
    }

    public void insertTemperature(TaskCallback taskCallback, Temperature temperature) {
        executor.execute(() -> {
            Long uid = temperatureDao.insertTemperature(temperature);
            List<Temperature> temperatureList = temperatureDao.getTemperatures();
            handler.post(() -> {
                taskCallback.onCompletedInsertTemperature(temperatureList);
            });
        });
    }

    public void insertTemperatures(Temperature... temperatures) {
        executor.execute(() -> {
            List<Long> uids = temperatureDao.insertTemperatures(temperatures);
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void updateTemperature(Temperature temperature) {
        executor.execute(() -> {
            Integer n = temperatureDao.updateTemperature(temperature);
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void updateTemperatures(Temperature... temperatures) {
        executor.execute(() -> {
            Integer n = temperatureDao.updateTemperatures(temperatures);
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void deleteTemperature(Temperature temperature) {
        executor.execute(() -> {
            Integer n = temperatureDao.deleteTemperature(temperature);
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void deleteTemperatures(Temperature... temperatures) {
        executor.execute(() -> {
            Integer n = temperatureDao.deleteTemperatures(temperatures);
            handler.post(() -> {
                //TODO Callback
            });
        });
    }
}