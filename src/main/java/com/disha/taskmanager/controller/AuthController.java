package com.disha.taskmanager.controller;

import com.disha.taskmanager.dto.LoginRequest;
import com.disha.taskmanager.dto.LoginResponse;
import com.disha.taskmanager.dto.RefreshTokenRequest;
import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> signup(@RequestBody UserEntity user) {

        UserEntity savedUser = authService.signup(user);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        return authService.login(request);
    }
    @PostMapping("/refresh-token")
    public LoginResponse refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {

        return authService.refreshToken(request);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestBody RefreshTokenRequest request
    ) {

        authService.logout(request.refreshToken());

        return ResponseEntity.ok("Logged out successfully");
    }
}