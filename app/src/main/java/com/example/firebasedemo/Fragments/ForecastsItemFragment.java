package com.example.firebasedemo.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.Model.WeatherForecastModel;
import com.example.firebasedemo.R;
import com.example.firebasedemo.Utils.FormatUtils;

public class ForecastsItemFragment extends DialogFragment {

    private WeatherForecastModel weatherForecastModel;
    public static ForecastsItemFragment newInstance(WeatherForecastModel weatherForecastModel) {
        ForecastsItemFragment fragment = new ForecastsItemFragment();
        Bundle args = new Bundle();
        args.putParcelable("weatherForecastModel", weatherForecastModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecasts_item, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherForecastModel = requireArguments().getParcelable("weatherForecastModel");
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the custom layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_forecasts_item, null);

        // Set the custom layout as the content view for the dialog
        builder.setView(dialogView);

        // Retrieve the views from the custom layout
        TextView dateTimeTextView = dialogView.findViewById(R.id.tv_date_time);
        TextView mainTextView = dialogView.findViewById(R.id.tv_main);
        TextView descriptionTextView = dialogView.findViewById(R.id.tv_description);
        TextView tempTextView = dialogView.findViewById(R.id.tv_temp);
        TextView tempMinTextView = dialogView.findViewById(R.id.tv_temp_min);
        TextView tempMaxTextView = dialogView.findViewById(R.id.tv_temp_max);
        TextView tempSunrise = dialogView.findViewById(R.id.tv_sunrise);
        TextView tempSunset = dialogView.findViewById(R.id.tv_sunset);
        TextView tempCity = dialogView.findViewById(R.id.tv_city);

        // Set the data to the views
        if (weatherForecastModel != null) {
            tempCity.setText(weatherForecastModel.getCity());
            String day = FormatUtils.getDayFromDate(FormatUtils.formatDate(weatherForecastModel.getDtTxt()));
            dateTimeTextView.setText(""+day+"\n"+FormatUtils.formatDate(weatherForecastModel.getDtTxt()));
            mainTextView.setText(weatherForecastModel.getWeatherMain());
            descriptionTextView.setText(weatherForecastModel.getWeatherDescription().substring(0,1).toUpperCase() + weatherForecastModel.getWeatherDescription().substring(1));
            tempTextView.setText("Temperature: "+weatherForecastModel.getTemp());
            tempMinTextView.setText("Min Temp: "+weatherForecastModel.getTempMin());
            tempMaxTextView.setText("Max Temp: "+weatherForecastModel.getTempMax());
            tempSunrise.setText("Sunrise: " + FormatUtils.getSunTime(weatherForecastModel.getSunrise()));
            tempSunset.setText("Sunset: " + FormatUtils.getSunTime(weatherForecastModel.getSunset()));
        }

        // Set the positive button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle positive button click
                dialog.dismiss(); // Dismiss the dialog after clicking "OK"
            }
        });
        return builder.create();
    }
}