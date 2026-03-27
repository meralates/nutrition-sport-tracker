package com.example.nutritionsporttracker.repository;

import com.example.nutritionsporttracker.model.Motivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MotivationRepository extends JpaRepository<Motivation, Long> {

    @Query("select m.id from Motivation m where m.isActive = true")
    List<Long> findActiveIds();

    // opsiyonel: id ile aktif kaydı çekmek için
    Motivation findByIdAndIsActiveTrue(Long id);
}