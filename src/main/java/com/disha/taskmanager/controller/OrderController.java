package com.disha.taskmanager.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    @GetMapping("/orders")
    public Map<String, Object> orders(Authentication authentication) {

        return Map.of(
                "user", authentication.getName(),
                "orders", List.of(
                        "Order-101",
                        "Order-102",
                        "Order-103"
                )
        );
    }
}
