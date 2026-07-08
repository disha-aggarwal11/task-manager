package com.disha.taskmanager.service;

import com.disha.taskmanager.entity.PasswordResetToken;
import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.repository.PasswordResetTokenRepository;
import com.disha.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Generate Password Reset Token
    public String createPasswordResetToken(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Delete old reset token if present
        tokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken =
                new PasswordResetToken(
                        token,
                        user,
                        LocalDateTime.now().plusMinutes(15)
                );

        tokenRepository.save(resetToken);

        return token;
    }

    // Reset Password
    public void resetPassword(
            String token,
            String newPassword
    ) {

        PasswordResetToken resetToken =
                tokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            tokenRepository.delete(resetToken);

            throw new RuntimeException("Reset token expired");
        }

        UserEntity user = resetToken.getUser();

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }
}