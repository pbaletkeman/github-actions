# Quiz Engine — Dart — Full Documentation

- [Quiz Engine — Dart — Full Documentation](#quiz-engine--dart--full-documentation)
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
    - [Global Options](#global-options)
  - [Docker Setup](#docker-setup)
    - [Building](#building)
    - [Running Interactively](#running-interactively)
    - [Docker Compose Services](#docker-compose-services)
    - [Multi-Architecture Build](#multi-architecture-build)
  - [Question File Format](#question-file-format)
  - [Configuration](#configuration)
  - [Testing](#testing)
  - [Building a Native Executable](#building-a-native-executable)
  - [Dependencies](#dependencies)
  - [Architecture](#architecture)
    - [Design Decisions](#design-decisions)

---

> Part of the [Quiz Engine multi-language collection](../../README.md)

A command-line quiz engine for GH-200 GitHub Actions certification preparation built with Dart 3, SQLite, and the `args` package. Compiles to a single native binary with no runtime dependency.

---

## Overview

### Features

- **Interactive CLI quiz** with shuffled answer options
- **SQLite persistence** — questions, sessions, and responses stored locally
- **Non-repetition cycle tracking** — questions cycle through before repeating
- **Markdown import** — load questions from `.md` files
- **Session history** — review past results with JSON/CSV export
- **Native executable** — compile to a single binary with no runtime dependency
- **≥90% test coverage** enforced by CI script

---

## Project Structure

```
quiz-engine-dart/
├── lib/
│   ├── main.dart                      # CLI entry point
│   └── src/
│       ├── models/
│       │   ├── question.dart          # Question model
│       │   ├── quiz_session.dart      # QuizSession model
│       │   └── quiz_response.dart     # QuizResponse model
│       ├── database/
│       │   └── database.dart          # SQLite AppDatabase (schema + DAOs)
│       ├── service/
│       │   ├── quiz_engine.dart       # Session orchestration
│       │   ├── quiz_service.dart      # Business logic
│       │   ├── answer_shuffler.dart   # Answer randomization
│       │   ├── markdown_parser.dart   # Markdown file import parser
│       │   ├── history_service.dart   # History queries
│       │   └── import_service.dart    # Bulk import
│       ├── cli/
│       │   ├── formatter.dart         # Terminal output helpers
│       │   ├── prompts.dart           # Interactive stdin prompts
│       │   └── commands/
│       │       ├── quiz_command.dart
│       │       ├── import_command.dart
│       │       ├── history_command.dart
│       │       └── clear_command.dart
│       └── exceptions/
│           └── quiz_exceptions.dart   # Custom exception classes
├── test/
│   ├── helpers.dart                   # Shared test fixtures
│   └── src/
│       ├── database/
│       │   └── database_test.dart
│       ├── service/
│       │   ├── quiz_engine_test.dart
│       │   ├── answer_shuffler_test.dart
│       │   └── markdown_parser_test.dart
│       └── models/
│           └── models_test.dart
├── scripts/
│   └── check_coverage.sh             # Enforces ≥90% coverage threshold
├── bin/                               # Output directory for compiled binary
├── Dockerfile                         # Multi-stage build
├── docker-compose.yml
├── pubspec.yaml                       # Package manifest
├── analysis_options.yaml              # Dart linter rules
└── README.md
```

---

## Prerequisites

| Tool | Version | Download |
|------|---------|----------|
| Dart SDK | 3.0+ | https://dart.dev/get-dart |
| Docker (optional) | 20.10+ | https://docs.docker.com/get-docker/ |

Verify:
```bash
dart --version
```

---

## Installation

```bash
# Install package dependencies
dart pub get

# Verify
dart run lib/main.dart --help
```

---

## Script Reference

### Build Scripts

Dart does not require explicit compilation to run in development mode, but does need a C compiler for native sqlite3 bindings on some platforms.

#### `build.bat` (Windows CMD)

```bat
build.bat
```

- Runs `dart pub get` to install dependencies
- Compiles the native executable: `dart compile exe lib/main.dart -o bin/quiz_engine.exe`
- Creates the `bin/` directory if needed

#### `build.ps1` (PowerShell)

```powershell
.\build.ps1
```

- Uses `$ErrorActionPreference = "Stop"` for fail-fast behavior
- Colorized output (cyan, yellow, green)
- Compiles to `bin/quiz_engine.exe` on Windows

#### `build.sh` (Bash / macOS / Linux)

```bash
./build.sh
```

- Uses `set -e` for fail-fast behavior
- Compiles to `bin/quiz_engine`
- Requires `libsqlite3-dev` on Debian/Ubuntu

---

### Quiz Scripts

#### `quiz.bat` / `quiz.ps1` / `quiz.sh`

```bash
quiz.bat 10        # Windows CMD — 10 questions
.\quiz.ps1 -Q 10   # PowerShell  — 10 questions
./quiz.sh 10       # Bash        — 10 questions
```

These scripts:
1. Check that the compiled binary exists in `bin/`
2. Run the quiz with the specified `--questions` count (default: 10)
3. Exit with an error if the binary is not found (run build script first)

---

### Import Scripts

#### `import.bat` / `import.ps1` / `import.sh`

```bash
import.bat questions.md            # single file
import.bat .\questions\            # directory
.\import.ps1 -Path questions.md    # PowerShell single file
./import.sh questions.md           # Bash single file
./import.sh ./questions/           # Bash directory
```

These scripts detect whether the argument is a file or directory and call:
- `./bin/quiz_engine import --file <path>` for single files
- `./bin/quiz_engine import --dir <path>` for directories

---

### History Scripts

#### `history.bat` / `history.ps1` / `history.sh`

```bash
history.bat
.\history.ps1
./history.sh
```

All call `./bin/quiz_engine history`, showing the most recent sessions.

---

## CLI Commands

### `import` — Load questions from Markdown

```bash
quiz_engine import --file questions.md
quiz_engine import --dir ./questions/
```

| Option | Description |
|--------|-------------|
| `--file` | Path to a single `.md` file |
| `--dir` | Path to a directory; imports all `.md` files |

---

### `quiz` — Take a quiz

```bash
quiz_engine quiz                    # 10 questions (default)
quiz_engine quiz --questions 20     # 20 questions
quiz_engine quiz --no-shuffle       # disable answer shuffling
```

| Option | Default | Description |
|--------|---------|-------------|
| `--questions` | 10 | Number of questions per session |
| `--no-shuffle` | false | Disable answer option shuffling |

---

### `history` — View past sessions

```bash
quiz_engine history                         # list all sessions
quiz_engine history --session-id <uuid>     # session detail
quiz_engine history --session-id <uuid> --review  # full answer review
quiz_engine history --export json           # export all sessions as JSON
quiz_engine history --export csv            # export all sessions as CSV
```

| Option | Description |
|--------|-------------|
| `--session-id` | UUID of the session to inspect |
| `--review` | Show full answer key for the session |
| `--export json\|csv` | Export all history to file |

---

### `clear` — Remove stored data

```bash
quiz_engine clear --questions --confirm
quiz_engine clear --history --confirm
quiz_engine clear --all --confirm
```

### Global Options

| Option | Default | Description |
|--------|---------|-------------|
| `--db` | `<exe_dir>/quiz_engine.db` | Path to SQLite database file |
| `--help` | — | Show help |
| `--version` | — | Show version |

---

## Docker Setup

### Building

The `Dockerfile` uses a **multi-stage build**:

1. **Builder stage** (`dart:3.0`) — fetches dependencies and compiles to native binary
2. **Runtime stage** (`debian:bullseye-slim`) — minimal image containing only the compiled binary and sqlite3 lib

```bash
# Build image
docker build -t quiz-engine-dart:latest .

# Verify
docker run --rm quiz-engine-dart:latest --help
```

### Running Interactively

```bash
# Run quiz with persistent data volume
docker run -it -v quiz-dart-data:/data quiz-engine-dart:latest quiz --questions 10

# Import a local file
docker run -it \
  -v quiz-dart-data:/data \
  -v "$(pwd)/questions.md:/tmp/questions.md" \
  quiz-engine-dart:latest import --file /tmp/questions.md
```

### Docker Compose Services

```bash
# Run the CLI
docker-compose up quiz-engine

# Run tests with coverage check
docker-compose up quiz-engine-test

# Build native executable
docker-compose up quiz-engine-build
```

| Service | Description |
|---------|-------------|
| `quiz-engine` | Interactive CLI |
| `quiz-engine-test` | Runs `dart test` with coverage and ≥90% check |
| `quiz-engine-build` | Compiles native binary |

### Multi-Architecture Build

```bash
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t myregistry/quiz-engine-dart:1.0.0 \
  --push .
```

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

The database path defaults to `<executable_directory>/quiz_engine.db`. Override with the `--db` flag:

```bash
./bin/quiz_engine --db /var/data/quiz.db quiz --questions 10
```

---

## Testing

```bash
# Run all tests
dart test

# Run with coverage collection
dart pub global activate coverage
dart test --coverage=coverage

# Convert to LCOV format
dart pub global run coverage:format_coverage \
  --lcov \
  --in=coverage \
  --out=coverage/lcov.info \
  --report-on=lib

# Enforce ≥90% threshold (used by CI)
bash scripts/check_coverage.sh

# Generate HTML report (requires lcov installed)
genhtml coverage/lcov.info --output-directory coverage/html
```

---

## Building a Native Executable

The compiled binary runs without the Dart SDK installed.

```bash
# Compile (output: bin/quiz_engine)
dart compile exe lib/main.dart -o bin/quiz_engine

# Windows
dart compile exe lib/main.dart -o bin/quiz_engine.exe
```

> **Note:** The `libsqlite3` native library must be present on the target system.
> Install with: `apt install libsqlite3-dev` (Debian/Ubuntu) or `brew install sqlite` (macOS).

---

## Dependencies

| Package | Purpose |
|---------|---------|
| `args` | CLI argument parsing |
| `sqlite3` | Direct SQLite access (no ORM) |
| `uuid` | UUID generation for session IDs |
| `path` | Cross-platform path utilities |
| `test` | Dart test framework |
| `coverage` | Code coverage collection |

---

## Architecture

See [architecture.md](architecture.md) for full sequence, class, ER, and data flow diagrams.

### Design Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Database | `sqlite3` package | Direct SQLite access, no code generation required |
| CLI | `args` package | Standard, well-maintained argument parsing |
| Testing | `test` package | Idiomatic Dart testing framework |
| Distribution | `dart compile exe` | Single native binary, no runtime needed |
| Non-repetition | Cycle columns | `usage_cycle` + `times_used` ensure questions exhaust before repeating |
