package com.cm.challenge03.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import com.cm.challenge03.R;
import com.cm.challenge03.ui.main.interfaces.FragmentChanger;
import com.cm.challenge03.ui.main.interfaces.MQTTInterface;

import java.util.Objects;

// TODO: Allow user to change notifications' priority
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.settings_toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.settings_menu);
        Menu menu = toolbar.getMenu();

        MenuItem settings = menu.findItem(R.id.settings_back);
        settings.setOnMenuItemClickListener(item -> {
            ((FragmentChanger) requireActivity()).popBackStack();
            return true;
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "led":
                String message = Boolean.toString(sharedPreferences.getBoolean(key, false));
                ((MQTTInterface) requireActivity()).publish(getResources().getString(R.string.led_topic), message);
                break;
            case "humidity":
                if (sharedPreferences.getBoolean(key, true)) {
                    ((MQTTInterface) requireActivity()).subscribe(getResources().getString(R.string.humidity_topic));
                } else {
                    ((MQTTInterface) requireActivity()).unsubscribe(getResources().getString(R.string.humidity_topic));
                }
                break;
            case "temperature":
                if (sharedPreferences.getBoolean(key, true)) {
                    ((MQTTInterface) requireActivity()).subscribe(getResources().getString(R.string.temperature_topic));
                } else {
                    ((MQTTInterface) requireActivity()).unsubscribe(getResources().getString(R.string.temperature_topic));
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).unregisterOnSharedPreferenceChangeListener(this);
    }
}