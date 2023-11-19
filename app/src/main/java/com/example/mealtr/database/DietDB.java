package com.example.mealtr.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DietDB {
    @PrimaryKey(autoGenerate = true)
    public long dietId;
    public String dietName;
    public double calories;
    public String createdDate;
    public String targetWeight;
    public String planDuration;
}
