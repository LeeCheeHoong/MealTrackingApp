package com.example.mealtr.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MealDAO {
    @Insert
    long insertMeal(MealDB meal);
    @Query("SELECT * FROM MealDB")
    LiveData<List<MealDB>> getMeals();

    @Update
    void updateMeal(MealDB meal);

    @Query("SELECT * FROM MealDB WHERE mealId=:mealId")
    MealDB getMealById(long mealId);

    @Query("DELETE FROM MealDB WHERE menuId=:menuId")
    void deleteMealByMenuId(long menuId);

    @Query("SELECT * FROM MealDB WHERE menuId = :menuId ORDER BY "+
            "CASE " +
            "WHEN type = 'Breakfast' THEN 1 " +
            "WHEN type = 'Lunch' THEN 2 " +
            "WHEN type = 'Dinner' THEN 3 " +
            "ELSE 4 " +
            "END")
    LiveData<List<MealDB>> getMealsFromMenu(long menuId);
}