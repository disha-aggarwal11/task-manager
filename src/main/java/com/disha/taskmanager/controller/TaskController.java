package com.disha.taskmanager.controller;

import com.disha.taskmanager.entity.TaskEntity;
import com.disha.taskmanager.service.TaskService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service){
        this.service=service;
    }
    @PostMapping
    public TaskEntity createTask(@RequestBody TaskEntity task) {
        return service.createTask(task);
    }

    @GetMapping
    public List<TaskEntity> getAllTasks() {
        return service.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskEntity getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public TaskEntity updateTask(@PathVariable Long id,
                                            @RequestBody TaskEntity updatedTask) {

        return service.updateTask(id, updatedTask);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        service.delete(id);
    }
}
