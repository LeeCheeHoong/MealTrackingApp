package com.example.mealtr;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mealtr.database.DietDB;
import com.example.mealtr.database.MenuDB;
import com.example.mealtr.database.MenuViewModel;
import com.example.mealtr.mealGenerator.ChildCallback;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneratedMenuFragment extends Fragment {

    public String menuName;
    public long dietId;
    public double calories;
    public String createdDate;
    public String targetWeight;
    public String duration;
    public MenuViewModel menuViewModel;
    private ChildCallback callback;

    Activity context;

    public GeneratedMenuFragment(DietDB menu) {
        this.dietId = menu.dietId;
        this.menuName = menu.dietName;
        this.calories = menu.calories;
        this.createdDate = menu.createdDate;
        this.targetWeight = menu.targetWeight;
        this.duration = menu.planDuration;
    }

    public static GeneratedMenuFragment newInstance( DietDB menu) {
        GeneratedMenuFragment fragment = new GeneratedMenuFragment(menu);
        Bundle args = new Bundle();
        args.putString("tag", String.valueOf(menu.dietId));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        return inflater.inflate(R.layout.fragment_generated_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView menuNameDisp = view.findViewById(R.id.menuList_menuName);
        TextView caloriesDisp = view.findViewById(R.id.menuList_menuCalories);
        TextView createdDateDisp = view.findViewById(R.id.menuList_createdDate);
        TextView targetDisp = view.findViewById(R.id.menuList_targetWeight);
        TextView durationDisp = view.findViewById(R.id.menuList_planDuration);

        menuNameDisp.setText(this.menuName);
        caloriesDisp.setText("Calories: ".concat(new DecimalFormat("0.00").format(this.calories).concat(" k")));
        targetDisp.setText("Target Weight: ".concat(this.targetWeight));
        createdDateDisp.setText("Created date: ".concat(this.createdDate));
        durationDisp.setText("Duration: ".concat(this.duration));

        Button btnView = view.findViewById(R.id.menuList_viewMenu);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getParentFragment().getParentFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.main_content, new MenuFragment(dietId));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Button btnDel = view.findViewById(R.id.menuList_deleteMenu);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onDeleteClicked(dietId);
                }
            }
        });
    }
    public void setCallback(ChildCallback callback) {
        this.callback = callback;
    }
}