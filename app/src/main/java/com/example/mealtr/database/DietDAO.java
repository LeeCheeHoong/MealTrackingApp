package com.example.mealtr.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface DietDAO {
    @Insert
    long insertDiet(DietDB diet);
    @Query("DELETE FROM DietDB WHERE dietId = :dietId")
    void deleteDiet(long dietId);
    @Query("SELECT * FROM DietDB")
    LiveData<List<DietDB>> getDiets();
}
