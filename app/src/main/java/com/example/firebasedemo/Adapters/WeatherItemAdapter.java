package com.example.firebasedemo.Adapters;

import static android.content.ContentValues.TAG;

import static com.example.firebasedemo.Constants.Constants.weatherIconAPI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
import com.example.firebasedemo.Utils.FormatUtils;
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
        private TextView temp,tempMin,tempMax,weatherMain,weatherDescription,weatherDate;
        private ImageView weatherIcon;


        public WeatherItemHolder(@NonNull View itemView) {
            super(itemView);
            temp = itemView.findViewById(R.id.weather_temp);
            tempMin = itemView.findViewById(R.id.weather_temp_min);
            tempMax = itemView.findViewById(R.id.weather_temp_max);
            weatherMain = itemView.findViewById(R.id.weather_main);
            weatherDescription = itemView.findViewById(R.id.weather_description);
            weatherDate = itemView.findViewById(R.id.weather_date);
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
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherItemHolder holder, int position) {
        WeatherForecastModel currentModel = mWeathers.get(position);
        holder.weatherMain.setText(currentModel.getWeatherMain());
        holder.weatherDescription.setText(currentModel.getWeatherDescription());
        String day = FormatUtils.getDayFromDate(FormatUtils.formatDate(currentModel.getDtTxt()));
        holder.weatherDate.setText(""+day+"\n"+FormatUtils.formatDate(currentModel.getDtTxt()));
        Picasso.get().load(weatherIconAPI + currentModel.getWeatherIcon() +"@2x.png").into(holder.weatherIcon);
        holder.temp.setText(currentModel.getTemp() + "" + mContext.getString(R.string.celcius));
        holder.tempMin.setText(currentModel.getTempMin() + "" + mContext.getString(R.string.celcius));
        holder.tempMax.setText(currentModel.getTempMax() + "" + mContext.getString(R.string.celcius));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForecastsItemFragment dialogFragment = ForecastsItemFragment.newInstance(currentModel);
                dialogFragment.show(((FragmentActivity) mContext).getSupportFragmentManager(), "dialog_fragment");

            }
        });
    }

    @Override
    public int getItemCount() {
        return mWeathers != null ? mWeathers.size(): 0;
    }

}
