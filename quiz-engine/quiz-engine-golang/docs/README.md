# Quiz Engine — Go — Full Documentation

> Part of the [Quiz Engine multi-language collection](../../README.md)

- [Quiz Engine — Go — Full Documentation](#quiz-engine--go--full-documentation)
  - [Overview](#overview)
    - [Features](#features)
  - [Project Structure](#project-structure)
  - [Prerequisites](#prerequisites)
    - [C Compiler Setup](#c-compiler-setup)
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
    - [Global Flags](#global-flags)
  - [Docker Setup](#docker-setup)
    - [Building](#building)
    - [Running Interactively](#running-interactively)
    - [Docker Compose Services](#docker-compose-services)
    - [Build Internals](#build-internals)
  - [Question File Format](#question-file-format)
  - [Configuration](#configuration)
  - [Testing](#testing)
  - [Build Notes — CGO Requirement](#build-notes--cgo-requirement)
  - [Dependencies](#dependencies)
  - [Architecture](#architecture)


---

A command-line quiz engine for GH-200 GitHub Actions certification preparation built with Go 1.21, Cobra CLI, and SQLite. Produces a single statically-linked binary.

---

## Overview

### Features

- **Interactive CLI quiz** with shuffled answer options
- **SQLite persistence** via `mattn/go-sqlite3` (CGO) or `modernc.org/sqlite` (pure Go)
- **Non-repetition cycle tracking** — questions cycle through before repeating
- **Markdown import** — load questions from `.md` files
- **Session history** — review past results
- **Colorized output** via `fatih/color` and `tablewriter`
- Single binary, cross-platform compilation

---

## Project Structure

```
quiz-engine-golang/
├── cmd/
│   ├── root.go          # Root Cobra command + global flags
│   ├── quiz.go          # `quiz` subcommand
│   ├── import.go        # `import` subcommand
│   ├── history.go       # `history` subcommand
│   └── clear.go         # `clear` subcommand
├── internal/
│   ├── models/
│   │   ├── question.go
│   │   ├── session.go
│   │   └── response.go
│   ├── database/
│   │   └── database.go  # SQLite schema + DAOs
│   ├── service/
│   │   ├── quiz_service.go
│   │   ├── import_service.go
│   │   └── history_service.go
│   └── util/
│       ├── parser.go    # Markdown parser
│       ├── shuffler.go  # Answer shuffler
│       └── formatter.go # Terminal output
├── test/                # Integration + unit tests
├── Dockerfile
├── docker-compose.yml
├── go.mod               # github.com/pbaletkeman/quiz-engine-golang
├── go.sum
├── build.bat            # Windows CMD build
├── build.sh             # Bash build (CGO_ENABLED=1)
├── quiz.bat/quiz.sh
├── import.bat/import.sh
├── history.bat/history.sh
└── README.md
```

---

## Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| Go | 1.21+ | https://go.dev/dl/ |
| C compiler | — | **Required for CGO/SQLite — see below** |
| Docker (optional) | 20.10+ | https://docs.docker.com/get-docker/ |

### C Compiler Setup

`mattn/go-sqlite3` uses CGO (C code compilation). A C compiler is **required**.

**Windows:**

Option A — Visual C++ Build Tools:
```
Install: https://visualstudio.microsoft.com/visual-cpp-build-tools/
Select: "Desktop development with C++" workload
Run builds from "x64 Native Tools Command Prompt"
```

Option B — MinGW-w64 (lighter):
```
Install: https://www.mingw-w64.org/
Add to PATH: C:\mingw64\bin
Compiler: gcc.exe
```

**macOS:**
```bash
xcode-select --install   # Install Xcode CLT
```

**Linux (Debian/Ubuntu):**
```bash
sudo apt install build-essential libsqlite3-dev
```

Verify:
```bash
gcc --version
go version
```

---

## Installation

```bash
# Install dependencies
go mod download

# Build with CGO enabled (required)
CGO_ENABLED=1 go build -o bin/quiz_engine .

# Verify
./bin/quiz_engine --help
```

---

## Script Reference

### Build Scripts

#### `build.bat` (Windows CMD)

```bat
build.bat
```

> **Warning:** `build.bat` may omit `CGO_ENABLED=1`. If you see a linking error about SQLite, use the PowerShell or Bash build script instead, or run builds from the Visual C++ Native Tools prompt with `CGO_ENABLED=1`.

#### `build.ps1` (PowerShell)

```powershell
.\build.ps1
```

- Sets `$env:CGO_ENABLED = "1"` before calling `go build`
- Colorized output with error handling
- Output: `bin\quiz_engine.exe`

#### `build.sh` (Bash / macOS / Linux)

```bash
./build.sh
```

- Sets `CGO_ENABLED=1 GOOS=linux go build`
- Uses `set -e` for fail-fast behavior
- Output: `bin/quiz_engine`

> On Linux the sqlite3 dev headers are needed: `apt install libsqlite3-dev`

---

### Quiz Scripts

#### `quiz.bat` / `quiz.ps1` / `quiz.sh`

```bash
quiz.bat 10           # Windows CMD — 10 questions
.\quiz.ps1 -N 10      # PowerShell  — 10 questions
./quiz.sh 10          # Bash        — 10 questions
```

These scripts:
1. Check that `bin/quiz_engine` (or `.exe`) exists
2. Run the quiz with the specified number of questions (default: 10)
3. Exit with a helpful error if the binary is missing (run build script first)

---

### Import Scripts

#### `import.bat` / `import.ps1` / `import.sh`

```bash
import.bat questions.md         # Windows CMD
import.bat .\questions\         # directory
.\import.ps1 -Path questions.md # PowerShell
./import.sh questions.md        # Bash single file
./import.sh ./questions/        # Bash directory
```

- Detects file vs. directory and calls the appropriate `--file` or `--dir` flag
- Prints import count on success

---

### History Scripts

#### `history.bat` / `history.ps1` / `history.sh`

```bash
history.bat
.\history.ps1
./history.sh
```

All shortcut `./bin/quiz_engine history`.

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
quiz_engine quiz                      # 10 questions (default)
quiz_engine quiz --questions 20
quiz_engine quiz --no-shuffle
```

| Option | Default | Description |
|--------|---------|-------------|
| `--questions` | 10 | Number of questions |
| `--no-shuffle` | false | Disable answer shuffling |

---

### `history` — View past sessions

```bash
quiz_engine history
quiz_engine history --session-id <id>
quiz_engine history --export json
```

| Option | Description |
|--------|-------------|
| `--session-id` | Filter to a specific session |
| `--export json\|csv` | Export session history |

---

### `clear` — Remove stored data

```bash
quiz_engine clear --questions --confirm
quiz_engine clear --history --confirm
quiz_engine clear --all --confirm
```

### Global Flags

| Flag | Default | Description |
|------|---------|-------------|
| `--db` | `./quiz_engine.db` | Path to SQLite database file |
| `--help` | — | Show help |
| `--version` | — | Show version |

---

## Docker Setup

The `Dockerfile` uses a **multi-stage build** that handles CGO inside the container, so no local C compiler is needed for Docker builds.

### Building

```bash
docker build -t quiz-engine-go:latest .
docker run --rm quiz-engine-go:latest --help
```

### Running Interactively

```bash
# Interactive quiz with persistent volume
docker run -it \
  -v quiz-go-data:/data \
  quiz-engine-go:latest quiz --questions 10

# Import a local file
docker run -it \
  -v quiz-go-data:/data \
  -v "$(pwd)/questions.md:/tmp/questions.md" \
  quiz-engine-go:latest import --file /tmp/questions.md
```

### Docker Compose Services

```bash
docker-compose up quiz-engine      # Run CLI
docker-compose up quiz-engine-test # Run tests
docker-compose up quiz-engine-build # Build binary
```

| Service | Description |
|---------|-------------|
| `quiz-engine` | Interactive CLI |
| `quiz-engine-test` | `go test ./... -cover` |
| `quiz-engine-build` | Compile binary with CGO |

### Build Internals

The Dockerfile sets `CGO_ENABLED=1 GOOS=linux` and uses a Go Alpine builder with `apk add gcc musl-dev sqlite-dev`. The final runtime image is `alpine:3.18` with `sqlite-libs`.

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
./bin/quiz_engine --db /var/data/quiz.db quiz --questions 10

# Environment variable (if supported by your build)
export QUIZ_DB_PATH=/var/data/quiz.db
./bin/quiz_engine quiz
```

---

## Testing

```bash
# Run all tests
go test ./...

# With coverage
go test ./... -coverprofile=coverage.out
go tool cover -html=coverage.out -o coverage.html

# Enforce ≥90% coverage threshold
./scripts/check_coverage.sh        # Linux/macOS
scripts\check_coverage.bat         # Windows

# Race detection
go test -race ./...

# Verbose
go test -v ./...
```

> **Coverage threshold:** The project enforces a minimum of **90%** total line coverage.
> Run `./scripts/check_coverage.sh` (or `scripts\check_coverage.bat` on Windows) before
> submitting — it exits with code 1 if coverage is below 90%.

---

## Build Notes — CGO Requirement

The project uses `mattn/go-sqlite3` which compiles a C extension. Cross-compilation requires matching C compilers:

| Target | Required | Notes |
|--------|---------|-------|
| `linux/amd64` | `x86_64-linux-musl-gcc` | Static musl build |
| `linux/arm64` | `aarch64-linux-musl-gcc` | For Raspberry Pi / ARM servers |
| `windows/amd64` | MinGW-w64 `x86_64-w64-mingw32-gcc` | |
| `darwin/arm64` | Xcode CLT | `xcode-select --install` |

Use `modernc.org/sqlite` (pure-Go, CGO-free) as a drop-in replacement if CGO is unavailable:

```bash
# Pure Go build (no C compiler needed)
CGO_ENABLED=0 go build -tags purego -o bin/quiz_engine .
```

---

## Dependencies

| Module | Version | Purpose |
|--------|---------|---------|
| `github.com/spf13/cobra` | v1.8+ | CLI framework |
| `github.com/mattn/go-sqlite3` | v1.14+ | SQLite (CGO) |
| `modernc.org/sqlite` | v1.27+ | SQLite (pure Go fallback) |
| `github.com/fatih/color` | v1.16+ | Colorized terminal output |
| `github.com/olekukonko/tablewriter` | v0.0.5+ | Table rendering |
| `github.com/stretchr/testify` | v1.9+ | Test assertions |

---

## Architecture

See [architecture.md](architecture.md) for full sequence, class, ER, and data flow diagrams.
