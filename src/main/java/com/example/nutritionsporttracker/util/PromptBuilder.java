package com.example.nutritionsporttracker.util;

import com.example.nutritionsporttracker.dto.AiRequestData;

public class PromptBuilder {

    private PromptBuilder() {
        // static class, nesne oluşturulmasın
    }

    public static String buildDailyPrompt(AiRequestData data) {
        return String.format("""
            Kullanıcı bilgileri:
            - Yaş: %d
            - Cinsiyet: %s
            - Boy: %d cm
            - Kilo: %d kg
            - Aktivite Seviyesi: %s
            - Hedef: %s

            Bugünkü veriler:
            - Alınan kalori: %d kcal
            - Yakılan kalori: %d kcal
            - Protein: %d g
            - Karbonhidrat: %d g
            - Yağ: %d g
            - Su tüketimi: %.1f litre
            - Yapılan egzersiz: %s

            Lütfen yukarıdaki verilere göre sağlıklı yaşam tavsiyesi ver. 
            Beslenme, su tüketimi ve egzersiz hakkında öneriler içersin.
            Cevap Türkçe ve kullanıcı dostu olsun.
            """,
                data.getAge(),
                data.getGender(),
                data.getHeight(),
                data.getWeight(),
                data.getActivityLevel(),
                data.getGoal(),
                data.getDailyCalories(),
                data.getCaloriesBurned(),
                data.getProtein(),
                data.getCarbs(),
                data.getFat(),
                data.getWaterIntake(),
                data.getExerciseDone()
        );
    }
}
