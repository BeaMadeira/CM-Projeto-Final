package com.cm.challenge03.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cm.challenge03.MainViewModel;
import com.cm.challenge03.R;
import com.cm.challenge03.database.entities.Humidity;
import com.cm.challenge03.database.entities.Temperature;
import com.cm.challenge03.ui.main.interfaces.FragmentChanger;
import com.cm.challenge03.ui.main.interfaces.TaskCallback;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BarGraphFragment extends Fragment {
    private MainViewModel mainViewModel;

    public BarGraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bar_graph, container, false);
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

        BarChart chart = (BarChart) view.findViewById(R.id.barChart);
        List<BarEntry> entries = new ArrayList<>();
        TaskCallback tc = new TaskCallback() {

            @Override
            public void onCompletedInsertHumidity(List<Humidity> result) { }
            @Override
            public void onCompletedGetHumidities(List<Humidity> result) { }
            @Override
            public void onCompletedInsertTemperature(List<Temperature> result) { }
            @Override
            public void onCompletedGetTemperatures(List<Temperature> result) {
               /* result.forEach(
                        temperature -> entries.add(
                                new BarEntry(
                                        temperature.getTime(),
                                        Float.parseFloat(temperature.getValue().toString()
                                        )
                                )
                        ));*/






                Date date = new Date();
                Timestamp timestamp2 = new Timestamp(date.getTime());
                long currentTime = System.currentTimeMillis();
                long teste = 1000000;
                BarEntry entry = new BarEntry(teste, 30f);

                entries.add(entry);


                Toast.makeText(view.getContext(),""+currentTime, Toast.LENGTH_SHORT).show();


                BarDataSet tempSet = new BarDataSet(entries, "Company 1");
                tempSet.setAxisDependency(YAxis.AxisDependency.LEFT);

                // use the interface IBarDataSet
                List<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(tempSet);
                BarData data = new BarData(dataSets);
                data.setBarWidth(0.4f);
                chart.setData(data);
                chart.setFitBars(true);

                /*ValueFormatter formatter = new ValueFormatter(){
                    @Override
                    public String getFormattedValue(float value){

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        return sdf.format(value);
                    }
                };
                XAxis xAxis = chart.getXAxis();

                xAxis.setGranularity(1f);
                xAxis.setAvoidFirstLastClipping(true);
                xAxis.setValueFormatter(formatter);*/
                chart.invalidate(); // refresh
            }
        };
        mainViewModel.getTemperatures(tc);
    }

}