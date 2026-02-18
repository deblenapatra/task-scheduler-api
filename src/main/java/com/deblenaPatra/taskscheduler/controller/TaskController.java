package com.deblenaPatra.taskscheduler.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deblenaPatra.taskscheduler.model.Status;
import com.deblenaPatra.taskscheduler.model.Task;
import com.deblenaPatra.taskscheduler.service.TaskService;


@RestController
@RequestMapping("/tasks")
public class TaskController {

	private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.createTask(task), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Task get(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id,
                       @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public Task delete(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }

    @GetMapping
    public List<Task> list() {
        return taskService.listActiveTasks();
    }

    @PatchMapping("/{id}/status")
    public Task updateStatus(@PathVariable Long id,
                             @RequestBody Map<String, String> request) {
        return taskService.updateStatus(id,
                Status.valueOf(request.get("status")));
    }
}
