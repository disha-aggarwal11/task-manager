package com.disha.taskmanager.controller;

import com.disha.taskmanager.entity.UserEntity;
import com.disha.taskmanager.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserEntity createUser(@RequestBody UserEntity user) {
        return service.createUser(user);
    }

    @GetMapping("/all")
    public List<UserEntity> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserEntity getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public UserEntity updateUser(@PathVariable Long id,
                                 @RequestBody UserEntity updatedUser) {

        return service.updateUser(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.delete(id);
    }
    @GetMapping
    public Page<UserEntity> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        return service.getUsers(page, size, sortBy);
    }
}