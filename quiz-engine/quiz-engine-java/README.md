# Quiz Engine -- Java

> Part of the [Quiz Engine multi-language collection](../README.md)

- [Quiz Engine -- Java](#quiz-engine----java)
  - [Get Started in 5 Minutes](#get-started-in-5-minutes)
    - [Prerequisites](#prerequisites)
    - [1. Build](#1-build)
    - [2. Import Questions](#2-import-questions)
    - [3. Take a Quiz](#3-take-a-quiz)
    - [4. View History](#4-view-history)
    - [5. Docker](#5-docker)

A CLI quiz engine for GitHub Actions (GH-200) certification prep. Built with Java 17, Gradle, and SQLite.

## Get Started in 5 Minutes

### Prerequisites
- [JDK 17+](https://adoptium.net/) and Gradle (or use the Gradle wrapper)

### 1. Build
```bash
build.bat          # Windows CMD
.\build.ps1        # PowerShell
./build.sh         # Bash
# or directly
./gradlew build -x test
```

### 2. Import Questions
```bash
import.bat questions.md            # Windows CMD
.\import.ps1 -Path questions.md    # PowerShell
./import.sh questions.md           # Bash
# or directly
java -jar build/libs/quiz-engine.jar import questions.md
```

### 3. Take a Quiz
```bash
quiz.bat 10            # Windows CMD
.\quiz.ps1 -Q 10       # PowerShell
./quiz.sh 10           # Bash
# or directly
java -jar build/libs/quiz-engine.jar quiz
```

### 4. View History
```bash
java -jar build/libs/quiz-engine.jar history
```

### 5. Docker
```bash
docker-compose up quiz-engine
```

---

Full docs: [docs/README.md](docs/README.md) | Architecture: [docs/architecture.md](docs/architecture.md)
