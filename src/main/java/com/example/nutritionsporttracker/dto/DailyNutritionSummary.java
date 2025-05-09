package com.example.nutritionsporttracker.dto;

public class DailyNutritionSummary {
    private int totalCalories;
    private double totalProtein;
    private double totalCarbs;
    private double totalFat;
    private int totalWaterIntakeMl;

    // --- Getter - Setter'lar ---

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    public double getTotalProtein() {
        return totalProtein;
    }

    public void setTotalProtein(double totalProtein) {
        this.totalProtein = totalProtein;
    }

    public double getTotalCarbs() {
        return totalCarbs;
    }

    public void setTotalCarbs(double totalCarbs) {
        this.totalCarbs = totalCarbs;
    }

    public double getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(double totalFat) {
        this.totalFat = totalFat;
    }

    public int getTotalWaterIntakeMl() {
        return totalWaterIntakeMl;
    }

    public void setTotalWaterIntakeMl(int totalWaterIntakeMl) {
        this.totalWaterIntakeMl = totalWaterIntakeMl;
    }
}
