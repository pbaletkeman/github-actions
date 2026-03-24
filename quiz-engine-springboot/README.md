# GH-200 Quiz Engine - Spring Boot

A Spring Boot quiz engine for GH-200 (GitHub Actions) certification practice.

## Features

- **Spring Boot 3.2** with Spring Data JPA
- **REST API** for quiz management
- **Thymeleaf** web interface at http://localhost:8080
- **CLI** interface via Picocli
- **H2** (test) / **SQLite** (production) database
- **JaCoCo** test coverage enforcement

## Quick Start

### Prerequisites
- Java 17+
- Gradle (or use included wrapper)

### Run the Application
```bash
./gradlew bootRun
```

Visit http://localhost:8080

### Run Tests
```bash
./gradlew test
```

### Run with Coverage
```bash
./gradlew test jacocoTestReport
# Report: build/reports/jacoco/test/html/index.html
```

### Build JAR
```bash
./gradlew build
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar
```

## REST API

### Start a Quiz
```bash
curl -X POST http://localhost:8080/api/quiz/start \
  -H 'Content-Type: application/json' \
  -d '{"numQuestions": 10}'
```

### Submit an Answer
```bash
curl -X POST http://localhost:8080/api/quiz/{sessionId}/answer \
  -H 'Content-Type: application/json' \
  -d '{"questionIndex": 0, "answer": "A", "timeTaken": 15}'
```

### Get History
```bash
curl http://localhost:8080/api/history
```

### Import Questions
```bash
curl -X POST http://localhost:8080/api/import \
  -H 'Content-Type: application/json' \
  -d '{"content": "## Question 1\n- A) ...", "source": "my-file.md"}'
```

## Docker

```bash
docker build -t quiz-engine .
docker run -p 8080:8080 quiz-engine
```

## Configuration

See `src/main/resources/application.yml` for configuration options.

## Project Structure

```
src/main/java/com/quizengine/
├── QuizEngineApplication.java     # Spring Boot entry point
├── entity/                        # JPA entities
├── repository/                    # Spring Data JPA repositories
├── service/                       # Business logic
├── controller/                    # REST + Web controllers
├── cli/                           # Picocli CLI commands
├── util/                          # Helper utilities
├── config/                        # Spring configuration
└── exception/                     # Custom exceptions
```
