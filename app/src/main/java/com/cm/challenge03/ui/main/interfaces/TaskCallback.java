package com.cm.challenge03.ui.main.interfaces;

import com.cm.challenge03.database.entities.Humidity;

import java.util.List;

public interface TaskCallback {
    void onCompletedInsertHumidity(List<Humidity> result);

    void onCompletedGetHumidities(List<Humidity> result);

    void onCompletedInsertTemperature(List<Temperature> result);

    void onCompletedGetTemperatures(List<Temperature> result);

}