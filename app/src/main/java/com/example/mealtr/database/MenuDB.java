package com.example.mealtr.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.Date;
import java.util.List;

@Entity
public class MenuDB {
    @PrimaryKey(autoGenerate = true)
    public long menuId;
    public String day;
    public long dietId;
}