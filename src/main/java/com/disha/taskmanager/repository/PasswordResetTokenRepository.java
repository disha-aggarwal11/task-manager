package com.disha.taskmanager.repository;

import com.disha.taskmanager.entity.PasswordResetToken;
import com.disha.taskmanager.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(UserEntity user);

    @Transactional
    @Modifying
    void deleteByUser(UserEntity user);
}