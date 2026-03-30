# Quiz Engine -- Rust

> Part of the [Quiz Engine multi-language collection](../README.md)

- [Quiz Engine -- Rust](#quiz-engine----rust)
  - [Get Started in 5 Minutes](#get-started-in-5-minutes)
    - [Prerequisites](#prerequisites)
    - [1. Build](#1-build)
    - [2. Import Questions](#2-import-questions)
    - [3. Take a Quiz](#3-take-a-quiz)
    - [4. View History](#4-view-history)
    - [5. Docker](#5-docker)


A high-performance CLI quiz engine for GitHub Actions (GH-200) certification prep. Built with Rust 1.70+, Clap, sqlx, and Tokio.

## Get Started in 5 Minutes

### Prerequisites
- [Rust 1.70+](https://www.rust-lang.org/tools/install) (includes cargo) and a C compiler

### 1. Build
```bash
build.bat          # Windows CMD
.\build.ps1        # PowerShell
./build.sh         # Bash / macOS / Linux
# or directly
cargo build --release
```

### 2. Import Questions
```bash
import.bat questions.md            # Windows CMD
.\import.ps1 -Path questions.md    # PowerShell
./import.sh questions.md           # Bash
# or directly
./target/release/quiz-engine import --file questions.md
```

### 3. Take a Quiz
```bash
quiz.bat 20            # Windows CMD
.\quiz.ps1 -Q 20       # PowerShell
./quiz.sh 20           # Bash
./target/release/quiz-engine quiz --questions 20
```

### 4. View History
```bash
./target/release/quiz-engine history
```

### 5. Docker
```bash
docker build -t quiz-engine-rust:latest .
docker-compose up quiz-engine
```

---

Full docs: [docs/README.md](docs/README.md) | Architecture: [docs/architecture.md](docs/architecture.md)
