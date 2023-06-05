package com.example.firebasedemo.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.firebasedemo.Singleton.MySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class WeatherForecastModel implements Parcelable {
    private long dateTime;
    private float temp;
    private float tempMin;
    private float tempMax;
    private int humidity;
    private String weatherMain;
    private String weatherDescription;
    private String weatherIcon;
    private int pressure;
    private int seaLevel;
    private int groundLevel;
    private float tempKF;
    private int cloudiness;
    private float windSpeed;
    private int windDegree;
    private float windGust;
    private int visibility;
    private float pop;
    private float rain3h;
    private String sysPod;
    private String dtTxt;
    private long sunrise;
    private long sunset;
    private String city;

    public WeatherForecastModel() {
    }

    protected WeatherForecastModel(Parcel in) {
        dateTime = in.readLong();
        temp = in.readFloat();
        tempMin = in.readFloat();
        tempMax = in.readFloat();
        humidity = in.readInt();
        weatherMain = in.readString();
        weatherDescription = in.readString();
        weatherIcon = in.readString();
        pressure = in.readInt();
        windSpeed = in.readFloat();
        windDegree = in.readInt();
        windGust = in.readFloat();
        dtTxt = in.readString();
        sunrise = in.readLong();
        sunset = in.readLong();
        city = in.readString();
    }

    public static final Creator<WeatherForecastModel> CREATOR = new Creator<WeatherForecastModel>() {
        @Override
        public WeatherForecastModel createFromParcel(Parcel in) {
            return new WeatherForecastModel(in);
        }

        @Override
        public WeatherForecastModel[] newArray(int size) {
            return new WeatherForecastModel[size];
        }
    };

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getTempMin() {
        return tempMin;
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

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(int windDegree) {
        this.windDegree = windDegree;
    }

    public float getWindGust() {
        return windGust;
    }

    public void setWindGust(float windGust) {
        this.windGust = windGust;
    }

    public float getRain3h() {
        return rain3h;
    }

    public void setRain3h(float rain3h) {
        this.rain3h = rain3h;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dateTime);
        dest.writeFloat(temp);
        dest.writeFloat(tempMin);
        dest.writeFloat(tempMax);
        dest.writeInt(humidity);
        dest.writeString(weatherMain);
        dest.writeString(weatherDescription);
        dest.writeString(weatherIcon);
        dest.writeInt(pressure);
        dest.writeFloat(windSpeed);
        dest.writeInt(windDegree);
        dest.writeFloat(windGust);
        dest.writeString(dtTxt);
        dest.writeLong(sunrise);
        dest.writeLong(sunset);
        dest.writeString(city);
    }
}