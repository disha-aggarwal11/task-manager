package com.disha.taskmanager.service;

import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserEntity createUser(UserEntity user) {

        if (user.getName() == null || user.getName().isBlank()) {
            throw new RuntimeException("Name cannot be empty.");
        }

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

        if (updateUser.getName() == null || updateUser.getName().isBlank()) {
            throw new RuntimeException("Name cannot be empty.");
        }

        UserEntity existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        existingUser.setName(updateUser.getName());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setPassword(updateUser.getPassword());

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