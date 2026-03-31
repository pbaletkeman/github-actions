# Quiz Engine — Rust — Full Documentation

> Part of the [Quiz Engine multi-language collection](../../README.md)

- [Quiz Engine — Rust — Full Documentation](#quiz-engine--rust--full-documentation)
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
    - [Global Options (Clap)](#global-options-clap)
  - [Docker Setup](#docker-setup)
    - [Running Interactively](#running-interactively)
    - [Environment Variables](#environment-variables)
    - [Docker Compose Services](#docker-compose-services)
  - [Question File Format](#question-file-format)
  - [Configuration](#configuration)
  - [Testing \& Benchmarks](#testing--benchmarks)
    - [Integration Tests](#integration-tests)
  - [Build Notes — C Compiler Requirement](#build-notes--c-compiler-requirement)
  - [Dependencies](#dependencies)
  - [Architecture](#architecture)


---

A command-line quiz engine for GH-200 GitHub Actions certification preparation built with Rust 1.70+, Clap CLI, sqlx (async SQLite), Tokio, and Serde. Compiles to a single ~10 MB binary with no runtime dependencies.

---

## Overview

### Features

- **Interactive CLI quiz** with shuffled answers (Clap + custom prompts)
- **Async SQLite** via `sqlx` with Tokio runtime — non-blocking I/O
- **Non-repetition cycle tracking** — `usage_cycle` + `times_used` columns
- **Markdown import** — parse and load `.md` question files
- **Session history** — browse results with export support
- **Serde JSON** — structured serialization for export
- **Criterion benchmarks** — performance regression testing
- Single binary (~10 MB), no runtime dependencies

---

## Project Structure

```
quiz-engine-rust/
├── src/
│   ├── main.rs              # Entry point + Tokio runtime setup
│   ├── cli/
│   │   ├── mod.rs           # Clap CLI root
│   │   ├── quiz.rs          # quiz subcommand handler
│   │   ├── import.rs        # import subcommand handler
│   │   ├── history.rs       # history subcommand handler
│   │   └── clear.rs         # clear subcommand handler
│   ├── models/
│   │   ├── question.rs      # Question struct + Serde
│   │   ├── session.rs       # QuizSession struct
│   │   └── response.rs      # QuizResponse struct
│   ├── database/
│   │   └── db.rs            # sqlx pool, schema, DAOs
│   ├── service/
│   │   ├── quiz_service.rs
│   │   ├── import_service.rs
│   │   └── history_service.rs
│   └── util/
│       ├── parser.rs        # Markdown parser
│       ├── shuffler.rs      # Answer randomization
│       └── formatter.rs     # Terminal output
├── tests/
│   └── integration/         # Integration tests against in-memory SQLite
├── benches/
│   └── quiz_bench.rs        # Criterion benchmarks
├── Cargo.toml
├── Cargo.lock
├── Dockerfile
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
| Rust + Cargo | 1.70+ | https://rustup.rs/ |
| C compiler | — | **Required for sqlx/SQLite — see below** |
| Docker (optional) | 20.10+ | https://docs.docker.com/get-docker/ |

Verify:
```bash
rustc --version   # 1.70+
cargo --version
```

### C Compiler Setup

`sqlx` links against `libsqlite3` by default. A C compiler is required.

**Windows:**

Option A — Visual C++ Build Tools:
```
Install: https://visualstudio.microsoft.com/visual-cpp-build-tools/
Select: "Desktop development with C++" workload
Run builds from "x64 Native Tools Command Prompt"
```

Option B — MinGW-w64:
```
Install: https://www.mingw-w64.org/
Add bin directory to PATH
```

**macOS:**
```bash
xcode-select --install
```

**Linux (Debian/Ubuntu):**
```bash
sudo apt install build-essential libsqlite3-dev
```

Alternatively, use the bundled SQLite feature (`sqlx` can bundle SQLite via `SQLX_OFFLINE` + `bundled` feature) to remove the system dependency.

---

## Installation

```bash
# Build release binary
cargo build --release

# Output
./target/release/quiz_engine   # Unix
.\target\release\quiz_engine.exe # Windows

# Verify
./target/release/quiz_engine --help
```

---

## Script Reference

### Build Scripts

#### `build.bat` (Windows CMD)

```bat
build.bat
```

- Runs `cargo build --release`
- Copies binary to `bin/quiz_engine.exe` for convenience
- Checks for C compiler before building

#### `build.ps1` (PowerShell)

```powershell
.\build.ps1
```

- Sets `$ErrorActionPreference = "Stop"` for fail-fast behavior
- Runs `cargo build --release`
- Colorized progress output

#### `build.sh` (Bash / macOS / Linux)

```bash
./build.sh
```

- Runs `cargo build --release`
- Copies output to `bin/quiz_engine`
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
1. Check that `bin/quiz_engine` (or `.exe`) exists
2. Run `./bin/quiz_engine quiz --questions <N>`
3. Default to 10 questions

---

### Import Scripts

#### `import.bat` / `import.ps1` / `import.sh`

```bash
import.bat questions.md          # Windows CMD single file
import.bat .\questions\          # Windows CMD directory
.\import.ps1 -Path questions.md  # PowerShell single file
./import.sh questions.md         # Bash single file
./import.sh ./questions/         # Bash directory
```

- Detects file vs. directory
- Calls `./bin/quiz_engine import --file <path>` or `--dir <path>`

---

### History Scripts

#### `history.bat` / `history.ps1` / `history.sh`

```bash
history.bat
.\history.ps1
./history.sh
```

All invoke `./bin/quiz_engine history`.

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
| `--dir` | Path to a directory |

---

### `quiz` — Take a quiz

```bash
quiz_engine quiz                     # 10 questions (default)
quiz_engine quiz --questions 20
quiz_engine quiz --no-shuffle
```

| Option | Default | Description |
|--------|---------|-------------|
| `--questions` | 10 | Number of questions per session |
| `--no-shuffle` | false | Disable answer shuffling |

---

### `history` — View past sessions

```bash
quiz_engine history
quiz_engine history --session-id <uuid>
quiz_engine history --export json
quiz_engine history --export csv
```

---

### `clear` — Remove stored data

```bash
quiz_engine clear --questions --confirm
quiz_engine clear --history --confirm
quiz_engine clear --all --confirm
```

### Global Options (Clap)

| Option | Default | Description |
|--------|---------|-------------|
| `--db` | `./quiz_engine.db` | SQLite database path |
| `--help` | — | Show help |
| `--version` | — | Show version |

---

## Docker Setup

The `Dockerfile` uses a **multi-stage build**:
1. **Builder** (`rust:1.70-alpine`) — compiles release binary with musl-libc
2. **Runtime** (`alpine:3.18`) — minimal image, ~10 MB

```bash
docker build -t quiz-engine-rust:latest .
docker run --rm quiz-engine-rust:latest --help
```

### Running Interactively

```bash
docker run -it \
  -v quiz-rust-data:/data \
  -e QUIZ_DB_PATH=/data/quiz.db \
  quiz-engine-rust:latest quiz --questions 10

# Import local file
docker run -it \
  -v quiz-rust-data:/data \
  -v "$(pwd)/questions.md:/tmp/questions.md" \
  quiz-engine-rust:latest import --file /tmp/questions.md
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `QUIZ_DB_PATH` | `./quiz_engine.db` | SQLite database path |

### Docker Compose Services

```bash
docker-compose up quiz-engine       # Run CLI
docker-compose up quiz-engine-test  # cargo test
docker-compose up quiz-engine-build # cargo build --release
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

```bash
# Custom database path
./bin/quiz_engine --db /var/data/quiz.db quiz --questions 10

# Environment variable
export QUIZ_DB_PATH=/var/data/quiz.db
./bin/quiz_engine quiz
```

---

## Testing & Benchmarks

```bash
# Run all unit and integration tests
cargo test

# Run with output (no capture)
cargo test -- --nocapture

# Run benchmarks
cargo bench

# Coverage (requires cargo-llvm-cov)
cargo install cargo-llvm-cov
cargo llvm-cov --html

# Enforce ≥90% coverage threshold
./scripts/check_coverage.sh        # Linux/macOS
scripts\check_coverage.bat         # Windows

# Check code (fast, no link)
cargo check

# Lint
cargo clippy
```

> **Coverage threshold:** The project enforces a minimum of **90%** total line coverage.
> Run `./scripts/check_coverage.sh` (or `scripts\check_coverage.bat` on Windows) before
> submitting — it exits with code 1 if coverage is below 90%.
> Requires `cargo-llvm-cov` (primary) or `cargo-tarpaulin` (fallback).

### Integration Tests

Integration tests in `tests/integration/` spin up an in-memory SQLite database (`:memory:`) for each test, ensuring zero side-effects and full isolation.

---

## Build Notes — C Compiler Requirement

`sqlx` links against the system SQLite library by default. Cross-compilation targets:

| Target | Setup |
|--------|-------|
| `x86_64-unknown-linux-musl` | `apt install musl-tools` + `rustup target add x86_64-unknown-linux-musl` |
| `aarch64-unknown-linux-gnu` | `apt install gcc-aarch64-linux-gnu` |
| `x86_64-pc-windows-msvc` | Visual Studio Build Tools |
| `x86_64-apple-darwin` | Xcode CLT |

To avoid the C compiler requirement entirely, enable the `bundled` feature in `Cargo.toml`:
```toml
[dependencies]
sqlx = { version = "0.7", features = ["runtime-tokio-native-tls", "sqlite", "bundled"] }
```

---

## Dependencies

| Crate | Version | Purpose |
|-------|---------|---------|
| `sqlx` | 0.7+ | Async SQLite with compile-time checked queries |
| `tokio` | 1.x | Async runtime |
| `clap` | 4.x | CLI parsing with derive macros |
| `serde` | 1.x | Serialization/deserialization |
| `serde_json` | 1.x | JSON export |
| `uuid` | 1.x | UUID generation |
| `chrono` | 0.4+ | Date/time handling |
| `criterion` | 0.5+ | Benchmarking framework |
| `anyhow` | 1.x | Error handling |
| `regex` | 1.x | Markdown parsing |

---

## Architecture

See [architecture.md](architecture.md) for full sequence, class, ER, and data flow diagrams.
