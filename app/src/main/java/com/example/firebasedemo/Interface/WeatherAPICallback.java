package com.example.firebasedemo.Interface;

import org.json.JSONObject;

public interface WeatherAPICallback {

    void onSuccess(JSONObject jsonObject);

    void onError();
}
