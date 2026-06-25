package com.disha.taskmanager.service;

import com.disha.taskmanager.dto.LoginRequest;
import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.exception.InvalidCredentialsException;
import com.disha.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository repository,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // Signup
    public UserEntity signup(UserEntity user) {

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new InvalidCredentialsException("Username cannot be empty.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidCredentialsException("Email cannot be empty.");
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new InvalidCredentialsException("Password must be at least 6 characters.");
        }

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new InvalidCredentialsException("Email already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }

    // Login
    public UserEntity login(LoginRequest request) {

        UserEntity user = repository.findByEmail(request.email())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return user;
    }
}