package com.example.firebasedemo.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.firebasedemo.Adapters.WeatherItemAdapter;
import com.example.firebasedemo.Enums.ForecastType;
import com.example.firebasedemo.Interface.DialogListener;
import com.example.firebasedemo.Interface.WeatherAPICallback;
import com.example.firebasedemo.Model.WeatherForecastModel;
import com.example.firebasedemo.R;
import com.example.firebasedemo.Services.WeatherDataService;
import com.example.firebasedemo.Utils.FormatUtils;
import com.example.firebasedemo.Utils.LocationUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForecastCurrentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastCurrentFragment extends Fragment implements View.OnClickListener, DialogListener {
    private RecyclerView myRecyclerView;
    private WeatherItemAdapter mWeatherAdapter;
    ProgressDialog pd;
    private LocationUtils locationUtils;
    private Button forecastDropdownBtn;
    private DialogListener dialogListener;
    private Context mcontext;

    public ForecastCurrentFragment() {
        // Required empty public constructor
    }
    public static ForecastCurrentFragment newInstance(String param1, String param2) {
        ForecastCurrentFragment fragment = new ForecastCurrentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        locationUtils = new LocationUtils();
        mcontext = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecasts_current, container, false);
        pd = new ProgressDialog(getActivity());
        myRecyclerView = view.findViewById(R.id.weather_recyclerView);
        forecastDropdownBtn = view.findViewById(R.id.dropdownButton);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Forecasts");
        forecastDropdownBtn.setText(R.string.current_location);
        forecastDropdownBtn.setOnClickListener(this);
        return view;
    }
    private void getCurrentWeatherForecast(){
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
                Toast.makeText(getActivity(),"Somethings Wrong "+message, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }
    private void getCurrentWeatherForecastSpecific(String Data){
        pd.setMessage("Fetching Data");
        pd.setCancelable(false);
        pd.show();
        WeatherDataService weatherDataService = new WeatherDataService(getActivity());
        weatherDataService.getForecastSpecific(Data, new WeatherAPICallback() {
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
                Toast.makeText(getActivity(),"City Not Found", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

    }
    private void showDropdownMenu(View anchorView) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.forecast_menu, popupMenu.getMenu());

        // Set item click listener
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.current_forecast:
                    forecastDropdownBtn.setText(R.string.current_location);
                    handleForecastType(ForecastType.CURRENT);
                    return true;
                case R.id.specific_forecast:
                    forecastDropdownBtn.setText(R.string.specify_location);
                    handleForecastType(ForecastType.SPECIFIC);
                    return true;
                default:
                    return false;
            }
        });
        // Show the popup menu
        popupMenu.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dropdownButton:
                showDropdownMenu(v);
                break;
        }
    }

    @Override
    public void onDataReceived(CharSequence Data) {
        getCurrentWeatherForecastSpecific(Data.toString());
    }
    private void showSpecificForecastDialog() {
        SpecificForecastFragment specificForecastFragment = SpecificForecastFragment.newInstance();
        specificForecastFragment.setCityEnteredListener(this); // Set the listener to receive the entered city
        specificForecastFragment.show(getParentFragmentManager(), "specific_forecast_dialog");
    }
    private void handleForecastType(ForecastType forecastType) {
        switch (forecastType) {
            case CURRENT:
                getCurrentWeatherForecast();
                break;
            case SPECIFIC:
                showSpecificForecastDialog();
                break;
        }
    }

}