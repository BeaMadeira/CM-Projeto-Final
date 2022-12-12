package com.cm.challenge03.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cm.challenge03.HumidityAdapter;
import com.cm.challenge03.MainViewModel;
import com.cm.challenge03.R;
import com.cm.challenge03.TemperatureAdapter;
import com.cm.challenge03.database.entities.Humidity;
import com.cm.challenge03.database.entities.Temperature;
import com.cm.challenge03.ui.main.interfaces.FragmentChanger;

import java.util.List;

// https://developer.android.com/develop/ui/views/notifications/build-notification

public class FirstFragment extends Fragment {
    private MainViewModel mainViewModel;
    private RecyclerView humidityRecyclerView;
    private RecyclerView temperatureRecyclerView;
    private HumidityAdapter humidityAdapter;
    private TemperatureAdapter temperatureAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu);
        Menu menu = toolbar.getMenu();

        MenuItem settings = menu.findItem(R.id.settings);
        settings.setOnMenuItemClickListener(item -> {
            ((FragmentChanger) requireActivity()).replaceFragment(SettingsFragment.class, true);
            return true;
        });

        humidityRecyclerView = view.findViewById(R.id.humidity_list);
        humidityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LiveData<List<Humidity>> humiditiesLiveData = mainViewModel.getHumiditiesList();
        humiditiesLiveData.observe(getViewLifecycleOwner(), humidities -> {
            humidityAdapter = new HumidityAdapter(humidities);
            humidityRecyclerView.setAdapter(humidityAdapter);
        });
        humidityRecyclerView.setAdapter(humidityAdapter);

        temperatureRecyclerView = view.findViewById(R.id.temperature_list);
        temperatureRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LiveData<List<Temperature>> temperaturesLiveData = mainViewModel.getTemperaturesList();
        temperaturesLiveData.observe(getViewLifecycleOwner(), temperatures -> {
            temperatureAdapter = new TemperatureAdapter(temperatures);
            temperatureRecyclerView.setAdapter(temperatureAdapter);
        });
        temperatureRecyclerView.setAdapter(humidityAdapter);
    }
}