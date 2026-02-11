# SilverCare REST API (Backend)

Spring Boot REST API for SilverCare elderly care services platform.

## Overview

- **Type**: Spring Boot REST API
- **Port**: 8080
- **Context Path**: `/api`
- **Full URL**: `http://localhost:8080/api`
- **Packaging**: WAR (can run standalone or deploy to Tomcat)

## Technology Stack

- Spring Boot 3.4.2
- Java 21
- PostgreSQL
- Stripe Payment Integration
- GSON for JSON
- jBCrypt for password hashing

## Project Structure

```
silvercare-api/
├── src/main/java/com/silvercare/
│   ├── Application.java              (Spring Boot main class)
│   ├── ServletInitializer.java       (WAR deployment support)
│   ├── config/
│   │   └── CorsConfig.java           (CORS configuration)
│   ├── controller/                   (REST Controllers)
│   │   ├── BookingRestController.java
│   │   ├── UserRestController.java
│   │   ├── ServicesRestController.java
│   │   ├── FeedbackRestController.java
│   │   └── StatusRestController.java
│   ├── service/                      (Business logic)
│   │   ├── BookingService.java
│   │   ├── UserService.java
│   │   ├── ServiceService.java
│   │   ├── FeedbackService.java
│   │   └── AdminReportService.java
│   ├── dao/                          (Data access)
│   │   ├── BookingDAO.java
│   │   ├── UserDAO.java
│   │   ├── ServiceDAO.java
│   │   ├── FeedbackDAO.java
│   │   └── AdminReportDAO.java
│   ├── model/                        (Entities)
│   │   ├── User.java
│   │   ├── Booking.java
│   │   ├── Service.java
│   │   ├── Feedback.java
│   │   └── ...
│   ├── payment/                      (Payment processing)
│   │   ├── PaymentProcessor.java
│   │   ├── StripePaymentProcessor.java
│   │   └── StripeMockProcessor.java
│   └── util/                         (Utilities)
│       ├── DBConnection.java
│       ├── BookingDBUtil.java
│       └── ...
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## API Endpoints

All endpoints are prefixed with `/api`:

### Status
- `GET /api/status` - Health check

### User Management
- `POST /api/users/login` - User login
- `POST /api/users/register` - User registration
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Services
- `GET /api/services` - Get all services
- `GET /api/services/{id}` - Get service by ID
- `GET /api/services/category/{categoryId}` - Get services by category
- `GET /api/services/categories` - Get all categories
- `POST /api/services` - Create service (admin)
- `PUT /api/services/{id}` - Update service (admin)
- `DELETE /api/services/{id}` - Delete service (admin)

### Bookings
- `GET /api/bookings` - Get all bookings
- `GET /api/bookings/{id}` - Get booking by ID
- `GET /api/bookings/user/{userId}` - Get user bookings
- `POST /api/bookings` - Create booking
- `PUT /api/bookings/{id}` - Update booking
- `DELETE /api/bookings/{id}` - Cancel booking

### Feedback
- `GET /api/feedback` - Get all feedback
- `GET /api/feedback/{id}` - Get feedback by ID
- `POST /api/feedback` - Submit feedback

## Running the Application

### Prerequisites
- Java 21
- Maven 3.6+
- PostgreSQL database

### Quick Start

```bash
cd silvercare-api
mvn spring-boot:run
```

The API will start on `http://localhost:8080/api`

### Build WAR file

```bash
mvn clean package
```

This creates `target/silvercare-api.war`

### Deploy to Tomcat

```bash
# Copy WAR to Tomcat webapps
copy target\silvercare-api.war C:\path\to\tomcat\webapps\

# Start Tomcat
C:\path\to\tomcat\bin\startup.bat
```

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server Configuration
server.servlet.context-path=/api
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/silvercare
spring.datasource.username=your_username
spring.datasource.password=your_password

# Stripe Configuration
stripe.secret.key=your_stripe_secret_key
stripe.publishable.key=your_stripe_publishable_key
stripe.webhook.secret=your_webhook_secret
```

### Environment Variables

For production, use environment variables:

```bash
export DB_URL=jdbc:postgresql://your-db-host:5432/silvercare
export DB_USER=your_username
export DB_PASSWORD=your_password
export STRIPE_SECRET_KEY=your_stripe_secret_key
export STRIPE_PUBLISHABLE_KEY=your_stripe_publishable_key
```

## Testing

### Health Check
```bash
curl http://localhost:8080/api/status
```

### Login
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john.doe@email.com","password":"customer123"}'
```

### Get Services
```bash
curl http://localhost:8080/api/services
```

### Get Service by ID
```bash
curl http://localhost:8080/api/services/1
```

## CORS Configuration

The API is configured to accept requests from:
- `http://localhost:8081` (Frontend web application)
- `http://localhost:8080` (Same origin)

To add more origins, edit `src/main/java/com/silvercare/config/CorsConfig.java`

## Database Setup

1. Create PostgreSQL database:
```sql
CREATE DATABASE silvercare;
```

2. Run schema script:
```bash
psql -U your_username -d silvercare -f src/main/webapp/db/schema_postgres.sql
```

3. Run seed data:
```bash
psql -U your_username -d silvercare -f src/main/webapp/db/seed_postgres.sql
```

## Features

### Core Features
- User authentication and authorization
- Service catalog management
- Booking system with payment
- Feedback and ratings
- Admin dashboard

### Advanced Features
- Service layer architecture
- Stripe payment integration
- BCrypt password hashing
- CORS support for frontend
- RESTful API design
- Error handling
- Input validation

## Architecture

```
Client (Frontend)
    ↓
REST Controller
    ↓
Service Layer (Business Logic)
    ↓
DAO Layer (Data Access)
    ↓
Database (PostgreSQL)
```

## Development

### Add New Endpoint

1. Create REST Controller:
```java
@RestController
@RequestMapping("/api/your-resource")
public class YourRestController {
    
    @Autowired
    private YourService yourService;
    
    @GetMapping
    public ResponseEntity<?> getAll() {
        // Implementation
    }
}
```

2. Create Service:
```java
@Service
public class YourService {
    
    @Autowired
    private YourDAO yourDAO;
    
    public List<YourModel> getAll() {
        // Implementation
    }
}
```

3. Create DAO:
```java
public class YourDAO {
    public List<YourModel> getAll() {
        // Database access
    }
}
```

## Troubleshooting

### Port already in use
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process
taskkill /PID <process_id> /F
```

### Database connection failed
- Check database is running
- Verify connection details in `application.properties`
- Check firewall settings

### CORS errors
- Verify frontend URL in `CorsConfig.java`
- Check browser console for specific error
- Ensure credentials are allowed if needed

## Notes

- This is a **pure REST API** - no JSPs, no Servlets, no web pages
- All responses are in JSON format
- Authentication is handled via the API
- The frontend (silvercare-web) calls this API for all data operations
- WAR packaging allows deployment to external Tomcat servers
- Can also run standalone using embedded Tomcat

## Related Projects

- **silvercare-web** - Frontend web application that consumes this API

## License

Educational project for JAD Assignment 2
