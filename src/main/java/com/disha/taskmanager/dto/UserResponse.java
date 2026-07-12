package com.disha.taskmanager.dto;

public record UserResponse(
        Long id,
        String username,
        String email
) {}