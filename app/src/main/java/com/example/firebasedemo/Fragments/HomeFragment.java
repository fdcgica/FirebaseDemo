package com.example.firebasedemo.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.example.firebasedemo.Constants.Constants.LATITUDE;
import static com.example.firebasedemo.Constants.Constants.LONGITUDE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.Interface.CoordinatesCallback;
import com.example.firebasedemo.Interface.WeatherTodayCallback;
import com.example.firebasedemo.Model.WeatherTodayModel;
import com.example.firebasedemo.R;
import com.example.firebasedemo.Services.WeatherTodayService;
import com.example.firebasedemo.Singleton.CurrentUserSingleton;
import com.example.firebasedemo.Utils.FormatUtils;
import com.example.firebasedemo.Utils.LocationUtils;
import com.google.android.gms.location.LocationCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mLocation, mDate, mTemp,mTempMin,mTempMax,mStatus,mSunrise,mSunset,mWind,mPressure,mHumidity;
    private LocationUtils locationUtils;
    private ProgressDialog pd;
    private LinearLayout mRefreshContainer;
    private ImageView mLocatebySpeech;
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
        mLocatebySpeech.setOnClickListener(this);
        mRefreshContainer.setOnClickListener(this);
        locationUtils = new LocationUtils();
        pd = new ProgressDialog(getActivity());
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Home");

        getWeatherToday();
        return view;
    }
    public void getWeatherbySpeech(String result){
        pd.setMessage("Fetching Data");
        pd.setCancelable(false);
        pd.show();

        WeatherTodayService weatherTodayService = new WeatherTodayService(getContext());
        weatherTodayService.getWeatherbySpeech(result, new WeatherTodayCallback(){

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(List<WeatherTodayModel> weatherTodayModels) {


                if (weatherTodayModels.size() > 0) {
                    WeatherTodayModel weatherToday = weatherTodayModels.get(0);
                    if (weatherToday.getCallback() != 404){
                        // Set the values in the views
                        mLocation.setText("" + weatherToday.getLocation());
                        mDate.setText("" + FormatUtils.formatDate(FormatUtils.formatDate(weatherToday.getDateTime())));
                        mTemp.setText("" + weatherToday.getTemp() + "°C");
                        mTempMin.setText("Min: " + weatherToday.getTempMin() + "°C");
                        mTempMax.setText("Max: " + weatherToday.getTempMax() + "°C");
                        mStatus.setText("" + weatherToday.getStatus());
                        mSunrise.setText("" + FormatUtils.getFormattedSunriseTime(weatherToday.getSunrise()));
                        mSunset.setText("" + FormatUtils.getFormattedSunsetTime(weatherToday.getSunset()));
                        mWind.setText("" + weatherToday.getWindSpeed() + " m/s");
                        mPressure.setText("" + weatherToday.getPressure() + " hPa");
                        mHumidity.setText("" + weatherToday.getHumidity() + "%");
                    }
                    else{
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String message) {

            }
        });
        pd.dismiss();
    }
    private void getWeatherToday(){
        pd.setMessage("Fetching Data");
        pd.setCancelable(false);
        pd.show();
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
                            mDate.setText("" + FormatUtils.formatDate(FormatUtils.formatDate(weatherToday.getDateTime())));
                            mTemp.setText("" + weatherToday.getTemp() + "°C");
                            mTempMin.setText("Min: " + weatherToday.getTempMin() + "°C");
                            mTempMax.setText("Max: " + weatherToday.getTempMax() + "°C");
                            mStatus.setText("" + weatherToday.getStatus());
                            mSunrise.setText("" + FormatUtils.getFormattedSunriseTime(weatherToday.getSunrise()));
                            mSunset.setText("" + FormatUtils.getFormattedSunsetTime(weatherToday.getSunset()));
                            mWind.setText("" + weatherToday.getWindSpeed() + " m/s");
                            mPressure.setText("" + weatherToday.getPressure() + " hPa");
                            mHumidity.setText("" + weatherToday.getHumidity() + "%");
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        });
    }
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak");
        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK){
            ArrayList<String> placesText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String result = "";
            result = placesText.get(0);
            getWeatherbySpeech(result);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refreshContainer:
                getWeatherToday();
                break;
            case R.id.speechSearch:
                startSpeechToText();
                break;
            default:
                break;
        }
    }
}