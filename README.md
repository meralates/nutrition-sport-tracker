# NutRise Backend

NutRise Backend is the Spring Boot REST API powering **NutRise**, a nutrition and fitness tracking mobile application.

The project provides secure authentication, nutrition and workout tracking, water intake management, daily summaries, scheduled reports, and external API integrations.

Beyond application development, the backend has been extended with production-oriented engineering practices including automated testing, Docker, CI/CD, database migrations, security scanning, health checks, and Railway deployment.

---

## 🚀 Production

The backend is deployed on **Railway** using the production Spring profile.

**Production API**

```text
https://nutrition-sport-tracker-production.up.railway.app
```

**Health Check**

```text
GET /actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

The production environment has been manually smoke-tested through the complete authentication flow:

```text
Health Check
     ↓
User Registration
     ↓
MySQL Persistence
     ↓
User Login
     ↓
BCrypt Password Verification
     ↓
JWT Generation
     ↓
JWT Authentication Filter
     ↓
Protected Endpoint
```

---

## ✨ Features

- User registration and login
- JWT-based stateless authentication
- BCrypt password hashing
- User profile management
- Meal logging and nutrition tracking
- Water intake tracking
- Workout logging
- Daily user summaries
- Daily and weekly scheduled reports
- External API integrations
- MySQL persistence
- Versioned database migrations with Flyway
- Spring Boot Actuator health checks

---

## 🛠 Tech Stack

### Backend

- Java 17
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- Hibernate
- JWT
- Maven

### Database

- MySQL
- Flyway

### External Services

- OpenRouter
- USDA FoodData Central
- Gmail SMTP

### Testing

- JUnit 5
- Mockito
- Spring Boot Test
- Testcontainers

### DevOps & Deployment

- Docker
- Docker Compose
- Multi-stage Docker builds
- GitHub Actions
- GitHub Container Registry (GHCR)
- Railway
- Dependabot
- CodeQL
- Trivy

---

## 🏗 Architecture

The backend follows a layered architecture:

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
MySQL
```

Protected requests also pass through Spring Security and the JWT authentication filter.

```text
Request
  ↓
Spring Security
  ↓
JwtAuthenticationFilter
  ↓
Controller
  ↓
Service
  ↓
Repository
```

This structure separates API handling, business logic, persistence, and security responsibilities.

---

## 🔐 Authentication

NutRise uses JWT-based stateless authentication.

### Register

```text
POST /api/auth/register
```

### Login

```text
POST /api/auth/login
```

A successful login returns a JWT.

Protected endpoints require the token in the request header:

```http
Authorization: Bearer <token>
```

Example protected endpoint:

```text
GET /api/users/me
```

Requests without valid authentication receive:

```text
401 Unauthorized
```

Passwords are hashed with BCrypt and password fields are excluded from API responses.

---

## ⚙️ Environment Profiles

Application configuration is separated by environment.

```text
application.yml
application-dev.yml
application-prod.yml
application-test.yml
```

The active Spring profile can be selected using:

```bash
SPRING_PROFILES_ACTIVE=dev
```

Production credentials and sensitive configuration are not hard-coded in source files.

They are provided through environment variables.

---

## 🔑 Environment Variables

Important environment variables include:

```text
SPRING_PROFILES_ACTIVE

SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD

JWT_SECRET
JWT_EXPIRATION

MAIL_USERNAME
MAIL_PASSWORD

OPENROUTER_API_KEY
OPENROUTER_HTTP_REFERER
OPENROUTER_X_TITLE

USDA_API_KEY

CORS_ALLOWED_ORIGIN_PATTERNS

SCHEDULING_ENABLED
```

Real credentials, passwords, JWT secrets, and API keys must never be committed to the repository.

Use `.env.example` as a reference when configuring the project locally.

---

## 🗄 Database Migrations

Database schema changes are managed with **Flyway**.

Migration files are stored under:

```text
src/main/resources/db/migration/
```

The project currently includes versioned migrations for the initial database schema and query indexes.

Production uses Hibernate schema validation instead of automatically modifying the database:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

This allows Flyway to remain responsible for controlled and versioned schema changes.

---

## 🐳 Docker

The backend is containerized using a **multi-stage Docker build**.

The build stage compiles the Spring Boot application using Maven and Java 17.

The runtime stage contains only the required Java runtime and packaged application.

The production container also runs using a **non-root user**.

Build the backend image locally:

```bash
docker build -t nutrise-backend:local .
```

---

## 🐳 Docker Compose

Docker Compose provides a reproducible local environment for the backend and MySQL database.

Start the complete stack:

```bash
docker compose up --build
```

Architecture:

```text
NutRise Backend
      ↓
    MySQL
```

The MySQL container includes a health check, and the backend waits for the database to become healthy before starting.

Stop the environment:

```bash
docker compose down
```

Remove containers and database volumes for a clean setup:

```bash
docker compose down -v
```

---

## 🩺 Health Check & Graceful Shutdown

Spring Boot Actuator is used to expose the application health endpoint:

```text
/actuator/health
```

Only the required health information is publicly exposed.

The application also supports graceful shutdown so active work can terminate cleanly when the container receives a shutdown signal.

Example production health response:

```json
{
  "status": "UP"
}
```

---

## 🧪 Testing

Run the automated test suite with:

```bash
./mvnw clean test
```

For full Maven verification:

```bash
./mvnw clean verify
```

The automated test suite covers areas including:

- User registration
- Successful and failed login
- Duplicate user scenarios
- JWT generation and validation
- Security rules
- Protected endpoint access
- Domain service behavior
- Water intake operations
- Workout operations
- Repository and persistence behavior

Database integration tests use **Testcontainers** to verify persistence behavior against a real MySQL container.

This prevents integration tests from depending on a developer's locally installed MySQL instance.

---

## 🔄 CI/CD

The project uses **GitHub Actions** for automated build, test, container publishing, and security checks.

The CI/CD pipeline provides automated verification before production changes are deployed.

### Backend CI

Backend changes are automatically validated using Maven.

The pipeline runs the project's tests and build verification before changes are merged.

### Container Publishing

Docker images are built automatically and published to **GitHub Container Registry (GHCR)**.

Versioned image tags make container builds traceable to their corresponding source code revision.

### Railway Deployment

The production backend is connected to the GitHub repository through Railway.

Production deployments use the `master` branch and are automatically triggered after accepted changes.

Railway verifies the application through:

```text
/actuator/health
```

A deployment is considered healthy only when the application successfully starts and responds to the health check.

---

## 🛡 Security

NutRise includes multiple application and DevSecOps security measures.

### Application Security

- JWT-based authentication
- BCrypt password hashing
- Stateless Spring Security configuration
- Protected API endpoints
- Controlled CORS configuration
- Externalized production secrets
- Production environment validation
- Non-root Docker runtime
- Password fields excluded from API responses

### Dependabot

Dependabot is enabled to identify and propose dependency updates.

### CodeQL

GitHub CodeQL performs automated static analysis of the source code.

### Trivy

Trivy scans container images for known vulnerabilities.

### Responsible Disclosure

Security-related information and reporting instructions are documented in:

```text
SECURITY.md
```

---

## 📁 Project Structure

```text
nutrition-sport-tracker/
├── .github/
│   ├── dependabot.yml
│   └── workflows/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/nutritionsporttracker/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       └── service/
│   │   │
│   │   └── resources/
│   │       ├── db/migration/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── application-prod.yml
│   │
│   └── test/
│
├── .dockerignore
├── .env.example
├── Dockerfile
├── docker-compose.yml
├── SECURITY.md
├── pom.xml
└── README.md
```

---

## 📱 About NutRise

NutRise is a full-stack nutrition and fitness tracking application.

The system consists of:

```text
React Native Mobile Application
             ↓
      Spring Boot REST API
             ↓
           MySQL
```

The mobile application communicates with the backend for authentication, nutrition tracking, workout tracking, water intake, and user-related operations.

The NutRise Android application has also been published to **Google Play**.

---

## ⚙️ Production Engineering Journey

NutRise started as a full-stack application and was later extended with production engineering practices.

```text
Backend Development
        ↓
Automated Testing
        ↓
Database Migrations
        ↓
Docker & Docker Compose
        ↓
GitHub Actions CI/CD
        ↓
Container Registry
        ↓
Security Scanning
        ↓
Railway Production Deployment
        ↓
Production Smoke Testing
```

The goal of the project is not only to implement application features, but also to demonstrate how a backend service can be securely configured, tested, containerized, deployed, and verified through a repeatable engineering process.

---

## 👩‍💻 Author

**Meral Ateş**

Computer Engineering Graduate  
Backend Developer

Main areas of interest:

- Backend Development
- Java & Spring Boot
- REST APIs
- Database Systems
- Docker
- CI/CD
- Production Engineering
````
