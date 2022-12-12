package com.cm.challenge03;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cm.challenge03.database.entities.Humidity;
import com.cm.challenge03.database.entities.Temperature;

import java.util.Date;
import java.util.List;

// https://developer.android.com/reference/android/text/format/DateUtils

public class TemperatureAdapter extends RecyclerView.Adapter<TemperatureAdapter.ViewHolder> {
    private final List<Temperature> temperatureList;

    public TemperatureAdapter(List<Temperature> courseModelArrayList) {
        this.temperatureList = courseModelArrayList;
    }

    @NonNull
    @Override
    public TemperatureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemperatureAdapter.ViewHolder holder, int position) {
        Temperature temperature = temperatureList.get(position);
        holder.value.setText(temperature.getValue().toString());
        holder.date.setText(DateUtils.getRelativeTimeSpanString(temperature.getDate().getTime(), (new Date()).getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE));
    }

    @Override
    public int getItemCount() {
        return temperatureList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView value;
        private final TextView date;

        public ViewHolder(@NonNull View view) {
            super(view);
            value = view.findViewById(R.id.value);
            date = view.findViewById(R.id.date);
        }
    }
}