package com.example.nutritionsporttracker.util;

import com.example.nutritionsporttracker.dto.DailySummaryDto;
import com.example.nutritionsporttracker.model.User;

import java.time.LocalDate;

public class DailyEmailTemplateBuilder {

    public static String build(User user, DailySummaryDto dto, String aiText, LocalDate date) {
        String fullName = (user == null) ? null : user.getFullName();
        String name = (fullName == null || fullName.isBlank()) ? "🌿" : fullName;

        return """
        <div style="font-family:Arial,sans-serif;max-width:620px;margin:0 auto;padding:16px;">
          <h2 style="margin:0 0 8px 0;">Günlük Sağlık Raporun — %s</h2>
          <p style="margin:0 0 16px 0;color:#444;">Merhaba <b>%s</b>, bugün için özetin burada:</p>

          <div style="display:flex;gap:10px;flex-wrap:wrap;margin-bottom:16px;">
            %s
          </div>

          <div style="padding:14px;border:1px solid #eee;border-radius:12px;background:#fafafa;">
            <h3 style="margin:0 0 8px 0;">AI Motivasyon</h3>
            <p style="white-space:pre-line;margin:0;color:#222;">%s</p>
          </div>

          <p style="margin-top:16px;color:#777;font-size:12px;">
            Nutri App • Otomatik günlük rapor
          </p>
        </div>
        """.formatted(
                date,
                escape(name),
                cards(dto),
                escape(aiText)
        );
    }

    private static String cards(DailySummaryDto dto) {
        return card("Kalori", String.valueOf((int) dto.getTotalCalories()))
                + card("Yakılan", String.valueOf((int) dto.getTotalBurned()))
                + card("Su (ml)", String.valueOf((int) dto.getTotalWater()))
                + card("Workout (dk)", String.valueOf((int) dto.getTotalWorkoutMinutes()));
    }

    private static String card(String title, String value) {
        return """
        <div style="flex:1;min-width:130px;padding:12px;border:1px solid #eee;border-radius:12px;">
          <div style="color:#666;font-size:12px;">%s</div>
          <div style="font-size:18px;font-weight:700;margin-top:4px;">%s</div>
        </div>
        """.formatted(escape(title), escape(value));
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}