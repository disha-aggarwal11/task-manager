package com.disha.taskmanager.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DashboardController {

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(Authentication authentication) {

        return Map.of(
                "message", "Dashboard Loaded",
                "user", authentication.getName()
        );
    }
}