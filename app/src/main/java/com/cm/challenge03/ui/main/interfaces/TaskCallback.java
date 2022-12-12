package com.cm.challenge03.ui.main.interfaces;

import java.util.List;

public interface TaskCallback {
    <T> void onCompletedInsertHumidity(List<T> result);

    <T> void onCompletedGetHumidities(List<T> result);

    <T> void onCompletedInsertTemperature(List<T> result);

    <T> void onCompletedGetTemperatures(List<T> result);
}