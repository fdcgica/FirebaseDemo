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
        seaLevel = in.readInt();
        groundLevel = in.readInt();
        tempKF = in.readFloat();
        cloudiness = in.readInt();
        windSpeed = in.readFloat();
        windDegree = in.readInt();
        windGust = in.readFloat();
        visibility = in.readInt();
        pop = in.readFloat();
        rain3h = in.readFloat();
        sysPod = in.readString();
        dtTxt = in.readString();
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

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
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

    public int getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(int seaLevel) {
        this.seaLevel = seaLevel;
    }

    public int getGroundLevel() {
        return groundLevel;
    }

    public void setGroundLevel(int groundLevel) {
        this.groundLevel = groundLevel;
    }

    public float getTempKF() {
        return tempKF;
    }

    public void setTempKF(float tempKF) {
        this.tempKF = tempKF;
    }

    public int getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(int cloudiness) {
        this.cloudiness = cloudiness;
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

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public float getPop() {
        return pop;
    }

    public void setPop(float pop) {
        this.pop = pop;
    }

    public float getRain3h() {
        return rain3h;
    }

    public void setRain3h(float rain3h) {
        this.rain3h = rain3h;
    }

    public String getSysPod() {
        return sysPod;
    }

    public void setSysPod(String sysPod) {
        this.sysPod = sysPod;
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
        dest.writeInt(seaLevel);
        dest.writeInt(groundLevel);
        dest.writeFloat(tempKF);
        dest.writeInt(cloudiness);
        dest.writeFloat(windSpeed);
        dest.writeInt(windDegree);
        dest.writeFloat(windGust);
        dest.writeInt(visibility);
        dest.writeFloat(pop);
        dest.writeFloat(rain3h);
        dest.writeString(sysPod);
        dest.writeString(dtTxt);
    }
}