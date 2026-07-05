package com.disha.taskmanager.controller;

import com.disha.taskmanager.dto.LoginRequest;
import com.disha.taskmanager.dto.LoginResponse;
import com.disha.taskmanager.dto.RefreshTokenRequest;
import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

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
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {

        LoginResponse loginResponse = authService.login(request);

        Cookie cookie = new Cookie(
                "refreshToken",
                loginResponse.refreshToken()
        );

        cookie.setHttpOnly(true);

        cookie.setSecure(false); // true after HTTPS deployment

        cookie.setPath("/");

        cookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(cookie);

        return ResponseEntity.ok(
                new LoginResponse(
                        loginResponse.accessToken(),
                        null
                )
        );
    }
    @PostMapping("/refresh-token")
    public LoginResponse refreshToken(
            @CookieValue("refreshToken") String refreshToken) {

        return authService.refreshToken(
                new RefreshTokenRequest(refreshToken)
        );
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response) {

        authService.logout(refreshToken);

        Cookie cookie = new Cookie(
                "refreshToken",
                null
        );

        cookie.setHttpOnly(true);

        cookie.setPath("/");

        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok(
                "Logged out successfully"
        );
    }
}