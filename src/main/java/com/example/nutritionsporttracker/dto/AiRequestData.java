package com.example.nutritionsporttracker.dto;

public class AiRequestData {
    private int age;
    private String gender;
    private int height;
    private int weight;
    private String activityLevel;
    private String goal;
    private int dailyCalories;
    private int caloriesBurned;
    private int protein;
    private int carbs;
    private int fat;
    private double waterIntake;
    private String exerciseDone;

    // --- Constructor ---
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

    // --- Getters and Setters ---
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
