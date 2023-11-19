package com.example.mealtr.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mealtr.mealGenerator.Menu;

@Database(entities = {MealDB.class, MenuDB.class, DietDB.class, RecipeDB.class}, version = 1)
public abstract class MenuDatabase extends RoomDatabase {
    private static MenuDatabase INSTANCE;
    public static synchronized MenuDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MenuDatabase.class, "menu_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
    public abstract MenuDAO menuDAO();
    public abstract DietDAO dietDAO();
    public abstract MealDAO mealDAO();
    public abstract RecipeDAO recipeDAO();
}
