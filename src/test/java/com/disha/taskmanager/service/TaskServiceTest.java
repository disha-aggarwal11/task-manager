package com.disha.taskmanager.service;

import com.disha.taskmanager.dto.TaskRequest;
import com.disha.taskmanager.dto.TaskResponse;
import com.disha.taskmanager.entity.TaskEntity;
import com.disha.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTaskShouldSaveTaskSuccessfully() {

        TaskRequest request = new TaskRequest(
                "Learn Spring",
                "Finish Mockito",
                false
        );

        TaskEntity savedTask = new TaskEntity();
        savedTask.setId(1L);
        savedTask.setTitle("Learn Spring");
        savedTask.setDescription("Finish Mockito");
        savedTask.setCompleted(false);

        when(repository.save(any(TaskEntity.class)))
                .thenReturn(savedTask);

        TaskResponse response = taskService.createTask(request);

        assertEquals(1L, response.id());
        assertEquals("Learn Spring", response.title());
        assertEquals("Finish Mockito", response.description());
        assertFalse(response.completed());

        verify(repository).save(any(TaskEntity.class));
    }

    @Test
    void getAllTasksShouldReturnTaskList() {

        TaskEntity task1 = new TaskEntity(1L,"Task 1","Desc 1",false);
        TaskEntity task2 = new TaskEntity(2L,"Task 2","Desc 2",true);

        when(repository.findAll())
                .thenReturn(List.of(task1, task2));

        List<TaskResponse> result = taskService.getAllTasks();

        assertEquals(2, result.size());

        assertEquals("Task 1", result.get(0).title());
        assertEquals("Task 2", result.get(1).title());

        verify(repository).findAll();
    }

    @Test
    void getAllTasksShouldReturnEmptyList() {

        when(repository.findAll())
                .thenReturn(List.of());

        List<TaskResponse> result = taskService.getAllTasks();

        assertTrue(result.isEmpty());

        verify(repository).findAll();
    }

    @Test
    void getTaskByIdShouldReturnTask() {

        TaskEntity task = new TaskEntity(
                1L,
                "Learn Spring",
                "Practice Mockito",
                false
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(task));

        TaskResponse response =
                taskService.getById(1L);

        assertEquals(1L,response.id());
        assertEquals("Learn Spring",response.title());
        assertEquals("Practice Mockito",response.description());
        assertFalse(response.completed());

        verify(repository).findById(1L);
    }

    @Test
    void getTaskByIdShouldThrowExceptionWhenTaskNotFound() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> taskService.getById(1L)
                );

        assertEquals("Task not found.", exception.getMessage());

        verify(repository).findById(1L);
    }

    @Test
    void updateTaskShouldUpdateSuccessfully() {

        TaskEntity existingTask =
                new TaskEntity(
                        1L,
                        "Old",
                        "Old Desc",
                        false
                );

        TaskRequest request =
                new TaskRequest(
                        "New",
                        "New Desc",
                        true
                );

        when(repository.findById(1L))
                .thenReturn(Optional.of(existingTask));

        when(repository.save(any(TaskEntity.class)))
                .thenReturn(existingTask);

        TaskResponse response =
                taskService.updateTask(1L, request);

        assertEquals("New", response.title());
        assertEquals("New Desc", response.description());
        assertTrue(response.completed());

        verify(repository).findById(1L);
        verify(repository).save(any(TaskEntity.class));
    }

    @Test
    void updateTaskShouldThrowExceptionWhenTaskNotFound() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        TaskRequest request =
                new TaskRequest(
                        "New",
                        "New Desc",
                        true
                );

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> taskService.updateTask(1L, request)
                );

        assertEquals("Task not found.", exception.getMessage());

        verify(repository).findById(1L);

        verify(repository, never())
                .save(any(TaskEntity.class));
    }

    @Test
    void deleteShouldDeleteTaskSuccessfully() {

        when(repository.existsById(1L))
                .thenReturn(true);

        taskService.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteShouldThrowExceptionWhenTaskNotFound() {

        when(repository.existsById(1L))
                .thenReturn(false);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> taskService.delete(1L)
                );

        assertEquals("Task not found.", exception.getMessage());

        verify(repository, never())
                .deleteById(anyLong());
    }

    @Test
    void getTasksShouldReturnPagedTasks() {

        TaskEntity task =
                new TaskEntity(
                        1L,
                        "Spring",
                        "Pagination",
                        false
                );

        Page<TaskEntity> page =
                new PageImpl<>(List.of(task));

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<TaskResponse> result =
                taskService.getTasks(
                        0,
                        10,
                        "title"
                );

        assertEquals(1, result.getTotalElements());

        assertEquals(
                "Spring",
                result.getContent().get(0).title()
        );

        verify(repository)
                .findAll(any(Pageable.class));
    }

}