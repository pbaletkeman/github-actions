# Quiz Engine — Python — Full Documentation

> Part of the [Quiz Engine multi-language collection](../../README.md)

- [Quiz Engine — Python — Full Documentation](#quiz-engine--python--full-documentation)
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
    - [Global Options (Typer)](#global-options-typer)
  - [Docker Setup](#docker-setup)
    - [Building](#building)
    - [Running Interactively](#running-interactively)
    - [Environment Variables](#environment-variables)
    - [Docker Compose Services](#docker-compose-services)
  - [Question File Format](#question-file-format)
  - [Configuration](#configuration)
  - [Testing](#testing)
  - [Dependencies](#dependencies)
    - [`requirements.txt` — Runtime](#requirementstxt--runtime)
    - [`requirements-dev.txt` — Development](#requirements-devtxt--development)
  - [Architecture](#architecture)


---

A command-line quiz engine for GH-200 GitHub Actions certification preparation built with Python 3.8+, Typer, Rich, Pydantic, and SQLite. Uses a virtual environment for isolated dependencies.

---

## Overview

### Features

- **Interactive CLI quiz** with Rich terminal UI and shuffled answers
- **SQLite persistence** via Python's `sqlite3` standard library (no ORM)
- **Non-repetition cycle tracking** — questions cycle through before repeating
- **Markdown import** — load questions from `.md` files
- **Session history** — browse results with export support
- **Pydantic validation** — strict data models for all entities
- **Virtual environment** — isolated, reproducible dependency management

> **Important:** ALL command scripts automatically activate the virtual environment before running. Always use the provided scripts rather than calling `python` directly.

---

## Project Structure

```
quiz-engine-python/
├── quiz_engine/              # Main package
│   ├── __init__.py
│   ├── main.py               # Typer CLI entry point
│   ├── models/
│   │   ├── question.py       # Pydantic Question model
│   │   ├── session.py        # Pydantic QuizSession model
│   │   └── response.py       # Pydantic QuizResponse model
│   ├── database/
│   │   └── database.py       # SQLite schema + DAOs
│   ├── service/
│   │   ├── quiz_service.py
│   │   ├── import_service.py
│   │   └── history_service.py
│   └── util/
│       ├── markdown_parser.py
│       ├── answer_shuffler.py
│       └── formatter.py      # Rich output helpers
├── scripts/
│   ├── import_questions.py   # Standalone import helper
│   └── view_history.py       # Standalone history viewer
├── tests/
│   ├── conftest.py           # Fixtures + in-memory DB
│   ├── test_quiz_service.py
│   ├── test_import_service.py
│   ├── test_history_service.py
│   ├── test_markdown_parser.py
│   └── test_database.py
├── requirements.txt          # Runtime dependencies
├── requirements-dev.txt      # Development + test dependencies
├── Dockerfile
├── docker-compose.yml
├── build.bat / build.ps1 / build.sh     # Create venv + install deps
├── quiz.bat / quiz.ps1 / quiz.sh
├── import.bat / import.ps1 / import.sh
├── history.bat / history.ps1 / history.sh
└── README.md
```

---

## Prerequisites

| Tool | Version | Download |
|------|---------|----------|
| Python | 3.8+ | https://www.python.org/downloads/ |
| pip | Included | `python -m pip` |
| Docker (optional) | 20.10+ | https://docs.docker.com/get-docker/ |

Verify:
```bash
python --version   # 3.8+
python -m pip --version
```

---

## Installation

The build scripts create the virtual environment and install all dependencies:

```bash
# Windows CMD
build.bat

# PowerShell
.\build.ps1

# Bash (macOS / Linux)
./build.sh

# Or manually
python -m venv .venv
.venv\Scripts\activate       # Windows
source .venv/bin/activate    # Unix

pip install -r requirements.txt
pip install -r requirements-dev.txt
```

---

## Script Reference

> All scripts activate the virtual environment before running. Do **not** call `python main.py` directly — always use the provided scripts.

### Build Scripts

#### `build.bat` (Windows CMD)

```bat
build.bat
```

- Creates `.venv` via `python -m venv .venv`
- Installs `requirements.txt` and `requirements-dev.txt`
- Verifies Typer CLI is importable

#### `build.ps1` (PowerShell)

```powershell
.\build.ps1
```

- Sets `$ErrorActionPreference = "Stop"` for fail-fast behavior
- Creates `.venv` and installs both requirements files
- Activates venv within the script and verifies the install

#### `build.sh` (Bash / macOS / Linux)

```bash
./build.sh
```

- Creates `.venv/` with `python3 -m venv .venv`
- Installs `requirements.txt` and `requirements-dev.txt`
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
1. Activate `.venv`
2. Call `python -m quiz_engine.main quiz --questions <N>`
3. Default is 10 questions

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

All scripts:
1. Activate `.venv`
2. Detect file vs. directory
3. Call `python -m quiz_engine.main import --file <path>` or `--dir <path>`

---

### History Scripts

#### `history.bat` / `history.ps1` / `history.sh`

```bash
history.bat
.\history.ps1
./history.sh
```

All activate `.venv` and run `python -m quiz_engine.main history`.

---

## CLI Commands

### `import` — Load questions from Markdown

```bash
python -m quiz_engine.main import --file questions.md
python -m quiz_engine.main import --dir ./questions/
```

| Option | Description |
|--------|-------------|
| `--file` | Path to a single `.md` file |
| `--dir` | Path to a directory |

---

### `quiz` — Take a quiz

```bash
python -m quiz_engine.main quiz                      # 10 questions
python -m quiz_engine.main quiz --questions 20
python -m quiz_engine.main quiz --no-shuffle
```

| Option | Default | Description |
|--------|---------|-------------|
| `--questions` | 10 | Number of questions per session |
| `--no-shuffle` | false | Disable answer option shuffling |

---

### `history` — View past sessions

```bash
python -m quiz_engine.main history
python -m quiz_engine.main history --session-id <uuid>
python -m quiz_engine.main history --session-id <uuid> --review
python -m quiz_engine.main history --export json
python -m quiz_engine.main history --export csv
```

---

### `clear` — Remove stored data

```bash
python -m quiz_engine.main clear --questions --confirm
python -m quiz_engine.main clear --history --confirm
python -m quiz_engine.main clear --all --confirm
```

### Global Options (Typer)

| Option | Default | Description |
|--------|---------|-------------|
| `--db` | `./quiz_engine.db` | SQLite database path |
| `--help` | — | Show help |
| `--version` | — | Show version |

---

## Docker Setup

### Building

```bash
docker build -t quiz-engine-python:latest .
docker run --rm quiz-engine-python:latest --help
```

### Running Interactively

```bash
# Interactive quiz
docker run -it \
  -v quiz-python-data:/data \
  -e QUIZ_DB_PATH=/data/quiz.db \
  quiz-engine-python:latest quiz --questions 10

# Import local file
docker run -it \
  -v quiz-python-data:/data \
  -v "$(pwd)/questions.md:/tmp/questions.md" \
  quiz-engine-python:latest import --file /tmp/questions.md
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `QUIZ_DB_PATH` | `./quiz_engine.db` | SQLite database path |

### Docker Compose Services

```bash
docker-compose up quiz-engine       # Run CLI
docker-compose up quiz-engine-test  # Run pytest with coverage
docker-compose up quiz-engine-build # Install deps and lint
```

| Service | Description |
|---------|-------------|
| `quiz-engine` | Interactive CLI (`python:3.11-slim`) |
| `quiz-engine-test` | `pytest --cov` + coverage check |
| `quiz-engine-build` | `pip install` both requirements |

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
# Custom database path via flag
python -m quiz_engine.main --db /var/data/quiz.db quiz --questions 10

# Or via environment variable
export QUIZ_DB_PATH=/var/data/quiz.db
python -m quiz_engine.main quiz
```

---

## Testing

```bash
# Activate venv first
source .venv/bin/activate    # Unix
.venv\Scripts\activate       # Windows

# Run all tests
pytest

# Run with coverage
pytest --cov=quiz_engine --cov-report=term-missing

# Generate HTML report
pytest --cov=quiz_engine --cov-report=html

# Enforce coverage threshold (also done by CI)
pytest --cov=quiz_engine --cov-fail-under=90
```

---

## Dependencies

### `requirements.txt` — Runtime

| Package | Version | Purpose |
|---------|---------|---------|
| `typer` | 0.24.1 | CLI framework |
| `rich` | 14.3.3 | Terminal UI / colors / tables |
| `pydantic` | 2.12.5 | Data validation |
| `python-dateutil` | 2.8.2 | Date parsing utilities |

### `requirements-dev.txt` — Development

| Package | Purpose |
|---------|---------|
| `pytest` | Test framework |
| `pytest-cov` | Coverage collection |
| `pytest-mock` | Mocking support |
| `ruff` | Linting + formatting |
| `mypy` | Static type checking |

---

## Architecture

See [architecture.md](architecture.md) for full sequence, class, ER, and data flow diagrams.
