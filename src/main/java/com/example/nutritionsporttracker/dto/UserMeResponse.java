package com.example.nutritionsporttracker.dto;

import com.example.nutritionsporttracker.model.User;

public class UserMeResponse {
    private Long id;
    private String email;
    private String fullName;
    private Integer age;

    private Double weight;
    private User.ActivityLevel activityLevel;
    private User.Goal goal;

    private Double dailyCalories;

    public static UserMeResponse from(User u) {
        UserMeResponse r = new UserMeResponse();
        r.id = u.getId();
        r.email = u.getEmail();
        r.fullName = u.getFullName();
        r.age = u.getAge();
        r.weight = u.getWeight();
        r.activityLevel = u.getActivityLevel();
        r.goal = u.getGoal();
        r.dailyCalories = u.getDailyCalories();
        return r;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public User.ActivityLevel getActivityLevel() { return activityLevel; }
    public void setActivityLevel(User.ActivityLevel activityLevel) { this.activityLevel = activityLevel; }

    public User.Goal getGoal() { return goal; }
    public void setGoal(User.Goal goal) { this.goal = goal; }

    public Double getDailyCalories() { return dailyCalories; }
    public void setDailyCalories(Double dailyCalories) { this.dailyCalories = dailyCalories; }
}
