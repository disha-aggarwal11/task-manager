package com.disha.taskmanager.repository;

import com.disha.taskmanager.entity.RefreshToken;
import com.disha.taskmanager.entity.Role;
import com.disha.taskmanager.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindRefreshTokenByToken() {

        UserEntity user = new UserEntity();
        user.setUsername("Kiara");
        user.setEmail("kiara@gmail.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        user = userRepository.save(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");
        refreshToken.setExpiryDate(Instant.now().plusSeconds(3600));
        refreshToken.setUser(user);

        repository.save(refreshToken);

        Optional<RefreshToken> result =
                repository.findByToken("refresh-token");

        assertTrue(result.isPresent());
        assertEquals("refresh-token", result.get().getToken());
    }

    @Test
    void shouldFindRefreshTokenByUser() {

        UserEntity user = new UserEntity();
        user.setUsername("Kiara");
        user.setEmail("kiara@gmail.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        user = userRepository.save(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");
        refreshToken.setExpiryDate(Instant.now().plusSeconds(3600));
        refreshToken.setUser(user);

        repository.save(refreshToken);

        Optional<RefreshToken> result =
                repository.findByUser(user);

        assertTrue(result.isPresent());
        assertEquals(user.getEmail(),
                result.get().getUser().getEmail());
    }

    @Test
    void shouldReturnEmptyWhenTokenNotFound() {

        Optional<RefreshToken> result =
                repository.findByToken("wrong-token");

        assertTrue(result.isEmpty());
    }
}