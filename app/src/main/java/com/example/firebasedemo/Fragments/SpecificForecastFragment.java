package com.example.firebasedemo.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.Interface.CitySpeechRecognitionListener;
import com.example.firebasedemo.Interface.DialogListener;
import com.example.firebasedemo.R;
import com.example.firebasedemo.Utils.FormatUtils;
import com.example.firebasedemo.Utils.SpeechUtils;
import com.google.android.material.textfield.TextInputEditText;

public class SpecificForecastFragment extends DialogFragment implements CitySpeechRecognitionListener {
    public interface CityEnteredListener {
        void onCityEntered(String city);
    }
    private TextInputEditText cityName;
    private ImageView speechInput;
    private DialogListener mListener;
    private SpeechUtils speechUtils;
    public SpecificForecastFragment() {
        // Required empty public constructor
    }
    public static SpecificForecastFragment newInstance(String param1, String param2) {
        SpecificForecastFragment fragment = new SpecificForecastFragment();
        return fragment;
    }

    public static SpecificForecastFragment newInstance() {
        SpecificForecastFragment specificForecastFragment = new SpecificForecastFragment();
        return specificForecastFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specific_forecast, container, false);
        return view;
    }
    private void registerSpeechRecognition() {
        if (speechUtils != null) {
            speechUtils.setFragmentAndRegisterLauncher(this, this);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Inflate the custom layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_specific_forecast, null);

        // Set the custom layout as the content view for the dialog
        builder.setTitle("Specific Location");
        registerSpeechRecognition();
        builder.setView(dialogView);

        // Initialize the speechUtils object
        speechUtils = new SpeechUtils();
        // Register speech recognition
        registerSpeechRecognition();
        // Retrieve the views from the custom layout

        cityName = dialogView.findViewById(R.id.city_name);
        speechInput = dialogView.findViewById(R.id.citySpeechSearch);
        speechInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechUtils.startSpeechToText();
            }
        });
        // Set the positive button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CharSequence input = cityName.getText().toString();
                if(TextUtils.isEmpty(input)){
                    Toast.makeText(getActivity(),"Please input all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    mListener.onDataReceived(input);
                    dialog.dismiss();
                }
            }
        });
        return builder.create();
    }
    public void setCityEnteredListener(DialogListener listener) {
        mListener = listener;
    }
    @Override
    public void onSpeechRecognitionResult(String result) {
        // Set the result on the cityName EditText
        cityName.setText(result);
    }
}