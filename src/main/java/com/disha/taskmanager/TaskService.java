package com.disha.taskmanager;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskEntity createTask(TaskEntity task) {

        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new RuntimeException("Title cannot be empty.");
        }

        if (task.getDescription() == null || task.getDescription().isBlank()) {
            throw new RuntimeException("Description cannot be empty.");
        }

        return repository.save(task);
    }

    public List<TaskEntity> getAllTasks() {
        return repository.findAll();
    }

    public TaskEntity getById(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Task not found.");
        }

        return repository.findById(id);
    }

    public TaskEntity updateTask(Long id, TaskEntity updatedTask) {

        if (updatedTask.getTitle() == null || updatedTask.getTitle().isBlank()) {
            throw new RuntimeException("Title cannot be empty.");
        }

        if (updatedTask.getDescription() == null || updatedTask.getDescription().isBlank()) {
            throw new RuntimeException("Description cannot be empty.");
        }

        if (!repository.existsById(id)) {
            throw new RuntimeException("Task not found.");
        }

        return repository.update(id, updatedTask);
    }

    public boolean delete(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Task not found.");
        }

        return repository.deleteById(id);
    }
}
