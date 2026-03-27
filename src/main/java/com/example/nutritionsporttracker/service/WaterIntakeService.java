package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.WaterIntake;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.repository.UserRepository;
import com.example.nutritionsporttracker.repository.WaterIntakeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WaterIntakeService {

    private final WaterIntakeRepository waterIntakeRepository;
    private final UserRepository userRepository;

    public WaterIntakeService(WaterIntakeRepository waterIntakeRepository,
                              UserRepository userRepository) {
        this.waterIntakeRepository = waterIntakeRepository;
        this.userRepository = userRepository;
    }

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
                .filter(w -> w.getAmountMl() != null)
                .mapToInt(WaterIntake::getAmountMl)
                .sum();
    }

    public void deleteWaterIntake(Long waterIntakeId, Long userId) {
        WaterIntake waterIntake = waterIntakeRepository.findById(waterIntakeId)
                .orElseThrow(() -> new RuntimeException("Su kaydı bulunamadı"));

        if (waterIntake.getUser() == null || !waterIntake.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu su kaydını silme yetkiniz yok");
        }

        waterIntakeRepository.delete(waterIntake);
    }
}