package com.example.mealtr.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class MealDB {
    @PrimaryKey(autoGenerate = true)
    public long mealId;
    public long recipeId;
    public String type;
    public String image;
    public String title;
    public String sourceUrl;
    public long menuId;
    public double calories;
    public double protein;
    public double fat;

    public MealDB(long recipeId, String type, String image, String title, String sourceUrl, double calories, double protein, double fat) {
        this.type = type;
        this.recipeId = recipeId;
        this.image = image;
        this.title = title;
        this.sourceUrl = sourceUrl;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
    }
}
