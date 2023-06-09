package com.example.firebasedemo.Utils;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
    public static String getDayFromDate(String formattedDate) {
        DateFormat inputDateFormat = new SimpleDateFormat("yy-MM-dd", Locale.getDefault());
        DateFormat outputDateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        try {
            Date date = inputDateFormat.parse(formattedDate);
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String formatDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
        return format.format(new Date(timestamp * 1000));
    }
    public static String getSunTime(long sunTime){
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
        return format.format(new Date(sunTime * 1000));
    }
    public static String eliminateSpaces(String input) {
        return input.replaceAll("\\s+", "");
    }
}
