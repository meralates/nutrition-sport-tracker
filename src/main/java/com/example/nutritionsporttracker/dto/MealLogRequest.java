package com.example.nutritionsporttracker.dto;

import com.example.nutritionsporttracker.model.MealTimeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MealLogRequest {

    @NotBlank
    private String foodName;

    @NotNull
    private MealTimeType mealTime;

    @Positive
    private double grams;

    @Positive
    private double calories;

    private double protein;
    private double carbs;
    private double fat;

    private String sourceFoodId;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public MealTimeType getMealTime() {
        return mealTime;
    }

    public void setMealTime(MealTimeType mealTime) {
        this.mealTime = mealTime;
    }

    public double getGrams() {
        return grams;
    }

    public void setGrams(double grams) {
        this.grams = grams;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public String getSourceFoodId() {
        return sourceFoodId;
    }

    public void setSourceFoodId(String sourceFoodId) {
        this.sourceFoodId = sourceFoodId;
    }
}