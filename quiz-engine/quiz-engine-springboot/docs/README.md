# Quiz Engine — Spring Boot — Full Documentation

> Part of the [Quiz Engine multi-language collection](../../README.md)

- [Quiz Engine — Spring Boot — Full Documentation](#quiz-engine--spring-boot--full-documentation)
  - [Overview](#overview)
    - [Features](#features)
  - [Project Structure](#project-structure)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Script Reference](#script-reference)
    - [Build Scripts](#build-scripts)
      - [`build.bat` (Windows CMD)](#buildbat-windows-cmd)
      - [`build.ps1` (PowerShell)](#buildps1-powershell)
      - [`build.sh` (Bash / macOS / Linux)](#buildsh-bash--macos--linux)
    - [Quiz Scripts](#quiz-scripts)
      - [`quiz.bat` / `quiz.ps1` / `quiz.sh`](#quizbat--quizps1--quizsh)
    - [Import Scripts](#import-scripts)
      - [`import.bat` / `import.ps1` / `import.sh`](#importbat--importps1--importsh)
    - [History Scripts](#history-scripts)
      - [`history.bat` / `history.ps1` / `history.sh`](#historybat--historyps1--historysh)
  - [CLI Commands](#cli-commands)
    - [`import` — Load questions](#import--load-questions)
    - [`quiz` — CLI quiz session](#quiz--cli-quiz-session)
    - [`history` — View sessions](#history--view-sessions)
    - [Global Options](#global-options)
  - [Web Interface](#web-interface)
  - [REST API Reference](#rest-api-reference)
    - [Import Questions](#import-questions)
    - [Start a Quiz Session](#start-a-quiz-session)
    - [Submit an Answer](#submit-an-answer)
    - [Finish Session](#finish-session)
    - [Get History](#get-history)
  - [Docker Setup](#docker-setup)
    - [Building](#building)
    - [Running Interactively](#running-interactively)
    - [Environment Variables](#environment-variables)
    - [Docker Compose Services](#docker-compose-services)
  - [Question File Format](#question-file-format)
  - [Configuration](#configuration)
    - [`application.properties` (production)](#applicationproperties-production)
    - [`application-test.properties` (test profile)](#application-testproperties-test-profile)
  - [Testing](#testing)
  - [Dependencies](#dependencies)
  - [Architecture](#architecture)

---

A quiz engine for GH-200 GitHub Actions certification preparation built with Spring Boot 3.2. **Unique among the collection** — this project provides both a command-line interface (Picocli) and a full Thymeleaf web UI accessible at `http://localhost:8080`. It also exposes a REST API.

---

## Overview

### Features

- **Web UI** — Thymeleaf-based quiz interface at `http://localhost:8080`
- **REST API** — JSON endpoints for import, quiz, history
- **CLI** — Picocli command-line interface (same commands as other engines)
- **Dual database** — H2 in-memory for tests, SQLite for production
- **Non-repetition cycle tracking** — `usageCycle` + `timesUsed` JPA columns
- **Markdown import** — load questions from `.md` files via REST or CLI
- **JaCoCo coverage** — enforced minimum threshold: 90%
- Gradle build system + Spring Data JPA

---

## Project Structure

```
quiz-engine-springboot/
├── src/
│   ├── main/
│   │   ├── java/com/quizengine/
│   │   │   ├── QuizEngineApplication.java  # Spring Boot entry point
│   │   │   ├── entity/
│   │   │   │   ├── Question.java           # JPA entity
│   │   │   │   ├── QuizSession.java
│   │   │   │   └── QuizResponse.java
│   │   │   ├── repository/
│   │   │   │   ├── QuestionRepository.java # Spring Data JPA
│   │   │   │   ├── SessionRepository.java
│   │   │   │   └── ResponseRepository.java
│   │   │   ├── service/
│   │   │   │   ├── QuizService.java
│   │   │   │   ├── ImportService.java
│   │   │   │   └── HistoryService.java
│   │   │   ├── controller/
│   │   │   │   ├── WebController.java      # Thymeleaf MVC
│   │   │   │   ├── QuizRestController.java # REST API
│   │   │   │   └── ImportRestController.java
│   │   │   ├── cli/
│   │   │   │   └── QuizCli.java            # Picocli commands
│   │   │   ├── util/
│   │   │   │   ├── MarkdownParser.java
│   │   │   │   └── AnswerShuffler.java
│   │   │   ├── config/
│   │   │   │   └── AppConfig.java          # SQLite DataSource config
│   │   │   └── exception/
│   │   │       └── QuizException.java
│   │   └── resources/
│   │       ├── application.properties      # Main config
│   │       ├── application-test.properties # H2 config
│   │       ├── templates/                  # Thymeleaf HTML templates
│   │       │   ├── index.html
│   │       │   ├── quiz.html
│   │       │   ├── results.html
│   │       │   └── history.html
│   │       └── static/                     # CSS / JS assets
│   └── test/
│       └── java/com/quizengine/
│           ├── service/
│           ├── controller/
│           └── repository/
├── build.gradle
├── gradlew / gradlew.bat
├── Dockerfile                              # gradle:8-jdk17 → temurin:17-jre-alpine
├── docker-compose.yml
├── build.bat / build.ps1 / build.sh
├── quiz.bat / quiz.ps1 / quiz.sh
├── import.bat / import.ps1 / import.sh
├── history.bat / history.ps1 / history.sh
└── README.md
```

---

## Prerequisites

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 17+ | https://adoptium.net/ |
| Gradle | via wrapper (`./gradlew`) | Included |
| Docker (optional) | 20.10+ | https://docs.docker.com/get-docker/ |

Verify:
```bash
java -version   # 17+
./gradlew --version
```

---

## Installation

```bash
# Build (compile + test + package)
./gradlew build           # macOS / Linux
gradlew.bat build         # Windows

# Or use provided scripts
./build.sh
build.bat
.\build.ps1

# Run the application (web server + CLI)
./gradlew bootRun

# Or run the packaged JAR
java -jar build/libs/quiz-engine-*.jar
```

The server starts at `http://localhost:8080`.

---

## Script Reference

### Build Scripts

#### `build.bat` (Windows CMD)

```bat
build.bat
```

- Calls `gradlew.bat build` — compiles, runs tests, packages JAR
- Prints build output and final artifact path

#### `build.ps1` (PowerShell)

```powershell
.\build.ps1
```

- `$ErrorActionPreference = "Stop"` for fail-fast behavior
- Runs `.\gradlew build` with colorized progress
- Reports test results and JAR location

#### `build.sh` (Bash / macOS / Linux)

```bash
./build.sh
```

- Calls `./gradlew build`
- Uses `set -e` for fail-fast behavior

---

### Quiz Scripts

For the CLI workflow (not the web UI):

#### `quiz.bat` / `quiz.ps1` / `quiz.sh`

```bash
quiz.bat 10           # Windows CMD — 10 questions
.\quiz.ps1 -N 10      # PowerShell  — 10 questions
./quiz.sh 10          # Bash        — 10 questions
```

These scripts:
1. Check the JAR exists in `build/libs/`
2. Run `java -jar quiz-engine.jar quiz --questions <N>`

---

### Import Scripts

#### `import.bat` / `import.ps1` / `import.sh`

```bash
import.bat questions.md              # Windows CMD single file
import.bat .\questions\              # directory
.\import.ps1 -Path questions.md      # PowerShell
./import.sh questions.md             # Bash
./import.sh ./questions/             # directory
```

---

### History Scripts

#### `history.bat` / `history.ps1` / `history.sh`

```bash
history.bat
.\history.ps1
./history.sh
```

All call `java -jar quiz-engine.jar history`.

---

## CLI Commands

Uses **Picocli** — same pattern as all other engines:

### `import` — Load questions

```bash
java -jar quiz-engine.jar import --file questions.md
java -jar quiz-engine.jar import --dir ./questions/
```

### `quiz` — CLI quiz session

```bash
java -jar quiz-engine.jar quiz --questions 10
java -jar quiz-engine.jar quiz --no-shuffle
```

### `history` — View sessions

```bash
java -jar quiz-engine.jar history
java -jar quiz-engine.jar history --session-id <uuid>
java -jar quiz-engine.jar history --export json
java -jar quiz-engine.jar history --export csv
```

| Option | Description |
|--------|-------------|
| `--session-id` | Show details for a specific session |
| `--export json\|csv` | Export all sessions to a timestamped file |

### `clear` — Delete stored data

```bash
java -jar quiz-engine.jar clear --questions --confirm
java -jar quiz-engine.jar clear --history --confirm
java -jar quiz-engine.jar clear --all --confirm
```

| Option | Description |
|--------|-------------|
| `--questions` | Delete all imported questions |
| `--history` | Delete all session history |
| `--all` | Delete everything (questions and history) |
| `--confirm` | Required flag to prevent accidental deletion |

### Global Options

| Option | Default | Description |
|--------|---------|-------------|
| `--db` | `./quiz_engine.db` | SQLite database path |

---

## Web Interface

Open `http://localhost:8080` in your browser after `./gradlew bootRun`.

| Page | URL | Description |
|------|-----|-------------|
| Home | `/` | Start a quiz or import questions |
| Quiz | `/quiz` | Interactive quiz session |
| Results | `/quiz/results` | Session score page |
| History | `/history` | All past sessions |

---

## REST API Reference

### Import Questions

```
POST /api/import
Content-Type: multipart/form-data

file=@questions.md
```

### Start a Quiz Session

```
POST /api/quiz/start
Content-Type: application/json

{
  "questionCount": 10,
  "shuffle": true
}

Response 201:
{ "sessionId": "<uuid>", "questions": [ ... ] }
```

### Submit an Answer

```
POST /api/quiz/{sessionId}/answer
Content-Type: application/json

{
  "questionId": "<uuid>",
  "selectedAnswer": "A"
}

Response 200:
{ "correct": true, "correctAnswer": "A", "explanation": "..." }
```

### Finish Session

```
POST /api/quiz/{sessionId}/finish

Response 200:
{ "sessionId": "...", "score": 8, "total": 10, "percent": 80.0 }
```

### Get History

```
GET /api/history
GET /api/history/{sessionId}
GET /api/history?export=json
GET /api/history?export=csv
```

---

## Docker Setup

### Building

```bash
docker build -t quiz-engine-spring:latest .
docker run -p 8080:8080 quiz-engine-spring:latest
```

Open `http://localhost:8080` in your browser.

### Running Interactively

```bash
docker run -it \
  -p 8080:8080 \
  -v quiz-spring-data:/data \
  -e QUIZ_DB_PATH=/data/quiz.db \
  -e SPRING_PROFILES_ACTIVE=prod \
  quiz-engine-spring:latest
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `QUIZ_DB_PATH` | `/data/quiz_engine.db` | SQLite database path |
| `SPRING_PROFILES_ACTIVE` | `prod` | Spring profile (`prod` uses SQLite, `test` uses H2) |
| `SERVER_PORT` | `8080` | HTTP server port |

### Docker Compose Services

```bash
docker-compose up quiz-engine            # Web server + CLI
docker-compose up quiz-engine-test      # Tests with JaCoCo report
docker-compose up quiz-engine-build     # Build JAR only
```

| Service | Port | Description |
|---------|------|-------------|
| `quiz-engine` | `8080` | Spring Boot app (web + CLI) |
| `quiz-engine-test` | — | `./gradlew test jacocoTestReport` |
| `quiz-engine-build` | — | `./gradlew build` |

---

## Question File Format

```markdown
# Quiz Title

**Iteration**: 1
**Total Questions**: 2

---

## Questions

---

### Question 1 — Workflow Trigger Events

**Difficulty**: Easy
**Answer Type**: one
**Topic**: schedule trigger

**Question**:
Which trigger event is used to run a workflow on a recurring time-based schedule?

- A) `on: timer`
- B) `on: cron`
- C) `on: schedule`
- D) `on: workflow_dispatch`

---

### Question 2 — Contextual Information

**Difficulty**: Medium
**Answer Type**: many
**Topic**: secrets context usage

**Scenario**:
Your team reviews a workflow and finds several usages of the `secrets` context.
You need to identify which usages are valid.

**(Select all that apply)**
Which locations in a workflow file can reference the `secrets` context?

- A) `jobs.<job_id>.steps[*].env`
- B) `jobs.<job_id>.steps[*].with`
- C) `jobs.<job_id>.strategy.matrix`
- D) `jobs.<job_id>.steps[*].run` (via expression `${{ secrets.MY_SECRET }}`)

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | C | `on: schedule` is the correct trigger. `cron` is the value of the `schedule` key, not the trigger name itself. | 05-Workflow-Trigger-Events.md | Easy |
| 2 | A, B, D | `secrets` is available in `steps[*].env`, `steps[*].with`, and `steps[*].run`. It is NOT available in `strategy.matrix`. | 02-Contextual-Information.md | Medium |
```

---

## Configuration

### `application.properties` (production)

```properties
spring.datasource.url=jdbc:sqlite:${QUIZ_DB_PATH:./quiz_engine.db}
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.hibernate.ddl-auto=update
server.port=${SERVER_PORT:8080}
```

### `application-test.properties` (test profile)

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
```

---

## Testing

```bash
# Run all tests (Spring uses H2 in-memory for tests)
./gradlew test

# JaCoCo HTML report (build/reports/jacoco/test/html/index.html)
./gradlew jacocoTestReport

# Enforce coverage thresholds
./gradlew jacocoTestCoverageVerification
```

---

## Dependencies

| Dependency | Purpose |
|-----------|---------|
| `spring-boot-starter-web` | Embedded Tomcat + REST |
| `spring-boot-starter-thymeleaf` | Server-side HTML templates |
| `spring-boot-starter-data-jpa` | JPA + Hibernate ORM |
| `xerial/sqlite-jdbc` | SQLite JDBC driver |
| `info.picocli:picocli-spring-boot-starter` | CLI integration |
| `com.h2database:h2` | In-memory DB for tests |
| `spring-boot-starter-test` | JUnit 5 + Mockito |
| `jacoco` | Code coverage |

---

## Architecture

See [architecture.md](architecture.md) for full sequence, class, ER, and data flow diagrams.
