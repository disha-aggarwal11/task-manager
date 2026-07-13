package com.disha.taskmanager.service;

import com.disha.taskmanager.entity.RefreshToken;
import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(
                refreshTokenService,
                "refreshTokenDuration",
                86400000L
        );
    }
    @Test
    void shouldReturnRefreshTokenWhenTokenExists() {

        RefreshToken token = new RefreshToken();
        token.setToken("refresh-token");

        when(refreshTokenRepository.findByToken("refresh-token"))
                .thenReturn(Optional.of(token));

        Optional<RefreshToken> result =
                refreshTokenService.findByToken("refresh-token");

        assertEquals("refresh-token", result.get().getToken());

        verify(refreshTokenRepository)
                .findByToken("refresh-token");
    }
    @Test
    void shouldReturnEmptyWhenTokenDoesNotExist() {

        when(refreshTokenRepository.findByToken("wrong-token"))
                .thenReturn(Optional.empty());

        Optional<RefreshToken> result =
                refreshTokenService.findByToken("wrong-token");

        assertEquals(true, result.isEmpty());

        verify(refreshTokenRepository)
                .findByToken("wrong-token");
    }
    @Test
    void shouldCreateRefreshTokenSuccessfully() {

        // Arrange
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("kiara@gmail.com");

        when(refreshTokenRepository.findByUser(user))
                .thenReturn(Optional.empty());

        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RefreshToken token =
                refreshTokenService.createRefreshToken(user);

        // Capture saved token
        ArgumentCaptor<RefreshToken> captor =
                ArgumentCaptor.forClass(RefreshToken.class);

        verify(refreshTokenRepository).save(captor.capture());

        RefreshToken savedToken = captor.getValue();

        // Assert
        assertEquals(user, savedToken.getUser());

        assertNotNull(savedToken.getToken());

        assertNotNull(savedToken.getExpiryDate());

        assertEquals(savedToken, token);
    }
    @Test
    void shouldDeleteExistingRefreshTokenBeforeCreatingNewOne() {

        UserEntity user = new UserEntity();

        RefreshToken oldToken = new RefreshToken();

        when(refreshTokenRepository.findByUser(user))
                .thenReturn(Optional.of(oldToken));

        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        refreshTokenService.createRefreshToken(user);

        verify(refreshTokenRepository).delete(oldToken);

        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }
    @Test
    void shouldReturnTokenWhenNotExpired() {

        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().plusSeconds(3600));

        RefreshToken result =
                refreshTokenService.verifyExpiration(token);

        assertEquals(token, result);

        verify(refreshTokenRepository, never())
                .delete(any());
    }
    @Test
    void shouldDeleteExpiredTokenAndThrowException() {

        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().minusSeconds(60));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> refreshTokenService.verifyExpiration(token)
                );

        assertEquals(
                "Refresh Token Expired",
                exception.getMessage()
        );

        verify(refreshTokenRepository)
                .delete(token);
    }
    @Test
    void shouldDeleteRefreshTokenByUser() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        refreshTokenService.deleteByUser(user);

        verify(refreshTokenRepository)
                .deleteByUser(user);
    }

}