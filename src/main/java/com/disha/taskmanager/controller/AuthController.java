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
import com.disha.taskmanager.dto.ForgotPasswordRequest;
import com.disha.taskmanager.dto.ResetPasswordRequest;
import com.disha.taskmanager.service.EmailService;
import com.disha.taskmanager.service.PasswordResetService;
import jakarta.validation.Valid;
import com.disha.taskmanager.dto.SignupRequest;
import com.disha.taskmanager.dto.UserResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    private final EmailService emailService;

    public AuthController(
            AuthService authService,
            PasswordResetService passwordResetService,
            EmailService emailService
    ) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
        this.emailService = emailService;
    }
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @Valid @RequestBody SignupRequest request) {

        return ResponseEntity.ok(authService.signup(request));
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
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) {

        String token = passwordResetService.createPasswordResetToken(
                request.email()
        );

        emailService.sendPasswordResetEmail(
                request.email(),
                token
        );

        return ResponseEntity.ok(
                "Password reset email sent successfully."
        );
    }
    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(

            @PathVariable String token,

            @RequestBody ResetPasswordRequest request
    ) {

        passwordResetService.resetPassword(
                token,
                request.newPassword()
        );

        return ResponseEntity.ok(
                "Password updated successfully."
        );
    }
}