package com.example.firebasedemo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebasedemo.Adapters.ForecastPagerAdapter;
import com.example.firebasedemo.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ForecastFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        List<Fragment> forecastFragments = new ArrayList<>();
        forecastFragments.add(new ForecastFragment());
        forecastFragments.add(new UserSettingsFragment());

        ForecastPagerAdapter adapter = new ForecastPagerAdapter(getChildFragmentManager(), getLifecycle(), forecastFragments);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Customize tab titles if needed
            switch (position) {
                case 0:
                    tab.setText("Tab 1");
                    break;
                case 1:
                    tab.setText("Tab 2");
                    break;
            }
        }).attach();
        return view;
    }
}