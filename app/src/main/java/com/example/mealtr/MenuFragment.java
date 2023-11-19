package com.example.mealtr;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mealtr.calorieCalculator.CalorieCalculator;
import com.example.mealtr.database.MealDB;
import com.example.mealtr.database.MenuDB;
import com.example.mealtr.database.MenuViewModel;
import com.example.mealtr.mealGenerator.DailyMenu;
import com.example.mealtr.mealGenerator.DayOrder;
import com.example.mealtr.mealGenerator.GenerateMealAsync;
import com.example.mealtr.mealGenerator.Meal;
import com.example.mealtr.mealGenerator.Menu;
import com.example.mealtr.mealGenerator.Nutrient;
import com.example.mealtr.mealGenerator.WeeklyMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MenuFragment extends Fragment {

    Activity context;
    long dietId;

    public MenuFragment(long dietId){
        this.dietId = dietId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        menuViewModel.getMenuByDietId(dietId).observe(getViewLifecycleOwner(), new Observer<List<MenuDB>>() {
            @Override
            public void onChanged(List<MenuDB> menuDBS) {
                FragmentManager manager = getChildFragmentManager();

                ViewPager viewPager = view.findViewById(R.id.menuCarousel);
                ViewPagerAdapter adapter = new ViewPagerAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

                for (MenuDB menu : menuDBS){
                    adapter.addFragment(new DailyMenuFragment(menu.day, menu.menuId));
                }
                viewPager.setAdapter(adapter);
            }
        });
    }
}