package com.disha.taskmanager.service;

import com.disha.taskmanager.entity.TaskEntity;
import com.disha.taskmanager.repository.TaskRepository;
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

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found."));
    }

    public TaskEntity updateTask(Long id, TaskEntity updatedTask) {

        if (updatedTask.getTitle() == null || updatedTask.getTitle().isBlank()) {
            throw new RuntimeException("Title cannot be empty.");
        }

        if (updatedTask.getDescription() == null || updatedTask.getDescription().isBlank()) {
            throw new RuntimeException("Description cannot be empty.");
        }

        TaskEntity existingTask = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found."));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());
        existingTask.setUser(updatedTask.getUser());

        return repository.save(existingTask);
    }

    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Task not found.");
        }

        repository.deleteById(id);
    }
}