# Task Scheduler API

This project is a simple Task Scheduler REST API built using Spring Boot.
It allows users to create tasks, update them, track status changes, and
soft delete tasks. The application follows basic REST principles and
includes validation and error handling.

## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- H2 Database (for local testing)
- JUnit 5
- Mockito
- Postman (for API testing)

## Features
- Create a new task
- Get task by id
- List all active tasks
- Update task details
- Update task status using a defined state flow
- Soft delete tasks
- Input validation and proper exception handling

## API Endpoints
- POST /tasks – create a task
- GET  /tasks – list all active tasks
- GET  /tasks/{id} – get task by id
- PUT  /tasks/{id} – update task details
- PATCH /tasks/{id}/status – update task status
- DELETE /tasks/{id} – soft delete task

## Task Status Flow
- PENDING → IN_PROGRESS → COMPLETED
- PENDING → CANCELLED
- IN_PROGRESS → CANCELLED

Invalid status transitions are restricted.

## How to Run the Project
1. Run- mvn clean install
2. Start the application using- TaskSchedulerApplication
3. Use Postman to test the APIs

## Testing
Basic unit tests are written for the service layer using JUnit and Mockito.
APIs were manually tested using Postman.
