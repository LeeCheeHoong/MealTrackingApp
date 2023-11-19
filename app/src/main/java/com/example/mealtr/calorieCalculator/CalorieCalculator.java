package com.example.mealtr.calorieCalculator;

import android.util.Log;

public class CalorieCalculator {
    public int weekNeeded=0;
    public double weightLossRatePerWeek=1;
    public double caloriesNeededPerDay=0;

    private double bmr;
    private double activityLevelFactor;
    private double calorieDeficitSurplusPerWeek;
    private int currentWeight;
    private int targetWeight;

    public void countCalories(int age, int height, int currentWeight, int targetWeight, String gender, String activity) {
        double activityLevelFactor;
        switch (activity){
            case "Not Active":
                activityLevelFactor = 1.2;
                break;
            case "Lightly Active":
                activityLevelFactor = 1.375;
                break;
            case "Moderately Active":
                activityLevelFactor = 1.55;
                break;
            case "Very Active":
                activityLevelFactor = 1.725;
                break;
            default:
                activityLevelFactor = 1;
        }

        double bmr; //Basal Metabolic Rate
        if (gender == "Male") {
            bmr = (10 * currentWeight) + (6.25 * height) - (5 * age) + 5;
        } else {
            bmr = (10 * currentWeight) + (6.25 * height) - (5 * age) - 161;
        }

        // Initialize weight loss rate
        double weightLossGainRateKgPerWeek = 1.0;

        // Calculate daily calorie intake to achieve the weight loss goal
        int weekNeeded = 0;
        double calorieDeficitSurplusPerWeek = 0;
        double caloriesNeededPerDay = 0;

        while (caloriesNeededPerDay < 1600) {
            calorieDeficitSurplusPerWeek = weightLossGainRateKgPerWeek * 7700;

            if (targetWeight > currentWeight) {
                caloriesNeededPerDay = bmr * activityLevelFactor + (calorieDeficitSurplusPerWeek / 7);
                weekNeeded = (int) ((targetWeight - currentWeight) / weightLossGainRateKgPerWeek);
            } else {
                caloriesNeededPerDay = bmr * activityLevelFactor - (calorieDeficitSurplusPerWeek / 7);
                if (caloriesNeededPerDay >= 1600 || weightLossGainRateKgPerWeek < 0.1) {
                    weekNeeded = (int) ((currentWeight - targetWeight) / weightLossGainRateKgPerWeek);
                    break;
                } else {
                    weightLossGainRateKgPerWeek -= 0.1;
                }
            }
        }

        this.bmr = bmr;
        this.activityLevelFactor = activityLevelFactor;
        this.calorieDeficitSurplusPerWeek = calorieDeficitSurplusPerWeek;
        this.currentWeight = currentWeight;
        this.targetWeight = targetWeight;

        this.caloriesNeededPerDay = caloriesNeededPerDay;
        this.weekNeeded = weekNeeded;
        this.weightLossRatePerWeek = weightLossGainRateKgPerWeek;
    }

    public void recalculateCalories(double extraCalories){
        this.caloriesNeededPerDay = this.caloriesNeededPerDay + extraCalories;
        double calorieDeficitSurplusPerWeek = (this.bmr * this.activityLevelFactor - this.caloriesNeededPerDay) * 7;
        this.weightLossRatePerWeek = calorieDeficitSurplusPerWeek / 7700;
        this.weekNeeded = (int) ((this.currentWeight - this.targetWeight) / this.weightLossRatePerWeek);
    }

    public double calculateMaxExtraCalories() {
        double maxExtraCalories = 0;

        while (true) {
            double newCaloriesNeededPerDay = this.caloriesNeededPerDay + maxExtraCalories;
            double calorieDeficitSurplusPerWeek = (this.bmr * this.activityLevelFactor - newCaloriesNeededPerDay) * 7;
            double newWeightLossRatePerWeek = calorieDeficitSurplusPerWeek / 7700;

            if (newWeightLossRatePerWeek >= 0.1) {
                maxExtraCalories += 1; // Increase the maxExtraCalories by 1 (you can adjust the step size if needed)
            } else {
                break;
            }
        }

        return maxExtraCalories;
    }
}
