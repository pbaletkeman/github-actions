# Prompt: Recreate quiz-engine-dart from Scratch

> **Purpose:** A self-contained reference enabling a developer with zero prior
> context to recreate the `quiz-engine-dart` project exactly.

---

## 1. Project Structure

Every file and its purpose:

```
quiz-engine-dart/
├── bin/
│   └── quiz_engine.dart          # Thin entry point — delegates to lib/main.dart
├── lib/
│   ├── main.dart                 # CLI entry: arg parsing, command dispatch, db lifecycle
│   ├── quiz_engine_dart.dart     # Public barrel export of the entire library
│   └── src/
│       ├── cli/
│       │   ├── formatter.dart    # ANSI-coloured terminal output (boxed, table, success/error/warning/info, progressBar)
│       │   ├── prompts.dart      # stdin helpers (readLine, confirm, readAnswer, pressEnterToContinue)
│       │   └── commands/
│       │       ├── quiz_command.dart     # `quiz` subcommand — interactive quiz session
│       │       ├── import_command.dart   # `import` subcommand — bulk markdown import
│       │       ├── history_command.dart  # `history` subcommand — session listing, detail, export
│       │       └── clear_command.dart    # `clear` subcommand — delete questions / history / all
│       ├── database/
│       │   └── database.dart     # AppDatabase: SQLite schema creation + all DAOs (questions, sessions, responses)
│       ├── exceptions/
│       │   └── quiz_exceptions.dart  # Custom exceptions: NoQuestionsException, InsufficientQuestionsException,
│       │                             #   SessionNotFoundException, ImportException, DatabaseException
│       ├── models/
│       │   ├── question.dart     # Question value object with fromMap / toInsertMap
│       │   ├── quiz_response.dart # QuizResponse value object with fromMap / toInsertMap / correct getter
│       │   └── quiz_session.dart  # QuizSession value object with fromMap / toInsertMap / copyWith
│       └── service/
│           ├── answer_shuffler.dart  # Fisher-Yates shuffle tracking correct-answer position → ShuffleResult
│           ├── history_service.dart  # History queries: sessions, responses, getQuestionsByIds, exportSessions
│           ├── import_service.dart   # Bulk file/directory import orchestration
│           ├── markdown_parser.dart  # Regex-based markdown parser (simple `## Question N` format)
│           ├── quiz_engine.dart      # Session orchestration: loadQuestions, submitAnswer, finalizeQuiz
│           └── quiz_service.dart     # Business logic facade over AppDatabase
├── test/
│   ├── helpers.dart              # openTestDatabase() + sampleQuestion() fixture factory
│   └── src/
│       ├── database/
│       │   └── database_test.dart  # DAO tests: insert, deduplicate, cycle, sessions, responses, clear
│       ├── models/
│       │   └── models_test.dart    # Round-trip serialisation tests for all three models
│       └── service/
│           ├── answer_shuffler_test.dart   # Shuffle correctness, label generation, 4- and 5-option questions
│           ├── markdown_parser_test.dart   # Parsing content blocks, section headings, option extraction
│           └── quiz_engine_test.dart       # QuizService + QuizEngine integration using in-memory db
├── scripts/
│   └── check_coverage.sh        # Enforces ≥ 90 % line-coverage threshold via lcov --summary
├── docs/
│   ├── README.md                # Full user-facing documentation (see Section 5 below)
│   └── architecture.md          # Mermaid sequence, class, ER, and data-flow diagrams
├── Dockerfile                   # Multi-stage build: dart:3.0 builder → alpine:latest runtime
├── docker-compose.yml           # Three services: quiz-engine, quiz-engine-test, quiz-engine-build
├── pubspec.yaml                 # Package manifest
├── pubspec.lock                 # Pinned resolved dependency versions
├── analysis_options.yaml        # Dart linter — includes package:lints/recommended.yaml
├── architecture.md              # Root-level architecture overview (duplicate of docs/architecture.md)
├── README.md                    # Root README (brief)
├── build.sh                     # Bash build script (compile to bin/quiz_engine)
├── build.bat                    # Windows CMD build script (compile to bin\quiz_engine.exe)
├── build.ps1                    # PowerShell build script (compile to bin\quiz_engine.exe)
├── quiz.sh                      # Bash convenience wrapper for `quiz` command
├── quiz.bat                     # Windows CMD convenience wrapper
├── quiz.ps1                     # PowerShell convenience wrapper
├── import.sh                    # Bash convenience wrapper for `import` command
├── import.bat                   # Windows CMD convenience wrapper
├── import.ps1                   # PowerShell convenience wrapper
├── history.sh                   # Bash convenience wrapper for `history` command
├── history.bat                  # Windows CMD convenience wrapper
└── history.ps1                  # PowerShell convenience wrapper
```

---

## 2. Language, Runtime, and Dependencies

### Language & SDK

| Item | Version |
|------|---------|
| Dart SDK (pubspec constraint) | `>=3.0.0 <4.0.0` |
| Dart SDK (lock file resolved) | `>=3.7.0 <4.0.0` |

### Runtime Dependencies (`pubspec.yaml`)

| Package | Constraint | Resolved | Purpose |
|---------|-----------|---------|---------|
| `args` | `^2.4.0` | `2.7.0` | CLI argument parsing |
| `sqlite3` | `^2.4.0` | `2.9.4` | Direct SQLite access (no ORM) |
| `path` | `^1.8.0` | `1.9.1` | Cross-platform path utilities |
| `uuid` | `^4.2.1` | `4.5.3` | UUID v4 session ID generation |

### Dev Dependencies

| Package | Constraint | Resolved | Purpose |
|---------|-----------|---------|---------|
| `lints` | `^3.0.0` | `3.0.0` | Dart recommended lint rules |
| `test` | `^1.24.0` | `1.31.0` | Dart test framework |
| `mocktail` | `^1.0.0` | `1.0.4` | Mock/stub helpers (declared but not yet actively used) |

### Key Transitive Dependencies

| Package | Resolved | Notes |
|---------|---------|-------|
| `coverage` | `1.15.0` | Activated globally via `dart pub global activate coverage` for coverage reports |
| `ffi` | `2.2.0` | Required by `sqlite3` for native bindings |

---

## 3. Database Schema

The database is a single SQLite file. Created automatically on first run by
`AppDatabase._createTables()` in `lib/src/database/database.dart`.

### PRAGMAs set on every connection

```sql
PRAGMA journal_mode=WAL;
PRAGMA foreign_keys=ON;
```

---

### Table: `questions`

```sql
CREATE TABLE IF NOT EXISTS questions (
  id              INTEGER PRIMARY KEY AUTOINCREMENT,
  question_text   TEXT    NOT NULL CHECK(length(question_text) >= 1),
  option_a        TEXT    NOT NULL,
  option_b        TEXT    NOT NULL,
  option_c        TEXT    NOT NULL,
  option_d        TEXT    NOT NULL,
  option_e        TEXT,                          -- nullable; present only for 5-option questions
  correct_answer  TEXT    NOT NULL CHECK(length(correct_answer) = 1),
  explanation     TEXT,
  section         TEXT,
  difficulty      TEXT,
  source_file     TEXT,
  usage_cycle     INTEGER NOT NULL DEFAULT 1,
  times_used      INTEGER NOT NULL DEFAULT 0,
  last_used_at    INTEGER,                       -- Unix epoch seconds, nullable
  created_at      INTEGER NOT NULL DEFAULT (strftime('%s','now'))  -- Unix epoch seconds
);
```

**Deduplication:** `insertQuestionIfNotExists` checks `question_text` uniqueness
before inserting; there is no database-level UNIQUE constraint on `question_text`.

---

### Table: `quiz_sessions`

```sql
CREATE TABLE IF NOT EXISTS quiz_sessions (
  session_id          TEXT    PRIMARY KEY,      -- UUID v4
  started_at          INTEGER NOT NULL DEFAULT (strftime('%s','now')),
  ended_at            INTEGER,                  -- NULL until finalizeQuiz()
  num_questions       INTEGER NOT NULL,
  num_correct         INTEGER NOT NULL DEFAULT 0,
  percentage_correct  REAL    NOT NULL DEFAULT 0.0,
  time_taken_seconds  INTEGER                   -- NULL until finalizeQuiz()
);
```

---

### Table: `quiz_responses`

```sql
CREATE TABLE IF NOT EXISTS quiz_responses (
  id                  INTEGER PRIMARY KEY AUTOINCREMENT,
  session_id          TEXT    NOT NULL,
  question_id         INTEGER NOT NULL,
  user_answer         TEXT    NOT NULL,         -- single uppercase letter, e.g. "A"
  is_correct          INTEGER NOT NULL DEFAULT 0,  -- 0 = false, 1 = true
  time_taken_seconds  INTEGER,
  UNIQUE(session_id, question_id)               -- INSERT OR REPLACE semantics
);
```

**Relationship summary:**
- `quiz_sessions` 1 → many `quiz_responses` (via `session_id`)
- `questions` 1 → many `quiz_responses` (via `question_id`)
- No explicit FOREIGN KEY DDL, but `PRAGMA foreign_keys=ON` is set; future
  migrations may add explicit FK constraints.

---

## 4. CLI Commands

The binary name after compilation is `quiz_engine` (Linux/macOS) or
`quiz_engine.exe` (Windows). During development use `dart run lib/main.dart`.

### Global Options (parsed before the subcommand)

| Flag / Option | Default | Description |
|--------------|---------|-------------|
| `--db <path>` | `<exe_dir>/quiz_engine.db` | Path to SQLite database file |
| `--help`, `-h` | — | Show global help and exit 0 |
| `--version`, `-v` | — | Print `quiz_engine_dart 1.0.0` and exit 0 |

---

### `import` — Load questions from Markdown

```
quiz_engine import [--file <path>] [--dir <path>] [--help]
```

| Option | Abbr | Description |
|--------|------|-------------|
| `--file` | `-f` | Path to a single `.md` or `.markdown` file |
| `--dir` | `-d` | Path to a directory; imports all `.md`/`.markdown` files (non-recursive) |
| `--help` | `-h` | Show subcommand help |

**Rules:**
- At least one of `--file` or `--dir` must be supplied; both may be used together.
- Duplicate questions (same `question_text`) are silently skipped.
- Returns exit code `0` on success, `1` on error.

**Example invocations:**

```bash
# Import a single file
dart run lib/main.dart import --file questions.md
# → ✓ Imported 25 question(s).

# Import an entire directory
dart run lib/main.dart import --dir ./quiz-source/
# → ✓ Imported 120 question(s).

# Show help
dart run lib/main.dart import --help
```

---

### `quiz` — Start an interactive quiz session

```
quiz_engine quiz [--questions <n>] [--no-shuffle] [--help]
```

| Option | Abbr | Default | Description |
|--------|------|---------|-------------|
| `--questions` | `-q` | `10` | Number of questions to ask (positive integer) |
| `--no-shuffle` | — | `false` | Disable answer option shuffling |
| `--help` | `-h` | — | Show subcommand help |

**Behaviour:**
- Selects `n` random questions from the current usage cycle.
- If fewer than `n` questions exist a warning is printed and the quiz proceeds
  with the available count.
- Answers are shuffled by default using the Fisher-Yates algorithm.
- After all questions are answered, a full answer review is printed, then the
  final score box.
- At session end, all used questions are marked (`times_used += 1`). When the
  entire pool is exhausted the cycle counter increments and `times_used` resets.

**Example invocations:**

```bash
# Default 10-question quiz
dart run lib/main.dart quiz

# 20-question quiz without shuffling
dart run lib/main.dart quiz --questions 20 --no-shuffle

# Use a custom database
dart run lib/main.dart --db /var/data/quiz.db quiz --questions 5
```

**Expected output (abbreviated):**

```
╔══════════════════════════════════════════════════════════════════════╗
║ GitHub Actions Quiz Engine                                           ║
╠──────────────────────────────────────────────────────────────────────╣
║ Quiz started — 10 questions                                          ║
║ Type the letter of your answer and press ENTER.                      ║
╚══════════════════════════════════════════════════════════════════════╝

Q1/10  [Section: Workflow Trigger Events]

Which trigger event is used to run a workflow on a time-based schedule?

  A) on: workflow_dispatch
  B) on: schedule
  C) on: timer
  D) on: cron

Your answer (A/B/C/D): B

...

╔══════════════════════════════════════════════════════════════════════╗
║ Quiz Complete                                                         ║
╠──────────────────────────────────────────────────────────────────────╣
║ Score: 8/10                                                           ║
║ Percentage: 80.0%                                                     ║
║ Session ID: 3f2504e0-4f89-11d3-9a0c-0305e82c3301                     ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

### `history` — View past quiz sessions

```
quiz_engine history [--session-id <uuid>] [--review] [--export json|csv] [--help]
```

| Option | Abbr | Default | Description |
|--------|------|---------|-------------|
| `--session-id` | `-s` | — | UUID of a specific session to inspect |
| `--review` | `-r` | `false` | Show full answer key for a session (requires `--session-id`) |
| `--export` | `-e` | — | Export all sessions as `json` or `csv` to stdout |
| `--help` | `-h` | — | Show subcommand help |

**Example invocations:**

```bash
# List all sessions (ASCII table)
dart run lib/main.dart history

# Session detail
dart run lib/main.dart history --session-id 3f2504e0-4f89-11d3-9a0c-0305e82c3301

# Full answer review for a session
dart run lib/main.dart history \
  --session-id 3f2504e0-4f89-11d3-9a0c-0305e82c3301 \
  --review

# Export as JSON
dart run lib/main.dart history --export json > sessions.json

# Export as CSV
dart run lib/main.dart history --export csv > sessions.csv
```

**Default table output format:**

```
+--------------------------------------+------------------+-------+-------+
| Session ID                           | Date             | Score | %     |
+--------------------------------------+------------------+-------+-------+
| 3f2504e0-4f89-11d3-9a0c-0305e82c3301 | 2024-06-15 14:22 | 8/10  | 80.0% |
+--------------------------------------+------------------+-------+-------+
```

**CSV export format:**

```
session_id,started_at,ended_at,num_questions,num_correct,percentage
3f2504e0-...,2024-06-15T14:22:00.000Z,2024-06-15T14:27:00.000Z,10,8,80.00
```

---

### `clear` — Remove stored data

```
quiz_engine clear [--questions] [--history] [--all] [--confirm] [--help]
```

| Flag | Default | Description |
|------|---------|-------------|
| `--questions` | `false` | Delete all questions |
| `--history` | `false` | Delete all sessions and responses |
| `--all` | `false` | Delete all data (questions + sessions + responses) |
| `--confirm` | `false` | Skip the interactive confirmation prompt |
| `--help`, `-h` | — | Show subcommand help |

**Rules:**
- At least one of `--questions`, `--history`, or `--all` must be specified.
- Without `--confirm`, the user is prompted `[y/N]`.

**Example invocations:**

```bash
# Delete questions, confirmed
dart run lib/main.dart clear --questions --confirm

# Delete history, confirmed
dart run lib/main.dart clear --history --confirm

# Delete everything without a prompt
dart run lib/main.dart clear --all --confirm
```

---

## 5. Documentation (`docs/README.md`)

Full section structure and key content:

### Table of Contents (in order)

1. Quiz Engine — Dart — Full Documentation *(title)*
2. Overview
   - Features
3. Project Structure *(tree diagram)*
4. Prerequisites *(table: Dart SDK ≥ 3.0, Docker ≥ 20.10)*
5. Installation *(dart pub get; dart run lib/main.dart --help)*
6. Script Reference
   - Build Scripts (`build.bat`, `build.ps1`, `build.sh`)
   - Quiz Scripts (`quiz.bat`, `quiz.ps1`, `quiz.sh`)
   - Import Scripts (`import.bat`, `import.ps1`, `import.sh`)
   - History Scripts (`history.bat`, `history.ps1`, `history.sh`)
7. CLI Commands
   - `import` — Load questions from Markdown *(options table)*
   - `quiz` — Take a quiz *(options table)*
   - `history` — View past sessions *(options table)*
   - `clear` — Remove stored data
   - Global Options *(table)*
8. Docker Setup
   - Building *(multi-stage build description)*
   - Running Interactively *(volume mounts)*
   - Docker Compose Services *(table of 3 services)*
   - Multi-Architecture Build *(buildx command)*
9. Question File Format *(annotated example — see Section 6 below)*
10. Configuration *(--db flag, default path)*
11. Testing *(dart test; coverage collection; check_coverage.sh; genhtml)*
12. Building a Native Executable *(dart compile exe)*
13. Dependencies *(table)*
14. Architecture *(link to architecture.md + Design Decisions table)*

### Design Decisions Table (from docs/README.md)

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Database | `sqlite3` package | Direct SQLite access, no code generation required |
| CLI | `args` package | Standard, well-maintained argument parsing |
| Testing | `test` package | Idiomatic Dart testing framework |
| Distribution | `dart compile exe` | Single native binary, no runtime needed |
| Non-repetition | Cycle columns | `usage_cycle` + `times_used` ensure questions exhaust before repeating |

---

## 6. Question File Formats

The `MarkdownParser` (`lib/src/service/markdown_parser.dart`) supports
**one format** with two layout variants for the answer/explanation block.

### Format: Simple `## Question N` blocks

The parser splits content on `## Question <digits>` headers using the regex:

```
##\s+Question\s+\d+
```

The **top-level `#` heading** (if present and not starting with "Question")
becomes the `section` field. Options are matched with:

```
^([A-Ea-e])[).]\s*(.+)$
```

The answer is matched with:

```
\*\*Answer:\*\*\s*([A-Ea-e])
```

The explanation is matched with:

```
\*\*Explanation:\*\*\s*(.+)
```

**Requirements for a block to be parsed:**
- At least 4 options (A–D); option E is optional.
- A `**Answer:**` marker is mandatory.
- The `**Explanation:**` marker is optional.

---

### Sample Question 1 — Four-option question with explanation

```markdown
# GitHub Actions Basics

## Question 1
What does CI stand for?

A) Continuous Integration
B) Code Import
C) Compile
D) Configure

**Answer:** A
**Explanation:** CI stands for Continuous Integration.
```

*Parsed result:*
```
question_text : "What does CI stand for?"
option_a      : "Continuous Integration"
option_b      : "Code Import"
option_c      : "Compile"
option_d      : "Configure"
option_e      : null
correct_answer: "A"
explanation   : "CI stands for Continuous Integration."
section       : "GitHub Actions Basics"
usage_cycle   : 1
times_used    : 0
```

---

### Sample Question 2 — Five-option question, lowercase answer

```markdown
## Question 2
What is a workflow in GitHub Actions?

A) A shell script
B) An automated process triggered by events
C) A Docker image
D) A repository branch
E) A YAML schema validator

**Answer:** b
**Explanation:** Workflows are automated processes triggered by events.
```

*Parsed result:*
```
question_text : "What is a workflow in GitHub Actions?"
option_a      : "A shell script"
option_b      : "An automated process triggered by events"
option_c      : "A Docker image"
option_d      : "A repository branch"
option_e      : "A YAML schema validator"
correct_answer: "B"          ← normalised to uppercase
explanation   : "Workflows are automated processes triggered by events."
section       : null         ← no top-level # heading in the file excerpt
```

---

### Answer-Key Table variant (used in docs/README.md example)

The **Answer-Key table** layout is shown in the documentation as an example
of how source question files in the repository may be structured. This format
is **not** directly parsed by `MarkdownParser` — the questions section and the
answer-key section are separate blocks that a question-file author may maintain
for human readability. The parser only reads the `## Question N` blocks.

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

> **Note:** To have `MarkdownParser` parse question files that use this format,
> the option lines must use the `A)` or `A.` style (not `- A)`). The file
> content above would need `A) ...` lines (without the leading `- `) for the
> regex `^([A-Ea-e])[).]\s*(.+)$` to match.

---

## 7. Unit Test Coverage

| Property | Value |
|----------|-------|
| **Enforced threshold** | **90 %** line coverage |
| **Tool** | `lcov` (via `dart pub global activate coverage`) |
| **Configuration file** | `scripts/check_coverage.sh` |
| **Threshold variable** | Hard-coded literal `90` on line 27: `if (( $(echo "$COVERAGE < 90" | bc -l) ))` |
| **Coverage data source** | `coverage/lcov.info` (LCOV format) |

### Full content of `scripts/check_coverage.sh`

```bash
#!/bin/bash
# scripts/check_coverage.sh
# Enforces a minimum 90% line coverage threshold.

set -euo pipefail

LCOV_FILE="coverage/lcov.info"

if [ ! -f "$LCOV_FILE" ]; then
  echo "ERROR: $LCOV_FILE not found. Run tests with coverage first."
  echo "  dart pub global activate coverage"
  echo "  dart test --coverage=coverage"
  echo "  dart pub global run coverage:format_coverage --lcov --in=coverage --out=coverage/lcov.info --report-on=lib"
  exit 1
fi

SUMMARY=$(lcov --summary "$LCOV_FILE" 2>&1)
COVERAGE=$(echo "$SUMMARY" | grep -i "lines" | awk '{print $2}' | tr -d '%')

if [ -z "$COVERAGE" ]; then
  echo "ERROR: Could not parse coverage percentage from lcov output."
  exit 1
fi

echo "Line coverage: ${COVERAGE}%"

if (( $(echo "$COVERAGE < 90" | bc -l) )); then
  echo "ERROR: Coverage ${COVERAGE}% is below the required 90%"
  exit 1
fi

echo "Coverage check passed: ${COVERAGE}%"
```

### How to run coverage locally

```bash
# Step 1 — Install the coverage tool globally
dart pub global activate coverage

# Step 2 — Collect coverage data
dart test --coverage=coverage

# Step 3 — Convert to LCOV format (report only on lib/ sources)
dart pub global run coverage:format_coverage \
  --lcov \
  --in=coverage \
  --out=coverage/lcov.info \
  --report-on=lib

# Step 4 — Enforce the 90% threshold
bash scripts/check_coverage.sh

# Optional — Generate HTML report (requires lcov installed system-wide)
genhtml coverage/lcov.info --output-directory coverage/html
```

---

## 8. Scripts

All scripts live at the project root unless noted. Run from the project root.

### Build Scripts

#### `build.sh` — Linux / macOS

```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Dart - Build ==="
echo "Installing dependencies..."
dart pub get
echo "Compiling native executable..."
mkdir -p bin
dart compile exe lib/main.dart -o bin/quiz_engine
echo "Build successful! Executable: bin/quiz_engine"
```

**Invoke:**
```bash
./build.sh
```

**Output:** `bin/quiz_engine` (Linux/macOS native binary)

---

#### `build.bat` — Windows CMD

```bat
@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Dart - Build ===
echo Installing dependencies...
dart pub get
if %ERRORLEVEL% NEQ 0 (
    echo Dependency installation failed!
    exit /b %ERRORLEVEL%
)
echo Compiling native executable...
if not exist "bin" mkdir bin
dart compile exe lib/main.dart -o bin\quiz_engine.exe
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b %ERRORLEVEL%
)
echo Build successful! Executable: bin\quiz_engine.exe
```

**Invoke:**
```bat
build.bat
```

---

#### `build.ps1` — PowerShell

```powershell
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Dart - Build ===" -ForegroundColor Cyan
Write-Host "Installing dependencies..." -ForegroundColor Yellow
dart pub get
Write-Host "Compiling native executable..." -ForegroundColor Yellow
if (-not (Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
dart compile exe lib/main.dart -o bin\quiz_engine.exe
Write-Host "Build successful! Executable: bin\quiz_engine.exe" -ForegroundColor Green
```

**Invoke:**
```powershell
.\build.ps1
```

---

### Quiz Scripts

#### `quiz.sh` — Linux / macOS

```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Dart - Start Quiz ==="
QUESTIONS=${1:-10}
echo "Starting quiz with $QUESTIONS questions..."
dart run lib/main.dart quiz --questions $QUESTIONS
```

**Invoke:** `./quiz.sh [count]` — e.g. `./quiz.sh 20`

---

#### `quiz.bat` — Windows CMD

```bat
@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Dart - Start Quiz ===
if "%~1"=="" (
    echo Starting quiz with 10 questions (default)...
    dart run lib/main.dart quiz
) else (
    echo Starting quiz with %~1 questions...
    dart run lib/main.dart quiz --questions %~1
)
```

**Invoke:** `quiz.bat [count]`

---

#### `quiz.ps1` — PowerShell

```powershell
param(
    [int]$Questions = 10
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Dart - Start Quiz ===" -ForegroundColor Cyan
Write-Host "Starting quiz with $Questions questions..." -ForegroundColor Yellow
dart run lib/main.dart quiz --questions $Questions
```

**Invoke:** `.\quiz.ps1 [-Questions 20]`

---

### Import Scripts

#### `import.sh` — Linux / macOS

```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Dart - Import Questions ==="
if [ -z "$1" ]; then
    echo "Usage: ./import.sh <file_or_directory>"
    echo "No path specified. Importing from current directory..."
    dart run lib/main.dart import --dir .
elif [ -d "$1" ]; then
    echo "Importing from directory: $1"
    dart run lib/main.dart import --dir "$1"
else
    echo "Importing from file: $1"
    dart run lib/main.dart import --file "$1"
fi
```

**Invoke:** `./import.sh questions.md` or `./import.sh ./questions/`

---

#### `import.bat` — Windows CMD

```bat
@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Dart - Import Questions ===
if "%~1"=="" (
    echo No path specified. Importing from current directory...
    dart run lib/main.dart import --dir .
) else (
    if exist "%~1\" (
        echo Importing from directory: %~1
        dart run lib/main.dart import --dir "%~1"
    ) else (
        echo Importing from file: %~1
        dart run lib/main.dart import --file "%~1"
    )
)
```

**Invoke:** `import.bat questions.md` or `import.bat .\questions\`

---

#### `import.ps1` — PowerShell

```powershell
param(
    [string]$Path = ""
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Dart - Import Questions ===" -ForegroundColor Cyan
if ($Path -eq "") {
    Write-Host "Importing from current directory..." -ForegroundColor Yellow
    dart run lib/main.dart import --dir .
} elseif (Test-Path $Path -PathType Container) {
    Write-Host "Importing from directory: $Path" -ForegroundColor Yellow
    dart run lib/main.dart import --dir $Path
} else {
    Write-Host "Importing from file: $Path" -ForegroundColor Yellow
    dart run lib/main.dart import --file $Path
}
```

**Invoke:** `.\import.ps1 -Path questions.md`

---

### History Scripts

#### `history.sh` — Linux / macOS

```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Dart - View History ==="
if [ -z "$1" ]; then
    echo "Showing all sessions..."
    dart run lib/main.dart history
else
    echo "Showing session: $1"
    dart run lib/main.dart history --session-id "$1"
fi
```

**Invoke:** `./history.sh` or `./history.sh <session-uuid>`

---

#### `history.bat` — Windows CMD

```bat
@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Dart - View History ===
if "%~1"=="" (
    echo Showing all sessions...
    dart run lib/main.dart history
) else (
    echo Showing session: %~1
    dart run lib/main.dart history --session-id "%~1"
)
```

**Invoke:** `history.bat` or `history.bat <session-uuid>`

---

#### `history.ps1` — PowerShell

```powershell
param(
    [string]$SessionId = ""
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Dart - View History ===" -ForegroundColor Cyan
if ($SessionId -eq "") {
    Write-Host "Showing all sessions..." -ForegroundColor Yellow
    dart run lib/main.dart history
} else {
    Write-Host "Showing session: $SessionId" -ForegroundColor Yellow
    dart run lib/main.dart history --session-id $SessionId
}
```

**Invoke:** `.\history.ps1` or `.\history.ps1 -SessionId <uuid>`

---

### Coverage Script

#### `scripts/check_coverage.sh`

See [Section 7 — Unit Test Coverage](#7-unit-test-coverage) for full content.

**Invoke from project root:**
```bash
bash scripts/check_coverage.sh
```

---

## 9. Docker Setup

### `Dockerfile` — Full content

```dockerfile
# Build stage
FROM dart:3.0 AS builder

WORKDIR /app

COPY pubspec.* ./
RUN dart pub get

COPY . .
RUN dart compile exe lib/main.dart -o bin/quiz-engine

# Runtime stage
FROM alpine:latest

WORKDIR /app

RUN apk add --no-cache libc6-compat sqlite-libs

COPY --from=builder /app/bin/quiz-engine .

# Create non-root user for security
RUN addgroup -g 1000 dartuser && \
    adduser -D -u 1000 -G dartuser dartuser && \
    chown -R dartuser:dartuser /app

USER dartuser

ENTRYPOINT ["./quiz-engine"]
CMD ["--help"]
```

**Key points:**
- **Stage 1** (`dart:3.0`): installs deps (`dart pub get`), compiles to native binary.
- **Stage 2** (`alpine:latest`): minimal runtime — adds only `libc6-compat` and
  `sqlite-libs`; copies only the compiled binary.
- Runs as non-root user `dartuser` (UID/GID 1000) for security.
- Default command is `--help`; override with any CLI command.

---

### `docker-compose.yml` — Full content

```yaml
version: '3.8'

services:
  quiz-engine:
    build: .
    container_name: quiz-engine-dev
    volumes:
      - .:/app
      - pub_cache:/app/.pub-cache
    working_dir: /app
    command: dart run lib/main.dart --help
    environment:
      - PUB_CACHE=/app/.pub-cache
    stdin_open: true
    tty: true

  quiz-engine-test:
    build: .
    container_name: quiz-engine-test
    volumes:
      - .:/app
      - pub_cache:/app/.pub-cache
    working_dir: /app
    command: >
      bash -c "dart pub get &&
               dart test &&
               dart pub global activate coverage &&
               dart test --coverage=coverage &&
               dart pub global run coverage:format_coverage
                 --lcov --in=coverage --out=coverage/lcov.info --report-on=lib &&
               bash scripts/check_coverage.sh"
    environment:
      - PUB_CACHE=/app/.pub-cache

  quiz-engine-build:
    build: .
    container_name: quiz-engine-build
    volumes:
      - .:/app
      - pub_cache:/app/.pub-cache
    working_dir: /app
    command: dart compile exe lib/main.dart -o bin/quiz-engine-release
    environment:
      - PUB_CACHE=/app/.pub-cache

volumes:
  pub_cache:
```

### Service Summary

| Service | Container Name | Purpose |
|---------|---------------|---------|
| `quiz-engine` | `quiz-engine-dev` | Interactive CLI (`stdin_open: true`, `tty: true`) |
| `quiz-engine-test` | `quiz-engine-test` | Runs full test suite + coverage + 90% threshold check |
| `quiz-engine-build` | `quiz-engine-build` | Compiles to `bin/quiz-engine-release` |

### Environment Variables

| Variable | Value | Scope |
|----------|-------|-------|
| `PUB_CACHE` | `/app/.pub-cache` | All three services — redirects pub cache into the mounted volume |

### Volume Mounts

| Volume / Bind | Container Path | Purpose |
|--------------|---------------|---------|
| `.` (bind) | `/app` | Project source — live mount enables running `dart run` against local source |
| `pub_cache` (named) | `/app/.pub-cache` | Persists downloaded packages across container restarts |

### Docker usage commands

```bash
# Build the image
docker build -t quiz-engine-dart:latest .

# Verify (shows help)
docker run --rm quiz-engine-dart:latest

# Interactive quiz with persistent data
docker run -it -v quiz-dart-data:/data \
  quiz-engine-dart:latest quiz --questions 10

# Import a local markdown file
docker run -it \
  -v quiz-dart-data:/data \
  -v "$(pwd)/questions.md:/questions.md" \
  quiz-engine-dart:latest import --file /questions.md

# Run via docker-compose
docker-compose up quiz-engine        # interactive CLI
docker-compose up quiz-engine-test   # test + coverage
docker-compose up quiz-engine-build  # compile release binary

# Multi-architecture build (requires Docker buildx)
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t myregistry/quiz-engine-dart:1.0.0 \
  --push .
```

---

## 10. Architecture Decisions

### Layer Overview

```
User (stdin/stdout)
       │
       ▼
bin/quiz_engine.dart          ← thin entry point
       │
       ▼
lib/main.dart                 ← ArgParser, command dispatch, AppDatabase lifecycle
       │
       ▼
lib/src/cli/commands/*.dart   ← one function per command, owns ArgParser for the subcommand
       │
       ▼
lib/src/service/              ← business logic layer (QuizEngine, QuizService, ImportService, HistoryService)
       │
       ▼
lib/src/database/database.dart ← data-access layer (AppDatabase, all SQL)
       │
       ▼
SQLite file (quiz_engine.db)
```

### Notable Patterns

#### Repository Pattern (via `AppDatabase`)
All SQL queries are encapsulated in `AppDatabase`. No raw SQL appears outside
`database.dart`. Higher layers work exclusively with model objects
(`Question`, `QuizSession`, `QuizResponse`).

#### Service Layer (Facade)
`QuizService` wraps `AppDatabase` methods and adds domain logic such as
`NoQuestionsException` / `InsufficientQuestionsException` guards.
`HistoryService` composes `QuizService` + direct `AppDatabase.db` access for
complex cross-table queries (e.g. `SELECT … WHERE id IN (…)`).

#### No ORM — Raw SQL
The project deliberately avoids ORM or code generation. `sqlite3` is used
directly with positional parameter lists. Schema is defined inline as
`CREATE TABLE IF NOT EXISTS` DDL strings.

#### Dependency Injection (Constructor Injection)
All services accept dependencies via constructor parameters with sensible
defaults:
- `QuizEngine(quizService:, shuffler:?, numQuestions:)`
- `ImportService(db, {parser?})`
- `AnswerShuffler({random?})` — `Random` is injected to allow deterministic
  tests.

#### Non-Repetition Cycle System
Two `INTEGER` columns on `questions` implement a rotating question pool:
- `usage_cycle` — which cycle number a question belongs to (starts at 1).
- `times_used` — how many times a question has been shown in the current cycle.

`getRandomQuestions()` selects only questions in the current cycle with
`times_used = 0`. When all questions are exhausted (`advanceCycleIfExhausted`)
the cycle counter increments globally and `times_used` is reset to 0, ensuring
all questions appear before any repeats.

#### Answer Shuffling
`AnswerShuffler` uses an in-place Fisher-Yates shuffle on a `List<String>` copy
of the options. The correct answer's *text* is tracked (not its index) so the
new label is found via `indexOf` after shuffling. This avoids off-by-one errors.
`ShuffleResult` carries `shuffledOptions`, `labels` (`['A','B','C','D'[,'E']]`),
and `correctLabel`.

#### CLI Dispatch Pattern
`lib/main.dart` registers subcommands with `ArgParser.allowAnything()` to
avoid the top-level parser consuming subcommand flags. Each subcommand handler
function (e.g. `quizCommand`) creates its own `ArgParser` and returns an `int`
exit code. The parent calls `exit(exitCode)` after closing the database.

#### In-Memory Database for Tests
`AppDatabase.inMemory()` factory uses `sqlite3.openInMemory()`. The test
helper `openTestDatabase()` calls this factory so every test gets an isolated
schema with no file I/O.

#### ANSI Terminal Formatting
`Formatter` uses ANSI escape codes (`\x1B[…m`) for colour/bold output and
Unicode box-drawing characters (`╔`, `╗`, `╚`, `╝`, `║`, `═`) for bordered
output boxes. `_stripAnsi()` is used to calculate padding without counting
invisible escape characters.
