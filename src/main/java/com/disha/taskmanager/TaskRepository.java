package com.disha.taskmanager;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {

    private List<Task> tasks = new ArrayList<>();
    private Long nextId = 1L;

    // Create
    public Task save(Task task) {
        task.setId(nextId);
        nextId++;

        tasks.add(task);
        return task;
    }

    // Read All
    public List<Task> findAll() {
        return tasks;
    }

    // Read By Id
    public Task findById(Long id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }

    // Check if task exists
    public boolean existsById(Long id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    // Update
    public Task update(Long id, Task updatedTask) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                task.setTitle(updatedTask.getTitle());
                task.setDescription(updatedTask.getDescription());
                task.setCompleted(updatedTask.isCompleted());

                return task;
            }
        }
        return null;
    }

    // Delete
    public boolean deleteById(Long id) {

        boolean deleted = tasks.removeIf(task -> task.getId().equals(id));

        if (tasks.isEmpty()) {
            nextId = 1L;
        }

        return deleted;
    }
}
