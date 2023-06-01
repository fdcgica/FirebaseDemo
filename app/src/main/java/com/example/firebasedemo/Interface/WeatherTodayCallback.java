package com.example.firebasedemo.Interface;

import com.example.firebasedemo.Model.WeatherTodayModel;

import java.util.List;

public interface WeatherTodayCallback {
    void onSuccess(List<WeatherTodayModel> weatherTodayModels);

    void onError(String message);
}
