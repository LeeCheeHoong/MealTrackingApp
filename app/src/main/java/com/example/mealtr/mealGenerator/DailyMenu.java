package com.example.mealtr.mealGenerator;

public class DailyMenu {
    public Meal[] meals;
    public Nutrient nutrients;

    public DailyMenu() {

    }

    public DailyMenu(Meal[] meals, Nutrient nutrients) {
        this.meals = meals;
        this.nutrients = nutrients;
    }
}
