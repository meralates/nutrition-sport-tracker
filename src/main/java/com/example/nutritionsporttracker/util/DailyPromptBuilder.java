package com.example.nutritionsporttracker.util;

import com.example.nutritionsporttracker.model.DailySummary;
import com.example.nutritionsporttracker.model.User;

public class DailyPromptBuilder {

    public static String build(User user, DailySummary summary) {
        return """
                Merhaba! İşte kullanıcının günlük sağlık özeti:

                📅 Tarih: %s
                👤 İsim: %s
                🎯 Hedef: %s

                🍽️ Alınan Kalori: %.0f kcal
                🔥 Yakılan Kalori: %.0f kcal
                🚰 Su Tüketimi: %.0f ml

                Bu değerlere göre gününü analiz et ve kullanıcıya yarın için motivasyon verici, pratik ve bilimsel öneriler sun.
                """.formatted(
                summary.getDate(),
                user.getFullName(),
                user.getGoal() != null ? user.getGoal().name() : "BELİRSİZ",
                summary.getTotalCalories(),
                summary.getTotalBurned(),
                summary.getTotalWater()
        );
    }
}
