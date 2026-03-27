package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.DailySummaryDto;
import com.example.nutritionsporttracker.service.DailySummaryService;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/summary")
@Validated
public class DailySummaryController {

    private final DailySummaryService dailySummaryService;

    public DailySummaryController(DailySummaryService dailySummaryService) {
        this.dailySummaryService = dailySummaryService;
    }

    // ✅ GET /api/summary/today
    @GetMapping("/today")
    public ResponseEntity<DailySummaryDto> today(Principal principal) {
        String email = principal.getName(); // JWT'den gelen username/email
        return dailySummaryService.getTodaySummaryDto(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ GET /api/summary/yesterday
    @GetMapping("/yesterday")
    public ResponseEntity<DailySummaryDto> yesterday(Principal principal) {
        String email = principal.getName();
        return dailySummaryService.getYesterdaySummaryDto(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ GET /api/summary/daily?date=2026-01-14
    @GetMapping("/daily")
    public ResponseEntity<DailySummaryDto> daily(
            Principal principal,
            @RequestParam
            @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "date format must be yyyy-MM-dd")
            String date
    ) {
        String email = principal.getName();
        LocalDate localDate = LocalDate.parse(date);

        return dailySummaryService.getDailySummaryDto(email, localDate)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
