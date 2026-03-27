package com.example.nutritionsporttracker.dto;

import com.example.nutritionsporttracker.model.User;
import jakarta.validation.constraints.*;

public class RegisterRequest {
    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 8, max = 64)
    private String password;

    @NotBlank @Size(min = 2, max = 100)
    private String fullName;

    @NotNull @Min(12) @Max(100)
    private Integer age;

    @NotNull @Positive
    private Double weight;

    @NotNull @Positive
    private Double height;

    @NotBlank
    private String gender;

    @NotNull
    private User.ActivityLevel activityLevel;

    @NotNull
    private User.Goal goal;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public User.ActivityLevel getActivityLevel() { return activityLevel; }
    public void setActivityLevel(User.ActivityLevel activityLevel) { this.activityLevel = activityLevel; }

    public User.Goal getGoal() { return goal; }
    public void setGoal(User.Goal goal) { this.goal = goal; }
}
