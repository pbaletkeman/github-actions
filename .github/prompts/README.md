# Quiz Engine Prompt Plans

This directory contains GitHub Copilot prompt files used to generate the quiz engine implementations that accompany the **GH-200 GitHub Actions Certification** study material. Each prompt is a detailed architectural blueprint used to scaffold and build a full, working quiz engine in a specific language and technology stack.

- [Quiz Engine Prompt Plans](#quiz-engine-prompt-plans)
  - [Prompts](#prompts)
  - [Purpose](#purpose)
  - [How to Use](#how-to-use)
  - [Common Structure](#common-structure)
  - [Shared Features Across All Implementations](#shared-features-across-all-implementations)
  - [Database Schema](#database-schema)
  - [CLI Command Summary](#cli-command-summary)
  - [Related Directories](#related-directories)

## Prompts

| # | File | Language / Stack | ORM / DB Layer |
|---|------|-----------------|----------------|
| 01 | [01-plan-quizEngine-python.prompt.md](01-plan-quizEngine-python.prompt.md) | Python 3 | SQLite (raw JDBC-style via `sqlite3`) |
| 02 | [02-plan-quizEngine-nodejs.prompt.md](02-plan-quizEngine-nodejs.prompt.md) | Node.js / TypeScript | TypeORM + SQLite |
| 03 | [03-plan-quizEngine-java.prompt.md](03-plan-quizEngine-java.prompt.md) | Java 17 | HikariCP + raw JDBC + SQLite |
| 04 | [04-plan-quizEngine-springboot.prompt.md](04-plan-quizEngine-springboot.prompt.md) | Java / Spring Boot | Spring Data JPA + SQLite |
| 05 | [05-plan-quizEngine-csharp.prompt.md](05-plan-quizEngine-csharp.prompt.md) | C# / .NET 8 | Entity Framework Core + SQLite |
| 06 | [06-plan-quizEngine-dart.prompt.md](06-plan-quizEngine-dart.prompt.md) | Dart 3 | Drift (SQLite) |
| 07 | [07-plan-quizEngine-golang.prompt.md](07-plan-quizEngine-golang.prompt.md) | Go / Golang | go-sqlite3 / GORM + SQLite |
| 08 | [08-plan-quizEngine-rust.prompt.md](08-plan-quizEngine-rust.prompt.md) | Rust | Diesel + SQLite |

## Purpose

These prompts were used with GitHub Copilot agent mode to generate the full source code for each `quiz-engine-*` project found in the `quiz-engine/` directory. Each prompt defines everything needed to produce a production-quality CLI application from scratch:

- Complete directory and file structure
- Docker and docker-compose configuration
- Database schema (all three tables)
- A phased implementation plan with exact code specifications
- Unit test requirements and coverage thresholds (≥ 90%)
- CLI command definitions with example input/output
- Core design decisions and architecture rationale
- Success criteria (functional and non-functional)

## How to Use

Open any prompt file in VS Code and run it in Copilot agent mode using the **Run Prompt** button in the editor toolbar, or reference it directly in a Copilot Chat session:

```
#file:01-plan-quizEngine-python.prompt.md
Implement this plan in the quiz-engine-python directory.
```

## Common Structure

Every prompt follows the same top-level structure:

```
# <Language> Quiz Engine for GH-200 Certification
## System Architecture Overview
  ### Directory Structure
  ### Docker & Containerization
  ### Database Schema
## Implementation Plan
  ### Phase 1: Foundation / Setup & Database
  ### Phase 2: Core Quiz Logic
  ### Phase 3: Data Management (Import, History, Clear)
  ### Phase 4: Unit Testing & Coverage Enforcement
  ### Phase 5: CLI Polish & Deployment  (some prompts)
## Core Design Decisions
## CLI Operations & Examples
## Success Criteria
## Implementation Notes
```

## Shared Features Across All Implementations

All eight quiz engines, regardless of language, implement the same feature set:

| Feature | Detail |
|---------|--------|
| **Non-Repetition Cycling** | Questions are tracked per-cycle so the same question is never repeated within a session. When all questions have been used, the cycle resets. |
| **Answer Shuffling & Concealment** | Answer options are randomised each session; the correct answer is never stored in a position that reveals itself by order. |
| **Interactive Quiz CLI** | `quiz` command with configurable question count, timing, and real-time feedback. |
| **Markdown Import** | `import` command parses structured `.md` files to populate the question database. |
| **Session History** | `history` command displays all past sessions with score, date, and timing. Full session review is supported. |
| **Clear Operations** | `clear` command for removing question data or session history independently. |
| **Docker Support** | Multi-stage `Dockerfile` (build + runtime) and `docker-compose.yml` for every project. |
| **≥ 90% Test Coverage** | Each prompt specifies coverage thresholds enforced at build time. |

## Database Schema

All implementations share the same three-table logical schema, adapted to the idioms of each ORM:

**`questions`** — Stores all imported quiz questions, with `used_in_current_cycle` and `current_cycle` fields that drive non-repetition logic.

**`quiz_sessions`** — One row per quiz attempt; records start/end time, question count, correct count, percentage, and time taken.

**`quiz_responses`** — One row per individual answer within a session; links to the session and the question, recording the user's answer and whether it was correct.

## CLI Command Summary

Each implementation exposes the same four commands:

```
quiz      --questions <n>    Run an interactive quiz
import    <file.md>          Import questions from a Markdown file
history   [--session-id <id>] [--review]  View past quiz sessions
clear     [--questions] [--history]       Remove stored data
```

## Related Directories

| Path | Description |
|------|-------------|
| `quiz-engine/quiz-engine-python/` | Python implementation |
| `quiz-engine/quiz-engine-nodejs/` | Node.js / TypeScript implementation |
| `quiz-engine/quiz-engine-java/` | Plain Java implementation |
| `quiz-engine/quiz-engine-springboot/` | Spring Boot implementation |
| `quiz-engine/quiz-engine-csharp/` | C# / .NET implementation |
| `quiz-engine/quiz-engine-dart/` | Dart implementation |
| `quiz-engine/quiz-engine-golang/` | Go implementation |
| `quiz-engine/quiz-engine-rust/` | Rust implementation |
| `quiz-source material/` | Source Markdown question files used with `import` |
