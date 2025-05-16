package com.example.nutritionsporttracker.dto;

public class LLMRequestData {

    private int age;
    private double weight;
    private double height;

    private int totalCalories;
    private double totalProtein;
    private double totalCarbs;
    private double totalFat;
    private int totalWaterIntakeMl;

    public LLMRequestData() {
        // No-args constructor
    }

    public LLMRequestData(int age,
                          double weight,
                          double height,
                          int totalCalories,
                          double totalProtein,
                          double totalCarbs,
                          double totalFat,
                          int totalWaterIntakeMl) {
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalCarbs = totalCarbs;
        this.totalFat = totalFat;
        this.totalWaterIntakeMl = totalWaterIntakeMl;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

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
