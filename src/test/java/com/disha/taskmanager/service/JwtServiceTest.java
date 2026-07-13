package com.disha.taskmanager.security;

import com.disha.taskmanager.entity.Role;
import com.disha.taskmanager.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private UserEntity user;

    @BeforeEach
    void setUp() {

        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secretKey",
                "ThisIsASecretKeyForJwtTestingWhichIsAtLeast32CharactersLong"
        );

        ReflectionTestUtils.setField(
                jwtService,
                "jwtExpiration",
                3600000L
        );

        user = new UserEntity();
        user.setId(1L);
        user.setUsername("Kiara");
        user.setEmail("kiara@gmail.com");
        user.setRole(Role.USER);
    }

    @Test
    void generateTokenShouldReturnToken() {

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractEmailShouldReturnCorrectEmail() {

        String token = jwtService.generateToken(user);

        String email = jwtService.extractEmail(token);

        assertEquals("kiara@gmail.com", email);
    }

    @Test
    void extractRoleShouldReturnCorrectRole() {

        String token = jwtService.generateToken(user);

        String role = jwtService.extractRole(token);

        assertEquals("USER", role);
    }

    @Test
    void tokenShouldBeValidForCorrectUser() {

        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void tokenShouldBeInvalidForDifferentUser() {

        String token = jwtService.generateToken(user);

        UserEntity anotherUser = new UserEntity();
        anotherUser.setEmail("another@gmail.com");

        assertFalse(jwtService.isTokenValid(token, anotherUser));
    }

    @Test
    void generateAccessTokenShouldReturnToken() {

        String token = jwtService.generateAccessToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

}