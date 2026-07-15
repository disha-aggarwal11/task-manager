<div align="center">

# 🚀 Task Manager Backend

### A production-oriented Task Management REST API built with Spring Boot & PostgreSQL

Built to demonstrate secure authentication, clean architecture, and production-ready backend development using Java and Spring Boot.

Secure • Layered Architecture • JWT Authentication • OAuth2 • Testing

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?logo=postgresql)
![Spring Security](https://img.shields.io/badge/Spring%20Security-Secured-success?logo=springsecurity)
![JWT](https://img.shields.io/badge/JWT-Authentication-black)
![Maven](https://img.shields.io/badge/Maven-Build-red?logo=apachemaven)
![JUnit5](https://img.shields.io/badge/JUnit5-Testing-brightgreen)

</div>

---

# 📖 About

Task Manager Backend is a RESTful API built using **Spring Boot** following clean architecture and modern backend development practices.

The project demonstrates secure authentication, authorization, database management, testing, and production-oriented backend engineering.

---

# 📌 Project Status

✅ Core Backend Complete

Current implementation includes:

- Authentication & Authorization
- Task CRUD APIs
- PostgreSQL Integration
- JWT Authentication
- Google OAuth2 Login
- Refresh Tokens
- Password Reset
- Repository & Service Testing

Planned improvements:

- Swagger/OpenAPI
- Docker
- CI/CD
- Cloud Deployment
- React Frontend

---

# ✨ Features

## 🔐 Authentication & Security

- User Registration
- User Login
- JWT Authentication
- Refresh Tokens
- HttpOnly Cookies
- BCrypt Password Hashing
- Forgot Password
- Google OAuth2 Authentication
- Role-Based Authorization
- CORS Configuration
- Rate Limiting

---

## ✅ Task Management

- Create Task
- Update Task
- Delete Task
- Get Task by ID
- Get All Tasks
- Pagination
- Sorting

---

## 🏗 Architecture

- Layered Architecture
- Controller Layer
- Service Layer
- Repository Layer
- DTO Pattern
- Dependency Injection
- Global Exception Handling
- Environment Variables
- Logging

---

## 🗄 Database

- PostgreSQL
- Spring Data JPA
- Entity Relationships
- Database Indexing

---

## 🧪 Testing

Implemented using:

- JUnit 5
- Mockito
- H2 Database

Test Coverage Includes:

- Service Layer
- Repository Layer
- JWT Service
- Password Reset Service
- Refresh Token Service

---

# 🛠 Tech Stack

| Category | Technology           |
|----------|----------------------|
| Language | Java 17              |
| Framework | Spring Boot 3.5.4    |
| Security | Spring Security      |
| Authentication | JWT + OAuth2         |
| Database | PostgreSQL           |
| ORM | Spring Data JPA      |
| Build Tool | Maven                |
| Testing | JUnit 5, Mockito, H2 |

---

# 📁 Project Structure

```text
src
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service
├── util
└── resources
```

---

# 🌐 REST API

## Authentication

| Method | Endpoint |
|---------|----------|
| POST | /auth/signup |
| POST | /auth/login |
| POST | /auth/logout |
| POST | /auth/refresh-token |
| POST | /auth/forgot-password |
| POST | /auth/reset-password/{token} |
| GET | /oauth2/authorization/google |

---

## Tasks

| Method | Endpoint |
|---------|----------|
| GET | /tasks |
| GET | /tasks/{id} |
| POST | /tasks |
| PATCH | /tasks/{id} |
| DELETE | /tasks/{id} |

---

# ⚙️ Running Locally

## Clone Repository

```bash
git clone https://github.com/disha-aggarwal11/task-manager.git
```

Move into the project directory:

```bash
cd task-manager
```

---

## Configure Environment Variables

Set the following environment variables:

```text
DB_PASSWORD
JWT_SECRET
MAIL_PASSWORD
GOOGLE_CLIENT_ID
GOOGLE_CLIENT_SECRET
```

---

## Create Database

Create a PostgreSQL database named:

```text
taskdb
```

---

## Run Application

```bash
mvn spring-boot:run
```

Application URL:

```text
http://localhost:8080
```

---

# 🧪 Run Tests

```bash
mvn test
```

---

# 🚀 Upcoming Improvements

- Swagger / OpenAPI Documentation
- Docker
- Docker Compose
- CI/CD (GitHub Actions)
- Cloud Deployment
- React Frontend

---

# 📚 What I Learned

- REST API Design
- Spring Boot
- Spring Security
- JWT Authentication
- OAuth2 Login
- PostgreSQL
- Spring Data JPA
- Backend Testing
- Layered Architecture
- Secure Backend Development

---

# 👩‍💻 Author

**Disha Aggarwal**

**GitHub:**  
https://github.com/disha-aggarwal11

**Project Repository:**  
https://github.com/disha-aggarwal11/task-manager

**LinkedIn:**  
https://www.linkedin.com/in/disha-aggarwal-a34517378

---

<div align="center">

⭐ If you found this project useful, consider giving it a star.

</div>