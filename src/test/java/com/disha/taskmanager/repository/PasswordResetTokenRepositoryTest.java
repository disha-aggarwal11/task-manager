package com.disha.taskmanager.repository;

import com.disha.taskmanager.entity.PasswordResetToken;
import com.disha.taskmanager.entity.Role;
import com.disha.taskmanager.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PasswordResetTokenRepositoryTest {

    @Autowired
    private PasswordResetTokenRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindPasswordResetTokenByToken() {

        UserEntity user = new UserEntity();
        user.setUsername("Kiara");
        user.setEmail("kiara@gmail.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        user = userRepository.save(user);

        PasswordResetToken token =
                new PasswordResetToken();

        token.setToken("reset-token");
        token.setUser(user);
        token.setExpiryDate(
                LocalDateTime.now().plusMinutes(15)
        );

        repository.save(token);

        Optional<PasswordResetToken> result =
                repository.findByToken("reset-token");

        assertTrue(result.isPresent());

        assertEquals(
                "reset-token",
                result.get().getToken()
        );
    }

    @Test
    void shouldFindPasswordResetTokenByUser() {

        UserEntity user = new UserEntity();
        user.setUsername("Kiara");
        user.setEmail("kiara@gmail.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        user = userRepository.save(user);

        PasswordResetToken token =
                new PasswordResetToken();

        token.setToken("reset-token");
        token.setUser(user);
        token.setExpiryDate(
                LocalDateTime.now().plusMinutes(15)
        );

        repository.save(token);

        Optional<PasswordResetToken> result =
                repository.findByUser(user);

        assertTrue(result.isPresent());

        assertEquals(
                user.getEmail(),
                result.get().getUser().getEmail()
        );
    }

    @Test
    void shouldReturnEmptyWhenTokenNotFound() {

        Optional<PasswordResetToken> result =
                repository.findByToken("wrong-token");

        assertTrue(result.isEmpty());
    }
}