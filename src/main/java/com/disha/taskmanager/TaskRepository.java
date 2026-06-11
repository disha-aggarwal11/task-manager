package com.disha.taskmanager;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TaskRepository {

    private List<Task> tasks = new ArrayList<>();

    // Create
    public Task save(Task task) {
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
        return tasks.removeIf(task -> task.getId().equals(id));
    }
}
