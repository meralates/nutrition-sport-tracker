package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.User;
import org.springframework.stereotype.Service;

@Service
public class CalorieTargetService {

    public double calculate(User user) {
        // Güvenlik
        if (user.getWeight() == null || user.getHeight() == null || user.getAge() == null) {
            return 2200; // fallback
        }

        // 1️⃣ BMR – Mifflin St Jeor
        double bmr;
        if ("FEMALE".equalsIgnoreCase(user.getGender())) {
            bmr = 10 * user.getWeight()
                    + 6.25 * user.getHeight()
                    - 5 * user.getAge()
                    - 161;
        } else {
            bmr = 10 * user.getWeight()
                    + 6.25 * user.getHeight()
                    - 5 * user.getAge()
                    + 5;
        }

        // 2️⃣ Aktivite çarpanı
        double activityMultiplier = switch (user.getActivityLevel()) {
            case SEDENTARY -> 1.2;
            case LIGHTLY_ACTIVE -> 1.375;
            case MODERATELY_ACTIVE -> 1.55;
            case VERY_ACTIVE -> 1.725;
            case SUPER_ACTIVE -> 1.9;
        };

        double tdee = bmr * activityMultiplier;

        // 3️⃣ Hedef ayarlaması
        double goalAdjustment = switch (user.getGoal()) {
            case WEIGHT_LOSS -> -500;
            case WEIGHT_GAIN -> +400;
            case MAINTAIN_WEIGHT -> 0;
        };

        return Math.round(tdee + goalAdjustment);
    }
}
