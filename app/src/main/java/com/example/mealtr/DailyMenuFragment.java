package com.example.mealtr;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mealtr.database.MealDB;
import com.example.mealtr.database.MenuViewModel;
import com.example.mealtr.database.RecipeDB;
import com.example.mealtr.mealGenerator.ChildCallback;
import com.example.mealtr.mealGenerator.DailyMenu;
import com.example.mealtr.mealGenerator.Meal;
import com.example.mealtr.mealGenerator.Nutrient;

import org.w3c.dom.Text;

import java.util.List;

public class DailyMenuFragment extends Fragment {
    Activity context;
    String day;
    long menuId;
    public DailyMenuFragment(String day, long menuId) {
        this.day = day.toUpperCase();
        this.menuId = menuId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();

        return inflater.inflate(R.layout.fragment_daily_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView labelDay = view.findViewById(R.id.menuDay);
        labelDay.setText(this.day);

        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            getChildFragmentManager().beginTransaction().remove(fragment).commit();
        }

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        menuViewModel.getMealsByMenuId(menuId).observe(getViewLifecycleOwner(), new Observer<List<MealDB>>() {
            @Override
            public void onChanged(List<MealDB> mealDBS) {
                double calories = 0;
                double protein = 0;
                double fat = 0;
                for(MealDB meal: mealDBS){
                    MenuItemFragment m = new MenuItemFragment(new Meal(
                            meal.type,
                            meal.mealId,
                            meal.image,
                            meal.title,
                            meal.sourceUrl,
                            meal.calories,
                            meal.protein,
                            meal.fat
                    ));
                    calories = calories + meal.calories;
                    protein = protein + meal.protein;
                    fat = fat + meal.fat;
                    transaction.add(R.id.menuContainer, m);
                }
                transaction.commit();
                TextView labelCalories = view.findViewById(R.id.caloriesAmount);
                TextView labelProtein = view.findViewById(R.id.proteinAmount);
                TextView labelFat = view.findViewById(R.id.fatAmount);

                labelCalories.setText("Calories: ".concat(String.valueOf(calories)).concat("k"));
                labelProtein.setText("Protein: ".concat(String.valueOf(protein)).concat("g"));
                labelFat.setText("Fat: ".concat(String.valueOf(fat)).concat("g"));
            }
        });
    }
}