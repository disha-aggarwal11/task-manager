package com.disha.taskmanager.service;

import com.disha.taskmanager.entity.TaskEntity;
import com.disha.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import com.disha.taskmanager.dto.TaskRequest;
import com.disha.taskmanager.dto.TaskResponse;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskResponse createTask(TaskRequest request) {

        TaskEntity task = new TaskEntity();

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setCompleted(request.completed());

        TaskEntity savedTask = repository.save(task);

        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.isCompleted()
        );
    }
    public List<TaskResponse> getAllTasks() {

        return repository.findAll()
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted()
                ))
                .toList();
    }
    public TaskResponse getById(Long id) {

        TaskEntity task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found."));

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );
    }

    public TaskResponse updateTask(Long id,
                                   TaskRequest request) {

        TaskEntity existingTask = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found."));

        existingTask.setTitle(request.title());
        existingTask.setDescription(request.description());
        existingTask.setCompleted(request.completed());

        TaskEntity updatedTask = repository.save(existingTask);

        return new TaskResponse(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.isCompleted()
        );
    }

    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Task not found.");
        }

        repository.deleteById(id);
    }
    public Page<TaskResponse> getTasks(int page,
                                       int size,
                                       String sortBy) {

        return repository.findAll(
                PageRequest.of(page, size, Sort.by(sortBy))
        ).map(task -> new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        ));
    }
}