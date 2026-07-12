package com.disha.taskmanager.controller;

import com.disha.taskmanager.entity.TaskEntity;
import com.disha.taskmanager.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.disha.taskmanager.dto.TaskRequest;
import com.disha.taskmanager.dto.TaskResponse;
import jakarta.validation.Valid;
import com.disha.taskmanager.dto.TaskRequest;
import com.disha.taskmanager.dto.TaskResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service){
        this.service=service;
    }

    @PostMapping
    public TaskResponse createTask(
            @Valid @RequestBody TaskRequest request) {

        return service.createTask(request);
    }

    @GetMapping("/all")
    public List<TaskResponse> getAllTasks() {
        return service.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {

        return service.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        service.delete(id);
    }
    @GetMapping
    public Page<TaskResponse> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        return service.getTasks(page, size, sortBy);
    }
}
