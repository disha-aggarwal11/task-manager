package com.disha.taskmanager.repository;

import com.disha.taskmanager.entity.RefreshToken;
import com.disha.taskmanager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(UserEntity user);

    void deleteByUser(UserEntity user);
}