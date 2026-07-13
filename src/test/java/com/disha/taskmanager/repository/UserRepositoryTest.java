package com.disha.taskmanager.repository;

import com.disha.taskmanager.entity.Role;
import com.disha.taskmanager.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void shouldFindUserByEmail() {

        UserEntity user = new UserEntity();
        user.setUsername("Kiara");
        user.setEmail("kiara@gmail.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        repository.save(user);

        Optional<UserEntity> result =
                repository.findByEmail("kiara@gmail.com");

        assertTrue(result.isPresent());

        assertEquals(
                "Kiara",
                result.get().getUsername()
        );
    }

    @Test
    void shouldReturnEmptyWhenEmailDoesNotExist() {

        Optional<UserEntity> result =
                repository.findByEmail("abc@gmail.com");

        assertTrue(result.isEmpty());
    }

}