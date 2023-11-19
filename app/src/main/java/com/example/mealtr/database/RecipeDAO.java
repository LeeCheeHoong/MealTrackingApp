package com.example.mealtr.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RecipeDB> recipes);

    @Query("SELECT t1.recipeId as recipeId, t1.title, t1.calories, t1.type,\n" +
            "      t2.recipeId as recipeId2,t2.title as title2, t2.calories as calories2, t2.type as type2,\n" +
            "      t3.recipeId as recipeId3,t3.title as title3, t3.calories as calories3, t3.type as type3\n" +
            "FROM RecipeDB t1, RecipeDB t2, RecipeDB t3\n" +
            "WHERE t1.type = 'breakfast'\n" +
            "  AND t2.type = 'meal'\n" +
            "  AND t3.type = 'meal'\n" +
            "  AND ABS(t1.calories + t2.calories + t3.calories - :calories) <= 50\n" +
            "ORDER BY RANDOM()" +
            "LIMIT 1;")
    List<GeneratedRecipe> getRecipesWithCalories(double calories);

    @Query("SELECT * FROM RecipeDB WHERE recipeId=:recipeId")
    RecipeDB getRecipeById(long recipeId);

    @Query("SELECT * FROM RecipeDB WHERE calories BETWEEN :calories -50 AND :calories + 50\n" +
            "ORDER BY RANDOM() " +
            "LIMIT 10;")
    LiveData<List<RecipeDB>> getSimilarRecipeByCalories(double calories);
}