package com.deblenaPatra.taskscheduler.service.impl;

import com.deblenaPatra.taskscheduler.exception.InvalidStatusTransitionException;
import com.deblenaPatra.taskscheduler.exception.TaskNotFoundException;
import com.deblenaPatra.taskscheduler.model.Priority;
import com.deblenaPatra.taskscheduler.model.Status;
import com.deblenaPatra.taskscheduler.model.Task;
import com.deblenaPatra.taskscheduler.repository.TaskRepository;
import com.deblenaPatra.taskscheduler.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Task createTask(Task task) {

        validateTaskInput(task);

        task.setStatus(Status.PENDING);
        task.setDeleted(false);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());

        return repository.save(task);
    }

    @Override
    public Task getTask(Long id) {

        Task task = repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task not found with id: " + id));

        if (Boolean.TRUE.equals(task.getDeleted())) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }

        return task;
    }

    @Override
    public Task updateTask(Long id, Task updatedTask) {

        Task existingTask = getTask(id);

        validateTaskInput(updatedTask);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setUpdatedAt(Instant.now());

        return repository.save(existingTask);
    }

    @Override
    public Task deleteTask(Long id) {

        Task task = getTask(id);

        task.setDeleted(true);
        task.setUpdatedAt(Instant.now());

        return repository.save(task);
    }

    @Override
    public List<Task> listActiveTasks() {

        return repository.findAll()
                .stream()
                .filter(task -> !Boolean.TRUE.equals(task.getDeleted()))
                .collect(Collectors.toList());
    }

    @Override
    public Task updateStatus(Long id, Status newStatus) {

        if (newStatus == null) {
            throw new RuntimeException("status must be a valid value");
        }

        Task task = getTask(id);

        validateTransition(task.getStatus(), newStatus);

        task.setStatus(newStatus);
        task.setUpdatedAt(Instant.now());

        return repository.save(task);
    }

    private void validateTaskInput(Task task) {

        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new RuntimeException("title is required");
        }

        if (task.getTitle().length() > 100) {
            throw new RuntimeException("title must be at most 100 characters");
        }

        if (task.getPriority() == null) {
            throw new RuntimeException("priority must be LOW, MEDIUM, or HIGH");
        }
    }

    private void validateTransition(Status current, Status next) {

        if (current == Status.COMPLETED) {
            throw new InvalidStatusTransitionException(
                    "Cannot transition from COMPLETED");
        }

        if (current == Status.CANCELLED) {
            throw new InvalidStatusTransitionException(
                    "Cannot transition from CANCELLED");
        }

        if (current == Status.PENDING &&
                (next == Status.IN_PROGRESS || next == Status.CANCELLED)) {
            return;
        }

        if (current == Status.IN_PROGRESS &&
                (next == Status.COMPLETED || next == Status.CANCELLED)) {
            return;
        }

        throw new InvalidStatusTransitionException(
                "Cannot transition from " + current + " to " + next);
    }
}
