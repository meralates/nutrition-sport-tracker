package com.example.nutritionsporttracker.dto;

import com.example.nutritionsporttracker.model.User;

public class UpdateMeRequest {
    private Double weight;
    private User.ActivityLevel activityLevel;
    private User.Goal goal;

    // mevcut ekranda var diye ekledim; istemezsen silebilirsin
    private Double dailyCalories;

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public User.ActivityLevel getActivityLevel() { return activityLevel; }
    public void setActivityLevel(User.ActivityLevel activityLevel) { this.activityLevel = activityLevel; }

    public User.Goal getGoal() { return goal; }
    public void setGoal(User.Goal goal) { this.goal = goal; }

    public Double getDailyCalories() { return dailyCalories; }
    public void setDailyCalories(Double dailyCalories) { this.dailyCalories = dailyCalories; }
}
