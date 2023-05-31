package com.example.firebasedemo.Fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.Model.WeatherForecastModel;
import com.example.firebasedemo.R;
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
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        TextView dateTimeTextView = view.findViewById(R.id.tv_date_time);
        TextView mainTextView = view.findViewById(R.id.tv_main);
        TextView descriptionTextView = view.findViewById(R.id.tv_description);
        TextView tempTextView = view.findViewById(R.id.tv_temp);
        TextView tempMinTextView = view.findViewById(R.id.tv_temp_min);
        TextView tempMaxTextView = view.findViewById(R.id.tv_temp_max);

        if (weatherForecastModel != null) {
            dateTimeTextView.setText(weatherForecastModel.getDtTxt());
            mainTextView.setText(weatherForecastModel.getWeatherMain());
            descriptionTextView.setText(weatherForecastModel.getWeatherDescription());
            tempTextView.setText(String.valueOf(weatherForecastModel.getTemp()));
            tempMinTextView.setText(String.valueOf(weatherForecastModel.getTempMin()));
            tempMaxTextView.setText(String.valueOf(weatherForecastModel.getTempMax()));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherForecastModel = requireArguments().getParcelable("weatherForecastModel");
    }

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

        // Set the data to the views
        if (weatherForecastModel != null) {
            dateTimeTextView.setText(weatherForecastModel.getDtTxt());
            mainTextView.setText(weatherForecastModel.getWeatherMain());
            descriptionTextView.setText(weatherForecastModel.getWeatherDescription());
            tempTextView.setText(String.valueOf(weatherForecastModel.getTemp()));
            tempMinTextView.setText(String.valueOf(weatherForecastModel.getTempMin()));
            tempMaxTextView.setText(String.valueOf(weatherForecastModel.getTempMax()));
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