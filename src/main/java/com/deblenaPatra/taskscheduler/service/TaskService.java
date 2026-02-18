package com.deblenaPatra.taskscheduler.service;

import java.util.List;

import com.deblenaPatra.taskscheduler.model.Status;
import com.deblenaPatra.taskscheduler.model.Task;


public interface TaskService {

    Task createTask(Task task);

    Task getTask(Long id);

    Task updateTask(Long id, Task task);

    Task deleteTask(Long id);

    List<Task> listActiveTasks();

    Task updateStatus(Long id, Status newStatus);
}
