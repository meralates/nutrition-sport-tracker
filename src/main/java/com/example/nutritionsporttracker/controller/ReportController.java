package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.model.Reports;
import com.example.nutritionsporttracker.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    //  GET http://localhost:8080/api/report/weekly
    // Header: Authorization: Bearer <jwt>
    @GetMapping("/weekly")
    public ResponseEntity<Reports> weekly(Principal principal) {
        String email = principal.getName(); // JWT -> username/email
        Reports report = reportService.generateWeeklyReport(email);
        return ResponseEntity.ok(report);
    }
}
