package com.disha.taskmanager;

import org.springframework.stereotype.Service;
import java.util.*;

@Service public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository){
        this.repository=repository;
    }
    public Task createTask(Task task){
        if(task.getTitle()==null || task.getTitle().isBlank()){
            throw new RuntimeException("Title cannot be empty.");
        }
        if(task.getId()==null || repository.existsById(task.getId())){
            throw new RuntimeException("Id cannot be duplicated");
        }
    }
}
