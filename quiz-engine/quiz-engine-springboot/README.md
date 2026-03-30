# Quiz Engine -- Spring Boot

> Part of the [Quiz Engine multi-language collection](../README.md)

- [Quiz Engine -- Spring Boot](#quiz-engine----spring-boot)
  - [Get Started in 5 Minutes](#get-started-in-5-minutes)
    - [Prerequisites](#prerequisites)
    - [1. Build \& Run (Web UI)](#1-build--run-web-ui)
    - [2. Import Questions via API](#2-import-questions-via-api)
    - [3. Start a Quiz via API](#3-start-a-quiz-via-api)
    - [4. Run Tests](#4-run-tests)
    - [5. Docker](#5-docker)

A Spring Boot quiz engine for GitHub Actions (GH-200) certification prep. Includes REST API, Thymeleaf web UI, and CLI interface.

## Get Started in 5 Minutes

### Prerequisites
- [JDK 17+](https://adoptium.net/)

### 1. Build & Run (Web UI)
```bash
./gradlew bootRun
```
Then open http://localhost:8080

### 2. Import Questions via API
```bash
curl -X POST http://localhost:8080/api/import \
  -H 'Content-Type: application/json' \
  -d '{"content": "## Q1\n...", "source": "questions.md"}'
```

### 3. Start a Quiz via API
```bash
curl -X POST http://localhost:8080/api/quiz/start \
  -H 'Content-Type: application/json' \
  -d '{"numQuestions": 10}'
```

### 4. Run Tests
```bash
./gradlew test
```

### 5. Docker
```bash
docker build -t quiz-engine-springboot:latest .
docker run -p 8080:8080 quiz-engine-springboot:latest
# or
docker-compose up quiz-engine
```

---

Full docs: [docs/README.md](docs/README.md) | Architecture: [docs/architecture.md](docs/architecture.md)
