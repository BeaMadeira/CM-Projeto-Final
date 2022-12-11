package com.cm.challenge03.ui.main.interfaces;

public interface MQTTInterface {
    void subscribe(String topic);

    void publish(String topic, String message);

    void unsubscribe(String topic);
}