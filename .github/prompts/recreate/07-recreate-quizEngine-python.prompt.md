# Prompt: Recreate quiz-engine-python from Scratch

> This file is a fully self-contained specification. A developer with no prior context can use it to recreate the `quiz-engine-python` project exactly as it exists.

---

## 1. Project Structure

```
quiz-engine-python/
│
├── quiz_engine/                  # Main Python package
│   ├── __init__.py               # Package init; exports __version__ = "1.0.0"
│   ├── main.py                   # argparse CLI entry point (python -m quiz_engine.main)
│   ├── models.py                 # Pydantic v2 data models: Question, QuizSession, QuizResponse
│   ├── database.py               # DatabaseManager class — all SQLite interactions, schema init, DAOs
│   ├── quiz.py                   # QuizEngine class — session lifecycle, answer checking, cycle tracking
│   ├── history.py                # HistoryManager class — session retrieval, CSV/JSON export
│   ├── cli.py                    # Rich terminal UI helpers (excluded from coverage via pragma)
│   └── utils.py                  # Pure utility functions: shuffle_answers, calculate_score,
│                                 #   format_time, parse_markdown_file, _parse_answer_key,
│                                 #   _extract_structured_question
│
├── scripts/                      # Standalone helper scripts (callable directly)
│   ├── __init__.py               # Empty package marker
│   ├── import_questions.py       # argparse wrapper: parse markdown + insert into DB
│   ├── view_history.py           # argparse wrapper: list/detail/export sessions
│   ├── clear_database.py         # argparse wrapper: delete all questions (requires --confirm)
│   └── clear_history.py          # argparse wrapper: delete sessions by id/all/age
│
├── tests/                        # pytest test suite
│   ├── __init__.py               # Empty package marker
│   ├── conftest.py               # Shared fixtures: db (in-memory), sample_questions, populated_db
│   ├── test_database.py          # 25+ tests covering all DatabaseManager methods
│   ├── test_models.py            # Pydantic validation tests for all three models
│   ├── test_quiz.py              # QuizEngine tests: load, submit, finalize, review
│   ├── test_history.py           # HistoryManager tests: list, detail, CSV/JSON export
│   ├── test_import.py            # End-to-end import pipeline tests
│   └── test_utils.py             # shuffle_answers, calculate_score, format_time,
│                                 #   parse_markdown_file (both formats)
│
├── docs/
│   ├── README.md                 # Full user-facing documentation (see Section 5)
│   └── architecture.md           # Mermaid diagrams: sequence, class, ER, data-flow
│
├── Dockerfile                    # python:3.11-slim image definition
├── docker-compose.yml            # Two services: quiz (interactive) and test (pytest)
├── pyproject.toml                # Build system, project metadata, pytest + coverage config
├── requirements.txt              # Runtime dependencies (pinned)
├── requirements-dev.txt          # Dev/test dependencies (pinned)
├── architecture.md               # Root-level architecture overview (Mermaid diagrams)
├── README.md                     # Brief top-level README
│
├── build.sh                      # Bash: create venv, install requirements.txt only
├── build.bat                     # Windows CMD: same
├── build.ps1                     # PowerShell: same
├── setup.sh                      # Bash: create .venv, install both req files, mkdir quiz_engine
├── setup.bat                     # Windows CMD: same
├── setup.ps1                     # PowerShell: same
├── quiz.sh                       # Bash: activate venv, run quiz (default 20 questions)
├── quiz.bat                      # Windows CMD: same
├── quiz.ps1                      # PowerShell: same (param $Questions = 20)
├── import.sh                     # Bash: activate venv, detect file vs dir, run import script
├── import.bat                    # Windows CMD: same
├── import.ps1                    # PowerShell: same (param $Path = "")
├── history.sh                    # Bash: activate venv, run view_history.py --summary
├── history.bat                   # Windows CMD: same
└── history.ps1                   # PowerShell: same
```

---

## 2. Language, Runtime, and Dependencies

### Runtime

| Setting        | Value                                 |
|----------------|---------------------------------------|
| Language       | Python                                |
| Minimum version | `>=3.9` (pyproject.toml)            |
| Docker image   | `python:3.11-slim`                    |
| Build backend  | `setuptools>=68.0` + `wheel`          |
| Package name   | `quiz-engine`                         |
| Version        | `1.0.0`                               |
| Description    | Python/SQLite Quiz Engine for GH-200 Certification |

### `requirements.txt` — Runtime Dependencies (pinned)

```
typer==0.24.1
rich==14.3.3
pydantic==2.12.5
python-dateutil==2.8.2
shellingham==1.5.4
```

| Package            | Purpose                              |
|--------------------|--------------------------------------|
| `typer==0.24.1`    | CLI framework (referenced in docs, not yet wired into main.py) |
| `rich==14.3.3`     | Terminal UI — colors, panels, tables |
| `pydantic==2.12.5` | Data validation for all models       |
| `python-dateutil==2.8.2` | Date parsing utilities          |
| `shellingham==1.5.4` | Shell detection (typer dependency) |

### `requirements-dev.txt` — Development Dependencies (pinned)

```
pytest==9.0.2
pytest-cov==7.1.0
coverage==7.13.5
```

### `pyproject.toml` — Full Contents

```toml
[build-system]
requires = ["setuptools>=68.0", "wheel"]
build-backend = "setuptools.build_meta"

[project]
name = "quiz-engine"
version = "1.0.0"
description = "Python/SQLite Quiz Engine for GH-200 Certification"
requires-python = ">=3.9"
dependencies = [
    "typer==0.24.1",
    "rich==14.3.3",
    "pydantic==2.12.5",
    "python-dateutil==2.8.2",
    "shellingham==1.5.4",
]

[tool.pytest.ini_options]
testpaths = ["tests"]
addopts = "--cov=quiz_engine --cov-report=html:coverage_html --cov-report=xml --cov-report=term-missing --cov-fail-under=90"

[tool.coverage.run]
branch = true
source = ["quiz_engine"]
omit = ["quiz_engine/cli.py", "quiz_engine/main.py"]

[tool.coverage.report]
fail_under = 90
show_missing = true
exclude_lines = [
    "pragma: no cover",
    "if __name__ == .__main__.:",
    "def __repr__",
]
```

---

## 3. Database Schema

The database is a single SQLite file (`quiz_engine/quiz.db` by default). Schema is created by `DatabaseManager.init_schema()` using `CREATE TABLE IF NOT EXISTS` statements. No ORM — raw `sqlite3` stdlib.

### Table: `questions`

```sql
CREATE TABLE IF NOT EXISTS questions (
    id               INTEGER  PRIMARY KEY AUTOINCREMENT,
    question_text    TEXT     NOT NULL,
    option_a         TEXT     NOT NULL,
    option_b         TEXT     NOT NULL,
    option_c         TEXT     NOT NULL,
    option_d         TEXT     NOT NULL,
    option_e         TEXT,                        -- optional 5th answer choice
    correct_answer   TEXT,                        -- single letter A–E; NULL when served for quiz display
    explanation      TEXT,
    section          TEXT,                        -- section/topic heading extracted from markdown
    difficulty       TEXT,                        -- e.g. "Easy", "Medium", "Hard"
    source_file      TEXT,                        -- basename of the .md file it was imported from
    usage_cycle      INTEGER  DEFAULT 1,          -- current cycle number (increments when all questions used)
    times_used       INTEGER  DEFAULT 0,          -- reset to 0 when cycle advances
    last_used_at     TEXT,                        -- ISO 8601 UTC timestamp of last use
    UNIQUE(question_text, option_a, option_b, option_c, option_d)  -- deduplication constraint
);

CREATE INDEX IF NOT EXISTS idx_questions_cycle      ON questions(usage_cycle);
CREATE INDEX IF NOT EXISTS idx_questions_difficulty ON questions(difficulty);
CREATE INDEX IF NOT EXISTS idx_questions_section    ON questions(section);
```

### Table: `quiz_sessions`

```sql
CREATE TABLE IF NOT EXISTS quiz_sessions (
    session_id          TEXT  PRIMARY KEY,        -- UUID string (str(uuid.uuid4()))
    started_at          TEXT,                     -- ISO 8601 UTC datetime
    ended_at            TEXT,                     -- ISO 8601 UTC datetime
    num_questions       INTEGER NOT NULL,
    num_correct         INTEGER DEFAULT 0,
    percentage_correct  REAL    DEFAULT 0.0,
    time_taken_seconds  INTEGER
);

CREATE INDEX IF NOT EXISTS idx_sessions_started ON quiz_sessions(started_at);
```

### Table: `quiz_responses`

```sql
CREATE TABLE IF NOT EXISTS quiz_responses (
    id                 INTEGER PRIMARY KEY AUTOINCREMENT,
    session_id         TEXT    NOT NULL,
    question_id        INTEGER NOT NULL,
    user_answer        TEXT    NOT NULL,          -- single letter A–E (uppercased)
    is_correct         INTEGER DEFAULT 0,         -- 0 or 1 (stored as integer, mapped to bool)
    time_taken_seconds INTEGER,
    FOREIGN KEY (session_id)  REFERENCES quiz_sessions(session_id),
    FOREIGN KEY (question_id) REFERENCES questions(id)
);

CREATE INDEX IF NOT EXISTS idx_responses_session ON quiz_responses(session_id);
```

### Relationships

```
quiz_sessions  1 ──< quiz_responses   (session_id FK)
questions      1 ──< quiz_responses   (question_id FK)
```

### Non-repetition Cycle Logic

- `get_current_cycle()` returns `MIN(usage_cycle)` across all questions.
- `get_random_questions()` only selects questions where `usage_cycle = current_cycle`.
- After a quiz session finalizes, `mark_question_used()` increments `times_used` and sets `last_used_at`.
- `advance_cycle_if_exhausted()` checks if any question at the current cycle still has `times_used = 0`. If none remain, it runs:
  ```sql
  UPDATE questions SET usage_cycle = usage_cycle + 1, times_used = 0
  WHERE usage_cycle = <current_cycle>
  ```

---

## 4. CLI Commands

The entry point is `python -m quiz_engine.main`. It uses `argparse` (not typer, despite typer being a dependency).

### Global Options

| Flag          | Default                  | Description                   |
|---------------|--------------------------|-------------------------------|
| `--db`        | `quiz_engine/quiz.db`    | Path to the SQLite database   |
| `--questions` | `20`                     | Number of questions per quiz  |
| `--difficulty`| _(none)_                 | Filter questions by difficulty|
| `--section`   | _(none)_                 | Filter questions by section   |

### Run a Quiz

```bash
python -m quiz_engine.main
python -m quiz_engine.main --questions 10
python -m quiz_engine.main --questions 20 --difficulty Easy
python -m quiz_engine.main --questions 15 --section "Workflow Trigger Events"
python -m quiz_engine.main --db /path/to/custom.db --questions 5
```

**Expected flow:**
1. Checks question count; exits with error if database is empty.
2. Loads N random questions (answers hidden).
3. For each question, displays question text and options A–D (or A–E) via Rich.
4. Prompts: `Your answer (A/B/C/D/E or Q to quit):` — loops until valid input.
5. Entering `Q` aborts the quiz with `Quiz aborted.`.
6. After all questions, displays an Answer Review table.
7. Saves session + responses to DB, marks questions used, advances cycle if exhausted.
8. Displays a Rich table with: Questions, Correct, Score %, Time (MM:SS).

**Example output:**

```
Question 1/10
╭──────────────────────────────────────────────────────╮
│ Which trigger event runs a workflow on a schedule?   │
╰──────────────────────────────────────────────────────╯
  A) on: timer
  B) on: cron
  C) on: schedule
  D) on: workflow_dispatch

Your answer (A/B/C/D/E or Q to quit): C
✓ Correct!

...

=== Answer Review ===
Q1: <question text>   Your answer: C  ✓ CORRECT
Q2: <question text>   Your answer: A  ✗ WRONG — Correct answer: B

             Quiz Results
┌────────────┬──────────┐
│ Metric     │ Value    │
├────────────┼──────────┤
│ Questions  │ 10       │
│ Correct    │ 7        │
│ Score      │ 70.0%    │
│ Time       │ 04:32    │
└────────────┴──────────┘
```

### Import Questions (`scripts/import_questions.py`)

```bash
python scripts/import_questions.py --file questions.md
python scripts/import_questions.py --dir ./questions/
python scripts/import_questions.py --file questions.md --db quiz_engine/quiz.db
```

| Flag     | Required | Default                | Description                        |
|----------|----------|------------------------|------------------------------------|
| `--file` | one of   | —                      | Path to a single `.md` file        |
| `--dir`  | one of   | —                      | Directory containing `*.md` files  |
| `--db`   | no       | `quiz_engine/quiz.db`  | Database path                      |

**Expected output:**
```
Importing ./questions/gh-200-iteration-1.md...
  Imported: 42, Skipped: 0, Errors: 0

Total: Imported=42, Skipped=0, Errors=0
```

### View History (`scripts/view_history.py`)

```bash
python scripts/view_history.py
python scripts/view_history.py --summary
python scripts/view_history.py --session-id <uuid>
python scripts/view_history.py --session-id <uuid> --review
python scripts/view_history.py --export csv --output history.csv
python scripts/view_history.py --export json --output history.json --include-answers
```

| Flag              | Description                                          |
|-------------------|------------------------------------------------------|
| `--db`            | Database path (default: `quiz_engine/quiz.db`)       |
| `--session-id`    | Show details for a specific session UUID             |
| `--summary`       | Print one summary line per session                   |
| `--review`        | With `--session-id`: print each response             |
| `--export`        | `csv` or `json`                                      |
| `--output`        | Output file path (defaults: `history.csv`/`.json`)   |
| `--include-answers` | Include response details in JSON export            |

**Summary line format:**
```
Session <uuid> | 2024-01-01 10:00 | 15/20 correct | 75.0% | 1800s
```

### Clear Database (`scripts/clear_database.py`)

```bash
python scripts/clear_database.py --confirm
python scripts/clear_database.py --confirm --db custom.db
```

| Flag       | Required | Description                        |
|------------|----------|------------------------------------|
| `--confirm`| **yes**  | Must be present or script exits    |
| `--db`     | no       | Database path                      |

**Output:** `Deleted 42 questions from database.`

### Clear History (`scripts/clear_history.py`)

```bash
python scripts/clear_history.py --all --confirm
python scripts/clear_history.py --session-id <uuid> --confirm
python scripts/clear_history.py --before 30 --confirm
```

| Flag           | Description                                      |
|----------------|--------------------------------------------------|
| `--confirm`    | **Required** — safety guard                      |
| `--session-id` | Delete a specific session (+ its responses)      |
| `--all`        | Delete every session and response                |
| `--before N`   | Delete sessions older than N days (UTC cutoff)   |
| `--db`         | Database path                                    |

---

## 5. Documentation

### `docs/README.md` — Full Structure

**Heading hierarchy:**

```
# Quiz Engine — Python — Full Documentation
  ## Overview
    ### Features
  ## Project Structure
  ## Prerequisites
  ## Installation
  ## Script Reference
    ### Build Scripts
      #### build.bat (Windows CMD)
      #### build.ps1 (PowerShell)
      #### build.sh (Bash / macOS / Linux)
    ### Quiz Scripts
      #### quiz.bat / quiz.ps1 / quiz.sh
    ### Import Scripts
      #### import.bat / import.ps1 / import.sh
    ### History Scripts
      #### history.bat / history.ps1 / history.sh
  ## CLI Commands
    ### import — Load questions from Markdown
    ### quiz — Take a quiz
    ### history — View past sessions
    ### clear — Remove stored data
    ### Global Options (Typer)
  ## Docker Setup
    ### Building
    ### Running Interactively
    ### Environment Variables
    ### Docker Compose Services
  ## Question File Format
  ## Configuration
  ## Testing
  ## Dependencies
    ### requirements.txt — Runtime
    ### requirements-dev.txt — Development
  ## Architecture
```

**Key tables in docs/README.md:**

Prerequisites table:
| Tool | Version | Download |
|------|---------|----------|
| Python | 3.8+ | https://www.python.org/downloads/ |
| pip | Included | `python -m pip` |
| Docker (optional) | 20.10+ | https://docs.docker.com/get-docker/ |

Dependencies — runtime:
| Package | Version | Purpose |
|---------|---------|---------|
| `typer` | 0.24.1 | CLI framework |
| `rich` | 14.3.3 | Terminal UI / colors / tables |
| `pydantic` | 2.12.5 | Data validation |
| `python-dateutil` | 2.8.2 | Date parsing utilities |

Dependencies — dev:
| Package | Purpose |
|---------|---------|
| `pytest` | Test framework |
| `pytest-cov` | Coverage collection |
| `pytest-mock` | Mocking support |
| `ruff` | Linting + formatting |
| `mypy` | Static type checking |

Docker Compose services table:
| Service | Description |
|---------|-------------|
| `quiz` | Interactive CLI (`python:3.11-slim`) |
| `test` | `pytest -v` with volume mount |

**Feature list (from docs/README.md):**
- Interactive CLI quiz with Rich terminal UI and shuffled answers
- SQLite persistence via Python's `sqlite3` standard library (no ORM)
- Non-repetition cycle tracking — questions cycle through before repeating
- Markdown import — load questions from `.md` files
- Session history — browse results with export support
- Pydantic validation — strict data models for all entities
- Virtual environment — isolated, reproducible dependency management

### `docs/architecture.md` — Diagrams Included

Contains four Mermaid diagrams:
1. **Sequence Diagram** — quiz command flow (User → CLI → QuizEngine → DatabaseManager → SQLite)
2. **Class Diagram** — all core classes with attributes and relationships
3. **Entity Relationship Diagram** — three tables with FK relationships
4. **Data Flow Diagram** — import and quiz flows as a flowchart

---

## 6. Question File Formats

The parser (`utils.parse_markdown_file`) supports **two formats**. Both can coexist in the same file.

---

### Format 1: Simple (inline `**Answer: X**`)

Headers: `## Question N` or `## Q<N>` (2–3 `#` signs).

Question text is extracted as plain text between the header and the first option line. Inline `> blockquote` text is also recognized as the question body.

**Answer** is given inline as `**Answer: X**`. Optional explanation text follows on the next line(s).

**Sample 1 — simple text body:**

```markdown
## Question 1
What is GitHub Actions used for?

- A) Automating CI/CD workflows
- B) Managing database schemas
- C) Creating user interfaces
- D) Building mobile apps

**Answer: A**

GitHub Actions automates software development workflows.
```

- `question_text` → `"What is GitHub Actions used for?"`
- `option_a` → `"Automating CI/CD workflows"`
- `option_b` → `"Managing database schemas"`
- `option_c` → `"Creating user interfaces"`
- `option_d` → `"Building mobile apps"`
- `correct_answer` → `"A"`
- `explanation` → `"GitHub Actions automates software development workflows."`

**Sample 2 — five options:**

```markdown
## Question 1
Which of the following are valid?

- A) Option one
- B) Option two
- C) Option three
- D) Option four
- E) All of the above

**Answer: E**
```

- `option_e` → `"All of the above"`
- `correct_answer` → `"E"`

---

### Format 2: Answer-Key Table (GH-200 iteration format)

Headers: `### Question N — Section Name` (3 `#` signs, section name after `—`).

Metadata is given as bold key-value pairs. The `**Answer Type**: many` marker causes a question to be **skipped** (only single-answer `one` type questions are imported).

The answer key is a Markdown table under `## Answer Key`. The parser strips this section before scanning for question blocks, so it never misidentifies key rows as questions.

**Answer Key table format:**
```markdown
## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1  | C         | Explanation text here. | source.md | Easy |
```

Only rows with a single letter in the Answer(s) column are imported. Multi-answer rows (`A, B, D`) are skipped.

**Sample 1 — structured question block (Scenario + Question):**

```markdown
### Question 1 — VS Code Extension

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Extension capabilities

**Question**:
What is the primary purpose of the VS Code extension?

- A) Execute workflow runs locally
- B) Provide YAML schema validation and IntelliSense
- C) Deploy files directly to GitHub
- D) Manage secrets from within the IDE

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1  | B         | The extension provides YAML schema validation and IntelliSense. | file.md | Easy |
```

- `question_text` → `"What is the primary purpose of the VS Code extension?"`
- `section` → `"VS Code Extension"`
- `difficulty` → `"Easy"`
- `correct_answer` → `"B"` (from Answer Key table)
- `explanation` → `"The extension provides YAML schema validation and IntelliSense."`

**Sample 2 — scenario + question combined:**

```markdown
### Question 2 — Context

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Scenario use

**Scenario**:
A developer wants to catch syntax errors before committing.

**Question**:
Which capability directly addresses this need without a workflow run?

- A) Run workflows locally using act
- B) Real-time YAML syntax validation with inline error highlighting
- C) Submit the file to a remote linter API
- D) Dry-run all run steps using a local shell

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 2  | B         | Real-time validation highlights errors without running the workflow. | file.md | Medium |
```

- `question_text` → `"A developer wants to catch syntax errors before committing.\n\nWhich capability directly addresses this need without a workflow run?"` (scenario + question concatenated)
- `section` → `"Context"`
- `difficulty` → `"Medium"`
- `correct_answer` → `"B"`

**Skipped question example (Answer Type: many):**

```markdown
### Question 3 — Context

**Difficulty**: Hard
**Answer Type**: many

**Question**:
Which of the following apply?

- A) Option one
- B) Option two
- C) Option three
- D) Option four
```

This question is silently skipped because `Answer Type` is `many` (not `one`).

---

## 7. Unit Test Coverage

### Enforced Threshold

**90%** branch and line coverage required.

### Configuration Location

**File:** `pyproject.toml`

**Properties:**

```toml
[tool.pytest.ini_options]
addopts = "--cov=quiz_engine --cov-report=html:coverage_html --cov-report=xml --cov-report=term-missing --cov-fail-under=90"

[tool.coverage.run]
branch = true
source = ["quiz_engine"]
omit = ["quiz_engine/cli.py", "quiz_engine/main.py"]

[tool.coverage.report]
fail_under = 90
show_missing = true
exclude_lines = [
    "pragma: no cover",
    "if __name__ == .__main__.:",
    "def __repr__",
]
```

### Tool

**`pytest-cov==7.1.0`** (backed by `coverage==7.13.5`)

- Coverage source: `quiz_engine` package
- **Excluded from coverage:** `quiz_engine/cli.py` (all functions decorated `# pragma: no cover`; Rich terminal UI) and `quiz_engine/main.py` (CLI entry point)
- Reports generated: HTML (`coverage_html/`), XML, terminal with missing lines
- Tests fail if coverage drops below 90%

### Running Tests

```bash
# Activate venv first
source venv/bin/activate     # Unix
venv\Scripts\activate        # Windows

# Run all tests (coverage enforced by addopts)
pytest

# Explicit coverage flags
pytest --cov=quiz_engine --cov-report=term-missing --cov-fail-under=90
```

### Test Files and What They Cover

| File                | Primary Class/Module Tested  | Key Scenarios                                             |
|---------------------|------------------------------|-----------------------------------------------------------|
| `test_database.py`  | `DatabaseManager`            | Schema init, CRUD for all 3 tables, cycle advance, filters |
| `test_models.py`    | `Question`, `QuizSession`, `QuizResponse` | Pydantic validators, defaults, invalid input |
| `test_quiz.py`      | `QuizEngine`                 | Load questions, submit answer, finalize, cycle advance, review |
| `test_history.py`   | `HistoryManager`             | List sessions, detail, summary format, CSV/JSON export    |
| `test_import.py`    | `parse_markdown_file` + DB   | Import pipeline, duplicate detection, option extraction    |
| `test_utils.py`     | `shuffle_answers`, `calculate_score`, `format_time`, `parse_markdown_file` | Both question formats, edge cases |

### Shared Fixtures (`conftest.py`)

```python
@pytest.fixture
def db(tmp_path):
    # In-memory SQLite at tmp_path/test.db; schema initialized; closed after test
    ...

@pytest.fixture
def sample_questions():
    # Returns List[Question] with 4 questions (answers A/B/C/D)
    ...

@pytest.fixture
def populated_db(db, sample_questions):
    # db with all 4 sample_questions inserted
    ...
```

---

## 8. Scripts

All platform-specific scripts (`.sh`, `.bat`, `.ps1`) live at the **project root**. Each group has three variants for different OS/shell environments.

### Invocation Convention

> All scripts must be run **from the project root** (`quiz-engine-python/`).
> They each `cd` to their own directory first (`cd "$(dirname "$0")"` / `Set-Location $PSScriptRoot`).

---

### Build Scripts — Create venv, install runtime deps

| Script      | Invoke from project root | Shell        |
|-------------|--------------------------|--------------|
| `build.sh`  | `./build.sh`             | Bash/macOS/Linux |
| `build.bat` | `build.bat`              | Windows CMD  |
| `build.ps1` | `.\build.ps1`            | PowerShell   |

**What they do:**
1. Create `venv/` via `python3 -m venv venv` (Bash) or `python -m venv venv` (Windows)
2. Activate the venv
3. Install `requirements.txt`
4. Print success message

> **Note:** `build.*` installs runtime dependencies only. For dev dependencies, use `setup.*`.

**`build.sh` full content:**
```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Python - Build ==="
echo "Creating virtual environment..."
python3 -m venv venv
echo "Activating virtual environment..."
source venv/bin/activate
echo "Installing dependencies..."
pip install -r requirements.txt
echo "Build successful! Virtual environment ready in venv/"
echo "To activate manually: source venv/bin/activate"
```

**`build.bat` full content:**
```bat
@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Python - Build ===
echo Creating virtual environment...
python -m venv venv
if %ERRORLEVEL% NEQ 0 (
    echo Failed to create virtual environment. Ensure Python is installed.
    exit /b %ERRORLEVEL%
)
echo Activating virtual environment...
call venv\Scripts\activate.bat
echo Installing dependencies...
pip install -r requirements.txt
if %ERRORLEVEL% NEQ 0 (
    echo Failed to install dependencies!
    exit /b %ERRORLEVEL%
)
echo Build successful! Virtual environment ready in venv\
echo To activate manually: venv\Scripts\activate
```

**`build.ps1` full content:**
```powershell
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Python - Build ===" -ForegroundColor Cyan
Write-Host "Creating virtual environment..." -ForegroundColor Yellow
python -m venv venv
Write-Host "Activating virtual environment..." -ForegroundColor Yellow
& venv\Scripts\Activate.ps1
Write-Host "Installing dependencies..." -ForegroundColor Yellow
pip install -r requirements.txt
Write-Host "Build successful! Virtual environment ready in venv\" -ForegroundColor Green
Write-Host "To activate manually: venv\Scripts\Activate.ps1" -ForegroundColor Cyan
```

---

### Setup Scripts — Create .venv, install runtime + dev deps

| Script      | Invoke from project root | Shell        |
|-------------|--------------------------|--------------|
| `setup.sh`  | `./setup.sh`             | Bash/macOS/Linux |
| `setup.bat` | `setup.bat`              | Windows CMD  |
| `setup.ps1` | `.\setup.ps1`            | PowerShell   |

**What they do:** Create `.venv/`, install both `requirements.txt` and `requirements-dev.txt`, create `quiz_engine/` directory.

**`setup.sh` full content:**
```bash
#!/bin/bash
set -e
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
pip install -r requirements-dev.txt
mkdir -p quiz_engine
echo "Setup complete. Run: source .venv/bin/activate"
```

---

### Quiz Scripts — Run an interactive quiz

| Script     | Invoke from project root         | Default questions |
|------------|----------------------------------|-------------------|
| `quiz.sh`  | `./quiz.sh [N]`                  | 20                |
| `quiz.bat` | `quiz.bat [N]`                   | 20                |
| `quiz.ps1` | `.\quiz.ps1 [-Questions N]`      | 20                |

**What they do:**
1. Check `venv/` exists (error if not)
2. Activate venv
3. Run `python -m quiz_engine.main --questions <N>`

**`quiz.sh` full content:**
```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Python - Start Quiz ==="
if [ ! -f "venv/bin/activate" ]; then
    echo "Virtual environment not found. Run build.sh first."
    exit 1
fi
source venv/bin/activate
QUESTIONS=${1:-20}
echo "Starting quiz with $QUESTIONS questions..."
python -m quiz_engine.main --questions $QUESTIONS
```

---

### Import Scripts — Import questions from Markdown

| Script       | Invoke from project root                   |
|--------------|--------------------------------------------|
| `import.sh`  | `./import.sh <file_or_directory>`          |
| `import.bat` | `import.bat <file_or_directory>`           |
| `import.ps1` | `.\import.ps1 -Path <file_or_directory>`   |

**What they do:**
1. Check `venv/` exists (error if not)
2. Activate venv
3. Detect if argument is a file or directory
4. Run `python scripts/import_questions.py --file <path>` or `--dir <path>`

**`import.sh` full content:**
```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Python - Import Questions ==="
if [ ! -f "venv/bin/activate" ]; then
    echo "Virtual environment not found. Run build.sh first."
    exit 1
fi
source venv/bin/activate
if [ -z "$1" ]; then
    echo "No path specified. Usage: ./import.sh <file_or_directory>"
    exit 1
elif [ -d "$1" ]; then
    echo "Importing from directory: $1"
    python scripts/import_questions.py --dir "$1"
else
    echo "Importing from file: $1"
    python scripts/import_questions.py --file "$1"
fi
```

---

### History Scripts — View quiz history summary

| Script        | Invoke from project root |
|---------------|--------------------------|
| `history.sh`  | `./history.sh`           |
| `history.bat` | `history.bat`            |
| `history.ps1` | `.\history.ps1`          |

**What they do:**
1. Check `venv/` exists (error if not)
2. Activate venv
3. Run `python scripts/view_history.py --summary`

---

## 9. Docker Setup

### `Dockerfile` — Full Content

```dockerfile
FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt requirements-dev.txt ./
RUN pip install --no-cache-dir -r requirements.txt -r requirements-dev.txt
COPY . .
RUN mkdir -p quiz_engine
CMD ["python", "-m", "quiz_engine.main"]
```

**Notes:**
- Base image: `python:3.11-slim`
- Both runtime and dev dependencies are installed in the image
- `quiz_engine/` directory is created for the SQLite database file
- Default `CMD` runs the quiz engine directly

### `docker-compose.yml` — Full Content

```yaml
version: "3.9"
services:
  quiz:
    build: .
    volumes:
      - ./quiz_engine:/app/quiz_engine
    stdin_open: true
    tty: true
  test:
    build: .
    command: python -m pytest tests/ -v
    volumes:
      - .:/app
```

### Service Definitions

| Service | Purpose | Key Settings |
|---------|---------|--------------|
| `quiz`  | Interactive quiz CLI | `stdin_open: true`, `tty: true`; mounts `./quiz_engine:/app/quiz_engine` to persist the SQLite DB |
| `test`  | Run pytest suite | Overrides CMD with `python -m pytest tests/ -v`; mounts entire project dir to `/app` |

### Volume Mounts

| Service | Host Path         | Container Path       | Purpose                      |
|---------|-------------------|----------------------|------------------------------|
| `quiz`  | `./quiz_engine`   | `/app/quiz_engine`   | Persist SQLite database file |
| `test`  | `.` (project root)| `/app`               | Live project files for tests |

### Environment Variables

| Variable       | Default              | Description              |
|----------------|----------------------|--------------------------|
| `QUIZ_DB_PATH` | `quiz_engine/quiz.db`| SQLite database file path (referenced in docs; must be passed via `-e` at runtime) |

### Common Docker Commands

```bash
# Build the image
docker build -t quiz-engine-python:latest .

# Run interactive quiz (persisting DB to local volume)
docker run -it \
  -v quiz-python-data:/data \
  -e QUIZ_DB_PATH=/data/quiz.db \
  quiz-engine-python:latest

# Run tests
docker-compose up test

# Run interactive quiz via compose
docker-compose run --rm quiz

# Import a local markdown file
docker run -it \
  -v quiz-python-data:/data \
  -v "$(pwd)/questions.md:/app/questions.md" \
  quiz-engine-python:latest \
  python scripts/import_questions.py --file /app/questions.md --db /data/quiz.db
```

---

## 10. Architecture Decisions

### Raw SQL over ORM

The project deliberately uses Python's `sqlite3` standard library with raw SQL strings — no SQLAlchemy, no Peewee. This is a conscious decision to keep dependencies minimal and keep the schema fully visible. The `DatabaseManager` class is the only place SQL appears.

### Repository Pattern via `DatabaseManager`

`DatabaseManager` acts as the sole data-access layer. All business logic (`QuizEngine`, `HistoryManager`) receives a `DatabaseManager` instance via constructor injection. This makes unit testing straightforward — tests pass a `DatabaseManager` backed by a temporary file DB rather than mocking SQL calls.

### Service Layer

Two service classes sit between CLI and DB:
- **`QuizEngine`** (`quiz.py`) — owns session lifecycle: `load_questions()` → `submit_answer()` → `finalize()`. Internally stores `_correct_answers` (private dict) so question objects served to the display layer never contain answers — preventing accidental leaks to the UI.
- **`HistoryManager`** (`history.py`) — thin service over `DatabaseManager`; adds formatting and export logic (CSV/JSON).

### Answer Hiding (Security by Design)

`get_random_questions()` deliberately excludes `correct_answer` and `explanation` columns from its SELECT statement. A second query (`get_questions_with_answers()`) fetches full data but is only called from `QuizEngine._correct_answers` (internal dict) and post-quiz review. The `correct_answer` field on `Question` is typed `Optional[str] = None` and the default is `None`, making this explicit in the model.

### Pydantic v2 Validation

All data entities are Pydantic `BaseModel` subclasses with field validators:
- `Question.question_text_not_empty` — rejects empty or whitespace-only strings
- `Question.valid_answer_letter` — accepts only `A`–`E`; auto-uppercases lowercase input; allows `None`

### Non-Repetition Cycle Tracking

Rather than shuffling or tracking a "seen" set per user, the engine uses a `usage_cycle` counter on every question row. The "current cycle" is `MIN(usage_cycle)` across all questions. Questions are only drawn from the current cycle. When all questions in the cycle have `times_used > 0`, the cycle number is incremented and `times_used` reset — effectively creating an infinite round-robin through the question bank.

### CLI Separation (`cli.py` excluded from coverage)

All Rich terminal I/O functions live in `cli.py` and are all marked `# pragma: no cover`. This cleanly separates testable business logic from untestable (or impractical-to-test) interactive I/O. The coverage configuration explicitly omits `cli.py` and `main.py` from measurement.

### Markdown Parser Design

`parse_markdown_file()` in `utils.py` supports two formats with a single pass:
1. Detect `## Answer Key` table and pre-parse it into a dict
2. Strip the answer key section from the content string
3. Split remaining content at `## Q<N>` / `## Question N` / `### Question N` headers
4. For each block: extract text (simple vs. structured), options A–D (mandatory) + E (optional), answer (key table first, inline `**Answer: X**` fallback)
5. Skip blocks with `Answer Type: many` or missing/multi-letter answers

The parser is a pure function with no side effects, making it fully unit-testable with `tmp_path` fixtures.

### Virtual Environment Convention

The `build.*` scripts create `venv/` (not `.venv/`). The `setup.*` scripts create `.venv/`. All runtime scripts (`quiz.*`, `import.*`, `history.*`) look for `venv/bin/activate` (not `.venv/`). Always use `build.*` first, then the runtime scripts.

### No Typer Wiring (Important Discrepancy)

Despite `typer==0.24.1` being listed in `requirements.txt` and extensively documented in `docs/README.md`, the actual `main.py` entry point uses `argparse`. Typer is imported in `cli.py` only indirectly (via Rich's Console). The documented Typer-style CLI commands (`python -m quiz_engine.main quiz --questions 10`) do **not** match the actual argparse implementation (`python -m quiz_engine.main --questions 10`). When recreating, match the actual `argparse` implementation in `main.py`, not the docs.
