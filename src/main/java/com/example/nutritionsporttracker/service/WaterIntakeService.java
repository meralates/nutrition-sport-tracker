package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.WaterIntake;
import com.example.nutritionsporttracker.repository.WaterIntakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WaterIntakeService {

    @Autowired
    private WaterIntakeRepository waterIntakeRepository;

    public WaterIntake addWaterIntake(WaterIntake waterIntake) {
        return waterIntakeRepository.save(waterIntake);
    }

    public List<WaterIntake> getWaterIntakesByUserId(Long userId) {
        return waterIntakeRepository.findByUserId(userId);
    }
    public int calculateDailyWaterIntake(Long userId, LocalDate date) {
        List<WaterIntake> waterIntakes = getWaterIntakesByUserId(userId);

        return waterIntakes.stream()
                .filter(w -> w.getCreatedAt().toLocalDate().equals(date))
                .filter(w -> w.getAmountMl() != null) // Null olanları filtrele
                .mapToInt(WaterIntake::getAmountMl)
                .sum();
    }


}
