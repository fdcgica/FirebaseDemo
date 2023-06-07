package com.example.firebasedemo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebasedemo.R;
public class SpecificForecastFragment extends DialogFragment {
    public SpecificForecastFragment() {
        // Required empty public constructor
    }
    public static SpecificForecastFragment newInstance(String param1, String param2) {
        SpecificForecastFragment fragment = new SpecificForecastFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_specific_forecast, container, false);
    }
}