# Tennis Club Service

A microservice for managing tennis clubs and their courts. This service is part of the tennis tournament management system.

## Features

- CRUD operations for tennis clubs
- Court management for clubs
- RESTful API with OpenAPI/Swagger documentation
- Spring Boot Actuator for health checks and metrics
- Prometheus metrics support

## Technology Stack

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (in-memory)
- SpringDoc OpenAPI
- Spring Boot Actuator
- Micrometer Prometheus

## Running the Service

### Using Gradle

```bash
./gradlew bootRun
```

The service will start on port **8081**.

### Using Docker

```bash
docker-compose up
```

## API Endpoints

### Club Management

- `POST /api/clubs` - Create a new tennis club
- `GET /api/clubs` - Get all tennis clubs
- `GET /api/clubs/{id}` - Get club by ID
- `PUT /api/clubs/{id}` - Update club
- `DELETE /api/clubs/{id}` - Delete club

### Court Management

- `POST /api/clubs/{clubId}/courts` - Add a court to a club
- `GET /api/clubs/{clubId}/courts` - Get all courts for a club
- `GET /api/clubs/{clubId}/courts/{courtId}` - Get court by ID
- `DELETE /api/clubs/{clubId}/courts/{courtId}` - Delete a court

### Actuator Endpoints

- `GET /actuator/health` - Health check
- `GET /actuator/info` - Application info
- `GET /actuator/metrics` - Available metrics
- `GET /actuator/prometheus` - Prometheus metrics

## API Documentation

Swagger UI is available at: http://localhost:8081/swagger-ui.html

## Database

The service uses an in-memory H2 database (`club_service_db`) with the following tables:
- `tennis_clubs` - Stores tennis club information
- `courts` - Stores court information linked to clubs

H2 Console is available at: http://localhost:8081/h2-console

## Testing

Run unit tests:
```bash
./gradlew test
```

## Configuration

Configuration is managed through `application.yml`. Key settings:
- Server port: 8081
- Database: H2 in-memory
- Actuator endpoints: health, info, metrics, prometheus

## Future Integration

This service is designed to be independently deployable. Future enhancements may include:
- REST client/Feign client for inter-service communication
- Database migration with Flyway/Liquibase
- External database support (PostgreSQL, MySQL)
- Service discovery integration
