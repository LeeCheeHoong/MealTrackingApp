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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealtr.database.DietDB;
import com.example.mealtr.database.MenuViewModel;
import com.example.mealtr.database.RecipeDB;
import com.example.mealtr.mealGenerator.ChildCallback;

import java.util.List;

public class AlternateMenuList extends Fragment {
    double calories;
    long mealId;
    MenuViewModel menuViewModel;

    public AlternateMenuList(double calories, long mealId){
        this.calories = calories;
        this.mealId = mealId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alternate_menu_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            getChildFragmentManager().beginTransaction().remove(fragment).commit();
        }
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.getSimilarRecipeByCalories(this.calories).observe(getViewLifecycleOwner(), new Observer<List<RecipeDB>>() {
            @Override
            public void onChanged(List<RecipeDB> RecipeDBS) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                for(RecipeDB recipe : RecipeDBS){
                    AlternateMenuItem m = new AlternateMenuItem(recipe, mealId);
                    transaction.add(R.id.alternateMenuContainer, m);
                }
                transaction.commit();
            }
        });
    }
}