# Quiz Engine -- Go

> Part of the [Quiz Engine multi-language collection](../README.md)

- [Quiz Engine -- Go](#quiz-engine----go)
  - [Get Started in 5 Minutes](#get-started-in-5-minutes)
    - [Prerequisites](#prerequisites)
    - [1. Build](#1-build)
    - [2. Import Questions](#2-import-questions)
    - [3. Take a Quiz](#3-take-a-quiz)
    - [4. View History](#4-view-history)
    - [5. Docker](#5-docker)


A CLI quiz engine for GitHub Actions (GH-200) certification prep. Built with Go 1.21, Cobra, and SQLite.

## Get Started in 5 Minutes

### Prerequisites
- [Go 1.21+](https://go.dev/dl/) and a C compiler (GCC / MinGW / MSVC)

### 1. Build
```bash
build.bat          # Windows CMD
./build.sh         # Bash / macOS / Linux
# or directly
CGO_ENABLED=1 go build -o bin/quiz-engine .
```

### 2. Import Questions
```bash
bin\quiz-engine.exe import --file questions.md   # Windows
./bin/quiz-engine import --file questions.md     # Linux/macOS
```

### 3. Take a Quiz
```bash
quiz.bat 20           # Windows CMD
./quiz.sh 20          # Bash
./bin/quiz-engine quiz --questions 20
```

### 4. View History
```bash
./bin/quiz-engine history
```

### 5. Docker
```bash
docker build -t quiz-engine-go:latest .
docker-compose up quiz-engine
```

---

Full docs: [docs/README.md](docs/README.md) | Architecture: [docs/architecture.md](docs/architecture.md)
