package com.example.nutritionsporttracker.dto;

import java.time.LocalDate;

public class DailySummaryDto {
    private Long userId;
    private String userName;
    private LocalDate date;
    private double totalCalories;
    private double totalBurned;
    private double totalWater;

    // Parametreli constructor
    public DailySummaryDto(Long userId, String userName, LocalDate date, double totalCalories, double totalBurned, double totalWater) {
        this.userId = userId;
        this.userName = userName;
        this.date = date;
        this.totalCalories = totalCalories;
        this.totalBurned = totalBurned;
        this.totalWater = totalWater;
    }

    // Boş constructor
    public DailySummaryDto() {
    }

    // Getters & Setters
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotalCalories() {
        return totalCalories;
    }
    public void setTotalCalories(double totalCalories) {
        this.totalCalories = totalCalories;
    }

    public double getTotalBurned() {
        return totalBurned;
    }
    public void setTotalBurned(double totalBurned) {
        this.totalBurned = totalBurned;
    }

    public double getTotalWater() {
        return totalWater;
    }
    public void setTotalWater(double totalWater) {
        this.totalWater = totalWater;
    }
}
