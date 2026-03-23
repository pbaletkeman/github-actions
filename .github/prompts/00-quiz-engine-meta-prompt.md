---
name: "Quiz Engine - Multi-Language Implementation Guide (All 8 Implementations)"
description: "Comprehensive meta-prompt for GitHub Copilot to reference and work with all 8 quiz engine implementations"
applyTo: "*"
---

# Quiz Engine - Unified Multi-Language Implementation Reference

This meta-prompt provides GitHub Copilot with a comprehensive overview of all 8 quiz engine implementations, enabling seamless collaboration and consistency across languages.

## Quick Navigation

| #  | Language      | File                              | Status       | Key Features                          |
|----|---------------|-----------------------------------|--------------|---------------------------------------|
| 01 | Python        | 01-plan-quizEngine-python.prompt.md | ✅ Complete | Flask, SQLAlchemy, unittest, pytest   |
| 02 | Node.js       | 02-plan-quizEngine-nodejs.prompt.md | ✅ Complete | Express, TypeORM, Jest, ESLint        |
| 03 | Java          | 03-plan-quizEngine-java.prompt.md | ✅ Complete | Gradle, JDBC, JaCoCo, Hibernate      |
| 04 | Spring Boot   | 04-plan-quizEngine-springboot.prompt.md | ✅ Complete | Spring Framework, JPA, Gradle         |
| 05 | C#/.NET       | 05-plan-quizEngine-csharp.prompt.md | ✅ Complete | .NET 8, EF Core, Coverlet, xUnit     |
| 06 | Dart          | 06-plan-quizEngine-dart.prompt.md | ✅ Complete | Flutter, Drift, pub.dev, test        |
| 07 | Go            | 07-plan-quizEngine-golang.prompt.md | ✅ Complete | Gin, GORM, go test, SQLite           |
| 08 | Rust          | 08-plan-quizEngine-rust.prompt.md | ✅ Complete | Actix, Diesel, cargo-tarpaulin       |

---

## Core Implementation Phases (All 8 Languages)

Every implementation follows this standardized 6-phase lifecycle:

### Phase 1: Environment & Project Setup
- **Python**: venv, pip, Flask, SQLAlchemy
- **Node.js**: npm, Express, TypeORM
- **Java**: Gradle, JDBC driver
- **Spring Boot**: Spring CLI, Gradle, JPA
- **C#**: .NET SDK, EF Core
- **Dart**: pub.dev, Drift ORM
- **Go**: Go modules, Gin framework
- **Rust**: Cargo, Actix-web

### Phase 2: Database Schema & ORM Setup
- All use SQL-based relational databases (SQLite, PostgreSQL, MySQL)
- ORM/Query builders available in each language
- Schema includes: questions, answers, quiz_sessions, user_responses
- Migrations framework specific to each language

### Phase 3: Core Quiz Engine Logic Implementation
- Quiz session creation and management
- Question shuffling and randomization
- Answer evaluation with immediate feedback
- Score calculation and reporting
- Session persistence and retrieval

### Phase 4: REST/HTTP API Design
- **Python**: Flask routes (`/api/v1/quizzes`, `/api/v1/sessions`, `/api/v1/submit`)
- **Node.js**: Express routes with TypeORM queries
- **Java/Spring Boot**: Spring Controller annotations (@RequestMapping, @GetMapping, @PostMapping)
- **C#**: ASP.NET Core API endpoints
- **Dart**: Shelf routes or Flutter integration
- **Go**: Gin RouterGroup with structured handlers
- **Rust**: Actix-web routes with extractors

### Phase 5: Comprehensive Testing (>90% Coverage Required)
- **Python**: pytest with pytest-cov (--cov-fail-under=90)
- **Node.js**: Jest with coverageThreshold {lines: 90, functions: 90}
- **Java**: JUnit + JaCoCo (jacocoTestCoverageVerification minimum=0.90)
- **Spring Boot**: JUnit + JaCoCo (minimum=0.90)
- **C#**: xUnit + Coverlet (/p:Threshold=90 /p:ThresholdType=line)
- **Dart**: test package + lcov (genhtml exit 1 if < 90%)
- **Go**: go test with awk coverage check (exit 1 if < 90%)
- **Rust**: cargo-tarpaulin (--fail-under 90)

### Phase 6: Docker & Containerization (Recently Added)
- **All 8 languages** now include:
  - Production Dockerfile with multi-stage builds where applicable
  - docker-compose.yml with dev/test/build services
  - Quick Start guide (5 steps)
  - Build & Push instructions for multi-architecture (linux/amd64, linux/arm64)

---

## Docker & Containerization (Phase 6 Details)

### Quick Start (All Languages Use Same Pattern)
```bash
# 1. Build the image
docker build -t quiz-engine:<language> .

# 2. Run development mode
docker-compose up quiz-engine

# 3. Run tests with coverage verification
docker-compose up quiz-engine-test

# 4. Build optimized production image
docker build -t quiz-engine:<language>:prod -f Dockerfile.prod .

# 5. Run interactively
docker run -it quiz-engine:<language> bash
```

### Base Image Strategy by Language
- **Python**: `python:3.11-slim` → Multi-stage not required
- **Node.js**: `node:20-alpine` → Minimal Alpine runtime
- **Java**: `gradle:8-jdk17` → `eclipse-temurin:17-jre-alpine` (multi-stage)
- **Spring Boot**: `gradle:8-jdk17` → `eclipse-temurin:17-jre-alpine` (multi-stage)
- **C#**: `mcr.microsoft.com/dotnet/sdk:8.0` → `mcr.microsoft.com/dotnet/runtime:8.0` (multi-stage)
- **Dart**: `google/dart:3.0` → `alpine:latest` (multi-stage compiled)
- **Go**: `golang:1.21-alpine` → `alpine:latest` (multi-stage minimal)
- **Rust**: `rust:1.75` → `debian:bookworm-slim` (multi-stage)

### Security: Non-Root Users in All Containers
- Python: `quizuser`
- Node.js: `nodeuser`
- Java: `javauser`
- Spring Boot: `springuser`
- C#: `dotnetuser`
- Dart: `dartuser`
- Go: `gouser`
- Rust: `rustuser`

---

## README.md Standards (Implementation Includes)

Each implementation should include a README with:

```markdown
# Quiz Engine - [Language]

## Quick Start
1. Clone repository
2. Install dependencies (npm/pip/gradle/dotnet/pub/go/cargo)
3. Configure database connection
4. Run: `<language-specific command>`
5. Access: http://localhost:3000

## Running Unit Tests
- Python: `pytest --cov=quiz_engine --cov-fail-under=90`
- Node.js: `npm test`
- Java: `gradle test jacocoTestCoverageVerification`
- Spring Boot: `./gradlew test jacocoTestCoverageVerification`
- C#: `dotnet test /p:Threshold=90`
- Dart: `pub run test && genhtml coverage/lcov.info -o coverage/html`
- Go: `go test ./... -cover`
- Rust: `cargo tarpaulin --fail-under 90`

## Docker Deployment
See Docker & Containerization section for container image details.

## Full Documentation
[Link to full specification and architecture details]

## Table of Contents
1. Overview
2. Prerequisites
3. Installation
4. Quick Start
5. Architecture
6. API Endpoints
7. Testing
8. Docker & Containerization
9. Troubleshooting
10. Contributing
```

---

## Environment Setup Instructions (Per Language)

### Python
```bash
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
```

### Node.js
```bash
npm install
npm run dev   # Development mode
npm test      # Run tests
```

### Java
```bash
gradle dependencies
gradle build
gradle run
```

### Spring Boot
```bash
./gradlew dependencies
./gradlew bootRun
./gradlew test
```

### C#
```bash
dotnet restore
dotnet build
dotnet run
dotnet test
```

### Dart
```bash
pub get
dart run bin/main.dart
```

### Go
```bash
go mod download
go run main.go
go test ./...
```

### Rust
```bash
cargo build
cargo run
cargo test
```

---

## Consistency Rules Across All 8 Implementations

### API Response Format (All Languages)
```json
{
  "success": true,
  "data": { /* specific response data */ },
  "error": null,
  "timestamp": "2024-03-23T12:34:56Z"
}
```

### Error Handling (All Languages)
- 400: Bad Request (validation failure)
- 401: Unauthorized (authentication failure)
- 403: Forbidden (authorization failure)
- 404: Not Found (resource not found)
- 500: Internal Server Error (server failure)

### Testing Coverage Thresholds (All Languages)
- **Minimum Coverage**: 90% (lines, functions, branches)
- **Build Fails If**: Coverage drops below threshold
- **Coverage Report**: Generated and committed to repository

### Performance Requirements (All Languages)
- API response time: < 200ms (p95)
- Quiz question retrieval: < 100ms
- Answer evaluation: < 50ms
- Database queries: Indexed and optimized

---

## Task Distribution Across Languages

When working with multiple implementations, keep these language-specific considerations in mind:

| Aspect | Python | Node.js | Java | Spring | C# | Dart | Go | Rust |
|--------|--------|---------|------|--------|----|----|----|----|
| **Package Manager** | pip | npm | gradle | gradle | nuget | pub | go mod | cargo |
| **Build Tool** | setuptools | npm | gradle | gradle | msbuild | dart | go build | cargo |
| **Testing Framework** | pytest | jest | junit | junit | xunit | test | go test | cargo test |
| **ORM** | SQLAlchemy | TypeORM | Hibernate | JPA | EF Core | Drift | GORM | Diesel |
| **Web Framework** | Flask | Express | Spring | Spring | ASP.NET | Shelf | Gin | Actix |
| **Docker Base** | python:3.11 | node:20 | eclipse-temurin:17 | eclipse-temurin:17 | dotnet:8 | google/dart | golang:1.21 | rust:1.75 |

---

## GitHub Copilot Collaboration Mode

When using this prompt, GitHub Copilot can:

1. **Generate consistent code** across all 8 languages following the specification above
2. **Reference specific phases** from each implementation file
3. **Ensure 90%+ test coverage** is maintained in all languages
4. **Apply Docker & containerization** standards uniformly
5. **Generate README.md** templates with consistent structure
6. **Validate architecture decisions** against established patterns

### Example Collaboration Prompts

```
"Generate a new API endpoint for '/api/v1/quizzes/{id}/leaderboard' across all 8 languages
following the existing patterns in the quiz engine implementations."

"Create unit tests for the Answer evaluation logic in [Language] that achieves
>90% code coverage following the pattern in 01-plan-quizEngine-[Language].prompt.md"

"Generate Docker deployment instructions for all 8 implementations following
the Docker & Containerization phase documented in each prompt."

"Create a README.md template that includes all sections outlined in this meta-prompt."
```

---

## Quick Reference: File Locations

All 8 prompt files are located in:
```
c:\Users\Pete\Desktop\github-actions\.github\prompts\
```

GitHub Repository:
```
https://github.com/pbaletkeman/github-actions
```

---

## Last Updated

- **Date**: March 23, 2026
- **Changes**: All 8 implementations updated with Docker & containerization support
- **Status**: ✅ Complete and ready for GitHub Copilot collaboration

---

**Instructions for GitHub Copilot Users:**

Use this prompt as your reference document when working with any of the 8 quiz engine implementations. When you need to:
- Generate new features across all languages
- Ensure consistency in API responses
- Apply testing standards
- Configure Docker containers
- Create documentation

Simply reference back to this meta-prompt and the specific implementation files for guidance. This ensures all 8 implementations remain synchronized and consistent.
