package com.example.firebasedemo.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ForecastPagerAdapter extends FragmentPagerAdapter {

    public ForecastPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getCount() {
        return 0;
    }

    public CharSequence getPageTitle(int position) {
        // Return the title for each tab based on the position
        switch (position) {
            case 0:
                return "Current Location";
            case 1:
                return "Specify Location";
            case 2:
            default:
                return null;
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }
}