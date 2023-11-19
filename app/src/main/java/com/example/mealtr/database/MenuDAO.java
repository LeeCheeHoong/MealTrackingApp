package com.example.mealtr.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mealtr.mealGenerator.Menu;

import java.util.List;

@Dao
public interface MenuDAO {
    @Insert
    long insertMenu(MenuDB menu);
    @Query("SELECT * FROM MenuDB")
    LiveData<List<MenuDB>> getMenus();

    @Query("DELETE FROM MenuDB WHERE dietId=:dietId")
    void deleteMenu(long dietId);

    @Query("SELECT * FROM MenuDB WHERE dietId = :dietId ORDER BY "+
            "CASE " +
            "WHEN day = 'Monday' THEN 1 " +
            "WHEN day = 'Tuesday' THEN 2 " +
            "WHEN day = 'Wednesday' THEN 3 " +
            "WHEN day = 'Thursday' THEN 4 " +
            "WHEN day = 'Friday' THEN 5 " +
            "WHEN day = 'Saturday' THEN 6 " +
            "WHEN day = 'Sunday' THEN 7 " +
            "ELSE 8 " +
            "END")
    LiveData<List<MenuDB>> getMenusFromDiet(long dietId);

    @Query("SELECT * FROM MenuDB WHERE dietId = :dietId")
    List<MenuDB> getMenusFromDietStatic(long dietId);
}
