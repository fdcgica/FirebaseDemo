package com.example.firebasedemo.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.firebasedemo.Interface.WeatherAPICallback;
import com.example.firebasedemo.Singleton.MySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {
    public static final String QUERY_FOR_CURRENT_LOCATION = "https://api.openweathermap.org/data/2.5/forecast?";
    public static final String API_KEY = "895284fb2d2c50a520ea537456963d9c";
    Context context;
    String stringRes;

    public WeatherDataService(Context context) {
        this.context = context;
    }

     public void getForecast(Double latitude, Double longitude, WeatherAPICallback weatherAPICallback){

         String url = QUERY_FOR_CURRENT_LOCATION + "lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY;
         List<WeatherForecastModel> forecast = new ArrayList<>();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    stringRes = "";
                    JSONArray list = response.optJSONArray("list");
                    if(list!=null){
                        for(int x = 0; x < list.length(); x+=8) {
                            WeatherForecastModel reportDay = new WeatherForecastModel();
                            JSONObject row = list.optJSONObject(x);
                            JSONObject dayMain = row.optJSONObject("main");
                            JSONArray dayWeather = row.optJSONArray("weather");

//                            for(int i = 0; i < dayWeather.length(); i++) {
//
//                            }
                            reportDay.setWeatherMain(dayWeather.optString(1));
                            reportDay.setWeatherMain(dayWeather.optString(2));
                            reportDay.setWeatherMain(dayWeather.optString(3));

                            reportDay.setDateTime(dayMain.optString("dt_txt"));
                            reportDay.setTemp(dayMain.optLong("temp"));
                            reportDay.setTempMin(dayMain.optLong("temp_min"));
                            reportDay.setTempMax(dayMain.optLong("temp_max"));

                            forecast.add(reportDay);
                        }
                    }
                    weatherAPICallback.onSuccess(forecast);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    weatherAPICallback.onError("Something's Wrong");
                }
            });
            MySingleton.getInstance(context).addToRequestQueue(request);
    }

//    public List<WeatherForecastModel> getForecastByCurrentLocation(Double latitude, Double longitude){
//
//    }
}
