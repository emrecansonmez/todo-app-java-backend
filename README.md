
# THY Todo App

## Project Overview
THY Todo App is a backend application that allows users to manage tasks by creating, listing, updating, and deleting them. Users can register, log in, and manage their own tasks securely using JWT (JSON Web Token)-based authentication.

## Technologies Used
The following technologies and tools are used in this project:
- **Java 17**: Main programming language.
- **Spring Boot**: Framework for building the backend application.
- **Spring Security**: Used for JWT-based authentication and security.
- **JWT (JSON Web Tokens)**: Used for secure user authentication.
- **Hibernate / JPA**: ORM for database operations.
- **H2 Embedded Database**: In-memory database for data storage.
- **JUnit & Mockito**: Used for testing.
- **Lombok**: Reduces boilerplate code by generating getters, setters, etc.

## Project Structure
The project is structured as follows:

```bash
src/
│
├── main/
│   ├── java/
│   │   └── com/
│   │       └── thy/
│   │           └── todoapp/
│   │               ├── configs/          # Configuration classes
│   │               │   └── ApplicationConfiguration.java
│   │               │   └── JwtAuthenticationFilter.java
│   │               │   └── SecurityConfiguration.java
│   │               ├── controllers/      # API Controllers
│   │               │   └── AuthenticationController.java
│   │               │   └── TaskController.java
│   │               ├── dtos/             # Data Transfer Objects (DTOs)
│   │               │   └── LoginResponse.java
│   │               │   └── LoginUserDto.java
│   │               │   └── RegisterUserDto.java
│   │               │   └── TaskCreateDto.java
│   │               ├── entities/         # Database Entities
│   │               │   └── Task.java
│   │               │   └── User.java
│   │               ├── exceptions/       # Global Exception Handling
│   │               │   └── GlobalExceptionHandler.java
│   │               ├── initializer/      # Data Initialization
│   │               │   └── DataInitializer.java
│   │               ├── repositories/     # Repositories for database operations
│   │               │   └── TaskRepository.java
│   │               │   └── UserRepository.java
│   │               ├── services/         # Business logic services
│   │               │   └── AuthenticationService.java
│   │               │   └── JwtService.java
│   │               │   └── TaskService.java
│   │               │   └── UserService.java
│   │               ├── specifications/   # Specifications for queries
│   │               │   └── TaskSpecification.java
│   │               └── TodoAppJavaBackendApplication.java   # Main Application Class
│   └── resources/
│       └── application.yml              # Application configuration
└── test/                                 # Unit and Integration Tests
```

## API Endpoints
Here are the main API endpoints provided by the application:

1. **User Registration (Sign Up)**
   - **URL**: `/auth/signup`
   - **Method**: `POST`
   - **Request Body**:
     ```json
     {
       "email": "user@example.com",
       "fullName": "John Doe",
       "password": "password123"
     }
     ```

2. **User Login**
   - **URL**: `/auth/login`
   - **Method**: `POST`
   - **Request Body**:
     ```json
     {
       "email": "user@example.com",
       "password": "password123"
     }
     ```

   - **Response**:
     ```json
     {
       "token": "jwt-token-string",
       "expiresIn": 3600
     }
     ```

3. **Create Task**
   - **URL**: `/tasks`
   - **Method**: `POST`
   - **Request Body**:
     ```json
     {
       "title": "Task Title",
       "description": "Task Description",
       "priority": 1,
       "state": 1
     }
     ```

4. **List Tasks**
   - **URL**: `/tasks`
   - **Method**: `GET`
   - **Request Parameters**:
     - **`page`**: Page number (optional)
     - **`size`**: Page size (optional)

## H2 Database Console Access
This project uses the H2 embedded in-memory database. You can access the H2 console using the following URL after starting the application:

- **URL**: `http://localhost:8080/h2-console`
- **Username**: `admin`
- **Password**: `admin`
- **JDBC URL**: `jdbc:h2:mem:testdb`

These configurations are set in the `application.yml` file as follows:

```yaml
spring:
  main:
    banner-mode: "console"
  h2:
    console:
      enabled: true
      path: /h2-console
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: admin
    password: admin
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.H2Dialect
  security:
    jwt:
      secret-key: 3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
      expiration-time: 86400000  # 24 hours in milliseconds
```

## Project Setup

Follow these steps to set up and run the project on your local machine:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/emrecansonmez/thy-todoapp-java-backend.git
   cd thy-todoapp-java-backend
   ```

2. **Install Maven Dependencies**:
   ```bash
   mvn clean install
   ```

3. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

The application will be available at **http://localhost:8080**.

## Running Tests
Tests for this project are written using **JUnit** and **Mockito**. To run the tests, execute the following command:

```bash
mvn test
```

Once the application is running, you can access the H2 database console and interact with the application through the provided API endpoints.
