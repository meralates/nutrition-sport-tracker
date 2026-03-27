package com.example.nutritionsporttracker.dto;

import java.time.LocalDate;

public class DailySummaryDto {

    private Long userId;
    private LocalDate date;
    private double totalCalories;
    private double totalBurned;
    private double protein;
    private double carbs;
    private double fat;
    private double totalWater; // ml (double’a çevrildi)
    private double totalWorkoutMinutes;
    private String exerciseSummary; // örn: "Koşu 30dk, Ağırlık 20dk"

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getTotalCalories() { return totalCalories; }
    public void setTotalCalories(double totalCalories) { this.totalCalories = totalCalories; }

    public double getTotalBurned() { return totalBurned; }
    public void setTotalBurned(double totalBurned) { this.totalBurned = totalBurned; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }

    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }

    public double getTotalWater() { return totalWater; }
    public void setTotalWater(double totalWater) { this.totalWater = totalWater; }

    public double getTotalWorkoutMinutes() { return totalWorkoutMinutes; }
    public void setTotalWorkoutMinutes(double totalWorkoutMinutes) { this.totalWorkoutMinutes = totalWorkoutMinutes; }

    public String getExerciseSummary() { return exerciseSummary; }
    public void setExerciseSummary(String exerciseSummary) { this.exerciseSummary = exerciseSummary; }
}
