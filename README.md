# Lab Scheduler Server

A comprehensive solution for managing and automatically allocating laboratory practice sessions for educational institutions.

## Features

### Account Management
- User registration and authentication
- Three levels of accounts: Manager, Lecturer, Student
- JWT-based authentication
- Role-based access control with appropriate permissions
- Account status management (active/locked)

### Schedule Management
- Automated scheduling of practice sessions based on predefined rules
- Section-based sequential allocation of practice sessions
- Conflict detection and resolution
- Room capacity and availability management
- Viewing schedules by various filters (semester, class, course, lecturer)

### Request Management
- Lecturer schedule change request creation
- Manager approval/rejection workflow
- Request tracking and history
- Email notifications for request status changes

### Course and Resource Management
- Course and section management
- Room and semester management
- Department and major organization
- Student class assignment

## Technology Stack
- Java 23
- Spring Boot
- Spring Data JPA
- Spring Security
- MySQL
- Hibernate
- Maven
- JWT
- Flyway (for database migrations)

## Project Structure
```
.
├── src/
│   ├── main/
│   │   ├── java/com/example/labschedulerserver/
│   │   │   ├── auth/           # Authentication components
│   │   │   ├── common/         # Common enums and constants
│   │   │   ├── controller/     # REST API controllers
│   │   │   ├── exception/      # Custom exceptions
│   │   │   ├── model/          # Domain models/entities
│   │   │   ├── payload/        # Request/Response DTOs
│   │   │   ├── repository/     # Data access layer
│   │   │   ├── service/        # Business logic
│   │   │   └── utils/          # Utility classes
│   │   └── resources/
│   │       ├── db/migration/   # Flyway migration scripts
│   │       └── application.properties
│   └── test/                   # Unit and integration tests
└── pom.xml                     # Maven configuration
```

## Getting Started

### Prerequisites
- JDK 23
- Maven
- MySQL database

### Installation
1. Clone the repository:
```
git clone https://github.com/yourusername/LabScheduler-Server.git
cd LabScheduler-Server
```

2. Install dependencies:
```
mvn clean install
```

### Configuration
Create or edit `application.properties` in the `src/main/resources` directory:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/lab_scheduler
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
app.jwt.secret=your-jwt-secret-key
app.jwt.expiration=86400000

# Email Configuration
spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=your-email@example.com
spring.mail.password=your-email-password
```

## Running the Service

Development mode:
```
mvn spring-boot:run
```

Production mode:
```
java -jar target/labscheduler-server.jar --spring.profiles.active=prod
```

## Scheduling Algorithm

### Scheduling Rules
1. Each section has only 1 practice session per week
2. Each section completes all its required practice sessions before moving to the next section
3. Practice sessions are scheduled consecutively across weeks for the same section
4. No overlap between sections - only one section practices per week
5. Each practice session occupies either 4 morning or 4 afternoon periods
6. Room capacity must be sufficient for the section
7. Rooms cannot be double-booked

### Algorithm Flow
1. Calculate required practice sessions for each section
2. Process sections sequentially (section 1, then section 2, etc.)
3. For each section:
   - Find optimal consecutive weeks with the same day, room, and time
   - Schedule all required sessions for that section
   - Remove used weeks from available weeks
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
The project uses Flyway for database migrations. Initial schema and test data are provided in:
- `V1__schema.sql`: Database schema
- `V2__data.sql`: Initial test data

## Best Practices for Using the System
1. **For Administrators**: Run the schedule allocation at the beginning of each semester
2. **For Lecturers**: Submit schedule change requests early for better processing
3. **Always check for conflicts**: Before manually creating schedules, check for potential conflicts

## Troubleshooting
Common issues:
- **No available slots**: Try adjusting room availability or adding more rooms
- **Scheduling conflicts**: Check existing schedules for overlaps
- **Insufficient consecutive weeks**: Ensure there are enough consecutive weeks in the semester for each section

## License
This project is licensed under the MIT License - see the LICENSE file for details.

---

Developed as part of the educational resource management system for higher education institutions.
