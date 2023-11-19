package com.example.mealtr;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

public class UserStatusFragment extends Fragment {

    Activity context;
    private double calories;
    private double week;
    public UserStatusFragment(double caloriesNeeded, double week) {
        this.calories = caloriesNeeded;
        this.week = week;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        return inflater.inflate(R.layout.fragment_user_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView caloriesDisplay = view.findViewById(R.id.caloriesRecommended);
        caloriesDisplay.setText(new DecimalFormat("0.00").format(this.calories).concat(" k"));
        TextView weekDisplay = view.findViewById(R.id.weeksToTarget);
        weekDisplay.setText(new DecimalFormat("0").format(this.week).concat(" weeks"));
    }
}