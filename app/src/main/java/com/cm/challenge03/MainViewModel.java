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

// TODO: Implement the ViewModel
public class MainViewModel extends AndroidViewModel implements TaskCallback {
    private final HumidityRepository humidityRepository;
    private final TemperatureRepository temperatureRepository;
    private MutableLiveData<List<Humidity>> humiditiesList;
    private MutableLiveData<List<Temperature>> temperaturesList;

    public MainViewModel(@NonNull Application application) {
        super(application);
        humidityRepository = new HumidityRepository(application);
        temperatureRepository = new TemperatureRepository(application);
    }

    public LiveData<List<Humidity>> getHumiditiesList() {
        if (humiditiesList == null) {
            humiditiesList = new MutableLiveData<>(new ArrayList<>());
            getHumidities(this);
        }
        return humiditiesList;
    }

    public LiveData<List<Temperature>> getTemperaturesList() {
        if (temperaturesList == null) {
            temperaturesList = new MutableLiveData<>(new ArrayList<>());
            getTemperatures(this);
        }
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
    public <T> void onCompletedInsertHumidity(List<T> result) {
        humiditiesList.setValue((List<Humidity>) result);
    }

    @Override
    public <T> void onCompletedGetHumidities(List<T> result) {
        humiditiesList.setValue((List<Humidity>) result);
    }

    @Override
    public <T> void onCompletedInsertTemperature(List<T> result) {
        temperaturesList.setValue((List<Temperature>) result);
    }

    @Override
    public <T> void onCompletedGetTemperatures(List<T> result) {
        temperaturesList.setValue((List<Temperature>) result);
    }
}