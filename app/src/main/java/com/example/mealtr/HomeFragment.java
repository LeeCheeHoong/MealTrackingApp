package com.example.mealtr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {
    Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button btnWeightLoss = (Button) context.findViewById(R.id.home_btn_weight);
        Button btnWater = (Button) context.findViewById(R.id.home_btn_water);

        btnWeightLoss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler("weight");
            }
        });
        btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler("water");
            }
        });
    }

    private void clickHandler(String type) {
        Bundle result = new Bundle();
        result.putString("type", type);
        getParentFragmentManager().setFragmentResult("type", result);

        FragmentManager manager = getParentFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if(type == "water"){
            transaction.replace(R.id.main_content, new WaterTrackingFragment());
        } else {
            transaction.replace(R.id.main_content, new UserDetailsFragment());
        }

        transaction.addToBackStack(null);
        transaction.commit();
    }

}