# Apprentice — Bytik Backend

Spring Boot 3.4 backend for the Bytik hackathon project.

## Tech stack

- Kotlin 2.1 / JVM 21
- Spring Boot 3.4
- Spring Data JPA + PostgreSQL
- Spring Security + JWT (jjwt 0.12)
- Gradle 8.12 with Kotlin DSL and version catalog

## Prerequisites

- JDK 21
- PostgreSQL running locally (or via Docker)

## Local setup

1. Start PostgreSQL and create the database:

```sql
CREATE USER bytik WITH PASSWORD 'bytik_secret';
CREATE DATABASE bytik OWNER bytik;
```

2. Run the application:

```bash
./gradlew bootRun
```

The server starts on port `8080`.

## Environment variables

| Variable                   | Default                                       |
|----------------------------|-----------------------------------------------|
| SPRING_DATASOURCE_URL      | jdbc:postgresql://localhost:5432/bytik        |
| SPRING_DATASOURCE_USERNAME | bytik                                         |
| SPRING_DATASOURCE_PASSWORD | bytik_secret                                  |
| JWT_SECRET                 | super-secret-jwt-key-for-bytik-2026-hackathon |
