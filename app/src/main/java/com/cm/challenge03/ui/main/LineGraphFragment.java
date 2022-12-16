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

import com.cm.challenge03.MainViewModel;
import com.cm.challenge03.R;
import com.cm.challenge03.database.entities.Humidity;
import com.cm.challenge03.database.entities.Temperature;
import com.cm.challenge03.ui.main.interfaces.FragmentChanger;
import com.cm.challenge03.ui.main.interfaces.TaskCallback;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// TODO: Add charts and plots
// TODO: Change plot type?
// TODO: Select time interval?
public class LineGraphFragment extends Fragment {
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_line_graph, container, false);
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

        LineChart lc = view.findViewById(R.id.lineChart);
        List<Entry> entries = new ArrayList<>();
        TaskCallback tc = new TaskCallback() {

            @Override
            public void onCompletedInsertHumidity(List<Humidity> result) { }
            @Override
            public void onCompletedGetHumidities(List<Humidity> result) { }
            @Override
            public void onCompletedInsertTemperature(List<Temperature> result) { }
            @Override
            public void onCompletedGetTemperatures(List<Temperature> result) {
                result.forEach(
                        temperature -> entries.add(
                                new Entry(
                                        temperature.getTime(),
                                        Float.parseFloat(temperature.getValue().toString()
                                        )
                                )
                        ));

                LineDataSet tempSet = new LineDataSet(entries, "Company 1");
                tempSet.setAxisDependency(YAxis.AxisDependency.LEFT);

                // use the interface ILineDataSet
                List<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(tempSet);
                LineData data = new LineData(dataSets);
                lc.setData(data);

                ValueFormatter formatter = new LargeValueFormatter(){
                    @Override
                    public String getFormattedValue(float value){

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
                        return sdf.format(value);
                    }
                };
                XAxis xAxis = lc.getXAxis();

                xAxis.setGranularity(1f);
                xAxis.setAvoidFirstLastClipping(true);
                xAxis.setValueFormatter(formatter);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularityEnabled(true);

                lc.invalidate(); // refresh
            }
        };
        mainViewModel.getTemperatures(tc);


        /*
        Log.d("DEBUG", temps.toString());





        LineDataSet dataSet = new LineDataSet(entries,"Label");
        LineData lineData = new LineData(dataSet);
        lc.setData(lineData);
        lc.invalidate();*/
    }
}