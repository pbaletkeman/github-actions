---
name: "Quiz Engine - Multi-Language Code Generation Prompt"
description: "Executable prompt for GitHub Copilot agents to generate all 8 quiz engine implementations from specification"
applyTo: "*"
---

# Quiz Engine - Complete Multi-Language Code Generation

**Objective:** Generate complete, production-ready quiz engine implementations across all 8 languages with unified architecture, >90% test coverage, Docker support, and consistency across implementations.

---

## Context & Reference

**Reference Documents:**
- Meta-Prompt: `.github/prompts/00-quiz-engine-meta-prompt.md` (architecture overview)
- Implementation Guides: `.github/prompts/0[1-8]-plan-quizEngine-*.prompt.md` (language-specific details)
- Docker Patterns: Each prompt file contains Docker section with examples

**Unified Architecture:**
All 8 implementations share:
- Same database schema (questions, answers, quiz_sessions, user_responses, users)
- Same REST API endpoints and response formats
- Same business logic (question shuffling, answer evaluation, scoring)
- >90% unit test coverage (language-specific tools)
- Docker containerization with multi-stage builds

---

## Task: Generate All 8 Quiz Engine Implementations

### Implementation 1: Python (Flask + SQLAlchemy)

**Project Structure:**
```
quiz-engine-python/
├── quiz_engine/
│   ├── __init__.py
│   ├── models.py              # SQLAlchemy ORM models
│   ├── database.py            # Database initialization
│   ├── quiz_service.py        # Core quiz logic
│   ├── api/
│   │   ├── __init__.py
│   │   ├── routes.py          # Flask routes
│   │   └── schemas.py         # Request/response schemas
│   └── utils/
│       ├── __init__.py
│       ├── shuffler.py        # Question shuffling
│       └── scorer.py          # Answer evaluation & scoring
├── tests/
│   ├── __init__.py
│   ├── test_models.py
│   ├── test_quiz_service.py
│   ├── test_api.py
│   └── test_utils.py
├── requirements.txt           # Flask, SQLAlchemy, pytest-cov
├── conftest.py               # Pytest configuration
├── main.py                   # Flask app entry point
├── Dockerfile                # Production Docker image
├── docker-compose.yml        # Dev and test services
└── README.md                 # Documentation
```

**Key Files to Generate:**
1. **models.py** - SQLAlchemy models for User, Question, Answer, QuizSession, UserResponse
2. **quiz_service.py** - Core logic: create_session, get_questions, evaluate_answer, calculate_score
3. **routes.py** - Flask endpoints: POST /quizzes, GET /quizzes/{id}, POST /sessions, etc.
4. **tests/** - Comprehensive unit tests for all functions with >90% coverage
5. **Dockerfile** - Multi-stage: Python 3.11-slim, non-root user (quizuser)
6. **docker-compose.yml** - Services: quiz-engine (dev), quiz-engine-test (pytest-cov >90%)

**Requirements:**
- Framework: Flask + SQLAlchemy ORM
- Database: SQLite (local) / PostgreSQL (production)
- Testing: pytest with pytest-cov (--cov-fail-under=90)
- Coverage Tools: pytest-cov
- Validation: Input validation, error handling
- API Format: JSON responses with standard envelope {success, data, error, timestamp}

**Generate:**
```python
# All 8 core files + Docker files
quiz_engine/models.py           # ~200 lines
quiz_engine/quiz_service.py     # ~250 lines
quiz_engine/api/routes.py       # ~200 lines
tests/test_quiz_service.py      # ~300 lines (>90% coverage)
tests/test_api.py              # ~250 lines
Dockerfile                      # ~30 lines
docker-compose.yml             # ~40 lines
main.py                        # ~20 lines
```

---

### Implementation 2: Node.js (Express + TypeORM)

**Project Structure:**
```
quiz-engine-nodejs/
├── src/
│   ├── entities/
│   │   ├── User.ts           # TypeORM entity
│   │   ├── Question.ts
│   │   ├── Answer.ts
│   │   ├── QuizSession.ts
│   │   └── UserResponse.ts
│   ├── services/
│   │   ├── QuizService.ts    # Core business logic
│   │   └── DatabaseService.ts
│   ├── routes/
│   │   └── quizRoutes.ts     # Express routes
│   ├── middleware/
│   │   └── errorHandler.ts   # Error handling
│   ├── utils/
│   │   ├── shuffler.ts       # Question shuffling
│   │   └── scorer.ts         # Score calculation
│   ├── database.ts           # TypeORM datasource
│   └── app.ts                # Express app setup
├── dist/                      # Compiled JavaScript
├── tests/
│   ├── quiz.service.test.ts
│   ├── quiz.routes.test.ts
│   └── utils.test.ts
├── package.json              # Dependencies + scripts
├── tsconfig.json
├── jest.config.js            # Jest configuration with coverage
├── Dockerfile
├── docker-compose.yml
└── README.md
```

**Key Files to Generate:**
1. **src/entities/*.ts** - TypeORM entities with decorators
2. **src/services/QuizService.ts** - Core quiz logic
3. **src/routes/quizRoutes.ts** - Express endpoints
4. **tests/*.test.ts** - Jest tests with >90% coverage threshold
5. **jest.config.js** - Coverage configuration
6. **Dockerfile** - node:20-alpine, non-root user (nodeuser)
7. **docker-compose.yml** - Dev, test (Jest coverage >90%), and lint services

**Requirements:**
- Framework: Express + TypeORM
- Language: TypeScript
- Database: SQLite / PostgreSQL
- Testing: Jest with coverage (lines: 90, functions: 90, branches: 90)
- API Format: Same JSON envelope as Python
- Build: npm run build, npm start

**Generate:**
```typescript
src/entities/*.ts              # ~150 lines total
src/services/QuizService.ts    # ~250 lines
src/routes/quizRoutes.ts       # ~180 lines
tests/*.test.ts               # ~400 lines (>90% coverage)
Dockerfile                     # ~35 lines
docker-compose.yml            # ~50 lines (3 services)
```

---

### Implementation 3: Java (Gradle + JDBC)

**Project Structure:**
```
quiz-engine-java/
├── src/
│   ├── main/java/com/quizengine/
│   │   ├── model/
│   │   │   ├── User.java
│   │   │   ├── Question.java
│   │   │   ├── Answer.java
│   │   │   ├── QuizSession.java
│   │   │   └── UserResponse.java
│   │   ├── service/
│   │   │   ├── QuizService.java     # Business logic
│   │   │   └── DatabaseService.java
│   │   ├── controller/
│   │   │   └── QuizController.java  # REST endpoints
│   │   ├── util/
│   │   │   ├── Shuffler.java
│   │   │   └── Scorer.java
│   │   └── App.java               # Entry point
│   └── test/java/com/quizengine/
│       ├── service/QuizServiceTest.java
│       ├── controller/QuizControllerTest.java
│       └── util/UtilTest.java
├── build.gradle                    # Gradle config + JaCoCo
├── Dockerfile                      # Multi-stage: gradle:8-jdk17 → eclipse-temurin:17-jre-alpine
├── docker-compose.yml
└── README.md
```

**Key Files to Generate:**
1. **model/*.java** - POJO classes with getters/setters
2. **service/QuizService.java** - JDBC or Hibernate for database access
3. **controller/QuizController.java** - REST endpoints (Spring MVC or vanilla)
4. **test/java/*.java** - JUnit tests with >90% coverage
5. **build.gradle** - JaCoCo coverage verification (minimum=0.90)
6. **Dockerfile** - Multi-stage build
7. **docker-compose.yml** - Dev and test services with coverage check

**Requirements:**
- Build: Gradle
- Database: JDBC + HikariCP connection pooling
- Testing: JUnit + JaCoCo (minimum 0.90 coverage)
- HTTP: HttpServer or embedded framework
- API Format: Same JSON envelope

**Generate:**
```java
model/*.java                   # ~200 lines total
service/QuizService.java       # ~300 lines
controller/QuizController.java # ~200 lines
test/java/*.java              # ~400 lines (>90% coverage)
build.gradle                   # ~50 lines
```

---

### Implementation 4: Spring Boot (Gradle + JPA)

**Project Structure:**
```
quiz-engine-springboot/
├── src/
│   ├── main/java/com/quizengine/
│   │   ├── entity/
│   │   │   ├── User.java
│   │   │   ├── Question.java
│   │   │   ├── Answer.java
│   │   │   ├── QuizSession.java
│   │   │   └── UserResponse.java
│   │   ├── repository/
│   │   │   ├── QuestionRepository.java
│   │   │   ├── QuizSessionRepository.java
│   │   │   └── UserResponseRepository.java
│   │   ├── service/
│   │   │   └── QuizService.java
│   │   ├── controller/
│   │   │   └── QuizController.java
│   │   ├── util/
│   │   │   ├── Shuffler.java
│   │   │   └── Scorer.java
│   │   ├── config/
│   │   │   └── SpringBootConfig.java
│   │   └── QuizEngineApplication.java
│   └── test/java/com/quizengine/
│       ├── service/QuizServiceTest.java
│       ├── controller/QuizControllerTest.java
│       └── util/UtilTest.java
├── src/main/resources/
│   └── application.properties
├── build.gradle                    # Spring Boot + JaCoCo
├── Dockerfile                      # Multi-stage, port 8080
├── docker-compose.yml
└── README.md
```

**Key Files to Generate:**
1. **entity/*.java** - JPA entities with @Entity annotations
2. **repository/*.java** - Spring Data JPA repositories
3. **service/QuizService.java** - Business logic with @Service
4. **controller/QuizController.java** - REST controllers with @RestController, @RequestMapping
5. **test/java/*.java** - Spring Test + JUnit tests (>90% coverage)
6. **Dockerfile** - Multi-stage, exposed port 8080
7. **docker-compose.yml** - Development and test services

**Requirements:**
- Framework: Spring Boot + Spring Web + Spring Data JPA
- Database: JPA/Hibernate
- Testing: Spring Test + JUnit + JaCoCo (minimum 0.90)
- API Format: Same JSON envelope
- HTTP Port: 8080

**Generate:**
```java
entity/*.java                  # ~200 lines total
repository/*.java             # ~50 lines
service/QuizService.java       # ~300 lines
controller/QuizController.java # ~220 lines
test/java/*.java              # ~450 lines (>90% coverage)
```

---

### Implementation 5: C# (.NET 8 + EF Core)

**Project Structure:**
```
quiz-engine-csharp/
├── QuizEngine/
│   ├── Models/
│   │   ├── User.cs
│   │   ├── Question.cs
│   │   ├── Answer.cs
│   │   ├── QuizSession.cs
│   │   └── UserResponse.cs
│   ├── Services/
│   │   └── QuizService.cs
│   ├── Controllers/
│   │   └── QuizController.cs
│   ├── Data/
│   │   └── QuizDbContext.cs
│   ├── Utils/
│   │   ├── Shuffler.cs
│   │   └── Scorer.cs
│   ├── Program.cs             # Minimal API setup
│   └── QuizEngine.csproj
├── QuizEngine.Tests/
│   ├── QuizServiceTests.cs
│   ├── QuizControllerTests.cs
│   └── UtilTests.cs
├── Dockerfile                 # Multi-stage: SDK → runtime
├── docker-compose.yml
└── README.md
```

**Key Files to Generate:**
1. **Models/*.cs** - Classes with properties
2. **Data/QuizDbContext.cs** - EF Core DbContext
3. **Services/QuizService.cs** - Business logic
4. **Controllers/QuizController.cs** - Minimal API or ASP.NET Core
5. **Tests/*.cs** - xUnit tests with Coverlet (>90% coverage)
6. **Dockerfile** - Multi-stage: .NET SDK 8 → runtime
7. **docker-compose.yml** - Dev and test services

**Requirements:**
- Framework: .NET 8 + ASP.NET Core (Minimal APIs)
- Database: EF Core
- Testing: xUnit + Coverlet (/p:Threshold=90 /p:ThresholdType=line)
- API Format: Same JSON envelope

**Generate:**
```csharp
Models/*.cs                    # ~180 lines total
Data/QuizDbContext.cs         # ~80 lines
Services/QuizService.cs       # ~300 lines
Controllers/QuizController.cs # ~200 lines
Tests/*.cs                    # ~400 lines (>90% coverage)
```

---

### Implementation 6: Dart (Flutter + Drift)

**Project Structure:**
```
quiz_engine_dart/
├── lib/
│   ├── models/
│   │   ├── user.dart
│   │   ├── question.dart
│   │   ├── answer.dart
│   │   ├── quiz_session.dart
│   │   └── user_response.dart
│   ├── database/
│   │   └── database.dart      # Drift database setup
│   ├── services/
│   │   └── quiz_service.dart
│   ├── utils/
│   │   ├── shuffler.dart
│   │   └── scorer.dart
│   └── main.dart              # Shelf HTTP server
├── test/
│   ├── quiz_service_test.dart
│   ├── utils_test.dart
│   └── api_test.dart
├── pubspec.yaml               # Dependencies
├── Dockerfile                 # Multi-stage: google/dart → alpine
├── docker-compose.yml
└── README.md
```

**Key Files to Generate:**
1. **models/*.dart** - Freezed/JSON models
2. **database/database.dart** - Drift database schema
3. **services/quiz_service.dart** - Business logic
4. **main.dart** - Shelf HTTP server with routes
5. **test/*.dart** - Dart test with >90% coverage (lcov)
6. **Dockerfile** - Multi-stage compiled executable
7. **docker-compose.yml** - Dev and test services

**Requirements:**
- Framework: Dart + Shelf (HTTP server)
- Database: Drift ORM
- Testing: test package (>90% coverage verified)
- API Format: Same JSON envelope
- Compilation: Compiled to native executable

**Generate:**
```dart
models/*.dart                 # ~150 lines total
database/database.dart        # ~100 lines
services/quiz_service.dart    # ~250 lines
main.dart                     # ~150 lines
test/*.dart                   # ~350 lines (>90% coverage)
```

---

### Implementation 7: Go (Gin + GORM)

**Project Structure:**
```
quiz-engine-go/
├── internal/
│   ├── model/
│   │   ├── user.go
│   │   ├── question.go
│   │   ├── answer.go
│   │   ├── quiz_session.go
│   │   └── user_response.go
│   ├── service/
│   │   ├── quiz_service.go
│   │   └── database_service.go
│   ├── handler/
│   │   └── quiz_handler.go
│   ├── utils/
│   │   ├── shuffler.go
│   │   └── scorer.go
│   └── server/
│       └── server.go
├── test/
│   ├── quiz_service_test.go
│   ├── handler_test.go
│   └── utils_test.go
├── main.go
├── go.mod
├── go.sum
├── Dockerfile                 # Multi-stage: golang:1.21-alpine → alpine
├── docker-compose.yml
└── README.md
```

**Key Files to Generate:**
1. **model/*.go** - Structs with JSON tags
2. **service/quiz_service.go** - Business logic with GORM
3. **handler/quiz_handler.go** - Gin route handlers
4. **internal/server/server.go** - Gin router setup
5. **test/*.go** - Go testing with >90% coverage
6. **main.go** - Entry point
7. **Dockerfile** - Multi-stage minimal binary
8. **docker-compose.yml** - Dev and test services with coverage check (awk)

**Requirements:**
- Framework: Gin Web Framework + GORM ORM
- Database: SQLite / PostgreSQL with GORM
- Testing: Go's testing package (>90% coverage verified with awk)
- API Format: Same JSON envelope

**Generate:**
```go
model/*.go                    # ~120 lines total
service/quiz_service.go       # ~250 lines
handler/quiz_handler.go       # ~200 lines
main.go                       # ~30 lines
test/*.go                     # ~400 lines (>90% coverage)
```

---

### Implementation 8: Rust (Actix-web + Diesel)

**Project Structure:**
```
quiz-engine-rust/
├── src/
│   ├── models/
│   │   ├── user.rs
│   │   ├── question.rs
│   │   ├── answer.rs
│   │   ├── quiz_session.rs
│   │   └── user_response.rs
│   ├── schema.rs              # Diesel schema (auto-generated)
│   ├── db.rs                  # Database connection pooling
│   ├── handlers/
│   │   └── quiz_handler.rs
│   ├── services/
│   │   └── quiz_service.rs
│   ├── utils/
│   │   ├── shuffler.rs
│   │   └── scorer.rs
│   ├── main.rs                # Actix-web server
│   └── lib.rs
├── migrations/                # Diesel SQL migrations
├── tests/
│   ├── quiz_service_tests.rs
│   ├── integration_tests.rs
│   └── utils_tests.rs
├── Cargo.toml                 # Dependencies + profile settings
├── Dockerfile                 # Multi-stage: rust:1.75 → debian
├── docker-compose.yml
└── README.md
```

**Key Files to Generate:**
1. **models/*.rs** - Serde serialization + Diesel Queryable
2. **db.rs** - r2d2 connection pooling
3. **handlers/quiz_handler.rs** - Actix route handlers
4. **services/quiz_service.rs** - Business logic
5. **tests/*.rs** - Rust tests with cargo-tarpaulin (--fail-under 90)
6. **main.rs** - Actix-web HttpServer setup
7. **Dockerfile** - Multi-stage build
8. **docker-compose.yml** - Dev and test services

**Requirements:**
- Framework: Actix-web + Diesel ORM
- Database: SQLite with Diesel migrations
- Testing: Rust's native testing + cargo-tarpaulin (>90% coverage)
- API Format: Same JSON envelope using Actix web handlers
- Port: 8080

**Generate:**
```rust
models/*.rs                   # ~180 lines total
db.rs                        # ~60 lines
handlers/quiz_handler.rs     # ~200 lines
services/quiz_service.rs     # ~280 lines
main.rs                      # ~40 lines
tests/*.rs                   # ~400 lines (>90% coverage)
Cargo.toml                   # ~40 lines
```

---

## Unified Standards (All 8 Implementations)

### API Endpoints (Consistent Across All Languages)

```
POST   /api/v1/quizzes                    # Create new quiz
GET    /api/v1/quizzes/{id}              # Get quiz details
GET    /api/v1/quizzes/{id}/questions    # Get questions for quiz
POST   /api/v1/sessions                  # Create quiz session
GET    /api/v1/sessions/{id}             # Get session details
POST   /api/v1/sessions/{id}/responses   # Submit answer
GET    /api/v1/sessions/{id}/score       # Get final score
```

### Response Format (Consistent JSON Envelope)

```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "questions": [...],
    "score": 85.5
  },
  "error": null,
  "timestamp": "2026-03-23T12:34:56Z"
}
```

### Error Responses

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "INVALID_INPUT",
    "message": "Question ID not found",
    "details": {}
  },
  "timestamp": "2026-03-23T12:34:56Z"
}
```

### Database Schema (Uniform Across All Languages)

```sql
CREATE TABLE users (
  id UUID PRIMARY KEY,
  name VARCHAR NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE questions (
  id UUID PRIMARY KEY,
  quiz_id UUID NOT NULL,
  text VARCHAR NOT NULL,
  difficulty INT,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE answers (
  id UUID PRIMARY KEY,
  question_id UUID NOT NULL,
  text VARCHAR NOT NULL,
  is_correct BOOLEAN NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE quiz_sessions (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  quiz_id UUID NOT NULL,
  started_at TIMESTAMP NOT NULL,
  completed_at TIMESTAMP,
  score DECIMAL(5,2)
);

CREATE TABLE user_responses (
  id UUID PRIMARY KEY,
  session_id UUID NOT NULL,
  question_id UUID NOT NULL,
  selected_answer_id UUID NOT NULL,
  is_correct BOOLEAN NOT NULL,
  created_at TIMESTAMP NOT NULL
);
```

### Core Business Logic (Same in All Languages)

```pseudocode
// 1. Create Quiz Session
function create_session(user_id, quiz_id):
  - Validate user and quiz exist
  - Create new session record
  - Return session ID

// 2. Get Questions for Session
function get_questions(session_id):
  - Retrieve 10 questions for quiz
  - SHUFFLE question order
  - Shuffle answers within each question
  - Return questions without answer correctness

// 3. Submit Answer & Evaluate
function submit_answer(session_id, question_id, selected_answer_id):
  - Validate session exists and is active
  - Validate question belongs to quiz
  - Check if answer is correct
  - Record user response
  - Update session score incrementally
  - Return {is_correct, points}

// 4. Calculate Final Score
function calculate_score(session_id):
  - Sum all correct responses
  - Convert to percentage (0-100)
  - Return final score
  - Mark session as completed
```

### Testing Requirements (All Languages)

**Coverage Threshold:** >90% of code must be tested
- Functions/methods: >90% must have tests
- Lines of code: >90% must be executed
- Branches: >90% must be covered

**Test Categories:**
1. Unit tests for each function
2. Integration tests for API endpoints
3. Database tests for CRUD operations
4. Edge case tests (empty inputs, null values, etc.)
5. Error handling tests

### Docker Requirements (All Languages)

**Dockerfile Structure:**
```dockerfile
# Stage 1: Build
FROM [build-image] AS builder
# Copy source, install dependencies, compile/build

# Stage 2: Runtime
FROM [minimal-image]
# Copy artifacts from builder
# Add non-root user
# Set security: no root
# Expose port or mount point
# ENTRYPOINT / CMD
```

**docker-compose.yml Structure:**
```yaml
version: "3.8"
services:
  quiz-engine:                    # Development service
    build: .
    ports: [8000:8000]

  quiz-engine-test:               # Test service with coverage
    build: .
    command: [test command with coverage verification >90%]

  quiz-engine-build:              # Optional build-only service
    build: .
    command: [build command]
```

---

## Execution Instructions for Copilot Agent

**When generating each implementation:**

1. **Read the corresponding prompt file** (e.g., `01-plan-quizEngine-[language].prompt.md`)
2. **Follow Phase 1-5 structure:**
   - Phase 1: Setup & configuration
   - Phase 2: Database schema & ORM
   - Phase 3: Core business logic
   - Phase 4: REST API
   - Phase 5: Testing (>90% coverage)
   - Phase 6: Docker containerization
3. **Generate files in order of dependency:**
   - Models/entities first
   - Database layer second
   - Business logic third
   - API routes fourth
   - Tests fifth
   - Docker files last
4. **Validate each implementation:**
   - All tests pass
   - Coverage >90%
   - Docker builds successfully
   - API responds correctly
5. **Commit to repository** when each implementation is complete

---

## Success Criteria

✅ **All 8 implementations complete:**
- [ ] Python (Flask + SQLAlchemy) - Fully functional, tested, dockerized
- [ ] Node.js (Express + TypeORM) - Fully functional, tested, dockerized
- [ ] Java (Gradle + JDBC) - Fully functional, tested, dockerized
- [ ] Spring Boot (Gradle + JPA) - Fully functional, tested, dockerized
- [ ] C# (.NET 8 + EF Core) - Fully functional, tested, dockerized
- [ ] Dart (Dart + Drift) - Fully functional, tested, dockerized
- [ ] Go (Gin + GORM) - Fully functional, tested, dockerized
- [ ] Rust (Actix-web + Diesel) - Fully functional, tested, dockerized

✅ **All implementations share:**
- [ ] Same database schema and data model
- [ ] Same REST API endpoints (POST /quizzes, etc.)
- [ ] Same response format (JSON envelope)
- [ ] >90% test coverage in each language
- [ ] Docker support with multi-stage builds
- [ ] Non-root user security in containers
- [ ] Production-ready code quality

✅ **Repository state:**
- [ ] All 8 project directories created
- [ ] Source files committed to GitHub
- [ ] Test files with >90% coverage
- [ ] Docker and docker-compose files
- [ ] README.md for each implementation
- [ ] CI/CD pipeline verified (tests pass, coverage validated)

---

## Next Steps for Copilot Agent

1. **Initialize:** Read this prompt and the 8 language-specific prompts
2. **Validate:** Confirm all prerequisites and dependencies are available
3. **Generate:** Create each implementation following the structure above
4. **Test:** Run tests and verify >90% coverage for each language
5. **Containerize:** Build Docker images and verify they run
6. **Commit:** Push all generated code to GitHub
7. **Report:** Provide summary of all 8 implementations created

---

**This Prompt Enables Complete Code Generation of Quiz Engine Across All 8 Languages Simultaneously**
