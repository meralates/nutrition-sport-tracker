package com.example.nutritionsporttracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FoodItem {

    @JsonProperty("food_name")
    private String foodName;

    @JsonProperty("nf_calories")
    private double calories;

    @JsonProperty("nf_protein")
    private double protein;

    @JsonProperty("nf_total_carbohydrate")
    private double carbs;

    @JsonProperty("nf_total_fat")
    private double fat;

    public String getFoodName() { // ❌ getFood_name() değil, getFoodName()
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
}
