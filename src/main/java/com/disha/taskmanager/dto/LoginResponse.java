package com.disha.taskmanager.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}