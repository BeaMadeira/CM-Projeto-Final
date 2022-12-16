package com.cm.challenge03.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

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
import java.text.ParseException;
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

        XAxis xAxis = lc.getXAxis();
        List<ILineDataSet> dataSets = new ArrayList<>();

        TaskCallback tc = new TaskCallback() {

            @Override
            public void onCompletedInsertHumidity(List<Humidity> result) { }
            @Override
            public void onCompletedGetHumidities(List<Humidity> result) {
                List<Entry> entries = new ArrayList<>();
                result.forEach(
                        humidity -> entries.add(
                                new Entry(
                                        humidity.getTime(),
                                        Float.parseFloat(humidity.getValue().toString()
                                        )
                                )
                        ));

                LineDataSet humSet = new LineDataSet(entries, "Humidity");
                humSet.setLineWidth(3f);
                humSet.setCircleRadius(6f);
                humSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

                // use the interface ILineDataSet
                dataSets.add(humSet);
                LineData data = new LineData(dataSets);
                lc.setData(data);

                ValueFormatter formatter = new LargeValueFormatter(){
                    @Override
                    public String getFormattedValue(float value){

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
                        return sdf.format(value);
                    }
                };

                xAxis.setGranularity(1f);
                xAxis.setAvoidFirstLastClipping(true);
                xAxis.setValueFormatter(formatter);
                lc.setDragEnabled(true);
                lc.invalidate(); // refresh
                // Disable the vertical grid lines

            }
            @Override
            public void onCompletedInsertTemperature(List<Temperature> result) { }
            @Override
            public void onCompletedGetTemperatures(List<Temperature> result) {
                List<Entry> entries = new ArrayList<>();

                result.forEach(
                        temperature -> entries.add(
                                new Entry(
                                        temperature.getTime(),
                                        Float.parseFloat(temperature.getValue().toString()
                                        )
                                )
                        ));

                LineDataSet tempSet = new LineDataSet(entries, "Temperature");
                tempSet.setLineWidth(3f);
                tempSet.setCircleRadius(6f);
                tempSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                tempSet.setCircleColor(Color.RED);
                tempSet.setColor(Color.RED);

                // use the interface ILineDataSet

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

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Long currentDayTimestamp = null;
                try {
                    currentDayTimestamp = sdf.parse(sdf.format(new Date())).getTime();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                xAxis.setGranularity(1f);
                xAxis.setAvoidFirstLastClipping(true);
                xAxis.setValueFormatter(formatter);
                float middleground = (currentDayTimestamp+(result.get(0).getTime()))*0.5f;
                middleground += 5000000f;
                xAxis.setAxisMinimum(middleground);

                lc.setDragEnabled(true);

                lc.setVisibleXRangeMaximum(result.get(0).getTime()*0.000007f);

                lc.invalidate(); // refresh
                // Disable the vertical grid lines

            }
        };



        mainViewModel.getHumidities(tc);


        Switch sw = (Switch)  view.findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Toast.makeText(getContext(),"checked",Toast.LENGTH_SHORT).show();
                    mainViewModel.getTemperatures(tc);

                } else {
                    // The toggle is disabled
                    Toast.makeText(getContext(),"not",Toast.LENGTH_SHORT).show();
                    LineData data = lc.getData();
                    ILineDataSet linedataset = data.getDataSetByLabel("Temperature",false);
                    data.removeDataSet(linedataset);
                    lc.setData(data);
                    lc.invalidate();

                }
            }
        });

        xAxis.setGridLineWidth(1f);
        lc.getAxisLeft().setDrawGridLines(false);
        lc.getAxisRight().setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);

    }
}