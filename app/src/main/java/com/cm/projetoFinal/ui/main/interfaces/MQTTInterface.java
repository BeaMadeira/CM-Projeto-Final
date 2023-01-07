package com.cm.projetoFinal.ui.main.interfaces;

import android.widget.Toast;

import com.cm.projetoFinal.tictactoe.Position;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MQTTInterface {
    void subscribe(String topic);

    void publish(String topic, String message);

    void publish(String topic, Position position);

    void unsubscribe(String topic);
}