package com.example.firebasedemo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.firebasedemo.Fragments.ForecastsItemFragment;
import com.example.firebasedemo.Model.WeatherForecastModel;
import com.example.firebasedemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WeatherItemAdapter extends RecyclerView.Adapter<WeatherItemAdapter.WeatherItemHolder> {
    private Context mContext;
    private List<WeatherForecastModel> mWeathers;

    public WeatherItemAdapter(FragmentActivity activity, List<WeatherForecastModel> weatherForecastModels) {
        mContext = activity;
        mWeathers = weatherForecastModels;
        notifyDataSetChanged();
    }

    class WeatherItemHolder extends ViewHolder{
        private TextView temp,temp_min,temp_max,weather_main,weather_description;
        private ImageView weatherIcon;


        public WeatherItemHolder(@NonNull View itemView) {
            super(itemView);
            temp = itemView.findViewById(R.id.weather_temp);
            temp_min = itemView.findViewById(R.id.weather_temp_min);
            temp_max = itemView.findViewById(R.id.weather_temp_max);
            weather_main = itemView.findViewById(R.id.weather_main);
            weather_description = itemView.findViewById(R.id.weather_description);
            weatherIcon = itemView.findViewById(R.id.weather_icon);
        }
    }

    @NonNull
    @Override
    public WeatherItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.weather_item, parent, false);
        return new WeatherItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherItemHolder holder, int position) {
        WeatherForecastModel currentModel = mWeathers.get(position);
        holder.weather_main.setText(currentModel.getWeatherMain());
        holder.weather_description.setText(currentModel.getDescription());
        Picasso.get().load("https://openweathermap.org/img/wn/" + currentModel.getWeatherIcon() +".png").into(holder.weatherIcon);
        holder.temp.setText(String.valueOf(currentModel.getTemp()) + R.string.celcius);
        holder.temp_min.setText(String.valueOf(currentModel.getTempMin()) + R.string.celcius);
        holder.temp_max.setText(String.valueOf(currentModel.getTempMax()) + R.string.celcius);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForecastsItemFragment dialogFragment = ForecastsItemFragment.newInstance();
                dialogFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "dialog_fragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWeathers.size();
    }

}
