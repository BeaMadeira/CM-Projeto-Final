package com.cm.challenge03;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.cm.challenge03.database.entities.Humidity;
import com.cm.challenge03.database.entities.Temperature;
import com.cm.challenge03.ui.main.FirstFragment;
import com.cm.challenge03.ui.main.interfaces.FragmentChanger;
import com.cm.challenge03.ui.main.interfaces.MQTTInterface;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Date;

// ARDUINO PROJECT: https://wokwi.com/projects/348786713380782674

public class MainActivity extends AppCompatActivity implements FragmentChanger, MQTTInterface {
    private MainViewModel mainViewModel;
    private MQTTHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = getViewModel(MainViewModel.class);
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
                subscribe(getResources().getString(R.string.led_status_topic));
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                publish(getResources().getString(R.string.led_topic), Boolean.toString(sharedPreferences.getBoolean("led", false)));
                if (sharedPreferences.getBoolean("humidity", true)) {
                    subscribe(getResources().getString(R.string.humidity_topic));
                }
                if (sharedPreferences.getBoolean("temperature", true)) {
                    subscribe(getResources().getString(R.string.temperature_topic));
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(getApplicationContext(), R.string.connection_lost, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                if (topic.equals(getResources().getString(R.string.led_status_topic))) {
                    String content = new String(message.getPayload());
                    if (content.equals("true")) {
                        Toast.makeText(getApplicationContext(), R.string.led_on, Toast.LENGTH_SHORT).show();
                    } else if (content.equals("false")) {
                        Toast.makeText(getApplicationContext(), R.string.led_off, Toast.LENGTH_SHORT).show();
                    }
                } else if (topic.equals(getResources().getString(R.string.humidity_topic))) {
                    // TODO Message arrived from topic cm/humidity
                    Toast.makeText(getApplicationContext(), new String(message.getPayload()), Toast.LENGTH_SHORT).show();
                    Double value = Double.parseDouble(new String(message.getPayload()));
                    mainViewModel.insertHumidity(new Humidity(new Date(), value));
                } else if (topic.equals(getResources().getString(R.string.temperature_topic))) {
                    // TODO Message arrived from topic cm/temperature
                    Toast.makeText(getApplicationContext(), new String(message.getPayload()), Toast.LENGTH_SHORT).show();
                    Double value = Double.parseDouble(new String(message.getPayload()));
                    mainViewModel.insertTemperature(new Temperature(new Date(), value));
                }
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
        } else {
            Toast.makeText(getApplication(), R.string.connection_not_established, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void publish(String topic, String message) {
        if (!helper.mqttAndroidClient.isConnected()) {
            Toast.makeText(getApplication(), R.string.connection_not_established, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            helper.mqttAndroidClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribe(String topic) {
        if (helper.mqttAndroidClient.isConnected()) {
            helper.unsubscribeToTopic(topic);
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