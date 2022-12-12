package com.cm.challenge03.database.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.cm.challenge03.database.AppDatabase;
import com.cm.challenge03.database.dao.HumidityDao;
import com.cm.challenge03.database.entities.Humidity;
import com.cm.challenge03.ui.main.interfaces.TaskCallback;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// TODO Handler Post
public class HumidityRepository {
    private final HumidityDao humidityDao;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public HumidityRepository(@NonNull Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        humidityDao = appDatabase.humidityDao();
    }

    public void getHumidities(TaskCallback taskCallback) {
        executor.execute(() -> {
            List<Humidity> humidityList = humidityDao.getHumidities();
            handler.post(() -> {
                taskCallback.onCompletedGetHumidities(humidityList);
            });
        });
    }

    public void insertHumidity(TaskCallback taskCallback, Humidity humidity) {
        executor.execute(() -> {
            Long uid = humidityDao.insertHumidity(humidity);
            List<Humidity> humidityList = humidityDao.getHumidities();
            handler.post(() -> {
                taskCallback.onCompletedInsertHumidity(humidityList);
            });
        });
    }

    public void insertHumidities(Humidity... humidities) {
        executor.execute(() -> {
            List<Long> uids = humidityDao.insertHumidities(humidities);
            List<Humidity> humidityList = humidityDao.getHumidities();
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void updateHumidity(Humidity humidity) {
        executor.execute(() -> {
            Integer n = humidityDao.updateHumidity(humidity);
            List<Humidity> humidityList = humidityDao.getHumidities();
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void updateHumidities(Humidity... humidities) {
        executor.execute(() -> {
            Integer n = humidityDao.updateHumidities(humidities);
            List<Humidity> humidityList = humidityDao.getHumidities();
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void deleteHumidity(Humidity humidity) {
        executor.execute(() -> {
            Integer n = humidityDao.deleteHumidity(humidity);
            List<Humidity> humidityList = humidityDao.getHumidities();
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void deleteHumidities(Humidity... humidities) {
        executor.execute(() -> {
            Integer n = humidityDao.deleteHumidities(humidities);
            List<Humidity> humidityList = humidityDao.getHumidities();
            handler.post(() -> {
                //TODO Callback
            });
        });
    }
}