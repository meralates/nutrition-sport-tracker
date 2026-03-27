package com.example.nutritionsporttracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyNutritionSummary {

    @JsonProperty("totalCalories")
    private double totalCalories;

    @JsonProperty("totalProtein")
    private double totalProtein;

    @JsonProperty("totalCarbs")
    private double totalCarbs;

    @JsonProperty("totalFat")
    private double totalFat;

    @JsonProperty("totalWaterIntakeMl")
    private int totalWaterIntakeMl;

    @JsonProperty("totalWorkoutMinutes")
    private int totalWorkoutMinutes; // yeni

    @JsonProperty("totalCaloriesBurned")
    private int totalCaloriesBurned; // yeni

    // No-args constructor
    public DailyNutritionSummary() {
    }

    // All-args constructor
    public DailyNutritionSummary(double totalCalories,
                                 double totalProtein,
                                 double totalCarbs,
                                 double totalFat,
                                 int totalWaterIntakeMl,
                                 int totalWorkoutMinutes,
                                 int totalCaloriesBurned) {
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalCarbs = totalCarbs;
        this.totalFat = totalFat;
        this.totalWaterIntakeMl = totalWaterIntakeMl;
        this.totalWorkoutMinutes = totalWorkoutMinutes;
        this.totalCaloriesBurned = totalCaloriesBurned;
    }

    // --- Getter & Setter'lar ---

    public double getTotalCalories() { return totalCalories; }
    public void setTotalCalories(double totalCalories) { this.totalCalories = totalCalories; }

    public double getTotalProtein() { return totalProtein; }
    public void setTotalProtein(double totalProtein) { this.totalProtein = totalProtein; }

    public double getTotalCarbs() { return totalCarbs; }
    public void setTotalCarbs(double totalCarbs) { this.totalCarbs = totalCarbs; }

    public double getTotalFat() { return totalFat; }
    public void setTotalFat(double totalFat) { this.totalFat = totalFat; }

    public int getTotalWaterIntakeMl() { return totalWaterIntakeMl; }
    public void setTotalWaterIntakeMl(int totalWaterIntakeMl) { this.totalWaterIntakeMl = totalWaterIntakeMl; }

    public int getTotalWorkoutMinutes() { return totalWorkoutMinutes; }
    public void setTotalWorkoutMinutes(int totalWorkoutMinutes) { this.totalWorkoutMinutes = totalWorkoutMinutes; }

    public int getTotalCaloriesBurned() { return totalCaloriesBurned; }
    public void setTotalCaloriesBurned(int totalCaloriesBurned) { this.totalCaloriesBurned = totalCaloriesBurned; }
}
