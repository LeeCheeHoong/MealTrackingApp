package com.example.mealtr.mealGenerator;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mealtr.DailyMenuFragment;
import com.example.mealtr.MenuFragment;
import com.example.mealtr.MenuItemFragment;
import com.example.mealtr.R;
import com.example.mealtr.UserStatusFragment;
import com.example.mealtr.ViewPagerAdapter;
import com.example.mealtr.calorieCalculator.CalorieCalculator;
import com.example.mealtr.database.DietDB;
import com.example.mealtr.database.GeneratedRecipe;
import com.example.mealtr.database.MealDB;
import com.example.mealtr.database.MenuDB;
import com.example.mealtr.database.MenuViewModel;
import com.example.mealtr.database.RecipeDB;
import com.example.mealtr.mealGenerator.*;

public class GenerateMealAsync extends AsyncTask<String, Void, String> {
    public Menu generatedMenu;
    public CalorieCalculator calorieCalculator;
    public MenuViewModel menuViewModel;
    public FragmentManager manager;
    public String target;
    private long dietId;

    public String dietName;

    public GenerateMealAsync(FragmentManager manager, CalorieCalculator calorieCalculator, MenuViewModel menuViewModel, String dietName) {
        this.manager = manager;
        this.calorieCalculator = calorieCalculator;
        this.menuViewModel = menuViewModel;
        this.dietName = dietName;
    }
    private void MealDBConverter(List<MealDB> meals) {
        List<GeneratedRecipe> _meals = menuViewModel.getRecipesByCalories(calorieCalculator.caloriesNeededPerDay);

        RecipeDB breakfast = menuViewModel.getRecipeById(_meals.get(0).recipeId);
        meals.add(new MealDB(
            breakfast.recipeId,
            "Breakfast",
            breakfast.image,
            breakfast.title,
            breakfast.sourceUrl,
            breakfast.calories,
            breakfast.protein,
            breakfast.fat
        ));
        RecipeDB lunch = menuViewModel.getRecipeById(_meals.get(0).recipeId2);
        meals.add(new MealDB(
            lunch.recipeId,
            "Lunch",
            lunch.image,
            lunch.title,
            lunch.sourceUrl,
            lunch.calories,
            lunch.protein,
            lunch.fat
        ));
        RecipeDB dinner = menuViewModel.getRecipeById(_meals.get(0).recipeId3);
        meals.add(new MealDB(
            dinner.recipeId,
            "Dinner",
            dinner.image,
            dinner.title,
            dinner.sourceUrl,
            dinner.calories,
            dinner.protein,
            dinner.fat
        ));
    }
    private MenuDB MenuDBConverter(String day) {
        MenuDB menu = new MenuDB();
        menu.day = day;
        return menu;
    }
    @Override
    protected String doInBackground(String... params) {
        this.target = params[1].concat("kg to ").concat(params[2]).concat("kg");

        DietDB diet = new DietDB();
        diet.calories = calorieCalculator.caloriesNeededPerDay;
        diet.dietName = this.dietName;
        diet.createdDate =  new SimpleDateFormat("yyyy MMM dd").format(new Date());
        diet.targetWeight = this.target;
        diet.planDuration = calorieCalculator.weekNeeded + " week(s)";

        List<MenuDB> menuDBs = new ArrayList<>();
        List<MealDB> mealDBs = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            switch (i){
                case 0:
                    menuDBs.add(MenuDBConverter("monday"));
                    MealDBConverter(mealDBs);
                    break;
                case 1:
                    menuDBs.add(MenuDBConverter("tuesday"));
                    MealDBConverter(mealDBs);
                    break;
                case 2:
                    menuDBs.add(MenuDBConverter("wednesday"));
                    MealDBConverter(mealDBs);
                    break;
                case 3:
                    menuDBs.add(MenuDBConverter("thursday"));
                    MealDBConverter(mealDBs);
                    break;
                case 4:
                    menuDBs.add(MenuDBConverter("friday"));
                    MealDBConverter(mealDBs);
                    break;
                case 5:
                    menuDBs.add(MenuDBConverter("saturday"));
                    MealDBConverter(mealDBs);
                    break;
                case 6:
                    menuDBs.add(MenuDBConverter("sunday"));
                    MealDBConverter(mealDBs);
                    break;
            }
        }

        FragmentTransaction transaction = manager.beginTransaction();

        menuViewModel.insertDiet(diet, menuDBs, mealDBs, transaction);

        return "";
    }

    @Override
    protected void onPostExecute(String response) {
        for(int i = 0; i < manager.getBackStackEntryCount(); ++i) {
            manager.popBackStack();
        }
    }
}
