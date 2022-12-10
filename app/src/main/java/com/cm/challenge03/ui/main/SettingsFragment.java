package com.cm.challenge03.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cm.challenge03.R;
import com.cm.challenge03.ui.main.interfaces.FragmentChanger;

public class SettingsFragment extends PreferenceFragmentCompat {
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);
        // TODO: Use the ViewModel
    }

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
}