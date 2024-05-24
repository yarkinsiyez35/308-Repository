# CS308 Term Project - Backend Repository

This is the backend repository of the term project in CS308.

## Prerequisites

- MySQL Workbench
- An IDE of your choice (e.g., IntelliJ IDEA, Eclipse)

## Getting Started

### Database Setup

1. Run `db_script.sql` in MySQL Workbench to set up the database schema.
2. Run `db_population.sql` in MySQL Workbench to fill the database with dummy data.

### Running the Application

1. Open the **Flight Scheduler** folder in your preferred IDE.
2. Run the application as a Spring Boot application.

## Project Structure

The project follows a structured organization based on the roles of the components. Here is a brief overview of the folders and their purposes:

- **Repository**: Contains JPARepository interfaces for database access.
- **Entity**: Contains the entity classes representing the database tables.
- **Service**: Contains the services that facilitate communication between the controllers and the repositories.
- **Controller**: Contains the controllers that expose RESTful endpoints for the frontend.
- **DTO**: Contains Data Transfer Objects for transferring data between the backend and frontend.
- **Security**: Contains security-related components including services, controllers, DTOs, configuration, and utilities.

### Detailed Folder Breakdown

- **Repository**:
  - JPARepository interfaces for database access.
  
- **Entity**:
  - Entity classes representing the database tables.
  
- **Service**:
  - Services for communication between controllers and repositories.
  
- **Controller**:
  - Controllers exposing RESTful endpoints.
  
- **DTO**:
  - Data Transfer Objects for communication between backend and frontend.
  
- **Security**:
  - **Service**: Security-related services.
  - **Controller**: Security-related controllers.
  - **DTO**: Security-related Data Transfer Objects.
  - **Config**: Configuration for security settings.
  - **Utils**: Utility classes, such as RSA key generation.


## License

This project is licensed under the MIT License.
