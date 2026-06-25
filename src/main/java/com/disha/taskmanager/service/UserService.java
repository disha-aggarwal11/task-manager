package com.disha.taskmanager.service;

import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;


    public UserEntity createUser(UserEntity user) {

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new RuntimeException("Name cannot be empty.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }

    public List<UserEntity> getAllUsers() {
        return repository.findAll();
    }

    public UserEntity getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    public UserEntity updateUser(Long id, UserEntity updateUser) {

        if (updateUser.getUsername() == null || updateUser.getUsername().isBlank()) {
            throw new RuntimeException("Name cannot be empty.");
        }

        UserEntity existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        existingUser.setUsername(updateUser.getUsername());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setPassword(
                passwordEncoder.encode(updateUser.getPassword())
        );
        return repository.save(existingUser);
    }

    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("User not found.");
        }

        repository.deleteById(id);
    }
    public Page<UserEntity> getUsers(int page, int size, String sortBy) {

        return repository.findAll(
                PageRequest.of(page, size, Sort.by(sortBy))
        );
    }
}