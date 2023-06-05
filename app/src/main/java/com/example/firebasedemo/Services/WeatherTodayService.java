package com.example.firebasedemo.Services;

import static com.example.firebasedemo.Constants.Constants.API_KEY;
import static com.example.firebasedemo.Constants.Constants.LATITUDE;
import static com.example.firebasedemo.Constants.Constants.LONGITUDE;
import static com.example.firebasedemo.Constants.Constants.QUERY_FOR_CURRENT_WEATHER;
import static com.example.firebasedemo.Constants.Constants.QUERY_FOR_WEATHER_BYSPEECH;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.firebasedemo.Interface.WeatherTodayCallback;
import com.example.firebasedemo.Model.WeatherForecastModel;
import com.example.firebasedemo.Model.WeatherTodayModel;
import com.example.firebasedemo.Singleton.MySingleton;
import com.example.firebasedemo.Utils.FormatUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WeatherTodayService {

    Context context;

    public WeatherTodayService (Context context){
        this.context = context;
    }

    public void getWeatherToday(WeatherTodayCallback weatherTodayCallback){
        String url = QUERY_FOR_CURRENT_WEATHER + "lat=" + LATITUDE + "&lon=" + LONGITUDE + "&appid=" + API_KEY;
        List<WeatherTodayModel> currentWeather = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                WeatherTodayModel reportDay = new WeatherTodayModel();

                JSONArray weather = response.optJSONArray("weather");
                if (weather != null){
                    JSONObject object = weather.optJSONObject(0);
                    reportDay.setStatus(object.optString("main"));
                }
                JSONObject main = response.optJSONObject("main");

                float kelvinTemp = main.optLong("temp");
                float kelvinTempMin = main.optLong("temp_min");
                float kelvinTempMax = main.optLong("temp_max");

                float celsiusTemp = FormatUtils.convertKelvinToCelsius(kelvinTemp);
                float celsiusTempMin = FormatUtils.convertKelvinToCelsius(kelvinTempMin);
                float celsiusTempMax = FormatUtils.convertKelvinToCelsius(kelvinTempMax);

                float formattedTemp = FormatUtils.formatFloatToTwoDecimalPlaces(celsiusTemp);
                float formattedTempMin = FormatUtils.formatFloatToTwoDecimalPlaces(celsiusTempMin);
                float formattedTempMax = FormatUtils.formatFloatToTwoDecimalPlaces(celsiusTempMax);

                reportDay.setPressure(main.optInt("pressure"));
                reportDay.setHumidity(main.optInt("humidity"));
                reportDay.setTemp(formattedTemp);
                reportDay.setTempMin(formattedTempMin);
                reportDay.setTempMax(formattedTempMax);

                JSONObject wind = response.optJSONObject("wind");

                reportDay.setWindSpeed(wind.optLong("speed"));

                JSONObject sys = response.optJSONObject("sys");

                reportDay.setSunrise(sys.optLong("sunrise"));
                reportDay.setSunset(sys.optLong("sunset"));
                reportDay.setDateTime(response.optLong("dt"));
                reportDay.setLocation(response.optString("name"));
                reportDay.setCallback(response.optInt("cod"));

                    currentWeather.add(reportDay);
                weatherTodayCallback.onSuccess(currentWeather);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherTodayCallback.onError("Something went wrong: "+ error);
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
    public void getWeatherbySpeech(String result, WeatherTodayCallback weatherTodayCallback){
        String url = QUERY_FOR_WEATHER_BYSPEECH + "q=" +result + "&appid=" + API_KEY;
        List<WeatherTodayModel> currentWeather = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                WeatherTodayModel reportDay = new WeatherTodayModel();

                JSONArray weather = response.optJSONArray("weather");
                if (weather != null){
                    JSONObject object = weather.optJSONObject(0);
                    reportDay.setStatus(object.optString("main"));
                }
                JSONObject main = response.optJSONObject("main");

                float kelvinTemp = main.optLong("temp");
                float kelvinTempMin = main.optLong("temp_min");
                float kelvinTempMax = main.optLong("temp_max");

                float celsiusTemp = FormatUtils.convertKelvinToCelsius(kelvinTemp);
                float celsiusTempMin = FormatUtils.convertKelvinToCelsius(kelvinTempMin);
                float celsiusTempMax = FormatUtils.convertKelvinToCelsius(kelvinTempMax);

                float formattedTemp = FormatUtils.formatFloatToTwoDecimalPlaces(celsiusTemp);
                float formattedTempMin = FormatUtils.formatFloatToTwoDecimalPlaces(celsiusTempMin);
                float formattedTempMax = FormatUtils.formatFloatToTwoDecimalPlaces(celsiusTempMax);

                reportDay.setPressure(main.optInt("pressure"));
                reportDay.setHumidity(main.optInt("humidity"));
                reportDay.setTemp(formattedTemp);
                reportDay.setTempMin(formattedTempMin);
                reportDay.setTempMax(formattedTempMax);

                JSONObject wind = response.optJSONObject("wind");

                reportDay.setWindSpeed(wind.optLong("speed"));

                JSONObject sys = response.optJSONObject("sys");

                reportDay.setSunrise(sys.optLong("sunrise"));
                reportDay.setSunset(sys.optLong("sunset"));
                reportDay.setDateTime(response.optLong("dt"));
                reportDay.setLocation(response.optString("name"));
                reportDay.setCallback(response.optInt("cod"));

                currentWeather.add(reportDay);
                weatherTodayCallback.onSuccess(currentWeather);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherTodayCallback.onError("Something went wrong: "+ error);
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
