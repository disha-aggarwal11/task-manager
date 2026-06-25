package com.disha.taskmanager.controller;

import com.disha.taskmanager.dto.LoginRequest;
import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> signup(@RequestBody UserEntity user) {

        UserEntity savedUser = service.signup(user);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserEntity> login(@RequestBody LoginRequest request) {

        UserEntity user = service.login(request);

        return ResponseEntity.ok(user);
    }
}