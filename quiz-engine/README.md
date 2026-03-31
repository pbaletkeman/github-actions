# Quiz Engine — Multi-Language Collection

A collection of **eight quiz engine implementations** for studying the **GitHub Actions GH-200 certification exam**. Each project implements the same quiz engine specification using a different language and technology stack, allowing direct comparison of idiomatic approaches across ecosystems.

- [Quiz Engine — Multi-Language Collection](#quiz-engine--multi-language-collection)
  - [Projects](#projects)
  - [Shared Specification](#shared-specification)
    - [Question Markdown Format](#question-markdown-format)
  - [Project Details](#project-details)
    - [quiz-engine-csharp](#quiz-engine-csharp)
    - [quiz-engine-dart](#quiz-engine-dart)
    - [quiz-engine-golang](#quiz-engine-golang)
    - [quiz-engine-java](#quiz-engine-java)
    - [quiz-engine-nodejs](#quiz-engine-nodejs)
    - [quiz-engine-python](#quiz-engine-python)
    - [quiz-engine-rust](#quiz-engine-rust)
    - [quiz-engine-springboot](#quiz-engine-springboot)
  - [Docker](#docker)
  - [Repository Layout](#repository-layout)

---

## Projects

| Project | Language / Stack | Key Tech |
|---|---|---|
| [quiz-engine-csharp](./quiz-engine-csharp/README.md) | C# / .NET 8 | Entity Framework Core, Spectre.Console, SQLite |
| [quiz-engine-dart](./quiz-engine-dart/README.md) | Dart 3 | SQLite, args package, native executable |
| [quiz-engine-golang](./quiz-engine-golang/README.md) | Go 1.21 | cobra, go-sqlite3, tablewriter |
| [quiz-engine-java](./quiz-engine-java/README.md) | Java 21 / Gradle | JDBC, SQLite, HikariCP |
| [quiz-engine-nodejs](./quiz-engine-nodejs/README.md) | TypeScript / Node.js | TypeORM, SQLite, Jest |
| [quiz-engine-python](./quiz-engine-python/README.md) | Python 3.9+ | Typer, Rich, SQLite, pytest |
| [quiz-engine-rust](./quiz-engine-rust/README.md) | Rust 1.70+ | sqlx, clap, tokio, criterion |
| [quiz-engine-springboot](./quiz-engine-springboot/README.md) | Java 21 / Spring Boot 3.2 | Spring Data JPA, Thymeleaf, Picocli, H2/SQLite |

---

## Shared Specification

All eight engines implement the same core feature set:

- **Markdown import** — parse questions from `.md` files with a consistent format
- **Cycle-aware question selection** — questions rotate through all available before repeating
- **Answer shuffling** — options randomised per-session to prevent memorisation
- **SQLite persistence** — questions, sessions, and responses stored locally
- **Session history** — view past scores, review answers, export to JSON/CSV
- **CLI interface** — `quiz`, `import`, `history`, and `clear` commands
- **Test suite** — ≥90% coverage enforced

### Question Markdown Format

Each source file contains a questions section followed by a separate answer key table.

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
| 1 | C | `on: schedule` is the correct trigger. `cron` is the value of the `schedule` key, not the trigger name itself. `timer` and `workflow_dispatch` serve different purposes. | 05-Workflow-Trigger-Events.md | Easy |
| 2 | A, B, D | `secrets` is available in `steps[*].env`, `steps[*].with`, and `steps[*].run`. It is NOT available in `strategy.matrix`. | 02-Contextual-Information.md | Medium |
```

---

## Project Details

### [quiz-engine-csharp](./quiz-engine-csharp/README.md)

A layered .NET 8 solution with four projects — Entities, Data (EF Core), Service, and CLI. Uses `Spectre.Console` for rich terminal output and `System.CommandLine` for argument parsing. xUnit + Moq test suite with an in-memory EF Core fixture and Coverlet for coverage.

**Quick start:**
```bash
cd quiz-engine-csharp
build.bat                        # Windows CMD
.\build.ps1                      # PowerShell
./build.sh                       # Bash / macOS / Linux
import.bat questions.md          # Windows CMD
.\import.ps1 -Path questions.md  # PowerShell
./import.sh questions.md         # Bash
quiz.bat 10                      # Windows CMD (10 questions)
.\quiz.ps1 -Questions 10         # PowerShell
./quiz.sh 10                     # Bash
```

---

### [quiz-engine-dart](./quiz-engine-dart/README.md)

Pure Dart 3 CLI application that compiles to a single native binary. Uses the `sqlite3` package directly, the `args` package for CLI parsing, and the `test` package for unit and integration tests. Coverage enforced by a shell script.

**Quick start:**
```bash
cd quiz-engine-dart
dart pub get
dart run lib/main.dart import --file questions.md
dart run lib/main.dart quiz
```

---

### [quiz-engine-golang](./quiz-engine-golang/README.md)

Idiomatic Go application using `cobra` for CLI commands and `go-sqlite3` (CGO) for database access. All database access types are in `internal/database`, engine logic in `internal/engine`, with table-formatted output via `tablewriter`.

**Quick start:**
```bash
cd quiz-engine-golang
CGO_ENABLED=1 go build -o bin/quiz-engine .
./bin/quiz-engine import --file questions.md
./bin/quiz-engine quiz --questions 20
```

---

### [quiz-engine-java](./quiz-engine-java/README.md)

Plain Java 21 Gradle project using raw JDBC with SQLite. Uses HikariCP for connection pooling — all ORM logic hand-written in DAO classes. Packaged as a fat JAR with the Gradle Shadow Plugin. JUnit 5 + Mockito test suite with JaCoCo coverage.

**Quick start:**
```bash
cd quiz-engine-java
build.bat                        # Windows CMD
.\build.ps1                      # PowerShell
./build.sh                       # Bash
# or directly
./gradlew shadowJar
import.bat questions.md          # Windows CMD
.\import.ps1 -Path questions.md  # PowerShell
./import.sh questions.md         # Bash
# or directly
java -jar build/libs/quiz-engine.jar import questions.md
java -jar build/libs/quiz-engine.jar quiz
```

---

### [quiz-engine-nodejs](./quiz-engine-nodejs/README.md)

TypeScript application using TypeORM with the SQLite driver. Features a strongly-typed model layer, migration-based schema management, and a Jest test suite with ≥90% coverage. Supports Docker for test isolation.

**Quick start:**
```bash
cd quiz-engine-nodejs
npm install
npm run build
npm run dev -- import --file questions.md
npm run dev -- quiz
```

---

### [quiz-engine-python](./quiz-engine-python/README.md)

Python 3.9+ application using Typer for CLI, Rich for formatted output, and SQLite via the standard library `sqlite3` module. Pydantic models enforce data validation. pytest with coverage enforcement.

**Quick start:**
```bash
cd quiz-engine-python
pip install -r requirements.txt
python scripts/import_questions.py --file questions.md
python -m quiz_engine.main --questions 20
```

---

### [quiz-engine-rust](./quiz-engine-rust/README.md)

High-performance Rust application using `sqlx` for async SQLite access, `clap` for CLI parsing, and `tokio` as the async runtime. Compiles to a single ~10 MB binary with no runtime dependencies. Includes criterion benchmarks.

**Quick start:**
```bash
cd quiz-engine-rust
cargo build --release
./target/release/quiz_engine import --file questions.md
./target/release/quiz_engine quiz
```

---

### [quiz-engine-springboot](./quiz-engine-springboot/README.md)

Spring Boot 3.2 application that adds a **REST API** and **Thymeleaf web interface** on top of the standard CLI feature set. Uses Spring Data JPA with H2 (test) and SQLite (production). Picocli handles CLI commands. JaCoCo enforces test coverage.

**Quick start:**
```bash
cd quiz-engine-springboot
./gradlew bootRun
# Web UI: http://localhost:8080
```

---

## Docker

Every project includes a `Dockerfile` and `docker-compose.yml` for containerised builds and test runs. Example:

```bash
cd quiz-engine-nodejs
docker-compose up --build
```

---

## Repository Layout

```
quiz-engine/
├── README.md                    ← this file
├── quiz-engine-csharp/
├── quiz-engine-dart/
├── quiz-engine-golang/
├── quiz-engine-java/
├── quiz-engine-nodejs/
├── quiz-engine-python/
├── quiz-engine-rust/
└── quiz-engine-springboot/
```
