package com.disha.taskmanager;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class TaskRepository {
    private List<Task> tasks = new ArrayList<>();

    public Task save(Task task){
        tasks.add(task);
        return task;
    }
    public List<Task> findAll(){
        return tasks;
    }
}
