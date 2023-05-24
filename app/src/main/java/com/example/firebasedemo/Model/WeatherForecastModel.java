package com.example.firebasedemo.Model;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.firebasedemo.Singleton.MySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherForecastModel {

    private String weatherIcon,weatherMain,description,dateTime;
    private float temp,tempMin,tempMax;

    public WeatherForecastModel() {
    }

    public WeatherForecastModel(String weatherIcon, String weatherMain, String description, String dateTime, float temp, float tempMin, float tempMax) {
        this.weatherIcon = weatherIcon;
        this.weatherMain = weatherMain;
        this.description = description;
        this.dateTime = dateTime;
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    @Override
    public String toString() {
        return "WeatherForecastModel{" +
                "weatherIcon='" + weatherIcon + '\'' +
                ", weatherMain='" + weatherMain + '\'' +
                ", description='" + description + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", temp=" + temp +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                '}';
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }
}
