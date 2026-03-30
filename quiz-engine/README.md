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
| [quiz-engine-java](./quiz-engine-java/README.md) | Java 17 / Maven | JDBC, SQLite, Picocli |
| [quiz-engine-nodejs](./quiz-engine-nodejs/README.md) | TypeScript / Node.js | TypeORM, SQLite, Jest |
| [quiz-engine-python](./quiz-engine-python/README.md) | Python 3.9+ | Typer, Rich, SQLite, pytest |
| [quiz-engine-rust](./quiz-engine-rust/README.md) | Rust 1.70+ | sqlx, clap, tokio, criterion |
| [quiz-engine-springboot](./quiz-engine-springboot/README.md) | Java 17 / Spring Boot 3.2 | Spring Data JPA, Thymeleaf, H2/SQLite |

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

---

## Project Details

### [quiz-engine-csharp](./quiz-engine-csharp/README.md)

A layered .NET 8 solution with four projects — Entities, Data (EF Core), Service, and CLI. Uses `Spectre.Console` for rich terminal output and `System.CommandLine` for argument parsing. xUnit test suite with an in-memory SQLite fixture.

**Quick start:**
```bash
cd quiz-engine-csharp
dotnet build
dotnet run --project QuizEngine.CLI -- import --file questions.md
dotnet run --project QuizEngine.CLI -- quiz
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

Plain Java 17 Maven project using raw JDBC with SQLite. No framework dependencies — all ORM logic hand-written in DAO classes. Packaged as a fat JAR with the Maven Shade Plugin. JUnit 5 test suite.

**Quick start:**
```bash
cd quiz-engine-java
mvn clean package
java -jar target/quiz-engine.jar import questions.md
java -jar target/quiz-engine.jar quiz
```

---

### [quiz-engine-nodejs](./quiz-engine-nodejs/README.md)

TypeScript application using TypeORM with the SQLite driver. Features a strongly-typed model layer, migration-based schema management, and a Jest test suite (122 tests, ≥90% coverage). Supports Docker for test isolation.

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
