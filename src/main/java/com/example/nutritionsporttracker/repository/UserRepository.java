package com.example.nutritionsporttracker.repository;

import com.example.nutritionsporttracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // E-posta ile kullanıcı arama
}
