package com.disha.taskmanager;

import com.disha.taskmanager.entity.TaskEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {

    private List<TaskEntity> tasks = new ArrayList<>();
    private Long nextId = 1L;

    // Create
    public TaskEntity save(TaskEntity task) {
        task.setId(nextId);
        nextId++;

        tasks.add(task);
        return task;
    }

    // Read All
    public List<TaskEntity> findAll() {
        return tasks;
    }

    // Read By Id
    public TaskEntity findById(Long id) {
        for (TaskEntity task : tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }

    // Check if task exists
    public boolean existsById(Long id) {
        for (TaskEntity task : tasks) {
            if (task.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    // Update
    public TaskEntity update(Long id, TaskEntity updatedTask) {
        for (TaskEntity task : tasks) {
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
