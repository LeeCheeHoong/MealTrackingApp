package com.example.mealtr.mealGenerator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class WeeklyMenu {
    @DayOrder(1)
    public DailyMenu monday;
    @DayOrder(2)
    public DailyMenu tuesday;
    @DayOrder(3)
    public DailyMenu wednesday;
    @DayOrder(4)
    public DailyMenu thursday;
    @DayOrder(5)
    public DailyMenu friday;
    @DayOrder(6)
    public DailyMenu saturday;
    @DayOrder(7)
    public DailyMenu sunday;
    public WeeklyMenu(){}

    public WeeklyMenu(DailyMenu monday, DailyMenu tuesday, DailyMenu wednesday, DailyMenu thursday, DailyMenu friday, DailyMenu saturday, DailyMenu sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public DailyMenu getDailyMenu(String day) {
        switch (day){
            case "monday":
            default:
                return this.monday;
            case "tuesday":
                return this.tuesday;
            case "wednesday":
                return this.wednesday;
            case "thursday":
                return this.thursday;
            case "friday":
                return this.friday;
            case "saturday":
                return this.saturday;
            case "sunday":
                return this.sunday;
        }
    }
}
