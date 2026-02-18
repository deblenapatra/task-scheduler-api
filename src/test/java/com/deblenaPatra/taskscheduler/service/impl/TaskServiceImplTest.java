package com.deblenaPatra.taskscheduler.service.impl;

import com.deblenaPatra.taskscheduler.exception.InvalidStatusTransitionException;
import com.deblenaPatra.taskscheduler.exception.TaskNotFoundException;
import com.deblenaPatra.taskscheduler.model.Priority;
import com.deblenaPatra.taskscheduler.model.Status;
import com.deblenaPatra.taskscheduler.model.Task;
import com.deblenaPatra.taskscheduler.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskServiceImpl service;

    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.PENDING);
        task.setDeleted(false);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
    }

    @Test
    void createTask_shouldSetDefaults() {
        when(repository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Task created = service.createTask(task);

        assertEquals(Status.PENDING, created.getStatus());
        assertFalse(created.getDeleted());
        assertNotNull(created.getCreatedAt());
        assertNotNull(created.getUpdatedAt());
    }

    @Test
    void getTask_shouldReturnTask() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(task));

        Task result = service.getTask(1L);

        assertEquals("Test Task", result.getTitle());
    }

    @Test
    void getTask_notFound_shouldThrow() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> service.getTask(99L));
    }

    @Test
    void deleteTask_shouldSoftDelete() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(task));
        when(repository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Task deleted = service.deleteTask(1L);

        assertTrue(deleted.getDeleted());
    }

    @Test
    void listActiveTasks_shouldExcludeDeleted() {
        Task deletedTask = new Task();
        deletedTask.setDeleted(true);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(deletedTask);

        when(repository.findAll()).thenReturn(tasks);

        List<Task> result = service.listActiveTasks();

        assertEquals(1, result.size());
    }

    @Test
    void updateStatus_validTransition_shouldWork() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(task));
        when(repository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Task updated = service.updateStatus(1L, Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, updated.getStatus());
    }

    @Test
    void updateStatus_invalidTransition_shouldThrow() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(task));

        assertThrows(InvalidStatusTransitionException.class,
                () -> service.updateStatus(1L, Status.COMPLETED));
    }
}
