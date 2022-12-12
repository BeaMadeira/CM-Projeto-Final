package com.cm.challenge03;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cm.challenge03.database.entities.Humidity;
import com.cm.challenge03.database.entities.Temperature;
import com.cm.challenge03.database.repositories.HumidityRepository;
import com.cm.challenge03.database.repositories.TemperatureRepository;
import com.cm.challenge03.ui.main.interfaces.TaskCallback;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel implements TaskCallback {
    private final HumidityRepository humidityRepository;
    private final TemperatureRepository temperatureRepository;
    private final MutableLiveData<List<Humidity>> humiditiesList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Temperature>> temperaturesList = new MutableLiveData<>(new ArrayList<>());

    public MainViewModel(@NonNull Application application) {
        super(application);
        humidityRepository = new HumidityRepository(application);
        temperatureRepository = new TemperatureRepository(application);
    }

    public LiveData<List<Humidity>> getHumiditiesList() {
        return humiditiesList;
    }

    public LiveData<List<Temperature>> getTemperaturesList() {
        return temperaturesList;
    }

    public void insertHumidity(Humidity humidity) {
        humidityRepository.insertHumidity(this, humidity);
    }

    public void insertHumidity(TaskCallback taskCallback, Humidity humidity) {
        humidityRepository.insertHumidity(taskCallback, humidity);
    }

    public void getHumidities() {
        humidityRepository.getHumidities(this);
    }

    public void getHumidities(TaskCallback taskCallback) {
        humidityRepository.getHumidities(taskCallback);
    }

    public void insertTemperature(Temperature temperature) {
        temperatureRepository.insertTemperature(this, temperature);
    }

    public void insertTemperature(TaskCallback taskCallback, Temperature temperature) {
        temperatureRepository.insertTemperature(taskCallback, temperature);
    }

    public void getTemperatures() {
        temperatureRepository.getTemperatures(this);
    }

    public void getTemperatures(TaskCallback taskCallback) {
        temperatureRepository.getTemperatures(taskCallback);
    }

    @Override
    public void onCompletedInsertHumidity(List<Humidity> result) {
        humiditiesList.setValue(result);
    }

    @Override
    public void onCompletedGetHumidities(List<Humidity> result) {
        humiditiesList.setValue(result);
    }

    @Override
    public void onCompletedInsertTemperature(List<Temperature> result) {
        temperaturesList.setValue(result);
    }

    @Override
    public void onCompletedGetTemperatures(List<Temperature> result) {
        temperaturesList.setValue(result);
    }
}