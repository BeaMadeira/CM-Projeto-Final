package com.cm.challenge03;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cm.challenge03.database.entities.Humidity;

import java.util.Date;
import java.util.List;

// https://developer.android.com/reference/android/text/format/DateUtils

public class HumidityAdapter extends RecyclerView.Adapter<HumidityAdapter.ViewHolder> {
    private final List<Humidity> humidityList;

    public HumidityAdapter(List<Humidity> courseModelArrayList) {
        this.humidityList = courseModelArrayList;
    }

    @NonNull
    @Override
    public HumidityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HumidityAdapter.ViewHolder holder, int position) {
        Humidity humidity = humidityList.get(position);
        holder.value.setText(humidity.getValue().toString());
        holder.date.setText(DateUtils.getRelativeTimeSpanString(humidity.getDate().getTime(), (new Date()).getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE));
    }

    @Override
    public int getItemCount() {
        return humidityList.size();
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