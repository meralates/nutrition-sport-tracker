package com.example.nutritionsporttracker.dto;

import com.example.nutritionsporttracker.model.MealTimeType;

public class MealLogResponse {

    private Long id;
    private String foodName;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
    private double grams;
    private MealTimeType mealTime;
    private String createdAt;

    public MealLogResponse() {}

    public MealLogResponse(Long id,
                           String foodName,
                           double calories,
                           double protein,
                           double carbs,
                           double fat,
                           double grams,
                           MealTimeType mealTime,
                           String createdAt) {
        this.id = id;
        this.foodName = foodName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.grams = grams;
        this.mealTime = mealTime;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
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

    public double getGrams() {
        return grams;
    }

    public void setGrams(double grams) {
        this.grams = grams;
    }

    public MealTimeType getMealTime() {
        return mealTime;
    }

    public void setMealTime(MealTimeType mealTime) {
        this.mealTime = mealTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}