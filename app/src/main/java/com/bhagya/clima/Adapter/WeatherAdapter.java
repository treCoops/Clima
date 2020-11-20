package com.bhagya.clima.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bhagya.clima.Model.WeatherModel;
import com.bhagya.clima.R;
import com.bhagya.clima.Util.WeatherUtil;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    List<WeatherModel> weatherDataList;
    Context context;

    public WeatherAdapter(List<WeatherModel> weatherDataList, Context context) {
        this.weatherDataList = weatherDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_cell, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WeatherModel data = weatherDataList.get(position);
        holder.txtDay.setText(data.getDay());
        holder.txtTemp.setText(data.getTemp()+"\u00B0C");
        holder.weatherIcon.setImageDrawable(WeatherUtil.selectDrawable(data.getImg(), context));
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTemp,txtDay;
        ImageView weatherIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            weatherIcon = itemView.findViewById(R.id.weatherIcon);
            txtTemp = itemView.findViewById(R.id.txtTemp);
            txtDay = itemView.findViewById(R.id.txtDay);
        }
    }
}
