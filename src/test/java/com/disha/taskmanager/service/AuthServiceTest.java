package com.disha.taskmanager.service;

import com.disha.taskmanager.dto.SignupRequest;
import com.disha.taskmanager.dto.UserResponse;
import com.disha.taskmanager.entity.Role;
import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.exception.InvalidCredentialsException;
import com.disha.taskmanager.repository.UserRepository;
import com.disha.taskmanager.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    // ===========================
    // Test 1 : Duplicate Email
    // ===========================
    @Test
    void signupShouldThrowExceptionWhenEmailAlreadyExists() {

        // Arrange
        SignupRequest request = new SignupRequest(
                "Kiara",
                "kiara@gmail.com",
                "kiara123"
        );

        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("kiara@gmail.com");

        when(repository.findByEmail(request.email()))
                .thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.signup(request)
        );

        // Verify save() is never called
        verify(repository, never()).save(any(UserEntity.class));
    }

    // ===========================
    // Test 2 : Successful Signup
    // ===========================
    @Test
    void signupShouldSaveUserSuccessfully() {

        // Arrange
        SignupRequest request = new SignupRequest(
                "Kiara",
                "kiara@gmail.com",
                "kiara123"
        );

        // Email does not exist
        when(repository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        // Mock password encoding
        when(passwordEncoder.encode(request.password()))
                .thenReturn("encodedPassword");

        // Fake saved user returned by repository
        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);
        savedUser.setUsername("Kiara");
        savedUser.setEmail("kiara@gmail.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.USER);

        when(repository.save(any(UserEntity.class)))
                .thenReturn(savedUser);

        // Act
        UserResponse response = authService.signup(request);

        // Capture the user passed to repository.save()
        ArgumentCaptor<UserEntity> captor =
                ArgumentCaptor.forClass(UserEntity.class);

        verify(repository).save(captor.capture());

        UserEntity capturedUser = captor.getValue();

        // Verify captured entity
        assertEquals("Kiara", capturedUser.getUsername());
        assertEquals("kiara@gmail.com", capturedUser.getEmail());
        assertEquals("encodedPassword", capturedUser.getPassword());
        assertEquals(Role.USER, capturedUser.getRole());

        // Verify returned response
        assertEquals(1L, response.id());
        assertEquals("Kiara", response.username());
        assertEquals("kiara@gmail.com", response.email());

        // Verify interactions
        verify(passwordEncoder).encode(request.password());
        verify(repository).findByEmail(request.email());
        verify(repository).save(any(UserEntity.class));
    }
}