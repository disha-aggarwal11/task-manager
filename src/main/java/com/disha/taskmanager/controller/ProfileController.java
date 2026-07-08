package com.disha.taskmanager.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProfileController {

    @GetMapping("/profile")
    public Map<String, Object> profile(Authentication authentication) {

        return Map.of(
                "message", "Welcome to your profile",
                "user", authentication.getName()
        );
    }
}
