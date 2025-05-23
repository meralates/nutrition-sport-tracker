package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.service.UserService;
import com.example.nutritionsporttracker.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtProvider;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> foundUser = userService.findByEmail(user.getEmail());

        if (foundUser.isPresent()) {
            String rawPassword = user.getPassword();
            String encodedPassword = foundUser.get().getPassword();

            System.out.println("Girilen Şifre: " + rawPassword);
            System.out.println("Veritabanındaki Hashlenmiş Şifre: " + encodedPassword);

            if (passwordEncoder.matches(rawPassword, encodedPassword)) {
                String token = jwtProvider.generateToken(foundUser.get().getEmail());
                return ResponseEntity.ok(token);
            } else {
                System.out.println("Şifre eşleşmedi!");
            }
        } else {
            System.out.println("Kullanıcı bulunamadı!");
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

}
