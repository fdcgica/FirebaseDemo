package com.example.firebasedemo.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.firebasedemo.Constants.Constants;
import com.example.firebasedemo.Interface.CoordinatesCallback;
import com.example.firebasedemo.Interface.WeatherAPICallback;
import com.example.firebasedemo.Services.WeatherDataService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;

public class LocationUtils {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    public void getCurrentLocation(Activity activity, WeatherAPICallback callback) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        WeatherDataService weatherDataService = new WeatherDataService(activity);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isGPSEnabled(activity)) {
                LocationCallback locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        fusedLocationProviderClient.removeLocationUpdates(this);

                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            Location lastLocation = locationResult.getLocations().get(locationResult.getLocations().size() - 1);
                            double latitude = lastLocation.getLatitude();
                            double longitude = lastLocation.getLongitude();

                            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                            String formattedLatitude = decimalFormat.format(latitude);
                            String formattedLongitude = decimalFormat.format(longitude);

                            Constants.LATITUDE = Double.parseDouble(formattedLatitude);
                            Constants.LONGITUDE = Double.parseDouble(formattedLongitude);
                            editor.putString("latitude", String.valueOf(Constants.LATITUDE));
                            editor.putString("longitude", String.valueOf(Constants.LONGITUDE));
                            editor.clear();
                            weatherDataService.getForecast(callback);
                        }
                    }
                };

                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            } else {
                turnOnGPS(activity);
            }
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public void getCoordinates(Activity activity, CoordinatesCallback callback) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isGPSEnabled(activity)) {
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int index = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(index).getLatitude();
                            double longitude = locationResult.getLocations().get(index).getLongitude();

                            DecimalFormat decimalFormat = new DecimalFormat("#0.0000");
                            String formattedLatitude = decimalFormat.format(latitude);
                            String formattedLongitude = decimalFormat.format(longitude);

                            Constants.LATITUDE = Double.parseDouble(formattedLatitude);
                            Constants.LONGITUDE = Double.parseDouble(formattedLongitude);

                            callback.onCoordinatesReceived(Constants.LATITUDE, Constants.LONGITUDE);
                        }
                    }
                };

                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            } else {
                turnOnGPS(activity);
            }
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


    private void turnOnGPS(Activity activity) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(activity)
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(activity, "GPS is already turned on", Toast.LENGTH_SHORT).show();
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(activity, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Device does not have location
                            break;
                    }
                }
            }
        });
    }

    private boolean isGPSEnabled(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}