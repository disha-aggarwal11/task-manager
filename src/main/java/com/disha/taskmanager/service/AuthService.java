package com.disha.taskmanager.service;

import com.disha.taskmanager.dto.LoginRequest;
import com.disha.taskmanager.dto.LoginResponse;
import com.disha.taskmanager.dto.RefreshTokenRequest;
import com.disha.taskmanager.entity.RefreshToken;
import com.disha.taskmanager.entity.Role;
import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.exception.InvalidCredentialsException;
import com.disha.taskmanager.repository.UserRepository;
import com.disha.taskmanager.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.disha.taskmanager.dto.SignupRequest;
import com.disha.taskmanager.dto.UserResponse;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository repository,
                       PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

        this.refreshTokenService = refreshTokenService;
    }

    // Signup
    public UserResponse signup(SignupRequest request) {

        if (repository.findByEmail(request.email()).isPresent()) {
            throw new InvalidCredentialsException("Email already exists.");
        }

        UserEntity user = new UserEntity();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        UserEntity savedUser = repository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    // Login
    public LoginResponse login(LoginRequest request) {

        System.out.println("LOGIN API HIT");

        UserEntity user = repository.findByEmail(request.email())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String accessToken = jwtService.generateToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken()
        );
    }

    public LoginResponse refreshToken(
            RefreshTokenRequest request
    ) {

        RefreshToken refreshToken = refreshTokenService
                .findByToken(request.refreshToken())
                .orElseThrow(() ->
                        new RuntimeException("Refresh Token not found"));

        refreshTokenService.verifyExpiration(refreshToken);

        UserEntity user = refreshToken.getUser();

        String accessToken =
                jwtService.generateAccessToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken()
        );
    }
    public void logout(String refreshToken) {

        RefreshToken token =
                refreshTokenService.findByToken(refreshToken)
                        .orElseThrow(() ->
                                new RuntimeException("Token not found"));

        refreshTokenService.deleteByUser(
                token.getUser()
        );
    }
    public LoginResponse loginWithUser(UserEntity user) {

        String accessToken = jwtService.generateAccessToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken()
        );
    }
}