package com.example.firebasedemo.Services;

import static android.content.ContentValues.TAG;

import static com.example.firebasedemo.Constants.Constants.API_KEY;
import static com.example.firebasedemo.Constants.Constants.LATITUDE;
import static com.example.firebasedemo.Constants.Constants.LONGITUDE;
import static com.example.firebasedemo.Constants.Constants.QUERY_FOR_FORECAST;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.firebasedemo.Interface.WeatherAPICallback;
import com.example.firebasedemo.Model.WeatherForecastModel;
import com.example.firebasedemo.Singleton.MySingleton;
import com.example.firebasedemo.Utils.FormatUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {
    Context context;

    public WeatherDataService(Context context) {
        this.context = context;
    }

     public void getForecast(WeatherAPICallback weatherAPICallback){

         String url = QUERY_FOR_FORECAST + "lat=" + LATITUDE + "&lon=" + LONGITUDE + "&appid=" + API_KEY;
         List<WeatherForecastModel> forecast = new ArrayList<>();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray list = response.optJSONArray("list");//list[]
                    if (list != null) {
                        //List
                        for (int x = 0; x < list.length(); x += 8) {
                            WeatherForecastModel reportDay = new WeatherForecastModel();
                            JSONObject row = list.optJSONObject(x);
                            JSONObject dayMain = row.optJSONObject("main");//main{}
                            JSONArray dayWeather = row.optJSONArray("weather");//weather[]

                            reportDay.setDtTxt(row.optString("dt_txt"));

                            //Weather loop
                            if (dayWeather != null && dayWeather.length() > 0) {
                                JSONObject weatherData = dayWeather.optJSONObject(0);
                                reportDay.setWeatherMain(weatherData.optString("main"));
                                reportDay.setWeatherDescription(weatherData.optString("description"));
                                reportDay.setWeatherIcon(weatherData.optString("icon"));
                            }

                            float kelvinTemp = dayMain.optLong("temp");
                            float kelvinTempMin = dayMain.optLong("temp_min");
                            float kelvinTempMax = dayMain.optLong("temp_max");

                            float celsiusTemp = FormatUtils.convertKelvinToCelsius(kelvinTemp);
                            float celsiusTempMin = FormatUtils.convertKelvinToCelsius(kelvinTempMin);
                            float celsiusTempMax = FormatUtils.convertKelvinToCelsius(kelvinTempMax);

                            float formattedTemp = FormatUtils.formatFloatToTwoDecimalPlaces(celsiusTemp);
                            float formattedTempMin = FormatUtils.formatFloatToTwoDecimalPlaces(celsiusTempMin);
                            float formattedTempMax = FormatUtils.formatFloatToTwoDecimalPlaces(celsiusTempMax);

                            reportDay.setTemp(formattedTemp);
                            reportDay.setTempMin(formattedTempMin);
                            reportDay.setTempMax(formattedTempMax);

                            JSONObject windMain = row.optJSONObject("wind");//wind{}
                            reportDay.setWindSpeed(windMain.optLong("speed"));
                            reportDay.setWindDegree(windMain.optInt("deg"));
                            reportDay.setWindGust(windMain.optLong("gust"));

                            forecast.add(reportDay);
                        }
                    }
                    JSONObject city = response.optJSONObject("city");//city{}
                    if(city != null){
                        for (int y = 0; y < city.length(); y++){

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
}
