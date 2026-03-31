# Quiz Engine — Java — Full Documentation

> Part of the [Quiz Engine multi-language collection](../../README.md)

- [Quiz Engine — Java — Full Documentation](#quiz-engine--java--full-documentation)
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
    - [`import` — Load questions from Markdown](#import--load-questions-from-markdown)
    - [`quiz` — Take a quiz](#quiz--take-a-quiz)
    - [`history` — View past sessions](#history--view-past-sessions)
    - [`clear` — Remove stored data](#clear--remove-stored-data)
    - [Global Options (picocli)](#global-options-picocli)
  - [Docker Setup](#docker-setup)
    - [Building](#building)
    - [Running Interactively](#running-interactively)
    - [Environment Variables](#environment-variables)
    - [Docker Compose Services](#docker-compose-services)
  - [Question File Format](#question-file-format)
  - [Configuration](#configuration)
  - [Testing](#testing)
  - [Build System Notes — Gradle vs Maven](#build-system-notes--gradle-vs-maven)
  - [Dependencies](#dependencies)
  - [Architecture](#architecture)


---

A command-line quiz engine for GH-200 GitHub Actions certification preparation built with Java 17, SQLite, and picocli. Packages as an executable JAR.

---

## Overview

### Features

- **Interactive CLI quiz** with shuffled answer options
- **SQLite persistence** via JDBC
- **Non-repetition cycle tracking** — questions cycle before repeating
- **Markdown import** — load questions from `.md` files
- **Session history** — browse past results
- **JaCoCo coverage** enforcement (≥90% line coverage)
- Packages as a single executable JAR

---

## Project Structure

```
quiz-engine-java/
├── src/
│   ├── main/
│   │   ├── java/com/quizengine/
│   │   │   ├── Main.java                  # Entry point
│   │   │   ├── model/
│   │   │   │   ├── Question.java
│   │   │   │   ├── QuizSession.java
│   │   │   │   └── QuizResponse.java
│   │   │   ├── dao/
│   │   │   │   ├── QuestionDao.java
│   │   │   │   ├── SessionDao.java
│   │   │   │   └── ResponseDao.java
│   │   │   ├── service/
│   │   │   │   ├── QuizService.java
│   │   │   │   ├── ImportService.java
│   │   │   │   └── HistoryService.java
│   │   │   ├── util/
│   │   │   │   ├── MarkdownParser.java
│   │   │   │   ├── AnswerShuffler.java
│   │   │   │   └── DatabaseManager.java
│   │   │   └── cli/
│   │   │       ├── MainCommand.java       # picocli root
│   │   │       ├── QuizCommand.java
│   │   │       ├── ImportCommand.java
│   │   │       ├── HistoryCommand.java
│   │   │       └── ClearCommand.java
│   │   └── resources/
│   │       └── schema.sql                 # DDL for auto-init
│   └── test/
│       └── java/com/quizengine/
│           ├── service/
│           ├── dao/
│           └── util/
├── build.gradle (or pom.xml)              # See Build System Notes
├── Dockerfile                             # Maven-based build
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
| Gradle | 8+ (via wrapper) | Included: `gradlew` |
| Docker (optional) | 20.10+ | https://docs.docker.com/get-docker/ |

Verify:
```bash
java -version    # 17+
./gradlew --version
```

---

## Installation

```bash
# Build with Gradle wrapper (cross-platform)
./gradlew build          # macOS / Linux
gradlew.bat build        # Windows

# Or use the provided script
./build.sh               # Bash
build.bat                # Windows CMD
.\build.ps1              # PowerShell

# Run
java -jar target/quiz-engine.jar --help
```

---

## Script Reference

### Build Scripts

#### `build.bat` (Windows CMD)

```bat
build.bat
```

- Calls `gradlew.bat build` or `.\gradlew build`
- Compiles, runs tests, packages the shadow/fat JAR
- Output: `build/libs/quiz-engine-<version>-all.jar` or `target/quiz-engine.jar`

#### `build.ps1` (PowerShell)

```powershell
.\build.ps1
```

- Sets `$ErrorActionPreference = "Stop"` for fail-fast behavior
- Outputs progress in cyan/green
- Calls `.\gradlew build`

#### `build.sh` (Bash / macOS / Linux)

```bash
./build.sh
```

- Calls `./gradlew build`
- Uses `set -e` for fail-fast behavior

---

### Quiz Scripts

#### `quiz.bat` / `quiz.ps1` / `quiz.sh`

```bash
quiz.bat 10           # Windows CMD — 10 questions
.\quiz.ps1 -N 10      # PowerShell  — 10 questions
./quiz.sh 10          # Bash        — 10 questions
```

These scripts:
1. Check that the JAR exists in `target/` or `build/libs/`
2. Run: `java -jar quiz-engine.jar quiz --questions <N>`
3. Exit with an error if JAR is missing (run build first)

---

### Import Scripts

#### `import.bat` / `import.ps1` / `import.sh`

```bash
import.bat questions.md        # Windows CMD
import.bat .\questions\        # Directory
.\import.ps1 -Path questions.md # PowerShell
./import.sh questions.md        # Bash single file
./import.sh ./questions/        # Bash directory
```

- Detects file vs. directory
- Calls `java -jar quiz-engine.jar import --file <path>` or `--dir <path>`

---

### History Scripts

#### `history.bat` / `history.ps1` / `history.sh`

```bash
history.bat
.\history.ps1
./history.sh
```

All invoke `java -jar quiz-engine.jar history`.

---

## CLI Commands

### `import` — Load questions from Markdown

```bash
quiz-engine import --file questions.md
quiz-engine import --dir ./questions/
```

| Option | Description |
|--------|-------------|
| `--file` | Path to a single `.md` file |
| `--dir` | Path to a directory |

---

### `quiz` — Take a quiz

```bash
quiz-engine quiz                     # 10 questions (default)
quiz-engine quiz --questions 20
quiz-engine quiz --no-shuffle
```

| Option | Default | Description |
|--------|---------|-------------|
| `--questions` | 10 | Number of questions |
| `--no-shuffle` | false | Disable answer shuffling |

---

### `history` — View past sessions

```bash
quiz-engine history
quiz-engine history --session-id <uuid>
quiz-engine history --export json
quiz-engine history --export csv
```

---

### `clear` — Remove stored data

```bash
quiz-engine clear --questions --confirm
quiz-engine clear --history --confirm
quiz-engine clear --all --confirm
```

### Global Options (picocli)

| Option | Default | Description |
|--------|---------|-------------|
| `--db` | `./quiz_engine.db` | Path to SQLite database |
| `--help` | — | Show help |
| `--version` | — | Show version |

---

## Docker Setup

> **Note:** The `Dockerfile` uses a **Maven** build (`FROM maven:3.9-eclipse-temurin-17 AS builder`) even though the project build scripts use Gradle. Both produce equivalent JARs. See [Build System Notes](#build-system-notes--gradle-vs-maven).

### Building

```bash
docker build -t quiz-engine-java:latest .
docker run --rm quiz-engine-java:latest --help
```

### Running Interactively

```bash
# Quiz with persistent volume
docker run -it \
  -v quiz-java-data:/data \
  -e QUIZ_DB_PATH=/data/quiz.db \
  quiz-engine-java:latest quiz --questions 10

# Import local file
docker run -it \
  -v quiz-java-data:/data \
  -v "$(pwd)/questions.md:/tmp/questions.md" \
  quiz-engine-java:latest import --file /tmp/questions.md
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `QUIZ_DB_PATH` | `/data/quiz_engine.db` | SQLite database path inside container |

### Docker Compose Services

```bash
docker-compose up quiz-engine       # Run CLI
docker-compose up quiz-engine-test  # Run tests with JaCoCo report
docker-compose up quiz-engine-build # Build JAR
```

| Service | Description |
|---------|-------------|
| `quiz-engine` | Interactive CLI container |
| `quiz-engine-test` | `mvn test` with JaCoCo report |
| `quiz-engine-build` | `mvn package` fat-JAR compilation |

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

```bash
# Custom database path
java -jar quiz-engine.jar --db /var/data/quiz.db quiz --questions 10

# Environment variable
export QUIZ_DB_PATH=/var/data/quiz.db
java -jar quiz-engine.jar quiz
```

---

## Testing

```bash
# Run tests with Gradle
./gradlew test

# JaCoCo coverage report (HTML at build/reports/jacoco/)
./gradlew jacocoTestReport

# Enforce ≥90% coverage threshold (fails build if below)
./gradlew jacocoTestCoverageVerification

# With Maven (Docker workflow)
mvn test
mvn jacoco:report
```

---

## Build System Notes — Gradle vs Maven

There is an **intentional inconsistency** between the build scripts and the Dockerfile:

| Context | Build Tool | Command |
|---------|-----------|---------|
| Local development | Gradle (`gradlew`) | `./gradlew build` |
| Docker / CI | Maven (`mvn`) | `mvn package` |

The Gradle wrapper (`gradlew`) is the primary local build tool. The Dockerfile uses Maven because it provides reliable, reproducible builds inside Alpine/Temurin images without needing to cache a Gradle home directory.

Both produce a runnable fat-JAR containing all dependencies. Use whichever is appropriate for your workflow.

---

## Dependencies

| Dependency | Version | Purpose |
|-----------|---------|---------|
| `org.xerial:sqlite-jdbc` | 3.45+ | SQLite JDBC driver |
| `info.picocli:picocli` | 4.7+ | CLI framework |
| `org.junit.jupiter:junit-jupiter` | 5.10+ | Unit testing |
| `org.mockito:mockito-core` | 5.8+ | Mocking |
| `org.jacoco:jacoco-maven-plugin` | 0.8.11+ | Code coverage |

---

## Architecture

See [architecture.md](architecture.md) for full sequence, class, ER, and data flow diagrams.
