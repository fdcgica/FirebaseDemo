package com.example.firebasedemo.Interface;

import com.example.firebasedemo.Model.WeatherForecastModel;

import org.json.JSONObject;

import java.util.List;

public interface WeatherAPICallback {

    void onSuccess(List<WeatherForecastModel> weatherForecastModels);

    void onError(String message);
}
