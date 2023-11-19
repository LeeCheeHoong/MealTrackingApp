package com.example.mealtr.mealGenerator;

public class Meal {
    public String type;
    public long id;
    public String image;
    public String title;
    public String sourceUrl;
    public double calories;
    public double protein;
    public double fat;

    public Meal(String type, long id, String image, String title,  String sourceURL, double calories, double protein, double fat) {
        this.type = type;
        this.id = id;
        this.image = image;
        this.title = title;
        this.sourceUrl = sourceURL;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
    }
}
