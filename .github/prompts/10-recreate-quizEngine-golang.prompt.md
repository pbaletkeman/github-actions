# Recreate quiz-engine-golang from Scratch

> **Self-contained prompt.** Everything a developer needs to recreate the `quiz-engine-golang` project from scratch — structure, code, schema, commands, tests, scripts, and Docker — is documented below.

---

## 1. Project Structure

```
quiz-engine-golang/
├── main.go                          # Entry point; calls cmd.Execute()
├── go.mod                           # Module: github.com/pbaletkeman/quiz-engine-golang, Go 1.21
├── go.sum                           # Locked dependency checksums
├── Makefile                         # build / run / test / coverage / clean / docker-build targets
├── Dockerfile                       # Multi-stage CGO build (golang:1.21-alpine → alpine:latest)
├── docker-compose.yml               # Two services: quiz-engine (CLI) and quiz-engine-test (tests)
├── .gitignore                       # Ignores bin/, *.db, coverage.out, coverage.html, quiz-engine[.exe]
├── README.md                        # Quick-start (5 minutes): build → import → quiz → history → docker
├── architecture.md                  # Mermaid diagrams: system overview, sequence, ER, class, data flow
│
├── cmd/                             # Cobra CLI commands (package cmd)
│   ├── root.go                      # Root command "quiz-engine"; global --db flag; registers all subcommands
│   ├── quiz.go                      # `quiz` subcommand: runs interactive quiz session
│   ├── import.go                    # `import` subcommand: parses markdown file → inserts into DB
│   ├── history.go                   # `history` subcommand: lists sessions, review, export JSON/CSV
│   └── clear.go                     # `clear` subcommand: deletes questions and/or history
│
├── internal/
│   ├── models/                      # Plain Go structs — no ORM tags
│   │   ├── question.go              # Question struct (16 fields)
│   │   ├── session.go               # QuizSession struct (7 fields)
│   │   └── response.go              # QuizResponse struct (6 fields)
│   │
│   ├── database/                    # Raw database/sql layer (package database)
│   │   ├── db.go                    # NewDB(), InitSchema() — creates all three tables + indexes
│   │   ├── question.go              # InsertQuestion, GetRandomQuestions, GetQuestionWithAnswer,
│   │   │                            #   GetAllQuestions, CountQuestions, GetCurrentCycle,
│   │   │                            #   MarkQuestionUsed, AdvanceCycleIfExhausted, DeleteAllQuestions
│   │   ├── session.go               # CreateSession, UpdateSession, GetSession, ListSessions
│   │   ├── response.go              # SaveResponse, GetSessionResponses, CountCorrect,
│   │   │                            #   DeleteSessionResponses, DeleteAllResponses
│   │   ├── db_test.go               # Tests: NewDB, InitSchema idempotency, file creation, invalid path
│   │   ├── question_test.go         # Tests: InsertQuestion, GetRandom, GetWithAnswer, Count, Cycle logic
│   │   ├── session_test.go          # Tests: CreateSession, UpdateSession, GetSession, ListSessions
│   │   └── response_test.go         # Tests: SaveResponse, GetSessionResponses, CountCorrect, Delete
│   │
│   ├── engine/                      # Quiz session logic (package engine)
│   │   ├── quiz.go                  # QuizEngine struct + NewQuizEngine, LoadQuestions, SubmitAnswer,
│   │   │                            #   FinalizeQuiz, GetReviewData
│   │   ├── quiz_test.go             # Tests: NewQuizEngine, LoadQuestions, SubmitAnswer, FinalizeQuiz
│   │   ├── shuffler.go              # ShuffleAnswers(options, correctAnswer) → ShuffleResult
│   │   └── shuffler_test.go         # Tests: shuffle length, correct index tracking, all options present
│   │
│   ├── parser/                      # Markdown question parser (package parser)
│   │   ├── markdown.go              # ParseMarkdownFile, ParseMarkdownContent + helpers
│   │   └── markdown_test.go         # Tests: basic, 5-option, scenario, multi-select skip, missing key
│   │
│   ├── service/                     # Service layer (package service)
│   │   ├── config.go                # Config struct + DefaultConfig()
│   │   ├── quiz_service.go          # QuizService struct: NewQuizService, NewEngine, Close
│   │   ├── history_service.go       # ListSessions, GetSessionWithResponses, ExportToJSON, ExportToCSV
│   │   └── service_test.go          # Tests: DefaultConfig, NewQuizService, NewEngine, ListSessions,
│   │                                #   GetSessionWithResponses (found/not-found), Close
│   │
│   └── cli/                         # Terminal output helpers (package cli)
│       ├── display.go               # DisplayQuestion, DisplayResult, DisplayFinalScore,
│       │                            #   DisplayHistoryTable, DisplayReview
│       ├── formatter.go             # PrintSuccess, PrintError, PrintInfo, PrintWarning, PrintHeader
│       └── prompts.go               # GetUserAnswer, AskYesNo, GetTimedAnswer (60-second timeout)
│
├── docs/
│   ├── README.md                    # Full documentation (prerequisites, scripts, CLI, Docker, testing,
│   │                                #   question format, dependencies, architecture reference)
│   └── architecture.md              # Mermaid sequence, class, ER, and data-flow diagrams
│
└── scripts/
    ├── check_coverage.sh            # Bash: run tests, parse total%, fail if < 90%
    └── check_coverage.bat           # Windows CMD: same logic via PowerShell one-liner
│
├── build.sh                         # Bash build wrapper (CGO_ENABLED=1, outputs bin/quiz-engine)
├── build.bat                        # Windows CMD build wrapper (outputs bin\quiz-engine.exe)
├── build.ps1                        # PowerShell build wrapper (sets $env:CGO_ENABLED=1)
├── quiz.sh                          # Bash: check binary exists, run quiz with N questions (default 20)
├── quiz.bat                         # Windows CMD: same
├── quiz.ps1                         # PowerShell: param [int]$Questions=20
├── import.sh                        # Bash: detect file vs dir, call --file or --dir
├── import.bat                       # Windows CMD: same
├── import.ps1                       # PowerShell: param [string]$Path
├── history.sh                       # Bash: run bin/quiz-engine history
├── history.bat                      # Windows CMD: same
└── history.ps1                      # PowerShell: same
```

---

## 2. Language, Runtime, and Dependencies

### Runtime

| Item | Value |
|------|-------|
| Language | Go |
| Go version | `1.21` (minimum; specified in `go.mod`) |
| Module path | `github.com/pbaletkeman/quiz-engine-golang` |
| CGO required | Yes (for `mattn/go-sqlite3`); pure-Go fallback available via `modernc.org/sqlite` |

### Direct Dependencies (`go.mod`)

| Module | Version | Purpose |
|--------|---------|---------|
| `github.com/fatih/color` | `v1.16.0` | Colorized terminal output (green/red/cyan/yellow) |
| `github.com/google/uuid` | `v1.6.0` | Generates UUID session IDs |
| `github.com/mattn/go-sqlite3` | `v1.14.37` | SQLite driver via CGO |
| `github.com/olekukonko/tablewriter` | `v0.0.5` | ASCII table rendering in terminal |
| `github.com/spf13/cobra` | `v1.8.0` | CLI framework (commands, flags, help) |
| `modernc.org/sqlite` | `v1.29.10` | Pure-Go SQLite fallback (no C compiler) |

### Indirect Dependencies

| Module | Version |
|--------|---------|
| `github.com/dustin/go-humanize` | `v1.0.1` |
| `github.com/hashicorp/golang-lru/v2` | `v2.0.7` |
| `github.com/inconshreveable/mousetrap` | `v1.1.0` |
| `github.com/mattn/go-colorable` | `v0.1.13` |
| `github.com/mattn/go-isatty` | `v0.0.20` |
| `github.com/mattn/go-runewidth` | `v0.0.9` |
| `github.com/ncruces/go-strftime` | `v0.1.9` |
| `github.com/remyoudompheng/bigfft` | `v0.0.0-20230129092748-24d4a6f8daec` |
| `github.com/spf13/pflag` | `v1.0.5` |
| `golang.org/x/sys` | `v0.19.0` |
| `modernc.org/gc/v3` | `v3.0.0-20240107210532-573471604cb6` |
| `modernc.org/libc` | `v1.49.3` |
| `modernc.org/mathutil` | `v1.6.0` |
| `modernc.org/memory` | `v1.8.0` |
| `modernc.org/strutil` | `v1.2.0` |
| `modernc.org/token` | `v1.1.0` |

---

## 3. Database Schema

The schema is created in `internal/database/db.go` via `InitSchema()`. All tables use `CREATE TABLE IF NOT EXISTS` (idempotent). The SQLite file is opened with WAL journal mode and a 5-second timeout: `?_journal=WAL&_timeout=5000`.

### Table: `questions`

| Column | Type | Constraints | Notes |
|--------|------|-------------|-------|
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | Internal row ID |
| `question_text` | `TEXT` | `NOT NULL` | Full question text (may include prepended scenario) |
| `option_a` | `TEXT` | `NOT NULL` | Answer choice A |
| `option_b` | `TEXT` | `NOT NULL` | Answer choice B |
| `option_c` | `TEXT` | `NOT NULL` | Answer choice C |
| `option_d` | `TEXT` | `NOT NULL` | Answer choice D |
| `option_e` | `TEXT` | nullable | Optional 5th answer choice |
| `correct_answer` | `TEXT` | `NOT NULL` | Letter of correct option (A–E) |
| `explanation` | `TEXT` | nullable | Explanation from answer key |
| `section` | `TEXT` | nullable | Section/topic name parsed from question header |
| `difficulty` | `TEXT` | nullable | `Easy`, `Medium`, or `Hard` |
| `source_file` | `TEXT` | nullable | Path of the markdown file imported from |
| `created_at` | `TIMESTAMP` | `DEFAULT CURRENT_TIMESTAMP` | Auto-set on insert |
| `usage_cycle` | `INTEGER` | `DEFAULT 1` | Cycle counter for non-repetition tracking |
| `times_used` | `INTEGER` | `DEFAULT 0` | Total times served in a quiz |
| `last_used_at` | `TIMESTAMP` | nullable | Set by `MarkQuestionUsed()` |

**Unique constraint:** `UNIQUE(question_text, correct_answer)` — prevents duplicate imports.

**Indexes:**
- `idx_questions_section ON questions(section)`
- `idx_questions_difficulty ON questions(difficulty)`
- `idx_questions_usage_cycle ON questions(usage_cycle)`

### Table: `quiz_sessions`

| Column | Type | Constraints | Notes |
|--------|------|-------------|-------|
| `session_id` | `TEXT` | `PRIMARY KEY` | UUID generated by `google/uuid` |
| `started_at` | `TIMESTAMP` | `DEFAULT CURRENT_TIMESTAMP` | Session start time |
| `ended_at` | `TIMESTAMP` | nullable | Set when quiz is finalized |
| `num_questions` | `INTEGER` | `NOT NULL` | Number of questions in this session |
| `num_correct` | `INTEGER` | `DEFAULT 0` | Count of correct answers |
| `percentage_correct` | `REAL` | `DEFAULT 0.0` | `(num_correct / num_questions) * 100` |
| `time_taken_seconds` | `INTEGER` | nullable | Total wall-clock time |

**Index:** `idx_sessions_date ON quiz_sessions(started_at DESC)`

### Table: `quiz_responses`

| Column | Type | Constraints | Notes |
|--------|------|-------------|-------|
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | |
| `session_id` | `TEXT` | `NOT NULL`, FK → `quiz_sessions(session_id)` | |
| `question_id` | `INTEGER` | `NOT NULL`, FK → `questions(id)` | |
| `user_answer` | `TEXT` | `NOT NULL` | Letter the user typed (A–E) |
| `is_correct` | `INTEGER` | `DEFAULT 0` | 1 = correct, 0 = wrong (SQLite boolean) |
| `time_taken_seconds` | `INTEGER` | nullable | Per-question time |

**Unique constraint:** `UNIQUE(session_id, question_id)` — one response per question per session.

**Index:** `idx_responses_session ON quiz_responses(session_id)`

### Relationships

```
questions (1) ──────< quiz_responses (many)
quiz_sessions (1) ──────< quiz_responses (many)
```

### Question Cycling Logic

- All questions start at `usage_cycle = 1`.
- `GetRandomQuestions` always queries `WHERE usage_cycle = MIN(usage_cycle)`.
- After finalizing a quiz, `MarkQuestionUsed` sets `last_used_at` on each answered question.
- `AdvanceCycleIfExhausted` checks if any `last_used_at IS NULL` remain in the current cycle; if none, it increments all rows' `usage_cycle` by 1, resetting the pool.

---

## 4. CLI Commands

The binary is called `quiz-engine` (or `quiz-engine.exe` on Windows). Version is `1.0.0`.

### Global Flags (on root command)

| Flag | Default | Description |
|------|---------|-------------|
| `--db` | `./quiz.db` | Path to the SQLite database file |
| `--help` | — | Show help text |
| `--version` | — | Show version (`1.0.0`) |

---

### `quiz` — Take an Interactive Quiz

```
quiz-engine quiz [flags]
```

**Flags:**

| Flag | Short | Default | Description |
|------|-------|---------|-------------|
| `--questions` | `-q` | `20` | Number of questions to serve |
| `--difficulty` | — | `""` (all) | Filter by difficulty: `easy`, `medium`, or `hard` |
| `--section` | — | `""` (all) | Filter by section name |

**Behavior:**
1. Initializes `QuizService` → opens/creates the SQLite DB.
2. Creates a `QuizEngine` with a new UUID session ID.
3. Calls `LoadQuestions()`: fetches N random questions from the current usage cycle, creates the session record, shuffles answer options.
4. For each question: displays the question via `cli.DisplayQuestion`, reads a timed answer (`GetTimedAnswer` — 60-second timeout, defaults to "A" on timeout), calls `SubmitAnswer`.
5. Calls `FinalizeQuiz()`, then `GetReviewData()` and displays a review table and final score table.

**Example invocations:**

```bash
# Default: 20 questions
./bin/quiz-engine quiz

# 10 questions, medium difficulty
./bin/quiz-engine quiz --questions 10 --difficulty medium

# Filter by section, custom DB path
./bin/quiz-engine --db /data/quiz.db quiz --questions 5 --section "Workflow Trigger Events"
```

**Expected output (abbreviated):**

```
Starting Quiz: 10 Questions
============================================================

Question 1 of 10
------------------------------------------------------------
Which trigger event is used to run a workflow on a schedule?

  A) on: workflow_dispatch
  B) on: schedule
  C) on: timer
  D) on: cron

Your answer: B

...

=== Quiz Complete ===
+------------+-------+
|   METRIC   | VALUE |
+------------+-------+
| Questions  | 10    |
| Correct    | 8     |
| Score      | 80.0% |
| Time Taken | 143s  |
+------------+-------+
```

---

### `import` — Import Questions from Markdown

```
quiz-engine import --file <path> [flags]
```

**Flags:**

| Flag | Short | Default | Required | Description |
|------|-------|---------|----------|-------------|
| `--file` | `-f` | `""` | **Yes** | Path to a markdown file to import |

**Note:** The `--file` flag is required; omitting it returns an error: `--file flag is required`.

**Behavior:**
1. Opens/creates the SQLite DB.
2. Calls `parser.ParseMarkdownFile(importFile)` — parses questions and answer key from markdown.
3. Inserts each question with `INSERT OR IGNORE` (duplicates silently skipped via the `UNIQUE(question_text, correct_answer)` constraint).
4. Prints counts of imported and skipped questions.

**Example invocations:**

```bash
./bin/quiz-engine import --file questions.md
./bin/quiz-engine import -f ./quiz-source/gh-200-iteration-9.md
./bin/quiz-engine --db /data/quiz.db import --file questions.md
```

**Expected output:**

```
Imported: 87 questions
Skipped (duplicates): 3 questions
```

---

### `history` — View Past Quiz Sessions

```
quiz-engine history [flags]
```

**Flags:**

| Flag | Default | Description |
|------|---------|-------------|
| `--session-id` | `""` | Session UUID to inspect |
| `--review` | `false` | When combined with `--session-id`, prints response count for that session |
| `--export` | `""` | Export format: `json` or `csv` |

**Behavior (no flags):** Renders a table of all sessions ordered by `started_at DESC`.

**Behavior (`--export json`):** Writes `quiz-history-<timestamp>.json` with full session + response data.

**Behavior (`--export csv`):** Writes `quiz-history-<timestamp>.csv` with one row per response.

**Behavior (`--session-id <id> --review`):** Displays the final score table for that session and prints the response count.

**Example invocations:**

```bash
# List all sessions
./bin/quiz-engine history

# Export to JSON
./bin/quiz-engine history --export json

# Export to CSV
./bin/quiz-engine history --export csv

# Review a specific session
./bin/quiz-engine history --session-id "550e8400-e29b-41d4-a716-446655440000" --review
```

**Expected output (list):**

```
+--------------------------------------+------------------+-----------+---------+-------+------+
|             SESSION ID               |       DATE       | QUESTIONS | CORRECT | SCORE | TIME |
+--------------------------------------+------------------+-----------+---------+-------+------+
| 550e8400-e29b-41d4-a716-446655440000 | 2024-01-15 14:32 | 20        | 17      | 85.0% | 312s |
+--------------------------------------+------------------+-----------+---------+-------+------+
```

---

### `clear` — Remove Data from the Database

```
quiz-engine clear [flags]
```

**Flags:**

| Flag | Default | Description |
|------|---------|-------------|
| `--questions` | `false` | Clear all questions |
| `--history` | `false` | Clear all quiz history and responses |
| `--confirm` | `false` | **Required to actually execute** the clear operation |

**Behavior:** If neither `--questions` nor `--history` is specified, **both** are set to true (clears everything). Without `--confirm`, prints a dry-run warning and does nothing. With `--confirm`, executes the deletes.

**Note:** Responses are deleted before questions to avoid FK constraint issues (responses reference both sessions and questions). The `DELETE FROM` statements do not cascade — the `clear` command deletes responses first when `--history` is true, then questions when `--questions` is true.

**Example invocations:**

```bash
# Dry-run: show what would be cleared
./bin/quiz-engine clear

# Clear everything (dry-run)
./bin/quiz-engine clear --questions --history

# Actually clear all questions
./bin/quiz-engine clear --questions --confirm

# Actually clear history only
./bin/quiz-engine clear --history --confirm

# Clear everything for real
./bin/quiz-engine clear --confirm
```

**Expected output (dry-run):**

```
Would clear: all questions
Would clear: all quiz history and responses
Use --confirm to execute the clear operation.
```

**Expected output (with `--confirm`):**

```
Cleared all quiz responses.
Cleared all questions.
```

---

## 5. Documentation

### `docs/README.md` — Full Documentation

Sections (in order):

1. **Title + TOC** — "Quiz Engine — Go — Full Documentation", nested TOC with anchor links
2. **Intro blurb** — "A command-line quiz engine for GH-200 GitHub Actions certification preparation built with Go 1.21, Cobra CLI, and SQLite. Produces a single statically-linked binary."
3. **Overview / Features** — bulleted list: interactive CLI with shuffled answers, SQLite persistence (mattn CGO + modernc pure-Go), non-repetition cycle tracking, markdown import, session history, colorized output, single binary
4. **Project Structure** — fenced code block showing directory tree
5. **Prerequisites** — table (Go 1.21+, C compiler, optional Docker); subsection "C Compiler Setup" with platform-specific instructions (Windows: MSVC Build Tools or MinGW-w64; macOS: `xcode-select --install`; Linux: `apt install build-essential libsqlite3-dev`)
6. **Installation** — `go mod download`, `CGO_ENABLED=1 go build -o bin/quiz_engine .`, verify with `--help`
7. **Script Reference** — subsections for Build Scripts, Quiz Scripts, Import Scripts, History Scripts; each with platform-specific invocation examples
8. **CLI Commands** — `import`, `quiz`, `history`, `clear`, Global Flags — tables of flags with defaults
9. **Docker Setup** — Building, Running Interactively (with volume mount examples), Docker Compose Services (table), Build Internals
10. **Question File Format** — full example markdown block showing the two-section format (Questions + Answer Key table)
11. **Configuration** — custom DB path via `--db`, environment variable mention
12. **Testing** — `go test ./...`, coverage, `./scripts/check_coverage.sh`, race detection, verbose
13. **Build Notes — CGO Requirement** — cross-compilation table (linux/amd64, linux/arm64, windows/amd64, darwin/arm64) + pure-Go fallback (`CGO_ENABLED=0 go build -tags purego`)
14. **Dependencies** — table of direct dependencies with versions and purposes
15. **Architecture** — one-liner pointing to `architecture.md`

### `docs/architecture.md` / `architecture.md` (root-level)

Both files contain Mermaid diagrams. The root `architecture.md` is the canonical source:

- **System Overview** (`graph TD`) — shows User → cmd/ → engine/service → database → SQLite; also parser → models; markdown files → parser
- **Sequence Diagram** (`sequenceDiagram`) — `quiz` command flow from User through cmd/quiz.go → service → engine → database → SQLite and back
- **ER Diagram** (`erDiagram`) — three tables with fields and relationships
- **Class Diagram** (`classDiagram`) — namespaced blocks for models, engine, service; with relationships
- **Data Flow Diagram** (`flowchart LR`) — import flow (Markdown → parser → cmd/import → SQLite) and quiz flow (SQLite → question.go → shuffler → display → response → DB update)

---

## 6. Question File Formats

The parser in `internal/parser/markdown.go` supports **one format** with two variants based on whether a `**Scenario**:` block is present.

### Format Requirements

- The markdown file must contain a `## Questions` section and a `## Answer Key` section.
- Only questions with `**Answer Type**: one` are imported. Questions with `many` or `none` are silently skipped.
- Each question block starts with `### Question N — Section Name`.
- Options must be on lines matching `- A) text`, `- B) text`, etc. (4 required; E is optional).
- The answer key is a pipe-delimited table: `| Q# | Answer(s) | Explanation | Source | Difficulty |`.

### Format A — Simple Question (no scenario)

```markdown
## Questions

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

### Question 2 — VS Code Extension

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Extension setup and file path requirement

**Question**:
For the GitHub Actions VS Code extension to activate its schema validation and IntelliSense features, workflow files must be located in which directory path?

- A) `workflows/` at the root of the repository
- B) `.github/workflows/` relative to the repository root
- C) `src/.github/actions/` relative to the project source
- D) Any directory ending in `/workflows/`

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | C | `on: schedule` is the correct trigger. `cron` is the value of the `schedule` key, not the trigger name itself. | 05-Workflow-Trigger-Events.md | Easy |
| 2 | B | Workflow files must be in `.github/workflows/` for the extension to activate schema validation and IntelliSense. | 01-GitHub-Actions-VS-Code-Extension.md | Easy |
```

**Parsed result for Question 1:**
- `QuestionText`: `"Which trigger event is used to run a workflow on a recurring time-based schedule?"`
- `OptionA`: `` "`on: timer`" ``, `OptionB`: `` "`on: cron`" ``, `OptionC`: `` "`on: schedule`" ``, `OptionD`: `` "`on: workflow_dispatch`" ``
- `CorrectAnswer`: `"C"`, `Explanation`: `` "`on: schedule` is the correct trigger..." ``, `Section`: `"Workflow Trigger Events"`, `Difficulty`: `"Easy"`

---

### Format B — Question with Scenario

```markdown
## Questions

### Question 1 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: secrets context usage

**Scenario**:
A developer references a third-party action using a branch tag like `uses: external-org/deploy-action@main`
in their workflow. The VS Code extension immediately highlights this line with a warning.

**Question**:
What is the extension warning about and what is the recommended fix?

- A) The action is from an external organization; switch to a first-party GitHub action
- B) Branch references can change at any time; pin the action to a specific commit SHA for reproducibility and security
- C) The action must be referenced by a semver version tag, not a branch name
- D) The `@main` syntax is not valid YAML and will cause a parse error

---

### Question 2 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: context availability

**Scenario**:
A developer is writing a workflow and needs to access the name of the event that triggered it.

**Question**:
Which context expression provides the triggering event name?

- A) `${{ runner.event_name }}`
- B) `${{ github.event_name }}`
- C) `${{ env.GITHUB_EVENT_NAME }}`
- D) `${{ job.event }}`

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | Branch tag references like `@main` are mutable; the extension warns to pin to a full commit SHA for security and reproducibility. | 01-GitHub-Actions-VS-Code-Extension.md | Medium |
| 2 | B | `github.event_name` is the correct context expression. The `GITHUB_EVENT_NAME` environment variable also works but uses a different access pattern. | 02-Contextual-Information.md | Medium |
```

**Parsed result for Question 1 (with scenario):**
- `QuestionText`: scenario text + `"\n\n"` + question text (concatenated)
- The scenario text: `"A developer references a third-party action..."` is prepended to the question text.
- `OptionA`–`OptionD`: the four answer choices
- `CorrectAnswer`: `"B"`, `Section`: `"Contextual Information"`, `Difficulty`: `"Medium"`

---

### Parser Regex Patterns

| Pattern | Regex | Purpose |
|---------|-------|---------|
| Question header | `(?m)^###\s+Question\s+(\d+)\s*[-—–]+\s*(.+)$` | Extracts question number and section name |
| Difficulty | `(?m)^\*\*Difficulty\*\*:\s*(.+)$` | Extracts difficulty string |
| Answer type | `(?m)^\*\*Answer Type\*\*:\s*(.+)$` | Identifies single vs. multi-answer |
| Option line | `(?m)^-\s+([A-E])\)\s+(.+)$` | Extracts option letter and text |
| Answer key row | `^\|\s*(\d+)\s*\|\s*([^|]+?)\s*\|\s*([^|]*?)\s*\|\s*([^|]*?)\s*\|\s*([^|]*?)\s*\|` | Parses answer key table rows |

---

## 7. Unit Test Coverage

### Threshold

**90% minimum** on core packages. Enforced in two places:

1. **`Makefile` `test` target** — runs `go test` with `-coverprofile=coverage_core.out` scoped to:
   - `./internal/database/...`
   - `./internal/engine/...`
   - `./internal/parser/...`
   - `./internal/service/...`

   Then uses `awk` to compare the total percentage: `awk "BEGIN { if (${TOTAL}+0 < 90) { print \"FAIL: coverage below 90%\"; exit 1 } ...`

2. **`scripts/check_coverage.sh`** — standalone script with the same 90% threshold. Runs `go test ./... -coverprofile=coverage.out -covermode=atomic`, parses `go tool cover -func=coverage.out | grep '^total:'`, and exits with code 1 if below threshold.

### Tool

```bash
CGO_ENABLED=1 go test ./... -coverprofile=coverage.out -covermode=atomic
go tool cover -func=coverage.out        # per-function summary
go tool cover -html=coverage.out -o coverage.html  # HTML report
```

### Configuration Locations

| Location | Property / Expression |
|----------|-----------------------|
| `Makefile` (line 17–20, `test` target) | `awk "BEGIN { if (${TOTAL}+0 < 90) { ... exit 1 }"` |
| `scripts/check_coverage.sh` (line 17) | `if awk "BEGIN { exit (${TOTAL} < 90) ? 0 : 1 }"; then` |
| `scripts/check_coverage.bat` (line 19) | `if ($pct -lt 90) { Write-Error ... exit 1 }` |
| `docker-compose.yml` (`quiz-engine-test` service) | Runs `./scripts/check_coverage.sh` as part of test command |

### Test Files Summary

| Package | Test File | Tests |
|---------|-----------|-------|
| `internal/database` | `db_test.go` | `TestNewDB_InMemory`, `TestNewDB_InvalidPath`, `TestInitSchema_Idempotent`, `TestNewDB_CreatesFile` |
| `internal/database` | `question_test.go` | Insert, GetRandom, GetWithAnswer, Count, cycle advance |
| `internal/database` | `session_test.go` | Create, Update, Get, List |
| `internal/database` | `response_test.go` | Save, GetSessionResponses, CountCorrect, Delete |
| `internal/engine` | `quiz_test.go` | NewQuizEngine, LoadQuestions, SubmitAnswer, FinalizeQuiz, GetReviewData |
| `internal/engine` | `shuffler_test.go` | Length preserved, correct index tracked, all options present |
| `internal/parser` | `markdown_test.go` | BasicQuestion, FiveOptions, WithExplanation, WithScenario, MultiSelectSkipped, MissingAnswerKey, ParseFile, Empty |
| `internal/service` | `service_test.go` | DefaultConfig, NewQuizService, InvalidPath, NewEngine, Close, ListSessions (empty + data), GetSessionWithResponses (found + not-found) |

---

## 8. Scripts

All scripts change directory to their own location before executing (`cd "$(dirname "$0")"` / `Set-Location $PSScriptRoot`), so they work correctly when invoked from any directory.

### Build Scripts

| File | Platform | Invoke from project root | Purpose |
|------|----------|--------------------------|---------|
| `build.sh` | Bash / macOS / Linux | `./build.sh` | `mkdir -p bin && CGO_ENABLED=1 go build -o bin/quiz-engine .` |
| `build.bat` | Windows CMD | `build.bat` | `mkdir bin & go build -o bin\quiz-engine.exe .` (**Note:** omits CGO_ENABLED=1; use PowerShell or MSVC prompt for CGO) |
| `build.ps1` | PowerShell | `.\build.ps1` | Sets `$env:CGO_ENABLED = "1"`, then `go build -o bin\quiz-engine.exe .` |

### Quiz Scripts

| File | Platform | Invoke from project root | Argument |
|------|----------|--------------------------|----------|
| `quiz.sh` | Bash | `./quiz.sh [N]` | `N` = number of questions (default: 20) |
| `quiz.bat` | Windows CMD | `quiz.bat [N]` | `N` = number of questions (default: 20) |
| `quiz.ps1` | PowerShell | `.\quiz.ps1 [-Questions N]` | `-Questions` param, default: 20 |

All check that `bin/quiz-engine[.exe]` exists; if not, print error and exit.

### Import Scripts

| File | Platform | Invoke from project root | Argument |
|------|----------|--------------------------|----------|
| `import.sh` | Bash | `./import.sh <file_or_dir>` | File path or directory path |
| `import.bat` | Windows CMD | `import.bat <file_or_dir>` | File path or directory path |
| `import.ps1` | PowerShell | `.\import.ps1 -Path <file_or_dir>` | `-Path` param (required) |

Detect file vs. directory: if directory → `--dir`, else → `--file`.

### History Scripts

| File | Platform | Invoke from project root | Notes |
|------|----------|--------------------------|-------|
| `history.sh` | Bash | `./history.sh` | Runs `./bin/quiz-engine history` |
| `history.bat` | Windows CMD | `history.bat` | Runs `bin\quiz-engine.exe history` |
| `history.ps1` | PowerShell | `.\history.ps1` | Runs `.\bin\quiz-engine.exe history` |

### Coverage Scripts

| File | Platform | Invoke from project root | Notes |
|------|----------|--------------------------|-------|
| `scripts/check_coverage.sh` | Bash | `./scripts/check_coverage.sh` | Runs full test suite; fails (exit 1) if total < 90% |
| `scripts/check_coverage.bat` | Windows CMD | `scripts\check_coverage.bat` | Same logic via PowerShell one-liner invocation |

---

## 9. Docker Setup

### `Dockerfile` (full content)

```dockerfile
# Build stage
FROM golang:1.21-alpine AS builder

WORKDIR /app

RUN apk add --no-cache gcc musl-dev sqlite-dev

COPY go.mod go.sum ./
RUN go mod download

COPY . .
RUN CGO_ENABLED=1 GOOS=linux go build -a -installsuffix cgo -ldflags "-s -w" -o quiz-engine .

# Runtime stage
FROM alpine:latest

WORKDIR /app

RUN apk add --no-cache sqlite-libs

COPY --from=builder /app/quiz-engine .

RUN addgroup -g 1000 gouser && adduser -D -u 1000 -G gouser gouser
RUN chown -R gouser:gouser /app
USER gouser

ENTRYPOINT ["./quiz-engine"]
CMD ["--help"]
```

**Key details:**
- Multi-stage: `golang:1.21-alpine` builder + `alpine:latest` runtime
- CGO enabled in builder via `apk add gcc musl-dev sqlite-dev`
- Binary stripped with `-ldflags "-s -w"` for smaller size
- Runs as non-root user `gouser` (UID 1000)
- Default command is `--help`

### `docker-compose.yml` (full content)

```yaml
version: '3.8'

services:
  quiz-engine:
    build: .
    container_name: quiz-engine-dev
    volumes:
      - ./data:/app/data
    command: ["--help"]
    stdin_open: true
    tty: true

  quiz-engine-test:
    build:
      context: .
      dockerfile: Dockerfile
      target: builder
    container_name: quiz-engine-test
    working_dir: /app
    command: >
      sh -c "CGO_ENABLED=1 go test ./... -coverprofile=coverage.out -covermode=atomic &&
             go tool cover -func=coverage.out &&
             ./scripts/check_coverage.sh"
```

**Service descriptions:**

| Service | Description | Volume | stdin/tty |
|---------|-------------|--------|-----------|
| `quiz-engine` | Interactive CLI container; runs `--help` by default | `./data:/app/data` | Yes (interactive) |
| `quiz-engine-test` | Builds from `builder` stage; runs full test suite + coverage check | None | No |

**Usage:**

```bash
# Build and start interactive CLI
docker-compose up quiz-engine

# Run tests with coverage enforcement
docker-compose up quiz-engine-test

# Build the image only
docker build -t quiz-engine:latest .

# Run interactive quiz with persistent DB
docker run -it -v "$(pwd)/data:/app/data" quiz-engine:latest quiz --questions 10 --db /app/data/quiz.db

# Import questions
docker run -it \
  -v "$(pwd)/data:/app/data" \
  -v "$(pwd)/questions.md:/app/questions.md" \
  quiz-engine:latest import --file /app/questions.md --db /app/data/quiz.db
```

---

## 10. Architecture Decisions

### 1. Repository Pattern via `internal/database` Package

All SQL is written by hand in `internal/database/*.go` using `database/sql` directly — **no ORM**. Each entity has its own file (`question.go`, `session.go`, `response.go`) containing plain functions that accept a `*sql.DB` parameter. This avoids magic, global state, and CGO-incompatible reflection-based ORMs.

### 2. Service Layer (`internal/service`)

A thin service layer sits between the CLI commands and the database package:
- `QuizService` wraps DB initialization and `QuizEngine` creation.
- `history_service.go` provides `ListSessions`, `GetSessionWithResponses`, `ExportToJSON`, `ExportToCSV` — keeping export logic out of the CLI command.
- `Config` / `DefaultConfig()` centralizes configuration with sensible defaults (20 questions, 60 s/question, 3600 s total).

### 3. Engine Package (`internal/engine`)

`QuizEngine` is a stateful struct that holds the session ID, questions slice, responses slice, shuffle data, and DB reference for the lifetime of one quiz session. It orchestrates the quiz loop but does not know about terminal I/O — that belongs to `internal/cli`.

### 4. Answer Shuffling

`engine/shuffler.go::ShuffleAnswers` takes the original options and correct answer letter, produces a shuffled slice, and returns a `ShuffleResult` with:
- `ShuffledOptions` — options in new random order
- `CorrectShuffledIndex` — where the correct answer landed
- `PositionMap` — maps new position → original letter (A/B/C/D/E)

`SubmitAnswer` uses `PositionMap` to translate the user's letter (A–E in shuffled order) back to the original letter before comparing to `CorrectAnswer`.

### 5. Cobra CLI Structure

The `cmd/` package uses [Cobra](https://github.com/spf13/cobra) with:
- `root.go` — registers the persistent `--db` flag and `AddCommand`s all four subcommands.
- Each subcommand file is self-contained: defines its `cobra.Command`, declares local flag variables, and handles all logic in `RunE` (which returns an `error`).
- `main.go` is a one-liner: `cmd.Execute()`.

### 6. Dual SQLite Drivers

`go.mod` includes **both** `mattn/go-sqlite3` (CGO, battle-tested) and `modernc.org/sqlite` (pure Go). `internal/database/db.go` uses `modernc.org/sqlite` (driver name `"sqlite"`) as the active driver because it requires no C compiler. The `mattn/go-sqlite3` package is listed as a dependency but the `Dockerfile` uses `CGO_ENABLED=1` with `gcc` / `musl-dev` on Alpine for the production build.

### 7. Non-Repetition Cycle Tracking

Rather than a random shuffle that could repeat questions, the engine tracks `usage_cycle` and `last_used_at` on each question row. Questions from the current (lowest) cycle are served first. When all questions in a cycle have been used (`last_used_at IS NOT NULL`), all rows are advanced to the next cycle by `AdvanceCycleIfExhausted`. This guarantees every question is seen before any repeats.

### 8. Dependency Injection via Function Parameters

There is no global state and no constructor injection via interfaces. Functions accept concrete `*sql.DB` or struct pointers directly. This keeps the code simple and avoids the overhead of interface indirection for a CLI tool where testability is achieved by passing temporary database files in tests (via `t.TempDir()`).

### 9. Terminal I/O Isolation

The `internal/cli` package contains all terminal concerns:
- `formatter.go` — colored print helpers (`fatih/color`)
- `display.go` — `tablewriter`-based tables for questions, results, history, and review
- `prompts.go` — stdin reading (`bufio.Scanner`) and timed input (`GetTimedAnswer` with goroutine + `time.After`)

This makes `internal/engine` and `internal/service` fully testable without mocking I/O.

### 10. Import-Only Markdown Parser (No Live Editing)

The parser reads a fully-formed markdown file at import time. There is no live watch or incremental update. Once questions are in the database, the markdown file is no longer needed. The `source_file` column on `questions` records provenance for auditing.
