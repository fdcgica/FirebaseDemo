package com.example.firebasedemo.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.firebasedemo.Interface.CitySpeechRecognitionListener;
import com.example.firebasedemo.Interface.CoordinatesCallback;
import com.example.firebasedemo.Interface.WeatherTodayCallback;
import com.example.firebasedemo.Model.WeatherTodayModel;
import com.example.firebasedemo.R;
import com.example.firebasedemo.Services.WeatherTodayService;
import com.example.firebasedemo.Utils.CommonUtils;
import com.example.firebasedemo.Utils.FormatUtils;
import com.example.firebasedemo.Utils.LoadingDialog;
import com.example.firebasedemo.Utils.LocationUtils;
import com.example.firebasedemo.Utils.SpeechUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
public class HomeFragment extends Fragment implements View.OnClickListener {
    private TextView mLocation, mDate, mTemp,mTempMin,mTempMax,mStatus,mSunrise,mSunset,mWind,mPressure,mHumidity;
    private LocationUtils locationUtils;
    private LinearLayout mRefreshContainer;
    private ImageView mLocatebySpeech, mLocatebyText;
    private ActivityResultLauncher<String> permissionLauncher;
    private ActivityResultLauncher<Intent> speechRecognitionLauncher;
    private SpeechUtils speechUtils;

    private LoadingDialog pd;
    private TextInputEditText cityName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the permission launcher
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // Permission granted
                getWeatherToday();
            } else {
                // Permission denied
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        });
        speechRecognitionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            ArrayList<String> placesText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            if (placesText != null && placesText.size() > 0) {
                                String results = placesText.get(0);
                                getWeatherBySpeech(results);
                            }
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        speechUtils = new SpeechUtils();
        speechUtils.setFragmentAndRegisterLauncher(this, new CitySpeechRecognitionListener() {
            @Override
            public void onSpeechRecognitionResult(String result) {
                getWeatherBySpeech(result);
            }
        });
        mLocation = view.findViewById(R.id.location);
        mDate = view.findViewById(R.id.updated_at);
        mTemp = view.findViewById(R.id.temp);
        mTempMin = view.findViewById(R.id.temp_min);
        mTempMax = view.findViewById(R.id.temp_max);
        mStatus = view.findViewById(R.id.status);
        mSunrise = view.findViewById(R.id.sunrise);
        mSunset = view.findViewById(R.id.sunset);
        mWind = view.findViewById(R.id.wind);
        mPressure = view.findViewById(R.id.pressure);
        mHumidity = view.findViewById(R.id.humidity);
        mRefreshContainer = view.findViewById(R.id.refreshContainer);
        mLocatebySpeech = view.findViewById(R.id.speechSearch);
        mLocatebyText = view.findViewById(R.id.searchButton);
        cityName = view.findViewById(R.id.cityNameInput);
        mLocatebyText.setOnClickListener(this);
        mLocatebySpeech.setOnClickListener(this);
        mRefreshContainer.setOnClickListener(this);
        locationUtils = new LocationUtils();
        pd = new LoadingDialog(getActivity());
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Home");

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getWeatherToday();
        } else {
            requestPermission();
        }
        return view;
    }
    private void requestPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed to use the application (Location-based services)")
                    .setPositiveButton("Okay", (dialog, which) -> {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create().show();
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }
    public void getWeatherBySpeech(String result){

        pd.show(R.layout.custom_loading_dialog);

        WeatherTodayService weatherTodayService = new WeatherTodayService(getContext());
        weatherTodayService.getWeatherbySpeech(result, new WeatherTodayCallback(){

            @SuppressLint("SetTextI18n")
            @Override
             public void onSuccess(List<WeatherTodayModel> weatherTodayModels) {


                if (weatherTodayModels.size() > 0) {
                    WeatherTodayModel weatherToday = weatherTodayModels.get(0);

                    // Set the values in the views
                    mLocation.setText("" + weatherToday.getLocation());
                    cityName.setText("" + weatherToday.getLocation());
                    mDate.setText("Updated at: " + FormatUtils.formatDate(weatherToday.getDateTime()));
                    mTemp.setText("" + weatherToday.getTemp() + "°C");
                    mTempMin.setText("Min: " + weatherToday.getTempMin() + "°C");
                    mTempMax.setText("Max: " + weatherToday.getTempMax() + "°C");
                    mStatus.setText("" + weatherToday.getStatus());
                    mSunrise.setText("" + FormatUtils.getSunTime(weatherToday.getSunrise()));
                    mSunset.setText("" + FormatUtils.getSunTime(weatherToday.getSunset()));
                    mWind.setText("" + weatherToday.getWindSpeed() + " m/s");
                    mPressure.setText("" + weatherToday.getPressure() + " hPa");
                    mHumidity.setText("" + weatherToday.getHumidity() + "%");
                }
                pd.dismiss();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), "City not Found", Toast.LENGTH_SHORT).show();
                mLocation.setText("Not found");
                mDate.setText("");
                mTemp.setText("");
                mTempMin.setText("");
                mTempMax.setText("");
                mStatus.setText("");
                mSunrise.setText("");
                mSunset.setText("");
                mWind.setText("");
                mPressure.setText("");
                mHumidity.setText("");
                pd.dismiss();
            }
        });
    }
    private void getWeatherToday(){
        pd.show(R.layout.custom_loading_dialog);
        WeatherTodayService weatherTodayService = new WeatherTodayService(getContext());
        locationUtils.getCoordinates(getActivity(), new CoordinatesCallback() {
            @Override
            public void onCoordinatesReceived(double latitude, double longitude) {
                weatherTodayService.getWeatherToday(new WeatherTodayCallback() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(List<WeatherTodayModel> weatherTodayModels) {

                        if (weatherTodayModels.size() > 0) {
                            WeatherTodayModel weatherToday = weatherTodayModels.get(0);

                            // Set the values in the views
                            mLocation.setText("" + weatherToday.getLocation());
                            mDate.setText("Updated at: " + FormatUtils.formatDate(weatherToday.getDateTime()));
                            mTemp.setText("" + weatherToday.getTemp() + "°C");
                            mTempMin.setText("Min: " + weatherToday.getTempMin() + "°C");
                            mTempMax.setText("Max: " + weatherToday.getTempMax() + "°C");
                            mStatus.setText("" + weatherToday.getStatus());
                            mSunrise.setText("" + FormatUtils.getSunTime(weatherToday.getSunrise()));
                            mSunset.setText("" + FormatUtils.getSunTime(weatherToday.getSunset()));
                            mWind.setText("" + weatherToday.getWindSpeed() + " m/s");
                            mPressure.setText("" + weatherToday.getPressure() + " hPa");
                            mHumidity.setText("" + weatherToday.getHumidity() + "%");
                        }
                       pd.dismiss();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getActivity(), "Error in fetching data", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
            }
        });
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refreshContainer:
                getWeatherToday();
                break;
            case R.id.speechSearch:
                speechUtils.startSpeechToText();
                break;
            case R.id.searchButton:
                String cityInput = cityName.getText().toString().trim();
                if(TextUtils.isEmpty(cityInput)){
                    Toast.makeText(getActivity(),"Enter a City", Toast.LENGTH_SHORT).show();
                }
                else{
                    cityName.clearFocus();
                    CommonUtils.closeKeyboard(getActivity());
                    getWeatherBySpeech(cityInput);
                }
                break;
            default:
                break;
        }
    }
}