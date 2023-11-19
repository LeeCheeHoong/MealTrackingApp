package com.example.mealtr.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class RecipeDB {
    @PrimaryKey
    public long recipeId;
    public String type;
    public String image;
    public String title;
    public String sourceUrl;
    public double calories;
    public double protein;
    public double fat;

    public RecipeDB(long recipeId, String type, String image, String title, String sourceUrl, double calories, double protein, double fat) {
        this.recipeId = recipeId;
        this.type = type;
        this.image = image;
        this.title = title;
        this.sourceUrl = sourceUrl;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
    }
}

