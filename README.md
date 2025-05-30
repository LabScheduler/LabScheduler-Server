# Lab Scheduler Server [![wakatime](https://wakatime.com/badge/user/fc05717b-168a-4ca3-ba2a-970e2d8d681f/project/f1c879fd-0b0e-452f-86aa-4cad878dec46.svg)](https://wakatime.com/badge/user/fc05717b-168a-4ca3-ba2a-970e2d8d681f/project/f1c879fd-0b0e-452f-86aa-4cad878dec46)

A comprehensive solution for managing and automatically allocating laboratory practice sessions for educational institutions.

## Overview
Lab Scheduler Server provides an automated scheduling system for laboratory practice sessions in educational institutions. It efficiently schedules lab sessions across multiple sections while ensuring there are no conflicts and that resources are optimally utilized.

## Features

### Account Management
- User registration and authentication with JWT
- Three user roles: Manager, Lecturer, and Student
- Role-based access control with appropriate permissions
- Account status management (active/locked)

### Schedule Management
- Automated scheduling of practice sessions with sequential allocation
- Intelligent conflict detection and resolution
- Room capacity and availability management
- Multiple view options: by semester, class, course, lecturer, or week

### Request Management
- Lecturers can create schedule change requests
- Managers can approve or reject requests through workflow
- Request tracking and history
- Email notifications for request status changes

### Course and Resource Management
- Course and section management
- Room and semester management
- Department and major organization
- Student class assignment

## Technology Stack
- Java 23
- Spring Boot 3.4.1
- Spring Data JPA
- Spring Security
- MySQL 8.0
- Hibernate
- Maven
- JWT
- Flyway (database migrations)
- Memcached
- Docker & Docker Compose

## Project Structure
```
.
├── src/
│   ├── main/
│   │   ├── java/com/example/labschedulerserver/
│   │   │   ├── auth/                # Authentication components
│   │   │   ├── common/              # Enums and constants
│   │   │   ├── configuration/       # Application configuration
│   │   │   ├── controller/          # REST API controllers
│   │   │   ├── exception/           # Custom exceptions
│   │   │   ├── model/               # Domain models/entities
│   │   │   ├── payload/             # Request/Response DTOs
│   │   │   ├── repository/          # Data access layer
│   │   │   ├── service/             # Business logic
│   │   │   └── utils/               # Utility classes
│   │   └── resources/
│   │       ├── db/migration/        # Flyway migration scripts
│   │       └── application.yml      # Application configuration
│   └── test/                        # Unit and integration tests
├── .mvn/                            # Maven wrapper
├── docker-compose.yml               # Docker Compose configuration
├── Dockerfile                       # Docker build configuration
├── pom.xml                          # Maven dependencies
└── .env                             # Environment variables
```

## Getting Started

### Prerequisites
- JDK 23
- Maven
- MySQL 8.0 (optional if using Docker)
- Docker and Docker Compose (optional)

### Installation

#### Option 1: Local Setup
1. Clone the repository:
```
git clone https://github.com/yourusername/LabScheduler-Server.git
cd LabScheduler-Server
```

2. Install dependencies:
```
mvn clean install
```

3. Configure your database in `src/main/resources/application.yml` or via environment variables:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/labscheduler
    username: your_username
    password: your_password
```

4. Run the application:
```
mvn spring-boot:run
```

#### Option 2: Docker Setup
1. Clone the repository:
```
git clone https://github.com/yourusername/LabScheduler-Server.git
cd LabScheduler-Server
```

2. Start the application using Docker Compose:
```
docker-compose up --build -d
```

This will:
- Start a MySQL 8.0 container
- Start a Memcached container
- Build and start the application container
- Connect all services via a Docker network

### Docker Configuration
The project includes:
- `Dockerfile`: Multi-stage build process for the application
- `docker-compose.yml`: Orchestrates MySQL, Memcached, and application containers

Default database credentials in Docker environment:
- Database URL: jdbc:mysql://mysql:3306/labscheduler
- Username: root
- Password: 123

## Scheduling Algorithm

### Scheduling Rules
1. Each section has only 1 practice session per week
2. Each section completes all its required practice sessions before moving to the next section
3. Practice sessions are scheduled consecutively across weeks for the same section
4. No overlap between sections - only one section practices per week
5. Each practice session occupies either 4 morning or 4 afternoon periods
6. Room capacity must be sufficient for the section size
7. Rooms cannot be double-booked

### Algorithm Flow
1. Calculate required practice sessions for each section based on total practice periods
2. Process sections sequentially (section 1, then section 2, etc.)
3. For each section:
   - Find optimal consecutive weeks with the same day, room, and time
   - Schedule all required sessions for that section
   - Remove used weeks from available weeks to prevent section overlap
4. Move to the next section once the current one is fully scheduled

## API Documentation

### Schedule Management
- `POST /api/schedule/allocate?course_id={id}`: Automatically allocate practice schedules
- `GET /api/schedule`: Get all schedules in current semester
- `GET /api/schedule/semester/{semesterId}`: Get schedules by semester
- `GET /api/schedule/class/{classId}`: Get schedules by class
- `GET /api/schedule/course/{courseId}`: Get schedules by course
- `GET /api/schedule/lecturer/{lecturerId}`: Get schedules by lecturer
- `GET /api/schedule/week/{weekId}`: Get schedules in specific week
- `POST /api/schedule/create`: Create a schedule manually
- `PATCH /api/schedule/cancel/{scheduleId}`: Cancel a schedule

### Lecturer Request Management
- `POST /api/lecturer-request/create`: Create schedule change request
- `GET /api/lecturer-request/get/pending`: Get all pending requests
- `GET /api/lecturer-request/get`: Get all requests
- `GET /api/lecturer-request/get/lecturer/{lecturerId}`: Get requests by lecturer
- `GET /api/lecturer-request/get/{requestId}`: Get request by ID
- `POST /api/lecturer-request/process-request`: Process (approve/reject) a request
- `PATCH /api/lecturer-request/cancel/{requestId}`: Cancel a request

## Database Migration
The project uses Flyway for database migrations:
- `V1__schema.sql`: Database schema
- `test.sql`: Initial test data

Migrations run automatically on application startup.

## Troubleshooting

### Common Issues
- **No available slots**: Try adjusting room availability or adding more rooms
- **Scheduling conflicts**: Check existing schedules for overlaps
- **Insufficient consecutive weeks**: Ensure there are enough consecutive weeks in the semester for each section

### Docker Issues
- **Container not starting**: Check container logs with `docker logs <container-name>`
- **Database connection issues**: Ensure MySQL container is healthy with `docker ps`
- **Application not connecting to database**: Verify the database URL in application configuration

## License
This project is licensed under the MIT License - see the LICENSE file for details.

---

Developed as part of the educational resource management system for higher education institutions.
