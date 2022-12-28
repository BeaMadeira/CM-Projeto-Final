package com.cm.projetoFinal;

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

import com.cm.projetoFinal.R;
import com.cm.projetoFinal.database.entities.Profile;
import com.cm.projetoFinal.ui.main.FirstFragment;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;
import com.cm.projetoFinal.ui.main.interfaces.MQTTInterface;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

// ARDUINO PROJECT: https://wokwi.com/projects/348786713380782674

// TODO: Refactor code to make it more modular and readable
public class MainActivity extends AppCompatActivity implements FragmentChanger, MQTTInterface {
    private final static String CHANNEL_ID = "CHALLENGE03";
    private int notificationId = 0;
    private MainViewModel mainViewModel;
    private MQTTHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = getViewModel(MainViewModel.class);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getBoolean("firstrun", true)) {
            Toast.makeText(getApplicationContext(), "Welcome to the app!", Toast.LENGTH_LONG).show();
            sharedPreferences.edit().putBoolean("firstrun", false).apply();
        }
        //createNotificationChannel();
        if (savedInstanceState == null) {
            addFragment(FirstFragment.class, false);
        }

        String clientId = MqttClient.generateClientId();

        helper = new MQTTHelper(getApplicationContext(), clientId, "");

        helper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                /*Toast.makeText(getApplicationContext(), R.string.connected, Toast.LENGTH_SHORT).show();
                subscribe(getResources().getString(R.string.led_status_topic));
                publish(getResources().getString(R.string.led_topic), Boolean.toString(sharedPreferences.getBoolean("led", false)));
                if (sharedPreferences.getBoolean("humidity", true)) {
                    subscribe(getResources().getString(R.string.humidity_topic));
                }
                if (sharedPreferences.getBoolean("temperature", true)) {
                    subscribe(getResources().getString(R.string.temperature_topic));
                }*/
            }

            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(getApplicationContext(), R.string.connection_lost, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
               /* if (topic.equals(getResources().getString(R.string.led_status_topic))) {
                    String content = new String(message.getPayload());
                    if (content.equals("true")) {
                        Toast.makeText(getApplicationContext(), R.string.led_on, Toast.LENGTH_SHORT).show();
                    } else if (content.equals("false")) {
                        Toast.makeText(getApplicationContext(), R.string.led_off, Toast.LENGTH_SHORT).show();
                    }
                } else if (topic.equals(getResources().getString(R.string.humidity_topic))) {
                    // Message arrived from topic cm/humidity
                    Toast.makeText(getApplicationContext(), new String(message.getPayload()), Toast.LENGTH_SHORT).show();
                    Double value = Double.parseDouble(new String(message.getPayload()));
                    mainViewModel.insertHumidity(new Humidity(new Date(), value));

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Double thresholdValue = Double.parseDouble(sharedPreferences.getString("humidity_notification", "0"));

                    if (value >= thresholdValue) {
                        // Issue Notification
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.outline_water_drop_24)
                                .setContentTitle("Humidity")
                                .setContentText("Humidity above threshold value " + thresholdValue)
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        notificationManager.notify(notificationId++, builder.build());
                    }
                } else if (topic.equals(getResources().getString(R.string.temperature_topic))) {
                    // Message arrived from topic cm/temperature
                    Toast.makeText(getApplicationContext(), new String(message.getPayload()), Toast.LENGTH_SHORT).show();
                    Double value = Double.parseDouble(new String(message.getPayload()));
                    mainViewModel.insertTemperature(new Temperature(new Date(), value));

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Double thresholdValue = Double.parseDouble(sharedPreferences.getString("temperature_notification", "0"));

                    if (value >= thresholdValue) {
                        // Issue Notification
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.outline_thermostat_24)
                                .setContentTitle("Temperature")
                                .setContentText("Temperature above threshold value " + thresholdValue)
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        notificationManager.notify(notificationId++, builder.build());
                    }
                }*/
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                //
            }
        });
        helper.connect();
    }

/*    // https://developer.android.com/develop/ui/views/notifications/build-notification
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/


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
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .replace(R.id.container, fragment, null);
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