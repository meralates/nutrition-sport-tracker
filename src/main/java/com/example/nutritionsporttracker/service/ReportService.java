package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.Reports;
import com.example.nutritionsporttracker.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Reports addReport(Reports report) {
        return reportRepository.save(report);
    }

    public List<Reports> getReportsByUserId(Long userId) {
        return reportRepository.findByUserId(userId);
    }
}
