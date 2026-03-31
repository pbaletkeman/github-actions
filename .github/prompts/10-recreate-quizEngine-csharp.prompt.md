# Recreate quiz-engine-csharp — Complete Project Specification

> This document is a self-contained specification for recreating the `quiz-engine-csharp` project from scratch.
> It is part of the Quiz Engine multi-language collection. The C# implementation targets **.NET 8** and uses
> **Entity Framework Core**, **Spectre.Console**, and **System.CommandLine**.

---

## Table of Contents

1. [Project Structure](#1-project-structure)
2. [Language, Runtime, and Dependencies](#2-language-runtime-and-dependencies)
3. [Database Schema](#3-database-schema)
4. [CLI Commands](#4-cli-commands)
5. [Documentation](#5-documentation)
6. [Question File Formats](#6-question-file-formats)
7. [Unit Test Coverage](#7-unit-test-coverage)
8. [Scripts](#8-scripts)
9. [Docker Setup](#9-docker-setup)
10. [Architecture Decisions](#10-architecture-decisions)

---

## 1. Project Structure

The solution root is `quiz-engine-csharp/`. Every file and its purpose is listed below.

```
quiz-engine-csharp/
│
├── QuizEngine.sln                          # Visual Studio solution file (VS 17 format)
│
├── QuizEngine.Entities/                    # Layer 4 (bottom): pure entity models, no external deps
│   ├── QuizEngine.Entities.csproj          # net8.0; depends only on EF Core (for data annotations)
│   ├── Question.cs                         # EF entity mapped to table "questions"
│   ├── QuizSession.cs                      # EF entity mapped to table "quiz_sessions"
│   └── QuizResponse.cs                     # EF entity mapped to table "quiz_responses"
│
├── QuizEngine.Data/                        # Layer 3: EF Core DbContext + repository implementations
│   ├── QuizEngine.Data.csproj              # net8.0; depends on Entities + EF Core + EF Sqlite
│   ├── QuizEngineDbContext.cs              # DbContext; configures all three DbSets, indexes, FK cascades
│   ├── IQuestionRepository.cs              # Repository interface for questions
│   ├── QuestionRepository.cs               # Concrete repo: cycle-aware selection, insert-dedup, mark-used
│   ├── ISessionRepository.cs               # Repository interface for quiz sessions
│   ├── SessionRepository.cs                # Concrete repo: upsert pattern, ordered by StartedAt DESC
│   ├── IResponseRepository.cs              # Repository interface for per-question responses
│   └── ResponseRepository.cs               # Concrete repo: save, get by session, count correct
│
├── QuizEngine.Service/                     # Layer 2: business logic, no CLI or DI framework deps
│   ├── QuizEngine.Service.csproj           # net8.0; depends on Data + Entities + EF Core
│   ├── QuizService.cs                      # Stateful quiz lifecycle (start, submit, finalize)
│   ├── HistoryService.cs                   # Session history retrieval + JSON/CSV export
│   ├── ImportService.cs                    # Orchestrates file/directory import via MarkdownParser
│   ├── MarkdownParser.cs                   # Static parser; detects and handles two MD formats
│   └── AnswerShuffler.cs                   # Fisher-Yates shuffle; preserves correct-answer mapping
│
├── QuizEngine.CLI/                         # Layer 1 (top): entry point, DI wiring, command definitions
│   ├── QuizEngine.CLI.csproj               # net8.0 Exe; depends on Service + Data + Entities
│   ├── Program.cs                          # DI container setup, DB ensure-created, root command wire-up
│   ├── Prompts.cs                          # ConsolePrompts static helpers (GetAnswer, Confirm, etc.)
│   ├── Commands/
│   │   ├── QuizCommand.cs                  # "quiz" subcommand; runs interactive session loop
│   │   ├── ImportCommand.cs                # "import" subcommand; delegates to ImportService
│   │   ├── HistoryCommand.cs               # "history" subcommand; supports sort, export, session detail
│   │   └── ClearCommand.cs                 # "clear" subcommand; bulk-deletes with --confirm guard
│   └── Formatters/
│       └── ConsoleFormatter.cs             # Spectre.Console rendering helpers (tables, panels, rules)
│
├── QuizEngine.Tests/                       # xUnit test project; ≥90% line coverage enforced
│   ├── QuizEngine.Tests.csproj             # net8.0; xunit 2.5.3, coverlet 6.0.2, Moq 4.20.70
│   ├── DatabaseFixture.cs                  # IClassFixture: shared in-memory EF context + BuildSampleQuestion()
│   ├── RepositoryTests.cs                  # Tests for QuestionRepository (insert, dedup, cycle, filter)
│   ├── QuizEngineTests.cs                  # Tests for QuizService (full session lifecycle)
│   ├── AnswerShufflerTests.cs              # Tests for AnswerShuffler (shuffle, identity, letter map)
│   ├── MarkdownParserTests.cs              # Tests for MarkdownParser (both formats, edge cases)
│   └── ServiceTests.cs                     # Integration tests for HistoryService + ImportService + repos
│
├── Dockerfile                              # Multi-stage build: sdk:8.0 builder → runtime:8.0 runner
├── docker-compose.yml                      # Three services: quiz-engine, quiz-engine-test, quiz-engine-build
│
├── build.bat                               # Windows CMD build script
├── build.ps1                               # PowerShell build script (fail-fast, colorized output)
├── build.sh                                # Bash build script (set -e)
│
├── quiz.bat                                # Windows CMD quiz runner (optional question count arg)
├── quiz.ps1                                # PowerShell quiz runner (-Questions param, default 10)
├── quiz.sh                                 # Bash quiz runner ($1 arg, default 10)
│
├── import.bat                              # Windows CMD import (file or directory detection)
├── import.ps1                              # PowerShell import (-Path param, file/dir detection)
├── import.sh                               # Bash import ($1 arg, -d test for directory)
│
├── history.bat                             # Windows CMD history viewer (optional session-id arg)
├── history.ps1                             # PowerShell history viewer (-SessionId param)
├── history.sh                              # Bash history viewer ($1 optional session-id)
│
├── README.md                               # Quick-start "5 minutes" guide; links to docs/
├── architecture.md                         # Top-level architecture diagrams (Mermaid)
├── docs/
│   ├── README.md                           # Full reference documentation (all commands, formats, Docker)
│   └── architecture.md                     # Detailed sequence, class, ER, and data-flow diagrams
│
└── .gitignore                              # Standard .NET gitignore
```

---

## 2. Language, Runtime, and Dependencies

### Runtime

| Item | Value |
|------|-------|
| Language | C# (nullable enabled, implicit usings enabled) |
| Target Framework | `net8.0` |
| SDK Image (Docker) | `mcr.microsoft.com/dotnet/sdk:8.0` |
| Runtime Image (Docker) | `mcr.microsoft.com/dotnet/runtime:8.0` |

### NuGet Packages — by project

#### `QuizEngine.Entities.csproj`

```xml
<PackageReference Include="Microsoft.EntityFrameworkCore" Version="8.0.0" />
```

#### `QuizEngine.Data.csproj`

```xml
<PackageReference Include="Microsoft.EntityFrameworkCore" Version="8.0.0" />
<PackageReference Include="Microsoft.EntityFrameworkCore.Design" Version="8.0.0">
  <IncludeAssets>runtime; build; native; contentfiles; analyzers; buildtransitive</IncludeAssets>
  <PrivateAssets>all</PrivateAssets>
</PackageReference>
<PackageReference Include="Microsoft.EntityFrameworkCore.Sqlite" Version="8.0.0" />
```

#### `QuizEngine.Service.csproj`

```xml
<PackageReference Include="Microsoft.EntityFrameworkCore" Version="8.0.0" />
```

_(No direct NuGet packages beyond EF Core; inherits Data and Entities via ProjectReference.)_

#### `QuizEngine.CLI.csproj`

```xml
<PackageReference Include="Microsoft.EntityFrameworkCore.Design" Version="8.0.0">
  <IncludeAssets>runtime; build; native; contentfiles; analyzers; buildtransitive</IncludeAssets>
  <PrivateAssets>all</PrivateAssets>
</PackageReference>
<PackageReference Include="Microsoft.EntityFrameworkCore.Sqlite" Version="8.0.0" />
<PackageReference Include="Microsoft.Extensions.Hosting" Version="8.0.0" />
<PackageReference Include="Spectre.Console" Version="0.49.1" />
<PackageReference Include="System.CommandLine" Version="2.0.0-beta4.22272.1" />
```

#### `QuizEngine.Tests.csproj`

```xml
<PackageReference Include="coverlet.collector" Version="6.0.2">
  <IncludeAssets>runtime; build; native; contentfiles; analyzers; buildtransitive</IncludeAssets>
  <PrivateAssets>all</PrivateAssets>
</PackageReference>
<PackageReference Include="coverlet.msbuild" Version="6.0.2">
  <IncludeAssets>runtime; build; native; contentfiles; analyzers; buildtransitive</IncludeAssets>
  <PrivateAssets>all</PrivateAssets>
</PackageReference>
<PackageReference Include="Microsoft.EntityFrameworkCore.InMemory" Version="8.0.0" />
<PackageReference Include="Microsoft.NET.Test.Sdk" Version="17.8.0" />
<PackageReference Include="Moq" Version="4.20.70" />
<PackageReference Include="xunit" Version="2.5.3" />
<PackageReference Include="xunit.runner.visualstudio" Version="2.5.3" />
```

### Project References (dependency graph)

```
QuizEngine.CLI
  → QuizEngine.Service
  → QuizEngine.Data
  → QuizEngine.Entities

QuizEngine.Service
  → QuizEngine.Data
  → QuizEngine.Entities

QuizEngine.Data
  → QuizEngine.Entities

QuizEngine.Tests
  → QuizEngine.Service
  → QuizEngine.Data
  → QuizEngine.Entities
```

### Solution File

`QuizEngine.sln` — Visual Studio Solution Format Version 12.00, Visual Studio Version 17.
Contains five C# projects (`{FAE04EC0-301F-11D3-BF4B-00C04F79EFBC}` type GUIDs).
Build configurations: `Debug|Any CPU` and `Release|Any CPU` for all five projects.

---

## 3. Database Schema

The database is SQLite managed by EF Core code-first. There are **no migration files** — the schema is
created at startup via `context.Database.EnsureCreated()`. The schema is derived from entity classes
decorated with Data Annotations.

### Table: `questions`

Defined by `QuizEngine.Entities.Question` (`[Table("questions")]`)

| Column | CLR Type | SQL Type | Constraints |
|--------|----------|----------|-------------|
| `Id` | `int` | `INTEGER` | PK, `AUTOINCREMENT`, `NOT NULL` |
| `QuestionText` | `string` | `TEXT` | `NOT NULL`, `MAXLENGTH(500)` |
| `OptionA` | `string` | `TEXT` | `NOT NULL`, `MAXLENGTH(200)` |
| `OptionB` | `string` | `TEXT` | `NOT NULL`, `MAXLENGTH(200)` |
| `OptionC` | `string` | `TEXT` | `NOT NULL`, `MAXLENGTH(200)` |
| `OptionD` | `string` | `TEXT` | `NOT NULL`, `MAXLENGTH(200)` |
| `OptionE` | `string?` | `TEXT` | nullable, `MAXLENGTH(200)` |
| `CorrectAnswer` | `string` | `TEXT` | `NOT NULL`, `MAXLENGTH(1)` |
| `Explanation` | `string?` | `TEXT` | nullable, `MAXLENGTH(1000)` |
| `Section` | `string?` | `TEXT` | nullable, `MAXLENGTH(100)`, **indexed** |
| `Difficulty` | `string?` | `TEXT` | nullable, `MAXLENGTH(50)`, **indexed** |
| `SourceFile` | `string?` | `TEXT` | nullable, `MAXLENGTH(255)` |
| `usage_cycle` | `int` | `INTEGER` | default `1`, **indexed** |
| `times_used` | `int` | `INTEGER` | default `0` |
| `last_used_at` | `DateTime?` | `TEXT` | nullable |
| `CreatedAt` | `DateTime` | `TEXT` | default `DateTime.UtcNow` |

**Unique constraint:** composite index on `(QuestionText, CorrectAnswer)` — prevents duplicate questions.

**Navigation property:** `ICollection<QuizResponse> Responses`

---

### Table: `quiz_sessions`

Defined by `QuizEngine.Entities.QuizSession` (`[Table("quiz_sessions")]`)

| Column | CLR Type | SQL Type | Constraints |
|--------|----------|----------|-------------|
| `SessionId` | `string` | `TEXT` | PK, `MAXLENGTH(36)`, default `Guid.NewGuid().ToString()` |
| `StartedAt` | `DateTime` | `TEXT` | default `DateTime.UtcNow` |
| `EndedAt` | `DateTime?` | `TEXT` | nullable |
| `NumQuestions` | `int` | `INTEGER` | `NOT NULL` |
| `NumCorrect` | `int` | `INTEGER` | default `0` |
| `PercentageCorrect` | `double` | `REAL` | default `0.0` |
| `TimeTakenSeconds` | `int?` | `INTEGER` | nullable |

**Relationship:** `HasMany(s => s.Responses).WithOne(r => r.Session).HasForeignKey(r => r.SessionId).OnDelete(DeleteBehavior.Cascade)`

**Navigation property:** `ICollection<QuizResponse> Responses`

---

### Table: `quiz_responses`

Defined by `QuizEngine.Entities.QuizResponse` (`[Table("quiz_responses")]`)

| Column | CLR Type | SQL Type | Constraints |
|--------|----------|----------|-------------|
| `Id` | `int` | `INTEGER` | PK, `AUTOINCREMENT`, `NOT NULL` |
| `SessionId` | `string` | `TEXT` | `NOT NULL`, `MAXLENGTH(36)`, FK → `quiz_sessions.SessionId` |
| `QuestionId` | `int` | `INTEGER` | `NOT NULL`, FK → `questions.Id` |
| `UserAnswer` | `string?` | `TEXT` | nullable, `MAXLENGTH(1)` |
| `IsCorrect` | `int` | `INTEGER` | default `0` (0 = false, 1 = true) |
| `TimeTakenSeconds` | `int?` | `INTEGER` | nullable |

**FK Behaviors:**
- `SessionId` → `quiz_sessions.SessionId` : `OnDelete(DeleteBehavior.Cascade)` (configured on QuizSession side)
- `QuestionId` → `questions.Id` : `OnDelete(DeleteBehavior.Restrict)` (configured on QuizResponse side)

**Navigation properties:** `QuizSession Session`, `Question Question`

---

### DbContext: `QuizEngineDbContext`

```csharp
public DbSet<Question> Questions { get; set; }
public DbSet<QuizSession> QuizSessions { get; set; }
public DbSet<QuizResponse> QuizResponses { get; set; }
```

Configured in `OnModelCreating` (fluent API supplements data annotations):
- `Questions`: indexes on `Section`, `Difficulty`, `UsageCycle`, and unique composite on `(QuestionText, CorrectAnswer)`
- `QuizSession.Responses`: cascade-delete relationship
- `QuizResponse.Question`: restrict-delete relationship

---

## 4. CLI Commands

The entry point is `QuizEngine.CLI`. The root command description is
**"GitHub Actions Quiz Engine - GH-200 Certification Prep"**.

Database path is resolved from environment variable `QUIZ_DB_PATH` (default: `"quiz.db"` in CWD).
DI is wired via `Microsoft.Extensions.DependencyInjection` (`ServiceCollection`).

---

### `quiz` — Interactive quiz session

```bash
dotnet run --project QuizEngine.CLI -- quiz
dotnet run --project QuizEngine.CLI -- quiz --questions 20
dotnet run --project QuizEngine.CLI -- quiz -n 20
dotnet run --project QuizEngine.CLI -- quiz --difficulty easy
dotnet run --project QuizEngine.CLI -- quiz --section "GitHub Actions"
dotnet run --project QuizEngine.CLI -- quiz --no-explanation
dotnet run --project QuizEngine.CLI -- quiz --no-shuffle
```

| Option | Alias | Type | Default | Description |
|--------|-------|------|---------|-------------|
| `--questions` | `-n` | `int` | `10` | Number of questions per session |
| `--difficulty` | — | `string?` | _(all)_ | Filter by `easy`, `medium`, or `hard` |
| `--section` | — | `string?` | _(all)_ | Filter by topic/section name |
| `--no-explanation` | — | `bool` (flag) | `false` | Skip answer explanations after quiz |
| `--no-shuffle` | — | `bool` (flag) | `false` | Keep answer options in original order |

**Behavior:**
1. Clears the terminal (ignores failure if non-interactive)
2. Renders ASCII art header via `FigletText("Quiz Engine")`
3. Calls `QuizService.StartQuizAsync()` — throws `InvalidOperationException` if no questions exist
4. Iterates all questions; user presses A–E or ENTER to skip
5. After all answers are collected, displays results section
6. Per-question result shows ✓/✗ and (optionally) the explanation
7. Calls `QuizService.FinalizeAsync()` and renders summary table with Session ID, score, time

**Expected output (summary table columns):** Session ID, Questions, Correct, Incorrect, Score (colored green ≥80 / yellow ≥60 / red <60), Time Taken

---

### `import` — Import questions from Markdown

```bash
dotnet run --project QuizEngine.CLI -- import --file questions.md
dotnet run --project QuizEngine.CLI -- import -f questions.md
dotnet run --project QuizEngine.CLI -- import --dir ./questions/
dotnet run --project QuizEngine.CLI -- import -d ./questions/
```

| Option | Alias | Type | Description |
|--------|-------|------|-------------|
| `--file` | `-f` | `string?` | Path to a single Markdown file |
| `--dir` | `-d` | `string?` | Path to a directory; imports all `.md` files recursively |

**Behavior:**
- Requires at least one of `--file` or `--dir`; prints error if neither provided
- Calls `ImportService.ImportFromFileAsync()` or `ImportService.ImportFromDirectoryAsync()`
- Reports: `✓ Imported: N questions, Skipped (duplicates): M`
- Raises `FileNotFoundException` / `DirectoryNotFoundException` on bad paths (shown as red error)

---

### `history` — View past quiz sessions

```bash
dotnet run --project QuizEngine.CLI -- history
dotnet run --project QuizEngine.CLI -- history --count 5
dotnet run --project QuizEngine.CLI -- history -n 5
dotnet run --project QuizEngine.CLI -- history --sort score --order asc
dotnet run --project QuizEngine.CLI -- history --sort questions --order desc
dotnet run --project QuizEngine.CLI -- history --sort time
dotnet run --project QuizEngine.CLI -- history --session-id beca2997
dotnet run --project QuizEngine.CLI -- history --export json
dotnet run --project QuizEngine.CLI -- history --export csv
```

| Option | Alias | Type | Default | Description |
|--------|-------|------|---------|-------------|
| `--count` | `-n` | `int` | `10` | Number of recent sessions to show |
| `--session-id` | — | `string?` | — | Show detail view for one session (prefix matching supported) |
| `--sort` | — | `string` | `"date"` | Sort field: `date`, `score`, `questions`, or `time` |
| `--order` | — | `string` | `"desc"` | Sort direction: `asc` or `desc` |
| `--export` | — | `string?` | — | Export all sessions to `json` or `csv` |

**Session list table columns:** #, Session ID, Date, Questions, Correct, Score, Time

**Session detail output:** Session ID, Date, Score line, then per-response table with Q#, Question (truncated at 50 chars), Your Answer, Result (✓/✗)

**Export filenames:** `quiz-history-<yyyyMMddHHmmss>.json` or `quiz-history-<yyyyMMddHHmmss>.csv`

**JSON export structure:**
```json
[
  {
    "session_id": "...",
    "date": "...",
    "score": 8,
    "total_questions": 10,
    "percentage": 80.0,
    "responses": [
      { "question_id": 42, "selected_answer": "B", "was_correct": true }
    ]
  }
]
```

**CSV export columns:** `session_id,date,score,total_questions,percentage,question_id,selected_answer,was_correct`

---

### `clear` — Delete stored data

```bash
dotnet run --project QuizEngine.CLI -- clear --questions --confirm
dotnet run --project QuizEngine.CLI -- clear --history --confirm
dotnet run --project QuizEngine.CLI -- clear --all --confirm
```

| Option | Type | Description |
|--------|------|-------------|
| `--questions` | `bool` (flag) | Delete all questions (and their associated responses) |
| `--history` | `bool` (flag) | Delete all sessions and responses |
| `--all` | `bool` (flag) | Delete everything (equivalent to `--questions --history`) |
| `--confirm` | `bool` (flag) | **Required** to execute — prevents accidental deletion |

**Behavior without `--confirm`:** prints red warning and returns without deleting.
**Order of deletion** (to respect FKs): `QuizResponses` first, then `QuizSessions` and/or `Questions`.
Uses `ExecuteDeleteAsync()` (EF Core bulk delete).

---

## 5. Documentation

### `docs/README.md` — Full Reference Documentation

**Heading structure:**

```
# Quiz Engine — C# / .NET 8 — Full Documentation
  ## Overview
    ### Features
  ## Project Structure
  ## Prerequisites
  ## Installation
  ## Script Reference
    ### Build Scripts
      #### build.bat (Windows CMD)
      #### build.ps1 (PowerShell)
      #### build.sh (Bash / macOS / Linux / WSL)
    ### Quiz Scripts
      #### quiz.bat (Windows CMD)
      #### quiz.ps1 (PowerShell)
      #### quiz.sh (Bash)
    ### Import Scripts
      #### import.bat (Windows CMD)
      #### import.ps1 (PowerShell)
      #### import.sh (Bash)
    ### History Scripts
      #### history.bat / history.ps1 / history.sh
  ## CLI Commands
    ### quiz — Take a quiz
    ### import — Import questions from Markdown
    ### history — View past sessions
    ### clear — Delete stored data
  ## Question File Format
    ### Format 1 — Simple (recommended for new files)
    ### Format 2 — Answer-Key (bulk exam files)
  ## Docker Setup
    ### Building the Image
    ### Running Interactively
    ### Environment Variables
    ### Docker Compose
  ## Configuration
    ### Database Path
  ## Testing
  ## Dependencies
  ## Architecture
    ### Repository Pattern
    ### Cycle-Aware Question Selection
    ### Answer Shuffling
    ### Layer Dependency Graph
```

**Prerequisites table:**

| Tool | Version | Download |
|------|---------|----------|
| .NET SDK | 8.0+ | https://dotnet.microsoft.com/download/dotnet/8 |
| Docker (optional) | 20.10+ | https://docs.docker.com/get-docker/ |

**Dependencies table (from docs/README.md):**

| Package | Version | Purpose |
|---------|---------|---------|
| `Microsoft.EntityFrameworkCore` | 8.0.0 | ORM |
| `Microsoft.EntityFrameworkCore.Sqlite` | 8.0.0 | SQLite EF provider |
| `Spectre.Console` | 0.49.1 | Rich terminal output |
| `System.CommandLine` | 2.0.0-beta4 | CLI argument parsing |
| `xUnit` | 2.9.3 | Test framework |
| `coverlet` | 6.0.2 | Code coverage |
| `Moq` | 4.20.70 | Mocking for tests |

**Layer Dependency Graph (from docs/README.md):**

```
QuizEngine.CLI
     ↓
QuizEngine.Service
     ↓
QuizEngine.Data  ←→  QuizEngine.Entities
     ↓
   SQLite
```

### `docs/architecture.md` — Detailed Architecture Diagrams

Contains four Mermaid diagrams:
1. **Sequence Diagram** — `quiz` command flow through CLI → Service → Repository → DB → Spectre output
2. **Class Diagram** — All entities, interfaces, and service/repository classes with typed members
3. **ER Diagram** — Three tables (`Questions`, `QuizSessions`, `QuizResponses`) with FK relationships
4. **Data Flow Diagram** — Flowchart from User input through each command to SQLite and back

### `README.md` — Quick-start Guide

Sections: "Get Started in 5 Minutes" with subsections: Prerequisites, 1. Build, 2. Import Questions,
3. Take a Quiz, 4. View History, 5. Docker.
Links to `docs/README.md` (full docs) and `docs/architecture.md`.

### `architecture.md` — Top-level Architecture Reference

Contains four Mermaid diagrams identical in purpose to `docs/architecture.md` but with slightly
different phrasing. Covers: 1000ft system graph, sequence diagram, ER diagram, class diagram,
data flow diagram.

---

## 6. Question File Formats

`MarkdownParser` (static class) auto-detects which format a file uses. Detection logic:

- **Format 2 (Answer-Key)** is identified when the content matches both:
  - `^###\s+Question\s+\d+\s+—` (Multiline)
  - `^\*\*Difficulty\*\*:` (Multiline)
- Otherwise **Format 1 (Simple)** is assumed.

Both formats support optional `OptionE`.

---

### Format 1 — Simple (recommended for new files)

**Detection:** `## Question` heading + `**Q: ...**` pattern + `**Answer: X**`

**Regex patterns used:**
- Question text: `^\*\*Q:\s*(.+?)\*\*\s*$` (Multiline)
- Answer: `\*\*Answer:\s*([A-E])\*\*` (Multiline)
- Explanation: `\*\*Explanation:\*\*\s*(.+?)(?=\n\n|\n##|\z)` (Singleline)
- Section: `(?:^|\n)Section:\s*(.+?)(?:\n|$)` (Multiline)
- Difficulty: `(?:^|\n)Difficulty:\s*(.+?)(?:\n|$)` (Multiline)
- Options: `^-\s+([A-E])\)\s+(.+)$` (Multiline)

**Complete sample — Question 1 (from MarkdownParserTests.cs):**

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

**Complete sample — Question 2 (from MarkdownParserTests.cs):**

```markdown
## Question 2

**Q: What is GitHub Actions?**

- A) A database service
- B) A CI/CD platform
- C) A version control system
- D) A container registry

**Answer: B**

Section: Workflows
Difficulty: medium
```

**Five-option example (from MarkdownParserTests.cs):**

```markdown
## Q5

**Q: Which are valid GitHub Actions events?**

- A) push
- B) pull_request
- C) schedule
- D) workflow_dispatch
- E) all of the above

**Answer: E**
```

**Parsed field mapping:**

| Markdown element | Entity property |
|-----------------|-----------------|
| `**Q: ...**` | `QuestionText` |
| `- A) ...` through `- E) ...` | `OptionA`–`OptionE` |
| `**Answer: X**` | `CorrectAnswer` |
| `**Explanation:** ...` | `Explanation` |
| `Section: ...` | `Section` |
| `Difficulty: ...` | `Difficulty` |

---

### Format 2 — Answer-Key (bulk exam files)

**Detection:** `### Question N — Topic` heading + `**Difficulty**:` field

**Structure:** Questions section followed by a separate Answer Key Markdown table at the end of the file.

**Regex patterns used:**
- Question header: `^###\s+Question\s+\d+\s+—\s+(.+?)\s*$` (Multiline)
- Difficulty: `^\*\*Difficulty\*\*:\s*(.+?)\s*$` (Multiline)
- Topic: `^\*\*Topic\*\*:\s*(.+?)\s*$` (Multiline)
- Question text: `^\*\*Question\*\*:\s*\n(.+?)(?=\n-\s+[A-E]\)|$)` (Multiline|Singleline)
- Options: `^-\s+([A-E])\)\s+(.+)$` (Multiline)
- Answer key rows: `^\|\s*(\d+)\s*\|\s*([A-E,\s]+?)\s*\|` (Multiline)

**Complete sample — Question 1 (from docs/README.md):**

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

**Complete sample — Question 2 (from docs/README.md):**

```markdown
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
```

**Answer Key Table (follows a `## Answer Key` heading):**

```markdown
## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | C | `on: schedule` is the correct trigger. `cron` is the value of the `schedule` key, not the trigger name itself. | 05-Workflow-Trigger-Events.md | Easy |
| 2 | A, B, D | `secrets` is available in `steps[*].env`, `steps[*].with`, and `steps[*].run`. It is NOT available in `strategy.matrix`. | 02-Contextual-Information.md | Medium |
```

**Answer Key table columns:** `Q#`, `Answer(s)`, `Explanation`, `Source`, `Difficulty`

**Parsed field mapping:**

| Markdown element | Entity property |
|-----------------|-----------------|
| `**Question**:\n...` | `QuestionText` |
| `- A) ...` through `- E) ...` | `OptionA`–`OptionE` |
| Answer key row column 2 (first letter only for multi-answer) | `CorrectAnswer` |
| Answer key row column 3 | `Explanation` |
| `**Topic**:` | `Section` |
| `**Difficulty**:` | `Difficulty` |

**Important parser note:** For multi-answer questions (e.g. `A, B, D`), only the **first letter** is
stored as `CorrectAnswer`. This is a current limitation of the single-answer model.

---

## 7. Unit Test Coverage

### Threshold

| Property | Value |
|----------|-------|
| **Enforced threshold** | **90%** |
| **Threshold type** | **line** |
| **Tool** | **coverlet** (MSBuild integration via `coverlet.msbuild` v6.0.2) |
| **Configuration location** | `docker-compose.yml` → `quiz-engine-test` service `command` |

### Coverage command (canonical form from docker-compose.yml)

```bash
dotnet test QuizEngine.Tests \
  --configuration Release \
  /p:CollectCoverage=true \
  /p:CoverletOutputFormat=lcov \
  /p:CoverletOutput=./coverage/ \
  /p:Threshold=90 \
  /p:ThresholdType=line \
  /p:ExcludeByFile="**/Program.cs;**/Migrations/**"
```

The same command can be run directly from the project root on any platform.

### Excluded from coverage

- `**/Program.cs` — CLI entry point (DI wiring, hard to unit test)
- `**/Migrations/**` — EF Core migration files (none currently present; future-proofed)

### Test files and what they cover

| File | Class Under Test | Notable Scenarios |
|------|-----------------|-------------------|
| `DatabaseFixture.cs` | Shared test infrastructure | `IClassFixture<DatabaseFixture>`, `BuildSampleQuestion(n, correctAnswer)` factory |
| `RepositoryTests.cs` | `QuestionRepository` | Insert, dedup, cycle advance, filter by difficulty/section, `CheckAnswer` |
| `QuizEngineTests.cs` | `QuizService` | Full lifecycle: start→submit→finalize, error cases, zero-correct scenario |
| `AnswerShufflerTests.cs` | `AnswerShuffler` | Fisher-Yates determinism, 4- and 5-option variants, letter map, `Identity()` |
| `MarkdownParserTests.cs` | `MarkdownParser` | Both formats, empty input, missing answer, 5-option question, file I/O |
| `ServiceTests.cs` | `HistoryService`, `ImportService`, `SessionRepository`, `ResponseRepository` | Sort, upsert, export, import from file and directory |

### In-memory database pattern

Tests use `Microsoft.EntityFrameworkCore.InMemory` with `Guid.NewGuid().ToString()` as the database
name to guarantee test isolation. Each test class creates its own isolated context.

---

## 8. Scripts

All scripts must be run from the project root (`quiz-engine-csharp/`). Each script `cd`s to its own
directory first (`$PSScriptRoot`, `%~dp0`, `$(dirname "$0")`), so they can also be invoked from any
working directory.

---

### Build Scripts

#### `build.sh` — Bash (Linux / macOS / WSL)

**Invoke:** `./build.sh`

```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine C# - Build ==="
echo "Building solution..."
dotnet build QuizEngine.sln
echo "Build successful!"
```

- `set -e`: exits immediately on any error
- Runs `dotnet build QuizEngine.sln` (Debug by default)

#### `build.ps1` — PowerShell 5.1+ / PowerShell Core 7+

**Invoke:** `.\build.ps1`

```powershell
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine C# - Build ===" -ForegroundColor Cyan
Write-Host "Building solution..." -ForegroundColor Yellow
dotnet build QuizEngine.sln
Write-Host "Build successful!" -ForegroundColor Green
```

#### `build.bat` — Windows CMD

**Invoke:** `build.bat`

```bat
@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine C# - Build ===
echo Building solution...
dotnet build QuizEngine.sln
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b %ERRORLEVEL%
)
echo Build successful!
```

---

### Quiz Scripts

#### `quiz.sh` — Bash

**Invoke:** `./quiz.sh` or `./quiz.sh 20`

```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine C# - Start Quiz ==="
QUESTIONS=${1:-10}
echo "Starting quiz with $QUESTIONS questions..."
dotnet run --project QuizEngine.CLI -- quiz --questions $QUESTIONS
```

- `$1` = question count; default `10` via `${1:-10}`

#### `quiz.ps1` — PowerShell

**Invoke:** `.\quiz.ps1` or `.\quiz.ps1 -Questions 20`

```powershell
param(
    [int]$Questions = 10
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine C# - Start Quiz ===" -ForegroundColor Cyan
Write-Host "Starting quiz with $Questions questions..." -ForegroundColor Yellow
dotnet run --project QuizEngine.CLI -- quiz --questions $Questions
```

#### `quiz.bat` — Windows CMD

**Invoke:** `quiz.bat` or `quiz.bat 20`

```bat
@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine C# - Start Quiz ===
if "%~1"=="" (
    echo Starting quiz with 10 questions (default)...
    dotnet run --project QuizEngine.CLI -- quiz
) else (
    echo Starting quiz with %~1 questions...
    dotnet run --project QuizEngine.CLI -- quiz --questions %~1
)
```

---

### Import Scripts

#### `import.sh` — Bash

**Invoke:** `./import.sh`, `./import.sh questions.md`, `./import.sh ./questions/`

```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine C# - Import Questions ==="
if [ -z "$1" ]; then
    echo "Usage: ./import.sh <file_or_directory>"
    echo "Example: ./import.sh questions.md"
    echo "No path specified. Importing from current directory..."
    dotnet run --project QuizEngine.CLI -- import --dir .
elif [ -d "$1" ]; then
    echo "Importing from directory: $1"
    dotnet run --project QuizEngine.CLI -- import --dir "$1"
else
    echo "Importing from file: $1"
    dotnet run --project QuizEngine.CLI -- import --file "$1"
fi
```

#### `import.ps1` — PowerShell

**Invoke:** `.\import.ps1`, `.\import.ps1 -Path questions.md`, `.\import.ps1 -Path .\questions\`

```powershell
param(
    [string]$Path = ""
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine C# - Import Questions ===" -ForegroundColor Cyan
if ($Path -eq "") {
    Write-Host "No path specified. Usage: .\import.ps1 -Path <file_or_directory>" -ForegroundColor Yellow
    Write-Host "Importing from current directory..." -ForegroundColor Yellow
    dotnet run --project QuizEngine.CLI -- import --dir .
} elseif (Test-Path $Path -PathType Container) {
    Write-Host "Importing from directory: $Path" -ForegroundColor Yellow
    dotnet run --project QuizEngine.CLI -- import --dir $Path
} else {
    Write-Host "Importing from file: $Path" -ForegroundColor Yellow
    dotnet run --project QuizEngine.CLI -- import --file $Path
}
```

#### `import.bat` — Windows CMD

**Invoke:** `import.bat`, `import.bat questions.md`, `import.bat .\questions\`

```bat
@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine C# - Import Questions ===
echo Usage: import.bat [file_or_directory]
echo   Example: import.bat questions.md
echo   Example: import.bat .\questions\
echo.
if "%~1"=="" (
    echo No path specified. Importing from current directory...
    dotnet run --project QuizEngine.CLI -- import --dir .
) else (
    if exist "%~1\" (
        dotnet run --project QuizEngine.CLI -- import --dir "%~1"
    ) else (
        dotnet run --project QuizEngine.CLI -- import --file "%~1"
    )
)
```

---

### History Scripts

#### `history.sh` — Bash

**Invoke:** `./history.sh` or `./history.sh <session-id-prefix>`

```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine C# - View History ==="
if [ -z "$1" ]; then
    echo "Showing all sessions..."
    dotnet run --project QuizEngine.CLI -- history
else
    echo "Showing session: $1"
    dotnet run --project QuizEngine.CLI -- history --session-id "$1"
fi
```

#### `history.ps1` — PowerShell

**Invoke:** `.\history.ps1` or `.\history.ps1 -SessionId <prefix>`

```powershell
param(
    [string]$SessionId = ""
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine C# - View History ===" -ForegroundColor Cyan
if ($SessionId -eq "") {
    Write-Host "Showing all sessions..." -ForegroundColor Yellow
    dotnet run --project QuizEngine.CLI -- history
} else {
    Write-Host "Showing session: $SessionId" -ForegroundColor Yellow
    dotnet run --project QuizEngine.CLI -- history --session-id $SessionId
}
```

#### `history.bat` — Windows CMD

**Invoke:** `history.bat` or `history.bat <session-id-prefix>`

```bat
@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine C# - View History ===
if "%~1"=="" (
    echo Showing all sessions...
    dotnet run --project QuizEngine.CLI -- history
) else (
    echo Showing session: %~1
    dotnet run --project QuizEngine.CLI -- history --session-id "%~1"
)
```

---

## 9. Docker Setup

### `Dockerfile` — Full Content

```dockerfile
# Build stage
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS builder

WORKDIR /app

# Copy solution and project files first for layer caching
COPY QuizEngine.sln .
COPY QuizEngine.Entities/QuizEngine.Entities.csproj QuizEngine.Entities/
COPY QuizEngine.Data/QuizEngine.Data.csproj QuizEngine.Data/
COPY QuizEngine.Service/QuizEngine.Service.csproj QuizEngine.Service/
COPY QuizEngine.CLI/QuizEngine.CLI.csproj QuizEngine.CLI/
COPY QuizEngine.Tests/QuizEngine.Tests.csproj QuizEngine.Tests/

# Restore dependencies
RUN dotnet restore

# Copy all source files
COPY . .

# Build in Release mode
RUN dotnet build -c Release --no-restore

# Publish CLI project
RUN dotnet publish QuizEngine.CLI/QuizEngine.CLI.csproj -c Release -o /app/publish --no-build

# Runtime stage
FROM mcr.microsoft.com/dotnet/runtime:8.0 AS runtime

WORKDIR /app

# Copy published output
COPY --from=builder /app/publish .

# Create non-root user for security
RUN useradd -m -u 1000 dotnetuser && \
    mkdir -p /data && \
    chown -R dotnetuser:dotnetuser /app /data

USER dotnetuser

# Store DB in a writable data directory
ENV QUIZ_DB_PATH=/data/quiz.db

VOLUME ["/data"]

ENTRYPOINT ["dotnet", "QuizEngine.CLI.dll"]
CMD ["--help"]
```

**Key design decisions in Dockerfile:**
- **Layer-caching optimization:** `.csproj` files are copied and `dotnet restore` runs before source is copied — only re-runs restore when project file dependencies change
- **Two-stage build:** `builder` stage uses full SDK; `runtime` stage uses minimal runtime image (~200MB vs ~800MB)
- **Non-root user:** `dotnetuser` with UID 1000 for security
- **Data volume:** `/data` directory owned by `dotnetuser`; `QUIZ_DB_PATH=/data/quiz.db` set in ENV
- **Default CMD:** `["--help"]` — shows usage when run without arguments

---

### `docker-compose.yml` — Full Content

```yaml
version: '3.8'

services:
  quiz-engine:
    build: .
    container_name: quiz-engine-dev
    volumes:
      - quiz-data:/data
    environment:
      - DOTNET_ENVIRONMENT=Development
      - QUIZ_DB_PATH=/data/quiz.db
    stdin_open: true
    tty: true
    command: ["--help"]

  quiz-engine-test:
    build:
      context: .
      target: builder
    container_name: quiz-engine-test
    volumes:
      - .:/app
    working_dir: /app
    command: >
      dotnet test QuizEngine.Tests
        --configuration Release
        /p:CollectCoverage=true
        /p:CoverletOutputFormat=lcov
        /p:CoverletOutput=./coverage/
        /p:Threshold=90
        /p:ThresholdType=line
        /p:ExcludeByFile="**/Program.cs;**/Migrations/**"
    environment:
      - DOTNET_ENVIRONMENT=Test

  quiz-engine-build:
    build:
      context: .
      target: builder
    container_name: quiz-engine-build
    volumes:
      - .:/app
    working_dir: /app
    command: dotnet build -c Release

volumes:
  quiz-data:
    driver: local
```

### Service Definitions

| Service | Stage | Purpose | Volume | Environment |
|---------|-------|---------|--------|-------------|
| `quiz-engine` | `runtime` (full build) | Interactive CLI app | `quiz-data:/data` (named volume) | `DOTNET_ENVIRONMENT=Development`, `QUIZ_DB_PATH=/data/quiz.db` |
| `quiz-engine-test` | `builder` (SDK) | Run tests + coverage enforcement | `.:/app` (bind mount) | `DOTNET_ENVIRONMENT=Test` |
| `quiz-engine-build` | `builder` (SDK) | Build only; exits when done | `.:/app` (bind mount) | — |

### Named Volumes

| Volume | Driver | Purpose |
|--------|--------|---------|
| `quiz-data` | `local` | Persistent SQLite database across container restarts |

### Environment Variables

| Variable | Default (in image) | Default (in compose) | Description |
|----------|-------------------|---------------------|-------------|
| `QUIZ_DB_PATH` | `/data/quiz.db` | `/data/quiz.db` | Full path to SQLite database file |
| `DOTNET_ENVIRONMENT` | `Production` | `Development` / `Test` | .NET runtime environment name |

### Docker usage examples

```bash
# Build image
docker build -t quiz-engine:latest .

# Verify (shows help)
docker run --rm quiz-engine:latest --help

# Interactive quiz (persistent data)
docker run -it -v quiz-data:/data quiz-engine:latest quiz --questions 10

# Import a local markdown file into the container
docker run -it -v quiz-data:/data -v "$(pwd)/questions.md:/tmp/questions.md" \
  quiz-engine:latest import --file /tmp/questions.md

# View history
docker run -it -v quiz-data:/data quiz-engine:latest history

# Run with Compose (interactive)
docker-compose run --rm quiz-engine quiz --questions 10

# Run tests with coverage enforcement
docker-compose up quiz-engine-test

# Build only
docker-compose up quiz-engine-build
```

---

## 10. Architecture Decisions

### Four-Layer Project Structure

The solution is split into four projects with a strict downward dependency direction:

```
QuizEngine.CLI  (top: presentation, DI wiring)
       ↓
QuizEngine.Service  (business logic, no UI deps)
       ↓
QuizEngine.Data  (EF Core repositories)
       ↓
QuizEngine.Entities  (pure POCO entities, minimal deps)
```

This separation ensures:
- Entities have no framework lock-in (only EF data annotation attributes)
- Service layer can be tested independently of CLI
- CLI is thin: it only wires DI and delegates to services

---

### Repository Pattern

All database access is hidden behind three interfaces:

| Interface | Concrete | Responsibilities |
|-----------|----------|-----------------|
| `IQuestionRepository` | `QuestionRepository` | CRUD, cycle-aware random selection, dedup insert, mark-used, advance-cycle |
| `ISessionRepository` | `SessionRepository` | Upsert (save-or-update), ordered retrieval, count |
| `IResponseRepository` | `ResponseRepository` | Append-only save, get by session, count correct |

**Benefit:** Tests use either real in-memory EF contexts (integration-style) or `Moq` mocks (unit-style)
without any concrete EF dependency. Swapping SQLite for PostgreSQL requires only changing the DI
registration in `Program.cs`.

---

### Dependency Injection Approach

`Microsoft.Extensions.DependencyInjection` (`ServiceCollection`) is used directly — **not**
`Microsoft.Extensions.Hosting.Host`. This keeps the startup lightweight (no hosted-service lifecycle).

```csharp
var services = new ServiceCollection();
services.AddDbContext<QuizEngineDbContext>(...);
services.AddScoped<IQuestionRepository, QuestionRepository>();
services.AddScoped<ISessionRepository, SessionRepository>();
services.AddScoped<IResponseRepository, ResponseRepository>();
services.AddScoped<QuizService>();
services.AddScoped<HistoryService>();
services.AddScoped<ImportService>();
var serviceProvider = services.BuildServiceProvider();
```

All services are `Scoped`. Each CLI command handler creates its own `IServiceScope` to get a fresh
`DbContext` per invocation.

---

### ORM vs Raw SQL

**EF Core code-first** is used exclusively — no raw SQL, no stored procedures, no Dapper.

- Schema created via `context.Database.EnsureCreated()` at startup (no migrations applied at runtime)
- LINQ queries used throughout; `EF.Functions.Random()` used for random question selection in SQLite
- `AsNoTracking()` used on read-only queries for performance
- `ExecuteDeleteAsync()` (EF Core 7+ bulk delete) used in `ClearCommand`

---

### Cycle-Aware Question Selection

The `UsageCycle` column on `Question` implements a spaced-repetition-like cycle:

1. All questions start at `UsageCycle = 1`, `TimesUsed = 0`
2. `GetRandomQuestionsAsync` only selects questions where `UsageCycle == MIN(UsageCycle)` — ensuring
   unseen questions are always preferred
3. `MarkQuestionUsedAsync` increments `TimesUsed` and sets `LastUsedAt`
4. `AdvanceCycleIfExhaustedAsync` — after a session finalizes, checks if all questions in the current
   cycle have `TimesUsed > 0`. If so, increments `UsageCycle` for all of them and resets `TimesUsed = 0`,
   beginning a fresh cycle

---

### Answer Shuffling (Fisher-Yates)

`AnswerShuffler` is a static class with three methods:

- `Shuffle(options[], correctLetter, rng?)` — Fisher-Yates in-place shuffle; returns `ShuffleResult`
- `Identity(options[], correctLetter)` — no-op shuffle for `--no-shuffle` mode
- `GetOptionsArray(Question)` — extracts A–D (or A–E) from entity into a string array

`ShuffleResult` carries:
- `ShuffledOptions string[]` — the options in new display order
- `CorrectShuffledIndex int` — zero-based index of the correct option after shuffling
- `CorrectShuffledLetter string` — letter label (A/B/C/D/E) for the correct option after shuffling
- `LetterMap Dictionary<string,string>` — maps new letter → original letter for each position

This mapping is critical: when the user answers `"B"`, `QuizService` compares it against
`shuffle.CorrectShuffledLetter` (not the original `Question.CorrectAnswer`).

---

### Rich Terminal UI (Spectre.Console)

All terminal output goes through `Spectre.Console`:
- `FigletText` — ASCII art header on quiz start
- `Panel` — bordered box around each question
- `Table` — quiz results summary, session history list, session detail
- `Rule` — horizontal dividers
- `AnsiConsole.MarkupLine` — inline color/style markup with `[green]`, `[red]`, `[yellow]`, `[dim]`, `[bold]`
- `Markup.Escape()` — escapes user-supplied text to prevent markup injection

Score coloring: **green** ≥80%, **yellow** ≥60%, **red** <60%.

---

### No Migration Files

There are no EF Core migration files in this project. The schema is created purely by
`context.Database.EnsureCreated()`. If schema changes are needed in the future, either:
- Drop and recreate the database (acceptable for a local CLI tool), or
- Add EF Core migrations (`dotnet ef migrations add`) and switch to `context.Database.Migrate()`

---

### Duplicate Question Prevention

Deduplication operates at two levels:

1. **Within a batch** (in `ImportService`): a `HashSet<string>` keyed on `$"{QuestionText}|{CorrectAnswer}"`
   prevents importing the same question twice in one directory scan
2. **Against the database** (in `QuestionRepository.InsertAsync`): checks for existing row with same
   `QuestionText` and `CorrectAnswer` before inserting; the unique index in the schema enforces this
   at the DB level too

---

*End of specification. A developer reading this document should be able to recreate the entire
`quiz-engine-csharp` project without access to the original source.*
