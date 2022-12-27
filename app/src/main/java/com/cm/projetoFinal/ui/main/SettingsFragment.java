package com.cm.projetoFinal.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import com.cm.projetoFinal.R;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;
import com.cm.projetoFinal.ui.main.interfaces.MQTTInterface;

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
        toolbar.setTitle(R.string.settings);
        toolbar.setNavigationOnClickListener(v -> ((FragmentChanger) requireActivity()).popBackStack());
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