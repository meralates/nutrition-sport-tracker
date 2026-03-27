package com.example.nutritionsporttracker.dto;

import jakarta.validation.constraints.*;

/**
 * Kullanıcının günlük verilerinden yapay zeka önerisi üretmek için kullanılan DTO.
 * Tüm sayısal değerler makul sınırlarla doğrulanır.
 */
public class AiRequestData {

    @Min(12) @Max(100)
    private int age;

    @NotBlank
    private String gender;

    @Min(100) @Max(250)
    private int height; // cm

    @Min(30) @Max(300)
    private int weight; // kg

    @NotBlank
    private String activityLevel; // örn: "MODERATE"

    @NotBlank
    private String goal; // örn: "FAT_LOSS"

    @PositiveOrZero
    private int dailyCalories;

    @PositiveOrZero
    private int caloriesBurned;

    @PositiveOrZero
    private int protein;

    @PositiveOrZero
    private int carbs;

    @PositiveOrZero
    private int fat;

    @DecimalMin(value = "0.0") @DecimalMax(value = "10.0")
    private double waterIntake; // litre

    @Size(max = 200)
    private String exerciseDone; // örn: "30dk koşu, 20dk ağırlık"

    // ---- Constructors ----
    public AiRequestData() {
    }

    public AiRequestData(int age, String gender, int height, int weight, String activityLevel,
                         String goal, int dailyCalories, int caloriesBurned,
                         int protein, int carbs, int fat, double waterIntake,
                         String exerciseDone) {
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.dailyCalories = dailyCalories;
        this.caloriesBurned = caloriesBurned;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.waterIntake = waterIntake;
        this.exerciseDone = exerciseDone;
    }

    // ---- Getters & Setters ----
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public int getDailyCalories() { return dailyCalories; }
    public void setDailyCalories(int dailyCalories) { this.dailyCalories = dailyCalories; }

    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }

    public int getProtein() { return protein; }
    public void setProtein(int protein) { this.protein = protein; }

    public int getCarbs() { return carbs; }
    public void setCarbs(int carbs) { this.carbs = carbs; }

    public int getFat() { return fat; }
    public void setFat(int fat) { this.fat = fat; }

    public double getWaterIntake() { return waterIntake; }
    public void setWaterIntake(double waterIntake) { this.waterIntake = waterIntake; }

    public String getExerciseDone() { return exerciseDone; }
    public void setExerciseDone(String exerciseDone) { this.exerciseDone = exerciseDone; }
}
