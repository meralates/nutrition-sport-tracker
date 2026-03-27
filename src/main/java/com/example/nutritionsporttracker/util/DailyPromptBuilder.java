package com.example.nutritionsporttracker.util;

import com.example.nutritionsporttracker.model.DailySummary;
import com.example.nutritionsporttracker.model.User;

public class DailyPromptBuilder {

    public static String build(User user, DailySummary summary) {

        String name = (user.getFullName() == null || user.getFullName().isBlank())
                ? "Kullanıcı"
                : user.getFullName();

        String goal = (user.getGoal() != null)
                ? user.getGoal().name()
                : "BELİRSİZ";

        return """
                Sen deneyimli bir beslenme ve egzersiz koçusun.

                Aşağıdaki günlük sağlık verilerine göre kullanıcıyı analiz et.
                Cevabın:
                - Kısa bir genel değerlendirme
                - 2-3 somut öneri
                - Motive edici bir kapanış cümlesi
                - Türkçe yaz
                - Çok uzun olmasın (maksimum 8-10 cümle)

                ---- KULLANICI VERİLERİ ----
                Tarih: %s
                İsim: %s
                Hedef: %s

                Alınan Kalori: %.0f kcal
                Yakılan Kalori: %.0f kcal
                Su Tüketimi: %.0f ml
                --------------------------------
                """.formatted(
                summary.getDate(),
                name,
                goal,
                summary.getTotalCalories(),
                summary.getTotalBurned(),
                summary.getTotalWater()
        );
    }
}