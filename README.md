# Calculator API – Spring Boot 3, Java 17, DDD, JWT Security

## Overview
This backend service powers a Calculator application. It follows Domain-Driven Design (DDD) and uses JWT-based stateless authentication.
The API allows users to register, log in, perform arithmetic and random string operations, track usage records, and manage account balances.

---

## Features
- User Registration & Login with JWT token issuance
- Stateless authentication with Spring Security 6 and JWT
- Arithmetic operations: Addition, Subtraction, Multiplication, Division, Square Root
- Random string operation via third-party API
- Balance deduction per operation
- Record history with soft deletion
- H2 in-memory database for development
- CORS enabled for Angular frontend (`http://localhost:4200`)

---

## Architecture (DDD)
```
src/main/java/net/mestizoftware
│
├── application/         # Application services (use cases)
│
├── domain/              # Domain models (entities, value objects, enums) & repository interfaces
│
├── infrastructure/      # Technical details (JPA repositories, security config, mappers)
│
└── interfaces/          # REST controllers and DTOs
```
The flow: Controller → Application Service → Domain Model/Repository → Persistence → DTO mapping → Response

---

## Data Model
- **User**: id, username, password (BCrypt), balance, status
- **Operation**: id, type (enum), cost
- **Record**: id, operation, user, amount, userBalance, operationResponse, createdAt, deleted (boolean)

---

## Authentication Flow
1. **Register** at `/api/v1/auth/register`
2. **Login** at `/api/v1/auth/login` to get JWT token
3. Send token in the `Authorization: Bearer <token>` header for secured endpoints
4. Token is validated by `JwtAuthFilter`

---

## API Endpoints

### Auth
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/auth/register` | Register new user |
| POST | `/api/v1/auth/login` | Login and return JWT token |

### Operations
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/operations` | List all operations |
| POST | `/api/v1/operations` | Create a new operation |
| POST | `/api/v1/operations/perform` | Perform an operation |

### Records
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/records` | List current user's records |
| DELETE | `/api/v1/records/{id}` | Soft delete a record |

---

## Running Locally

### Prerequisites
- Java 17
- Maven 3.9+

### Commands
```bash
mvn clean install
mvn spring-boot:run
```
The API runs on `http://localhost:8080`.

---

## H2 Database
- URL: `http://localhost:8080/h2-console`
- JDBC: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: *(empty)*

---

## Example Requests

### Register
```bash
curl -X POST http://localhost:8080/api/v1/auth/register   -H "Content-Type: application/json"   -d '{"username":"user@example.com","password":"secret"}'
```

### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login   -H "Content-Type: application/json"   -d '{"username":"user@example.com","password":"secret"}'
```
Response:
```json
{ "token": "eyJhbGciOi..." }
```

### Perform Operation
```bash
curl -X POST http://localhost:8080/api/v1/operations/perform   -H "Authorization: Bearer <token>"   -H "Content-Type: application/json"   -d '{"type":"ADDITION","input":5}'
```

---

## Configuration Notes
- JWT secret and expiration are set in `application.yml`
- CORS is configured in `SecurityConfig` to allow `http://localhost:4200`
- Use `.headers(h -> h.frameOptions().sameOrigin())` to enable H2 console

---

## Troubleshooting
- **401 Unauthorized**: Ensure JWT token is in the Authorization header, re-login after backend restart (H2 resets data)
- **Enum errors**: Send `type` in uppercase (e.g., `"ADDITION"`)

