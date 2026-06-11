package com.disha.taskmanager;

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
    public Task createTask(@RequestBody Task task) {
        return service.createTask(task);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return service.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public Task updateTask(@PathVariable Long id,
                           @RequestBody Task updatedTask) {

        return service.updateTask(id, updatedTask);
    }

    @DeleteMapping("/{id}")
    public boolean deleteTask(@PathVariable Long id) {
        return service.delete(id);
    }
}
