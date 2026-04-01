# Prompt: Recreate quiz-engine-rust from Scratch

> Self-contained specification. Everything needed to recreate the project is in this file.

---

## 1. Project Structure

```
quiz-engine-rust/
├── src/
│   ├── main.rs                          # Binary entry point — Clap parse, DB init, dispatch
│   ├── lib.rs                           # Crate root — re-exports cli, db, error, models, service
│   ├── error.rs                         # QuizError enum (thiserror) + Result<T> type alias
│   ├── cli/
│   │   ├── mod.rs                       # Re-exports run_* fns and *Args structs
│   │   ├── formatter.rs                 # print_table, print_result_box, print_divider, print_review
│   │   ├── prompts.rs                   # prompt(), prompt_choice(), confirm() — stdin helpers
│   │   └── commands/
│   │       ├── mod.rs                   # Re-exports all command handlers
│   │       ├── quiz.rs                  # QuizArgs (#[arg short/long, default 10]) + run_quiz()
│   │       ├── import.rs                # ImportArgs (--file PathBuf) + run_import()
│   │       ├── history.rs               # HistoryArgs (--session-id, --review, --export) + run_history()
│   │       └── clear.rs                 # ClearArgs (--questions, --history, --all, --confirm) + run_clear()
│   ├── models/
│   │   ├── mod.rs                       # Re-exports Question, NewQuestion, QuizQuestion, QuizSession, QuizResponse
│   │   ├── question.rs                  # Question (sqlx::FromRow), NewQuestion (insert DTO), QuizQuestion (runtime view)
│   │   ├── quiz_session.rs              # QuizSession (sqlx::FromRow) + grade()/passed() methods
│   │   └── quiz_response.rs             # QuizResponse (sqlx::FromRow) + correct() method
│   ├── db/
│   │   ├── mod.rs                       # Re-exports connection fns and repo structs
│   │   ├── connection.rs                # create_pool(), run_migrations(), create_test_pool(), default_db_path(), ensure_db_dir()
│   │   └── repositories/
│   │       ├── mod.rs                   # Re-exports QuestionRepo, SessionRepo, ResponseRepo
│   │       ├── question_repo.rs         # QuestionRepo — insert, insert_if_not_exists, get_all, count, get_random_for_quiz, mark_used, advance_cycle_if_exhausted, get_by_id, delete_all
│   │       ├── session_repo.rs          # SessionRepo — create, finalize, get_by_id, list_all, delete_all
│   │       └── response_repo.rs         # ResponseRepo — record, get_by_session, delete_by_session, delete_all
│   └── service/
│       ├── mod.rs                       # Re-exports all service modules
│       ├── quiz_engine.rs               # QuizEngine struct — owns session, questions, drives quiz loop
│       ├── quiz_service.rs              # QuizService — get_random_questions, mark_question_used, question_count, current_cycle
│       ├── history_service.rs           # HistoryService — list_sessions, get_session, get_responses, summary, export_json, export_csv
│       ├── import_service.rs            # ImportService — import_from_file (parse + deduplicate insert)
│       ├── markdown_parser.rs           # parse_markdown_file(), parse_markdown_content() — auto-detects format
│       ├── answer_shuffler.rs           # shuffle_answers(), letter_to_index(), index_to_letter()
│       └── quiz_utils.rs                # calculate_percentage(), grade_from_percentage(), format_duration()
├── migrations/
│   └── 001_create_tables.sql            # Single migration — creates questions, quiz_sessions, quiz_responses + indexes
├── tests/
│   ├── database_tests.rs                # QuestionRepo unit/integration tests against :memory: SQLite
│   ├── service_tests.rs                 # QuizEngine, HistoryService, QuizService, repo integration tests
│   └── integration_tests.rs            # Full end-to-end: import → quiz → history workflow tests
├── benches/
│   └── quiz_bench.rs                    # Criterion benchmarks: shuffle_answers, calculate_percentage, db_insert, db_get_random
├── scripts/
│   ├── check_coverage.sh                # Enforce ≥90% line coverage (llvm-cov primary, tarpaulin fallback)
│   └── check_coverage.bat               # Windows equivalent of check_coverage.sh
├── docs/
│   ├── README.md                        # Full user documentation (see Section 5)
│   └── architecture.md                  # Mermaid diagrams: system overview, sequence, ER, class, data flow
├── architecture.md                      # Root-level copy of architecture diagrams
├── Cargo.toml                           # Package manifest
├── Cargo.lock                           # Locked dependency tree
├── Dockerfile                           # Multi-stage build: rust:1.75 builder → debian:bookworm-slim runtime
├── docker-compose.yml                   # Three services: quiz-engine, quiz-engine-test, quiz-engine-build
├── build.sh / build.ps1 / build.bat     # Cross-platform build scripts (cargo build --release)
├── quiz.sh / quiz.ps1 / quiz.bat        # Cross-platform quiz launcher
├── import.sh / import.ps1 / import.bat  # Cross-platform import launcher
├── history.sh / history.ps1 / history.bat # Cross-platform history launcher
├── .gitignore
└── README.md                            # Root README (same content as docs/README.md)
```

---

## 2. Language, Runtime, and Dependencies

### Language & Edition
- **Rust** edition `2021`, minimum version **1.70+** (Dockerfile uses `rust:1.75`)

### Cargo.toml — exact dependency declarations

```toml
[package]
name = "quiz_engine"
version = "0.1.0"
edition = "2021"
description = "GH-200 Certification Quiz Engine built with Rust, sqlx, and clap"
authors = ["Quiz Engine Contributors"]

[[bin]]
name = "quiz_engine"
path = "src/main.rs"

[lib]
name = "quiz_engine"
path = "src/lib.rs"

[dependencies]
sqlx       = { version = "0.8",  features = ["sqlite", "runtime-tokio-native-tls", "chrono"] }
tokio      = { version = "1",    features = ["full"] }
clap       = { version = "4.5",  features = ["derive"] }
uuid       = { version = "1.11", features = ["v4"] }
serde      = { version = "1.0",  features = ["derive"] }
serde_json = "1.0"
chrono     = { version = "0.4",  features = ["serde"] }
rand       = "0.8"
regex      = "1.11"
anyhow     = "1.0"
thiserror  = "2.0"
dotenvy    = "0.15"

[dev-dependencies]
tempfile  = "3.14"
criterion = { version = "0.5", features = ["async_tokio"] }
tokio     = { version = "1", features = ["full"] }

[[bench]]
name    = "quiz_bench"
harness = false

[profile.release]
opt-level      = 3
lto            = true
codegen-units  = 1
panic          = "abort"
strip          = true

[profile.test]
opt-level = 0
debug     = true
```

### Locked versions (from Cargo.lock)

| Crate | Locked version |
|---|---|
| `sqlx` | 0.7.3 (declared 0.8; lock resolves to latest 0.8.x) |
| `sqlx-core` / `sqlx-macros` | 0.8.6 |
| `tokio` | 1.x (full) |
| `clap` | 4.6.0 |
| `clap_builder` / `clap_derive` | 4.6.0 |
| `uuid` | 1.11.x |
| `serde` / `serde_derive` / `serde_json` | 1.0.228 |
| `chrono` | 0.4.44 |
| `rand` | 0.8.5 |
| `regex` | 1.12.3 |
| `anyhow` | 1.0.102 |
| `thiserror` | 2.x |
| `dotenvy` | 0.2.5 |
| `tempfile` | 3.14.x |
| `criterion` | 0.5.1 |

---

## 3. Database Schema

Single migration file: `migrations/001_create_tables.sql`

```sql
CREATE TABLE IF NOT EXISTS questions (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    question_text   TEXT    NOT NULL CHECK(length(question_text) <= 2000),
    option_a        TEXT    NOT NULL CHECK(length(option_a)      <= 500),
    option_b        TEXT    NOT NULL CHECK(length(option_b)      <= 500),
    option_c        TEXT    NOT NULL CHECK(length(option_c)      <= 500),
    option_d        TEXT    NOT NULL CHECK(length(option_d)      <= 500),
    option_e        TEXT            CHECK(length(option_e)       <= 500),
    correct_answer  TEXT    NOT NULL CHECK(length(correct_answer) = 1),
    explanation     TEXT            CHECK(length(explanation)    <= 2000),
    section         TEXT            CHECK(length(section)        <= 100),
    difficulty      TEXT            CHECK(length(difficulty)     <= 50),
    source_file     TEXT            CHECK(length(source_file)    <= 255),
    usage_cycle     INTEGER NOT NULL DEFAULT 1,
    times_used      INTEGER NOT NULL DEFAULT 0,
    last_used_at    TEXT,
    created_at      TEXT    NOT NULL DEFAULT (datetime('now'))
);

CREATE INDEX IF NOT EXISTS idx_usage_cycle ON questions(usage_cycle);
CREATE INDEX IF NOT EXISTS idx_section     ON questions(section);

CREATE TABLE IF NOT EXISTS quiz_sessions (
    session_id          TEXT    PRIMARY KEY CHECK(length(session_id) = 36),
    started_at          TEXT    NOT NULL DEFAULT (datetime('now')),
    ended_at            TEXT,
    num_questions       INTEGER NOT NULL,
    num_correct         INTEGER NOT NULL DEFAULT 0,
    percentage_correct  REAL    NOT NULL DEFAULT 0.0,
    time_taken_seconds  INTEGER
);

CREATE INDEX IF NOT EXISTS idx_started_at ON quiz_sessions(started_at);

CREATE TABLE IF NOT EXISTS quiz_responses (
    id                  INTEGER PRIMARY KEY AUTOINCREMENT,
    session_id          TEXT    NOT NULL CHECK(length(session_id) = 36),
    question_id         INTEGER NOT NULL,
    user_answer         TEXT    NOT NULL CHECK(length(user_answer) = 1),
    is_correct          INTEGER NOT NULL DEFAULT 0,
    time_taken_seconds  INTEGER,
    FOREIGN KEY (session_id)  REFERENCES quiz_sessions(session_id),
    FOREIGN KEY (question_id) REFERENCES questions(id),
    UNIQUE(session_id, question_id)
);

CREATE INDEX IF NOT EXISTS idx_responses_session_id  ON quiz_responses(session_id);
CREATE INDEX IF NOT EXISTS idx_responses_question_id ON quiz_responses(question_id);
```

### Relationships
- `quiz_sessions` 1 → many `quiz_responses` (via `session_id`)
- `questions` 1 → many `quiz_responses` (via `question_id`)
- `UNIQUE(session_id, question_id)` prevents duplicate responses per question per session

### Non-repetition cycle tracking
- `usage_cycle` starts at `1`. After each quiz, `mark_used()` increments `usage_cycle + 1` and sets `last_used_at`.
- `get_random_for_quiz()` always selects from `WHERE usage_cycle = MIN(usage_cycle)`, ensuring every question is seen before any repeats.

### Migrations applied via
```rust
sqlx::migrate!("./migrations").run(pool).await
```
Called in `main.rs` before any command runs. Also called in `create_test_pool()` for tests.

---

## 4. CLI Commands

Binary name: `quiz_engine`

### Global option

| Flag | Default | Description |
|---|---|---|
| `--db <URL>` | `sqlite:./quiz_engine.db` (or `DATABASE_URL` env var) | SQLite database path |

### `import` — Load questions from Markdown

```bash
quiz_engine import --file <path/to/questions.md>
# short flag: -f
```

| Flag | Required | Description |
|---|---|---|
| `--file` / `-f` | Yes | Path to a single `.md` file |

**Example output:**
```
Importing questions from: questions.md
Import complete:
  Imported: 42
  Skipped (duplicates): 3
```

**Duplicate detection:** skips any question whose `question_text` already exists in the DB.

---

### `quiz` — Take a quiz

```bash
quiz_engine quiz                      # 10 questions (default)
quiz_engine quiz --questions 20       # 20 questions
quiz_engine quiz -q 5                 # 5 questions (short flag)
```

| Flag | Short | Default | Description |
|---|---|---|---|
| `--questions` | `-q` | `10` | Number of questions per session |

**Example output:**
```
Starting quiz with 10 questions...
Press Ctrl+C at any time to exit.

────────────────────────────────────────────────────────────
Question 1 of 10
Section: Workflow Trigger Events

Which trigger event runs a workflow on a schedule?
  A) on: timer
  B) on: schedule
  C) on: cron
  D) on: workflow_dispatch
Your answer [A/B/C/D]: B

...

╔════════════════════════════╗
║ Quiz Complete              ║
╠════════════════════════════╣
║ Score:    8/10             ║
║ Percent:  80.0%            ║
║ Grade:    B                ║
║ Duration: 3m 42s           ║
║ Result:   PASSED ✓         ║
║ Session:  <uuid>           ║
╚════════════════════════════╝

╔════════════════════════════════════════════════════════════╗
║ Answer Review                                              ║
╚════════════════════════════════════════════════════════════╝

  ✓ Q1: Which trigger event runs a workflow on a schedule?
    Your answer: B)  ✓
  ✗ Q2: ...
    Your answer:    A)
    Correct answer: C) <text>
    Explanation: <text>
```

**Grade thresholds** (implemented in `QuizSession::grade()`):

| Range | Grade |
|---|---|
| 90–100% | A |
| 80–89% | B |
| 70–79% | C |
| 60–69% | D |
| < 60% | F |

**Pass threshold:** ≥ 70% (`QuizSession::passed()`)

Answers are **shuffled** on every quiz run via `answer_shuffler::shuffle_answers()` (uses `rand::thread_rng()`). There is **no `--no-shuffle` flag** in the actual implementation (the docs/README mentions it but it is not present in the code).

---

### `history` — View past sessions

```bash
quiz_engine history                                    # list all sessions
quiz_engine history --session-id <uuid>                # detail for one session
quiz_engine history --session-id <uuid> --review       # + per-question answer review
quiz_engine history --export json                      # export all sessions to quiz-history-<ts>.json
quiz_engine history --export csv                       # export all sessions to quiz-history-<ts>.csv
```

| Flag | Description |
|---|---|
| `--session-id <uuid>` | Show details for a specific session |
| `--review` | (requires `--session-id`) Show per-question answer table |
| `--export json\|csv` | Export all sessions to a timestamped file |

**List output (table with box-drawing chars):**
```
┌──────────────────────────────────────┬────────────┬───────────┬─────────┬─────────┬───────┐
│ Session ID                           │ Date       │ Questions │ Correct │ Score % │ Grade │
├──────────────────────────────────────┼────────────┼───────────┼─────────┼─────────┼───────┤
│ <uuid>                               │ 2024-01-15 │ 10        │ 8       │ 80.0    │ B     │
└──────────────────────────────────────┴────────────┴───────────┴─────────┴─────────┴───────┘
```

**JSON export schema per session:**
```json
{
  "session_id": "<uuid>",
  "started_at": "2024-01-15 10:00:00",
  "score": 8,
  "total_questions": 10,
  "percentage_correct": 80.0,
  "responses": [
    { "question_id": 1, "user_answer": "B", "is_correct": true }
  ]
}
```

**CSV export columns:**
```
session_id,started_at,score,total_questions,percentage_correct,question_id,user_answer,is_correct
```

---

### `clear` — Remove stored data

```bash
quiz_engine clear --questions --confirm     # delete all questions
quiz_engine clear --history --confirm       # delete all sessions + responses
quiz_engine clear --all --confirm           # delete everything
```

| Flag | Description |
|---|---|
| `--questions` | Delete all rows from `questions` table |
| `--history` | Delete all rows from `quiz_sessions` and `quiz_responses` |
| `--all` | Equivalent to `--questions --history` |
| `--confirm` | **Required** — without it the command returns `ConfirmationRequired` error |

**Example output:**
```
Cleared 2 session(s) and 20 response(s).
Cleared 42 question(s).
```

---

## 5. Documentation — `docs/README.md` Structure

Full table of contents and content summary:

```
# Quiz Engine — Rust — Full Documentation

## Overview
  ### Features
    - Interactive CLI quiz with shuffled answers
    - Async SQLite via sqlx with Tokio runtime
    - Non-repetition cycle tracking (usage_cycle + times_used)
    - Markdown import
    - Session history with JSON/CSV export
    - Serde JSON serialization
    - Criterion benchmarks
    - Single binary (~10 MB), no runtime deps

## Project Structure
  (tree listing of all files)

## Prerequisites
  | Tool          | Version | Download              |
  | Rust + Cargo  | 1.70+   | https://rustup.rs/    |
  | C compiler    | —       | required for sqlx     |
  | Docker        | 20.10+  | optional              |

  ### C Compiler Setup
    Windows Option A — Visual C++ Build Tools
    Windows Option B — MinGW-w64
    macOS — xcode-select --install
    Linux  — sudo apt install build-essential libsqlite3-dev

## Installation
  cargo build --release
  ./target/release/quiz_engine --help

## Script Reference
  ### Build Scripts
    build.bat / build.ps1 / build.sh
  ### Quiz Scripts
    quiz.bat / quiz.ps1 / quiz.sh
  ### Import Scripts
    import.bat / import.ps1 / import.sh
  ### History Scripts
    history.bat / history.ps1 / history.sh

## CLI Commands
  import, quiz, history, clear (with tables of flags/defaults)

## Docker Setup
  Multi-stage build explanation
  docker run examples
  ### Environment Variables
    | QUIZ_DB_PATH | ./quiz_engine.db | SQLite database path |
  ### Docker Compose Services
    docker-compose up quiz-engine
    docker-compose up quiz-engine-test
    docker-compose up quiz-engine-build

## Question File Format
  (full example — see Section 6 below)

## Configuration
  --db flag and QUIZ_DB_PATH env var

## Testing & Benchmarks
  cargo test
  cargo test -- --nocapture
  cargo bench
  cargo llvm-cov --html
  ./scripts/check_coverage.sh
  Coverage threshold: ≥90% total line coverage

  ### Integration Tests
    Use :memory: SQLite for isolation

## Build Notes — C Compiler Requirement
  Cross-compilation target table

## Dependencies
  (table of crates and purposes)

## Architecture
  See architecture.md
```

---

## 6. Question File Formats

The parser auto-detects format by checking for `## Answer Key` in the file content.

### Format 1 — Legacy (simple) format

**Trigger:** No `## Answer Key` section present.

**Structure:**
- Block headers: `## Q<n>` or `### Q<n>`
- Question text: `> <text>` (blockquote)
- Options: `- A) text`, `- B) text`, etc. (4 or 5 options, A–E)
- Answer: `**Answer: X**` (case-insensitive)
- Explanation (optional): `> Explanation: <text>`

**Sample 1:**
```markdown
## Q1
> Which trigger event runs a workflow on a recurring time-based schedule?
- A) `on: timer`
- B) `on: cron`
- C) `on: schedule`
- D) `on: workflow_dispatch`
**Answer: C**
> Explanation: `on: schedule` is the correct trigger. `cron` is the value of the schedule key.
```

**Sample 2:**
```markdown
## Q2
> What is the default shell used by GitHub Actions on Ubuntu runners?
- A) sh
- B) bash
- C) zsh
- D) dash
**Answer: B**
```

**Parsing rules:**
- Blocks without a complete set of A–D options are silently skipped.
- Blocks without `**Answer: X**` are skipped.
- Valid answer letters: A, B, C, D, E only. Invalid letters produce a `ParseError`.
- `option_e` is optional; 5-option questions (A–E) are fully supported.

---

### Format 2 — GH-200 / Answer-Key table format

**Trigger:** File contains `## Answer Key` section.

**Structure:**
- Question headers: `### Question N — Section Name`
- Metadata lines: `**Difficulty**: Easy`, `**Answer Type**: one`, `**Topic**: <text>`
- Optional scenario: `**Scenario**:` followed by paragraph text
- Question body: `**Question**:` followed by paragraph text
- Options: `- A) text` through `- D) text` (or E)
- Answer key table at the end under `## Answer Key`

**Answer Key table format:**
```markdown
## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1  | C         | `on: schedule` is the correct trigger. | 05-Workflow-Trigger-Events.md | Easy |
| 2  | A, B, D   | Multi-answer — skipped by parser       | 02-Contextual-Information.md  | Medium |
```

**Sample 1:**
```markdown
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
```

**Sample 2 (with scenario):**
```markdown
### Question 3 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: github context

**Scenario**:
A workflow needs to reference the repository name at runtime without hardcoding it.

**Question**:
Which expression provides the repository name in a GitHub Actions workflow?

- A) `${{ env.REPO_NAME }}`
- B) `${{ github.repository }}`
- C) `${{ runner.name }}`
- D) `${{ job.name }}`
```

**Parsing rules:**
- `**Answer Type**: many` and `**Answer Type**: none` questions are **skipped** (multi-select not supported).
- Multi-answer rows in the Answer Key table (containing a comma, e.g. `A, B, D`) are also skipped.
- `section` is extracted from the header text after `—` (em dash, en dash, or hyphen).
- Scenario text is prepended to question text: `"<scenario> <question>"`.
- Questions with no matching entry in the Answer Key are skipped.

---

## 7. Unit Test Coverage

| Property | Value |
|---|---|
| **Enforced threshold** | **90%** total line coverage |
| **Primary tool** | `cargo-llvm-cov` |
| **Fallback tool** | `cargo-tarpaulin` |
| **Configuration file** | `scripts/check_coverage.sh` — `THRESHOLD=90` (line 6) |
| **Windows config file** | `scripts/check_coverage.bat` — `set THRESHOLD=90` (line 5) |

**Install the tool:**
```bash
cargo install cargo-llvm-cov
# or fallback:
cargo install cargo-tarpaulin
```

**Run coverage:**
```bash
cargo llvm-cov --html          # generates target/llvm-cov/html/index.html
cargo llvm-cov --summary-only  # text summary only
./scripts/check_coverage.sh    # enforces ≥90%, exits 1 if below
```

The script parses the `TOTAL` line from `cargo llvm-cov --summary-only` output, extracts the percentage with `awk`, and exits with code 1 if below `$THRESHOLD`.

**Test files:**

| File | What it tests |
|---|---|
| `tests/database_tests.rs` | `QuestionRepo` CRUD, cycle tracking, deduplication |
| `tests/service_tests.rs` | `QuizEngine`, `HistoryService`, `QuizService`, `SessionRepo`, `ResponseRepo`, utils |
| `tests/integration_tests.rs` | Full import→quiz→history workflow, cycle advancement, multi-session summary |
| Inline `#[cfg(test)]` in `src/service/markdown_parser.rs` | Legacy and GH-200 parsing, edge cases |
| Inline `#[cfg(test)]` in `src/service/answer_shuffler.rs` | Shuffle correctness, index/letter conversion |
| Inline `#[cfg(test)]` in `src/service/quiz_utils.rs` | Percentage, grade, duration formatting |

All integration/database tests use `create_test_pool()` which creates an in-memory SQLite database (`sqlite::memory:`) and runs migrations — zero filesystem side-effects.

---

## 8. Scripts

All scripts are at the **project root** (`quiz-engine-rust/`). Invoke from project root.

### Build Scripts

| Script | Platform | Invoke | What it does |
|---|---|---|---|
| `build.sh` | Bash / macOS / Linux | `./build.sh` | `set -e`, `cd` to script dir, `cargo build --release` |
| `build.ps1` | PowerShell | `.\build.ps1` | `$ErrorActionPreference = "Stop"`, `cargo build --release` |
| `build.bat` | Windows CMD | `build.bat` | Adds `~\.cargo\bin` to PATH, `cargo build --release`, checks `%ERRORLEVEL%` |

### Quiz Scripts

| Script | Invoke | What it does |
|---|---|---|
| `quiz.sh` | `./quiz.sh [N]` | `cargo run --release -- quiz --questions ${1:-10}` |
| `quiz.ps1` | `.\quiz.ps1 [-Questions N]` | `cargo run --release -- quiz --questions $Questions` (default 10) |
| `quiz.bat` | `quiz.bat [N]` | `cargo run --release -- quiz` or `quiz --questions %~1` |

### Import Scripts

| Script | Invoke | What it does |
|---|---|---|
| `import.sh` | `./import.sh <file.md>` | `cargo run --release -- import --file "$1"` (errors if no arg) |
| `import.ps1` | `.\import.ps1 -File <file.md>` | `cargo run --release -- import --file $File` (errors if empty) |
| `import.bat` | `import.bat <file.md>` | `cargo run --release -- import --file "%~1"` (prints usage if no arg) |

### History Scripts

| Script | Invoke | What it does |
|---|---|---|
| `history.sh` | `./history.sh` | `cargo run --release -- history` |
| `history.ps1` | `.\history.ps1` | `cargo run --release -- history` |
| `history.bat` | `history.bat` | `cargo run --release -- history` |

### Coverage Scripts

| Script | Location | Invoke | What it does |
|---|---|---|---|
| `check_coverage.sh` | `scripts/check_coverage.sh` | `./scripts/check_coverage.sh` | Runs `cargo llvm-cov --summary-only`, parses TOTAL%, exits 1 if < 90 |
| `check_coverage.bat` | `scripts/check_coverage.bat` | `scripts\check_coverage.bat` | Same logic via PowerShell inline script |

---

## 9. Docker Setup

### Dockerfile (full content)

```dockerfile
# Build stage
FROM rust:1.75 AS builder

WORKDIR /app

RUN apt-get update && apt-get install -y libsqlite3-dev && rm -rf /var/lib/apt/lists/*

# Cache dependencies layer
COPY Cargo.toml Cargo.lock ./
RUN mkdir src && echo "fn main() {}" > src/main.rs && echo "" > src/lib.rs \
    && cargo build --release \
    && rm -rf src

COPY . .
RUN cargo build --release

# Runtime stage
FROM debian:bookworm-slim

WORKDIR /app

RUN apt-get update && apt-get install -y libsqlite3-0 && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/release/quiz_engine .
COPY --from=builder /app/migrations ./migrations

# Create non-root user
RUN useradd -m -u 1000 rustuser && chown -R rustuser:rustuser /app
USER rustuser

ENTRYPOINT ["./quiz_engine"]
CMD ["--help"]
```

**Key design notes:**
- Dependency caching layer: copies only `Cargo.toml`/`Cargo.lock`, builds a dummy binary, then replaces `src/` with actual source. This avoids re-downloading crates on every source change.
- Non-root user `rustuser` (UID 1000) for security.
- Runtime image is `debian:bookworm-slim` (not Alpine) because sqlx links against `libsqlite3-0`.

### docker-compose.yml (full content)

```yaml
version: '3.8'

services:
  quiz-engine:
    build: .
    container_name: quiz-engine-dev
    volumes:
      - .:/app
      - cargo-cache:/usr/local/cargo/registry
    working_dir: /app
    command: cargo run -- --help
    environment:
      - CARGO_NET_OFFLINE=false
      - DATABASE_URL=sqlite:./quiz_engine.db
    stdin_open: true
    tty: true

  quiz-engine-test:
    build: .
    container_name: quiz-engine-test
    volumes:
      - .:/app
      - cargo-cache:/usr/local/cargo/registry
    working_dir: /app
    command: sh -c "cargo test && ./scripts/check_coverage.sh"
    environment:
      - CARGO_NET_OFFLINE=false

  quiz-engine-build:
    build: .
    container_name: quiz-engine-build
    volumes:
      - .:/app
      - cargo-cache:/usr/local/cargo/registry
    working_dir: /app
    command: cargo build --release

volumes:
  cargo-cache:
```

### Service summary

| Service | Container | Command | Purpose |
|---|---|---|---|
| `quiz-engine` | `quiz-engine-dev` | `cargo run -- --help` | Interactive development shell |
| `quiz-engine-test` | `quiz-engine-test` | `cargo test && ./scripts/check_coverage.sh` | CI test + coverage gate |
| `quiz-engine-build` | `quiz-engine-build` | `cargo build --release` | Release build only |

**Shared named volume** `cargo-cache` mounts to `/usr/local/cargo/registry` in all services to avoid re-downloading crates.

### Running interactively

```bash
# Build image
docker build -t quiz-engine-rust:latest .

# Run help
docker run --rm quiz-engine-rust:latest --help

# Interactive quiz with persistent DB
docker run -it \
  -v quiz-rust-data:/data \
  -e DATABASE_URL=sqlite:/data/quiz.db \
  quiz-engine-rust:latest quiz --questions 10

# Import from host file
docker run -it \
  -v quiz-rust-data:/data \
  -v "$(pwd)/questions.md:/questions.md" \
  -e DATABASE_URL=sqlite:/data/quiz.db \
  quiz-engine-rust:latest import --file /questions.md

# Compose shortcuts
docker-compose up quiz-engine
docker-compose up quiz-engine-test
docker-compose up quiz-engine-build
```

### Environment variables

| Variable | Default | Description |
|---|---|---|
| `DATABASE_URL` | `sqlite:./quiz_engine.db` | SQLite file path (read by `dotenvy::var("DATABASE_URL")`) |

---

## 10. Architecture Decisions

### Async runtime: Tokio
All I/O — database reads/writes and file I/O — is async. `#[tokio::main]` drives the entire program. All repository methods are `async fn`. There is no synchronous database access.

### ORM vs. Raw SQL: Raw SQL via sqlx
The project uses **raw SQL strings** with sqlx's `query_as::<_, Model>()` and `sqlx::query()` macros — not an ORM. SQL is not compile-time checked via `sqlx::query!` macro (which requires `DATABASE_URL` at compile time and `sqlx prepare`); instead `query_as` with manual binding is used, trading compile-time checking for simpler setup.

### Repository Pattern
Each table has a dedicated repository struct (`QuestionRepo`, `SessionRepo`, `ResponseRepo`) with only associated functions (`impl Repo { pub async fn method(...) }`). No trait abstractions — concrete structs only. Repositories receive `&Pool<Sqlite>` by reference on every call (no stored pool).

### Service Layer
Business logic lives in service structs (`QuizEngine`, `QuizService`, `HistoryService`, `ImportService`) that sit between CLI command handlers and repositories. Services coordinate across multiple repositories.

### `QuizEngine` as Session State
`QuizEngine` is a stateful struct that owns the `session_id`, `questions: Vec<QuizQuestion>`, and `num_correct`. It is created per quiz session, tracks state through `submit_answer()` calls, and is consumed by `finalize()`. Guards prevent double-finalization (`SessionAlreadyFinalized` error).

### Dependency Injection via Pool passing
`sqlx::Pool<Sqlite>` is created once in `main.rs` and passed by value/clone to command handlers, and by reference to repositories. No global state, no `lazy_static`, no `OnceCell`. The pool is cloneable (Arc-backed internally).

### Error Handling
A unified `QuizError` enum (via `thiserror`) covers all error cases: `Database(sqlx::Error)`, `Io(std::io::Error)`, `ParseError`, `NoQuestionsFound`, `NotEnoughQuestions`, `SessionNotFound`, `QuestionNotFound`, `InvalidAnswer`, `InvalidQuestionIndex`, `SessionAlreadyFinalized`, `ConfirmationRequired`, and `Other(String)`. All public functions return `Result<T>` = `std::result::Result<T, QuizError>`.

### Answer Shuffling
`answer_shuffler::shuffle_answers()` takes the original options slice and the correct letter, shuffles with `rand::thread_rng()`, and returns `ShuffleResult { shuffled_options, correct_shuffled_index }`. The correct answer index is tracked through the shuffle so `quiz.rs` can compare without knowing the original order.

### Markdown Format Auto-detection
`parse_markdown_content()` checks for `## Answer Key` substring to select between the GH-200 format parser and the legacy format parser. No configuration or file extension is required.

### Non-repetition Cycle Tracking
`usage_cycle` acts as a logical "round number." After a question is used, its `usage_cycle` increments by 1. `get_random_for_quiz()` always queries `WHERE usage_cycle = MIN(usage_cycle)`, ensuring every question is drawn once before any repeats. This is a pure-SQL approach — no application-level shuffle tracking needed.

### CLI Framework: Clap Derive
All CLI arguments use `#[derive(Parser)]` and `#[derive(Args)]` with `#[arg(...)]` attributes. The `--db` global option is declared with `global = true` so it is accessible from any subcommand. Default values are declared inline: `#[arg(short, long, default_value = "10")]`.

### Test Isolation
All tests use `create_test_pool()` which opens `sqlite::memory:` and runs migrations. Each `#[tokio::test]` gets a fresh in-memory database with no shared state. The `tempfile` crate is used when file-path-based parsing needs to be tested.
