package com.example.mealtr.calorieCalculator;

import android.util.Log;

public class BMICalculator {
    public String type = "";

    public boolean isValid(Double height, Double weight, Double targetWeight) {
        double heightInM = Double.parseDouble(String.valueOf(height/100)) ;

        if(targetWeight < weight  && (weight / (heightInM * heightInM)) < 18.5){
            // Not recommended if underweight
            this.type = "underweight";
            return false;
        } else if (targetWeight > weight && (weight / (heightInM * heightInM)) > 25) {
            // Not recommended if overweight
            this.type = "overweight";
            return false;
        } else {
            return true;
        }
    }
}
