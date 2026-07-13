package com.disha.taskmanager.service;

import com.disha.taskmanager.entity.PasswordResetToken;
import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.repository.PasswordResetTokenRepository;
import com.disha.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetService passwordResetService;

    // ==========================================
    // createPasswordResetToken()
    // ==========================================

    @Test
    void createPasswordResetTokenShouldGenerateTokenSuccessfully() {

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("kiara@gmail.com");

        when(userRepository.findByEmail("kiara@gmail.com"))
                .thenReturn(Optional.of(user));

        when(tokenRepository.save(any(PasswordResetToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String token =
                passwordResetService.createPasswordResetToken(
                        "kiara@gmail.com"
                );

        assertNotNull(token);

        ArgumentCaptor<PasswordResetToken> captor =
                ArgumentCaptor.forClass(PasswordResetToken.class);

        verify(tokenRepository).save(captor.capture());

        PasswordResetToken savedToken =
                captor.getValue();

        assertEquals(user, savedToken.getUser());

        assertEquals(token, savedToken.getToken());

        assertNotNull(savedToken.getExpiryDate());

        verify(tokenRepository).deleteByUser(user);

        verify(userRepository).findByEmail("kiara@gmail.com");
    }

    @Test
    void createPasswordResetTokenShouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> passwordResetService
                                .createPasswordResetToken(
                                        "unknown@gmail.com"
                                )
                );

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(tokenRepository, never())
                .save(any());

        verify(tokenRepository, never())
                .deleteByUser(any());
    }

    // ==========================================
    // resetPassword()
    // ==========================================

    @Test
    void resetPasswordShouldUpdatePasswordSuccessfully() {

        UserEntity user = new UserEntity();
        user.setPassword("oldPassword");

        PasswordResetToken token =
                new PasswordResetToken();

        token.setToken("reset-token");
        token.setUser(user);
        token.setExpiryDate(
                LocalDateTime.now().plusMinutes(15)
        );

        when(tokenRepository.findByToken("reset-token"))
                .thenReturn(Optional.of(token));

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("encodedPassword");

        passwordResetService.resetPassword(
                "reset-token",
                "newPassword"
        );

        assertEquals(
                "encodedPassword",
                user.getPassword()
        );

        verify(passwordEncoder)
                .encode("newPassword");

        verify(userRepository)
                .save(user);

        verify(tokenRepository)
                .delete(token);
    }

    @Test
    void resetPasswordShouldThrowExceptionWhenTokenInvalid() {

        when(tokenRepository.findByToken("wrong-token"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> passwordResetService.resetPassword(
                                "wrong-token",
                                "password123"
                        )
                );

        assertEquals(
                "Invalid token",
                exception.getMessage()
        );

        verify(userRepository, never())
                .save(any());

        verify(tokenRepository, never())
                .delete(any());
    }

    @Test
    void resetPasswordShouldThrowExceptionWhenTokenExpired() {

        UserEntity user = new UserEntity();

        PasswordResetToken token =
                new PasswordResetToken();

        token.setUser(user);

        token.setExpiryDate(
                LocalDateTime.now().minusMinutes(5)
        );

        when(tokenRepository.findByToken("expired-token"))
                .thenReturn(Optional.of(token));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> passwordResetService.resetPassword(
                                "expired-token",
                                "password123"
                        )
                );

        assertEquals(
                "Reset token expired",
                exception.getMessage()
        );

        verify(tokenRepository)
                .delete(token);

        verify(userRepository, never())
                .save(any());
    }

}