package com.example.firebasedemo.Model;

public class WeatherTodayModel {

    private String location;

    public WeatherTodayModel() {
    }

    private String status;
    private long dateTime;
    private long sunrise;
    private long sunset;
    private int pressure;
    private int humidity;
    private float temp;
    private float tempMin;
    private int callback;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public int getCallback() {
        return callback;
    }

    public void setCallback(int callback) {
        this.callback = callback;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
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

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    private float tempMax;
    private float windSpeed;

    public WeatherTodayModel(int callback, String location, String status, long dateTime, long sunrise, long sunset, int pressure, int humidity, float temp, float tempMin, float tempMax, float windSpeed) {
        this.callback = callback;
        this.location = location;
        this.status = status;
        this.dateTime = dateTime;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.windSpeed = windSpeed;
    }
}
