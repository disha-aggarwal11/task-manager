package com.disha.taskmanager.service;

import com.disha.taskmanager.entity.RefreshToken;
import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenDuration;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(UserEntity user) {

        Optional<RefreshToken> existingToken =
                refreshTokenRepository.findByUser(user);

        existingToken.ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);

        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken.setExpiryDate(
                Instant.now().plusMillis(refreshTokenDuration)
        );

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {

        return refreshTokenRepository.findByToken(token);
    }

    public boolean isExpired(RefreshToken token) {

        return token.getExpiryDate().isBefore(Instant.now());
    }

    public void deleteByUser(UserEntity user) {

        refreshTokenRepository.deleteByUser(user);
    }
}