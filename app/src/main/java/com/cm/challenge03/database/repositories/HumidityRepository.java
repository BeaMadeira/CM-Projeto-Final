package com.cm.challenge03.database.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.cm.challenge03.database.AppDatabase;
import com.cm.challenge03.database.dao.HumidityDao;
import com.cm.challenge03.database.entities.Humidity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HumidityRepository {
    private final HumidityDao humidityDao;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public HumidityRepository(@NonNull Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        humidityDao = appDatabase.humidityDao();
    }

    public void getHumidities() {
        executor.execute(() -> {
            List<Humidity> humidityList = humidityDao.getHumidities();
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void insertHumidity(Humidity humidity) {
        executor.execute(() -> {
            Long uid = humidityDao.insertHumidity(humidity);
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void insertHumidities(Humidity... humidities) {
        executor.execute(() -> {
            List<Long> uids = humidityDao.insertHumidities(humidities);
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void updateHumidity(Humidity humidity) {
        executor.execute(() -> {
            Integer n = humidityDao.updateHumidity(humidity);
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void updateHumidities(Humidity... humidities) {
        executor.execute(() -> {
            Integer n = humidityDao.updateHumidities(humidities);
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void deleteHumidity(Humidity humidity) {
        executor.execute(() -> {
            Integer n = humidityDao.deleteHumidity(humidity);
            handler.post(() -> {
                //TODO Callback
            });
        });
    }

    public void deleteHumidities(Humidity... humidities) {
        executor.execute(() -> {
            Integer n = humidityDao.deleteHumidities(humidities);
            handler.post(() -> {
                //TODO Callback
            });
        });
    }
}