# Development Notes

## Overall Approach
The project was developed in small steps starting with creating a Spring Boot application.
First, the basic project structure was set up along with Maven dependencies.
Then the Task entity and enums for status and priority were created.
After that, the repository, service layer, and controller were implemented.
Validation, exception handling, and unit tests were added at the end.
All APIs were tested manually using Postman.

## Key Design Decisions
- Followed a layered architecture using controller, service, and repository layers.
- Used a service interface to keep the controller independent from the implementation.
- Implemented soft delete instead of deleting records permanently.
- Used enums for task status and priority to restrict invalid values.
- Handled errors using custom exceptions and a global exception handler.

## Trade-offs Considered
- Authentication and authorization were not implemented as they were not required.
- Used an in-memory database for simplicity and faster development.
- Validation was kept simple without using advanced validation frameworks.

## Assumptions
- Tasks can only move through valid status transitions.
- Deleted tasks should not be returned in the task list.
- The application is intended for internal use and trusted clients.
