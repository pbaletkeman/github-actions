# Quiz Engine — C# / .NET 8 — Full Documentation

> Part of the [Quiz Engine multi-language collection](../../README.md)

- [Quiz Engine — C# / .NET 8 — Full Documentation](#quiz-engine--c--net-8--full-documentation)
  - [Overview](#overview)
    - [Features](#features)
  - [Project Structure](#project-structure)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Script Reference](#script-reference)
    - [Build Scripts](#build-scripts)
      - [`build.bat` (Windows CMD)](#buildbat-windows-cmd)
      - [`build.ps1` (PowerShell)](#buildps1-powershell)
      - [`build.sh` (Bash / macOS / Linux / WSL)](#buildsh-bash--macos--linux--wsl)
    - [Quiz Scripts](#quiz-scripts)
      - [`quiz.bat` (Windows CMD)](#quizbat-windows-cmd)
      - [`quiz.ps1` (PowerShell)](#quizps1-powershell)
      - [`quiz.sh` (Bash)](#quizsh-bash)
    - [Import Scripts](#import-scripts)
      - [`import.bat` (Windows CMD)](#importbat-windows-cmd)
      - [`import.ps1` (PowerShell)](#importps1-powershell)
      - [`import.sh` (Bash)](#importsh-bash)
    - [History Scripts](#history-scripts)
      - [`history.bat` / `history.ps1` / `history.sh`](#historybat--historyps1--historysh)
  - [CLI Commands](#cli-commands)
    - [`quiz` — Take a quiz](#quiz--take-a-quiz)
    - [`import` — Import questions from Markdown](#import--import-questions-from-markdown)
    - [`history` — View past sessions](#history--view-past-sessions)
    - [`clear` — Delete stored data](#clear--delete-stored-data)
  - [Question File Format](#question-file-format)
    - [Format 1 — Simple (recommended for new files)](#format-1--simple-recommended-for-new-files)
    - [Format 2 — Answer-Key (bulk exam files)](#format-2--answer-key-bulk-exam-files)
  - [Docker Setup](#docker-setup)
    - [Building the Image](#building-the-image)
    - [Running Interactively](#running-interactively)
    - [Environment Variables](#environment-variables)
    - [Docker Compose](#docker-compose)
  - [Configuration](#configuration)
    - [Database Path](#database-path)
  - [Testing](#testing)
  - [Dependencies](#dependencies)
  - [Architecture](#architecture)
    - [Repository Pattern](#repository-pattern)
    - [Cycle-Aware Question Selection](#cycle-aware-question-selection)
    - [Answer Shuffling](#answer-shuffling)
    - [Layer Dependency Graph](#layer-dependency-graph)
---

A command-line quiz engine for GH-200 GitHub Actions certification preparation built with .NET 8, Entity Framework Core, Spectre.Console, and System.CommandLine.

---

## Overview

### Features

- **Question Management** — Import questions from Markdown files (two supported formats)
- **Answer Shuffling** — Randomizes answer order per question to prevent memorization
- **Cycle-Aware Selection** — Never repeats a question until all have been seen
- **SQLite Persistence** — EF Core with automatic database creation
- **Rich CLI** — Formatted output with Spectre.Console tables, panels, and colors
- **History Tracking** — View past sessions with scores and per-question review
- **xUnit Tests** — 90%+ coverage enforced by Coverlet

---

## Project Structure

```
quiz-engine-csharp/
├── QuizEngine.sln                    # Visual Studio solution file
├── QuizEngine.Entities/              # Entity models (no dependencies)
│   ├── Question.cs
│   ├── QuizSession.cs
│   └── QuizResponse.cs
├── QuizEngine.Data/                  # EF Core data access layer
│   ├── QuizEngineDbContext.cs
│   ├── IQuestionRepository.cs
│   ├── QuestionRepository.cs
│   ├── ISessionRepository.cs
│   ├── SessionRepository.cs
│   ├── IResponseRepository.cs
│   └── ResponseRepository.cs
├── QuizEngine.Service/               # Business logic
│   ├── QuizService.cs
│   ├── HistoryService.cs
│   ├── ImportService.cs
│   ├── MarkdownParser.cs
│   └── AnswerShuffler.cs
├── QuizEngine.CLI/                   # CLI entry point
│   ├── Program.cs
│   ├── Prompts.cs
│   ├── Commands/
│   │   ├── QuizCommand.cs
│   │   ├── ImportCommand.cs
│   │   ├── HistoryCommand.cs
│   │   └── ClearCommand.cs
│   └── Formatters/
│       └── ConsoleFormatter.cs
├── QuizEngine.Tests/                 # xUnit tests (≥90% coverage required)
│   ├── DatabaseFixture.cs
│   ├── RepositoryTests.cs
│   ├── QuizEngineTests.cs
│   ├── AnswerShufflerTests.cs
│   ├── MarkdownParserTests.cs
│   └── ServiceTests.cs
├── Dockerfile                        # Multi-stage build (SDK → runtime)
├── docker-compose.yml                # Compose services (app, test, build)
├── build.bat / build.ps1 / build.sh  # Build scripts
├── quiz.bat / quiz.ps1 / quiz.sh     # Quiz runner scripts
├── import.bat / import.ps1 / import.sh # Import helper scripts
├── history.bat / history.ps1 / history.sh # History viewer scripts
└── quiz.db                           # SQLite database (auto-created)
```

---

## Prerequisites

| Tool | Version | Download |
|------|---------|----------|
| .NET SDK | 8.0+ | https://dotnet.microsoft.com/download/dotnet/8 |
| Docker (optional) | 20.10+ | https://docs.docker.com/get-docker/ |

Verify installations:
```bash
dotnet --version   # should be 8.x.x
docker --version   # optional
```

---

## Installation

```bash
# 1. Navigate to the project directory
cd quiz-engine-csharp

# 2. Restore packages
dotnet restore

# 3. Build the solution
dotnet build QuizEngine.sln
```

---

## Script Reference

### Build Scripts

These scripts build the entire .NET solution from the project root.

#### `build.bat` (Windows CMD)

```bat
build.bat
```

- Sets working directory to script location
- Runs `dotnet build QuizEngine.sln`
- Exits with error code on failure
- Use in CI pipelines or plain `cmd.exe` sessions

#### `build.ps1` (PowerShell)

```powershell
.\build.ps1
```

- Uses `$ErrorActionPreference = "Stop"` for fail-fast behavior
- Colorized output (cyan header, yellow progress, green success)
- Suitable for PowerShell 5.1+ and PowerShell Core 7+

#### `build.sh` (Bash / macOS / Linux / WSL)

```bash
./build.sh
```

- Uses `set -e` for fail-fast behavior
- Resolves script directory with `$(dirname "$0")`
- Compatible with bash 3.2+ (macOS default)

---

### Quiz Scripts

These scripts start an interactive quiz session.

#### `quiz.bat` (Windows CMD)

```bat
quiz.bat           # 10 questions (default)
quiz.bat 20        # 20 questions
```

Parameter: first argument `%~1` sets question count (default: 10).

#### `quiz.ps1` (PowerShell)

```powershell
.\quiz.ps1                  # 10 questions (default)
.\quiz.ps1 -Questions 20    # 20 questions
```

Named parameter `-Questions [int]` with default value of 10.

#### `quiz.sh` (Bash)

```bash
./quiz.sh         # 10 questions (default)
./quiz.sh 20      # 20 questions
```

Positional argument `$1` sets question count (`QUESTIONS=${1:-10}`).

---

### Import Scripts

These scripts import questions from Markdown files or directories.

#### `import.bat` (Windows CMD)

```bat
import.bat                    # imports from current directory
import.bat questions.md       # imports a single file
import.bat .\questions\       # imports all .md files in directory
```

Detects whether the argument is a file or directory using `if exist "%~1\"`.

#### `import.ps1` (PowerShell)

```powershell
.\import.ps1                          # imports from current directory
.\import.ps1 -Path questions.md       # single file
.\import.ps1 -Path .\questions\       # directory
```

Uses `Test-Path $Path -PathType Container` to distinguish files from directories.

#### `import.sh` (Bash)

```bash
./import.sh                    # imports from current directory
./import.sh questions.md       # single file
./import.sh ./questions/       # directory
```

Uses `[ -d "$1" ]` to detect directories.

---

### History Scripts

These scripts display quiz session history.

#### `history.bat` / `history.ps1` / `history.sh`

```bat
history.bat
.\history.ps1
./history.sh
```

All three call `dotnet run --project QuizEngine.CLI -- history` with no arguments, showing the most recent sessions sorted by date (descending).

---

## CLI Commands

### `quiz` — Take a quiz

```bash
dotnet run --project QuizEngine.CLI -- quiz
dotnet run --project QuizEngine.CLI -- quiz --questions 20
dotnet run --project QuizEngine.CLI -- quiz --difficulty easy
dotnet run --project QuizEngine.CLI -- quiz --section "GitHub Actions"
dotnet run --project QuizEngine.CLI -- quiz --no-explanation
```

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `--questions` | int | 10 | Number of questions per session |
| `--difficulty` | string | (all) | Filter by `easy`, `medium`, or `hard` |
| `--section` | string | (all) | Filter by topic section |
| `--no-explanation` | flag | false | Skip answer explanations at the end |
| `--no-shuffle` | flag | false | Keep answer options in original order (do not randomise) |

> **Note:** Explanations are shown after _all_ questions are answered, not after each one.

---

### `import` — Import questions from Markdown

```bash
dotnet run --project QuizEngine.CLI -- import --file questions.md
dotnet run --project QuizEngine.CLI -- import --dir ./questions/
```

| Option | Description |
|--------|-------------|
| `--file` | Path to a single Markdown file |
| `--dir` | Path to a directory; imports all `.md` files |

---

### `history` — View past sessions

```bash
dotnet run --project QuizEngine.CLI -- history
dotnet run --project QuizEngine.CLI -- history --count 5
dotnet run --project QuizEngine.CLI -- history --sort score --order desc
dotnet run --project QuizEngine.CLI -- history --sort questions --order asc
dotnet run --project QuizEngine.CLI -- history --session-id beca2997
dotnet run --project QuizEngine.CLI -- history --export json
dotnet run --project QuizEngine.CLI -- history --export csv
```

| Option | Values | Default | Description |
|--------|--------|---------|-------------|
| `--count` | int | 10 | Number of sessions to show |
| `--sort` | `date`, `score`, `questions`, `time` | `date` | Sort field |
| `--order` | `asc`, `desc` | `desc` | Sort direction |
| `--session-id` | UUID prefix | — | Show detail for a specific session |
| `--export` | `json`, `csv` | — | Export all sessions to a timestamped file |

Session IDs display the first 18 characters; prefix matching is supported.

---

### `clear` — Delete stored data

```bash
dotnet run --project QuizEngine.CLI -- clear --questions --confirm
dotnet run --project QuizEngine.CLI -- clear --history --confirm
dotnet run --project QuizEngine.CLI -- clear --all --confirm
```

| Option | Description |
|--------|-------------|
| `--questions` | Delete all imported questions |
| `--history` | Delete all session history |
| `--all` | Delete everything |
| `--confirm` | Required flag to prevent accidental deletion |

---

## Question File Format

The `MarkdownParser` supports two formats:

### Format 1 — Simple (recommended for new files)

```markdown
## Question 1

**Q: What does CI stand for?**

- A) Continuous Integration
- B) Code Integration
- C) Complete Infrastructure
- D) Cloud Infrastructure

**Answer: A**

**Explanation:** CI stands for Continuous Integration.

Section: GitHub Actions
Difficulty: easy
```

### Format 2 — Answer-Key (bulk exam files)

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

## Docker Setup

### Building the Image

The `Dockerfile` uses a **multi-stage build**:

1. **Builder stage** (`mcr.microsoft.com/dotnet/sdk:8.0`) — restores, builds, and publishes
2. **Runtime stage** (`mcr.microsoft.com/dotnet/runtime:8.0`) — minimal runtime image

```bash
# Build image
docker build -t quiz-engine:latest .

# Verify
docker run --rm quiz-engine:latest --help
```

### Running Interactively

The container requires `-it` flags for interactive CLI use.

```bash
# Run quiz with persistent data volume
docker run -it -v quiz-data:/data quiz-engine:latest quiz --questions 10

# Import a local file into the container
docker run -it -v quiz-data:/data -v "$(pwd)/questions.md:/tmp/questions.md" \
  quiz-engine:latest import --file /tmp/questions.md

# View history
docker run -it -v quiz-data:/data quiz-engine:latest history
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `QUIZ_DB_PATH` | `/data/quiz.db` | Path to SQLite database file |
| `DOTNET_ENVIRONMENT` | `Production` | Runtime environment |

The container runs as non-root user `dotnetuser` (UID 1000) for security.

### Docker Compose

`docker-compose.yml` defines three services:

```bash
# Run the CLI application
docker-compose up quiz-engine

# Run tests with coverage enforcement (≥90%)
docker-compose up quiz-engine-test

# Build only
docker-compose up quiz-engine-build
```

**Services:**

| Service | Description |
|---------|-------------|
| `quiz-engine` | Interactive CLI with `/data` volume for persistence |
| `quiz-engine-test` | Runs `dotnet test` with Coverlet coverage collection |
| `quiz-engine-build` | Builds in Release mode; exits when done |

---

## Configuration

### Database Path

By default, the database is created as `quiz.db` in the current directory. Override with the `QUIZ_DB_PATH` environment variable:

```bash
# Linux/macOS
QUIZ_DB_PATH=/var/data/quiz.db dotnet run --project QuizEngine.CLI -- quiz

# Windows CMD
set QUIZ_DB_PATH=C:\data\quiz.db && dotnet run --project QuizEngine.CLI -- quiz

# PowerShell
$env:QUIZ_DB_PATH="C:\data\quiz.db"; dotnet run --project QuizEngine.CLI -- quiz
```

---

## Testing

```bash
# Run all tests
dotnet test

# Run with verbose output
dotnet test --logger "console;verbosity=detailed"

# Run with coverage enforcement (≥90% lines required)
dotnet test /p:CollectCoverage=true \
            /p:CoverletOutputFormat=lcov \
            /p:CoverletOutput=./coverage/ \
            /p:Threshold=90 \
            /p:ThresholdType=line \
            /p:ExcludeByFile="**/Program.cs;**/Migrations/**"
```

Test projects and their coverage:

| Test File | Covers |
|-----------|--------|
| `RepositoryTests.cs` | EF Core repositories |
| `QuizEngineTests.cs` | Quiz session lifecycle |
| `AnswerShufflerTests.cs` | Answer randomization |
| `MarkdownParserTests.cs` | Both MD parse formats |
| `ServiceTests.cs` | Service layer integration |

---

## Dependencies

| Package | Version | Purpose |
|---------|---------|---------|
| `Microsoft.EntityFrameworkCore` | 8.0.0 | ORM |
| `Microsoft.EntityFrameworkCore.Sqlite` | 8.0.0 | SQLite EF provider |
| `Spectre.Console` | 0.49.1 | Rich terminal output |
| `System.CommandLine` | 2.0.0-beta4 | CLI argument parsing |
| `xUnit` | 2.9.3 | Test framework |
| `coverlet` | 6.0.2 | Code coverage |
| `Moq` | 4.20.70 | Mocking for tests |

---

## Architecture

See [architecture.md](architecture.md) for full sequence, class, ER, and data flow diagrams.

### Repository Pattern

Data access is abstracted behind interfaces (`IQuestionRepository`, `ISessionRepository`, `IResponseRepository`), enabling:
- Easy unit testing with mocks
- Swappable persistence (e.g., PostgreSQL instead of SQLite)

### Cycle-Aware Question Selection

The `UsageCycle` field on `Question` ensures no question is repeated until all others have been answered at least once. After a full cycle, all questions advance to the next cycle.

### Answer Shuffling

`AnswerShuffler` randomizes option order for each question and maintains a mapping from displayed letter → original letter to ensure correct scoring regardless of display order.

### Layer Dependency Graph

```
QuizEngine.CLI
     ↓
QuizEngine.Service
     ↓
QuizEngine.Data  ←→  QuizEngine.Entities
     ↓
   SQLite
```
