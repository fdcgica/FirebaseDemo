package com.example.firebasedemo.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.Adapters.WeatherItemAdapter;
import com.example.firebasedemo.Interface.WeatherAPICallback;
import com.example.firebasedemo.Model.WeatherForecastModel;
import com.example.firebasedemo.R;
import com.example.firebasedemo.Utils.LocationUtils;
import com.google.android.gms.location.LocationRequest;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForecastsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mLat;
    private Button forecastBtn;
    private LocationRequest locationRequest;
    private RecyclerView myRecyclerView;
    private WeatherItemAdapter mWeatherAdapter;
    ProgressDialog pd;
    private LocationUtils locationUtils;

    public ForecastsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForecastsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForecastsFragment newInstance(String param1, String param2) {
        ForecastsFragment fragment = new ForecastsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        locationUtils = new LocationUtils();

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecasts, container, false);

        forecastBtn = view.findViewById(R.id.forecast_button);
        pd = new ProgressDialog(getActivity());
        myRecyclerView = view.findViewById(R.id.weather_recyclerView);
        forecastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Fetching Data");
                pd.setCancelable(false);
                pd.show();
                locationUtils.getCurrentLocation(getActivity(), new WeatherAPICallback() {
                    @Override
                    public void onSuccess(List<WeatherForecastModel> weatherForecastModels) {
                        mWeatherAdapter = new WeatherItemAdapter(getActivity(), weatherForecastModels);
                        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        myRecyclerView.setHasFixedSize(true);
                        myRecyclerView.setAdapter(mWeatherAdapter);
                        pd.dismiss();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getActivity(),"Somethings Wrong", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
            }
        });
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Forecasts");
        return view;
    }
}