package com.example.firebasedemo.Utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FormatUtils {
    public static float convertKelvinToCelsius(float kelvinValue) {
        return kelvinValue - 273.15f;
    }

    public static float formatFloatToTwoDecimalPlaces(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return Float.parseFloat(decimalFormat.format(value));
    }
}
