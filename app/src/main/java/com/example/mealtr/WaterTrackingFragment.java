package com.example.mealtr;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class WaterTrackingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_water_tracking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView waterConsumed = view.findViewById(R.id.waterConsumed);
        Button drinkWater = view.findViewById(R.id.drink_water);
        drinkWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String water = waterConsumed.getText().toString();
                Integer waterCurrent = Integer.parseInt(water.substring(0, water.length() - 2));
                waterConsumed.setText(String.valueOf(waterCurrent + 250).concat("ml"));
            }
        });

    }
}