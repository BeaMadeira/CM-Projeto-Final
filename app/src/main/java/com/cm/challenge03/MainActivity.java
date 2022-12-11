package com.cm.challenge03;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cm.challenge03.ui.main.FirstFragment;
import com.cm.challenge03.ui.main.interfaces.FragmentChanger;
import com.cm.challenge03.ui.main.interfaces.MQTTInterface;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

// ARDUINO PROJECT: https://wokwi.com/projects/348786713380782674

public class MainActivity extends AppCompatActivity implements FragmentChanger, MQTTInterface {
    private MainViewModel mViewModel;
    private MQTTHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = getViewModel(MainViewModel.class);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            addFragment(FirstFragment.class, false);
        }

        String clientId = MqttClient.generateClientId();

        helper = new MQTTHelper(getApplicationContext(), clientId, "");

        helper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Toast.makeText(getApplicationContext(), R.string.connected, Toast.LENGTH_SHORT).show();
                // TODO Subscribe to topics based on current Preferences
            }

            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(getApplicationContext(), R.string.connection_lost, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                // TODO Message arrived from topic cm/humidity
                // TODO Messagearrived from topic cm/temperature
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                //
            }
        });
        helper.connect();
    }

    @Override
    public void addFragment(Class<? extends Fragment> fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().add(R.id.container, fragment, null);
        if (addToBackStack) {
            transaction = transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void replaceFragment(Class<? extends Fragment> fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.container, fragment, null);
        if (addToBackStack) {
            transaction = transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public <T extends ViewModel> T getViewModel(Class<T> viewModel) {
        return new ViewModelProvider(this).get(viewModel);
    }

    @Override
    public void subscribe(String topic) {
        if (helper.mqttAndroidClient.isConnected()) {
            helper.subscribeToTopic(topic);
            // TODO
        } else {
            Toast.makeText(getApplication(), R.string.connection_not_established, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void publish(String topic, String message) {
        byte[] encodedPayload;
        if (!helper.mqttAndroidClient.isConnected()) {
            Toast.makeText(getApplication(), R.string.connection_not_established, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // TODO
            encodedPayload = SerializationUtils.serialize(message);
            MqttMessage payload = new MqttMessage(encodedPayload);
            helper.mqttAndroidClient.publish(topic, payload);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribe(String topic) {
        if (helper.mqttAndroidClient.isConnected()) {
            helper.unsubscribeToTopic(topic);
            // TODO
        } else {
            Toast.makeText(getApplication(), R.string.connection_not_established, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.stop();
    }
}