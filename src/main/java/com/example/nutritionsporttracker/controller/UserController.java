package com.example.nutritionsporttracker.controller;

import com.example.nutritionsporttracker.dto.UpdateMeRequest;
import com.example.nutritionsporttracker.dto.UserMeResponse;
import com.example.nutritionsporttracker.model.User;
import com.example.nutritionsporttracker.security.CustomUserDetails;
import com.example.nutritionsporttracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getMe(@AuthenticationPrincipal CustomUserDetails me) {
        return userService.findByEmail(me.getUsername())
                .map(UserMeResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/me")
    public ResponseEntity<UserMeResponse> updateMe(
            @AuthenticationPrincipal CustomUserDetails me,
            @RequestBody UpdateMeRequest req
    ) {
        User updated = userService.updateMeByEmail(me.getUsername(), req);
        return ResponseEntity.ok(UserMeResponse.from(updated));
    }
}
