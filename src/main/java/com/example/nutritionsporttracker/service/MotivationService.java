package com.example.nutritionsporttracker.service;

import com.example.nutritionsporttracker.model.Motivation;
import com.example.nutritionsporttracker.repository.MotivationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MotivationService {

    private final MotivationRepository motivationRepository;

    public MotivationService(MotivationRepository motivationRepository) {
        this.motivationRepository = motivationRepository;
    }

    public Motivation getRandomMotivation() {
        List<Long> ids = motivationRepository.findActiveIds();
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("Aktif motivasyon bulunamadı.");
        }

        int index = ThreadLocalRandom.current().nextInt(ids.size());
        Long randomId = ids.get(index);

        Motivation m = motivationRepository.findByIdAndIsActiveTrue(randomId);
        if (m == null) {
            // çok nadir: kayıt o anda pasife çekildiyse fallback
            return getRandomMotivation();
        }
        return m;
    }
}