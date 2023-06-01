package com.example.firebasedemo.Utils;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtils {
    public static float convertKelvinToCelsius(float kelvinValue) {
        return kelvinValue - 273.15f;
    }

    public static float formatFloatToTwoDecimalPlaces(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return Float.parseFloat(decimalFormat.format(value));
    }

    public static String formatDate(String inputDate) {
        DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        DateFormat outputDateFormat = new SimpleDateFormat("yy-MM-dd", Locale.getDefault());
        try {
            Date date = inputDateFormat.parse(inputDate);
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String formatDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(new Date(timestamp * 1000));
    }
    public static String getFormattedSunriseTime(long sunriseTime) {
        Date sunriseDate = new Date(sunriseTime * 1000); // Convert Unix timestamp to milliseconds
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return format.format(sunriseDate);
    }
    public static String getFormattedSunsetTime(long sunsetTime) {
        Date sunsetDate = new Date(sunsetTime * 1000); // Convert Unix timestamp to milliseconds
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return format.format(sunsetDate);
    }
}
