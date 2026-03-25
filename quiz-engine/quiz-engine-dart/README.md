# Quiz Engine — Dart

> Part of the [Quiz Engine multi-language collection](../README.md)

A command-line quiz engine for **GitHub Actions (GH-200) certification** preparation, built with Dart 3, SQLite, and the `args` package.

## Features

- **Interactive CLI quiz** with shuffled answer options and immediate feedback
- **SQLite persistence** — questions, sessions and responses stored locally
- **Non-repetition cycle tracking** — questions cycle through before repeating
- **Markdown import** — load questions from `.md` files
- **Session history** — review past results with JSON/CSV export
- **Native executable** — compile to a single binary with no runtime dependency
- **>90% test coverage** enforced by CI script

---

## Quick Start

### Prerequisites

- Dart SDK ≥ 3.0 — [install](https://dart.dev/get-dart)

### Install dependencies

```bash
dart pub get
```

### Run (development mode)

```bash
dart run lib/main.dart --help
```

### Build native executable

```bash
dart compile exe lib/main.dart -o bin/quiz_engine
./bin/quiz_engine --help
```

---

## CLI Reference

### `import` — Load questions

```bash
# Single file
quiz_engine import --file questions.md

# All markdown files in a directory
quiz_engine import --dir ./questions/
```

### `quiz` — Take a quiz

```bash
# Default (10 questions)
quiz_engine quiz

# Custom count
quiz_engine quiz --questions 20

# Skip answer shuffling
quiz_engine quiz --no-shuffle
```

### `history` — View past sessions

```bash
# List all sessions
quiz_engine history

# Session detail
quiz_engine history --session-id <uuid>

# Full answer review
quiz_engine history --session-id <uuid> --review

# Export
quiz_engine history --export json
quiz_engine history --export csv
```

### `clear` — Remove data

```bash
quiz_engine clear --questions --confirm
quiz_engine clear --history --confirm
quiz_engine clear --all --confirm
```

### Global options

```
--db      Path to SQLite database file (default: <executable dir>/quiz_engine.db)
--help    Show help
--version Show version
```

---

## Question File Format

Questions are imported from Markdown files using the following format:

```markdown
# Section Title

## Question 1
What does CI stand for?

A) Continuous Integration
B) Code Import
C) Compile
D) Configure

**Answer:** A
**Explanation:** CI stands for Continuous Integration.

## Question 2
What triggers a GitHub Actions workflow?

A) A Dockerfile
B) An event such as a push or pull request
C) A cron job only
D) Manual trigger only

**Answer:** B
**Explanation:** Workflows are triggered by events defined in the `on:` key.
```

---

## Running Tests

```bash
# Run all tests
dart test

# Run with coverage collection
dart pub global activate coverage
dart test --coverage=coverage

# Convert to LCOV
dart pub global run coverage:format_coverage \
  --lcov \
  --in=coverage \
  --out=coverage/lcov.info \
  --report-on=lib

# Enforce 90% threshold
bash scripts/check_coverage.sh

# Generate HTML report (requires lcov)
genhtml coverage/lcov.info --output-directory coverage/html
open coverage/html/index.html
```

---

## Docker

### Quick start

```bash
# Build image
docker build -t quiz-engine:latest .

# Run interactive quiz
docker run -it quiz-engine:latest dart run lib/main.dart quiz --questions 10

# Run tests with coverage check
docker-compose up quiz-engine-test

# Build native executable
docker-compose up quiz-engine-build
```

### Multi-arch build and push

```bash
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t myregistry/quiz-engine:1.0.0 \
  --push .
```

---

## Project Structure

```
quiz_engine_dart/
├── lib/
│   ├── main.dart                      # CLI entry point
│   └── src/
│       ├── models/
│       │   ├── question.dart          # Question model
│       │   ├── quiz_session.dart      # QuizSession model
│       │   └── quiz_response.dart     # QuizResponse model
│       ├── database/
│       │   └── database.dart          # SQLite AppDatabase (schema + DAOs)
│       ├── service/
│       │   ├── quiz_engine.dart       # Session orchestration
│       │   ├── quiz_service.dart      # Business logic
│       │   ├── answer_shuffler.dart   # Answer randomisation
│       │   ├── markdown_parser.dart   # MD file import parser
│       │   ├── history_service.dart   # History queries
│       │   └── import_service.dart    # Bulk import
│       ├── cli/
│       │   ├── formatter.dart         # Terminal output helpers
│       │   ├── prompts.dart           # Interactive stdin prompts
│       │   └── commands/
│       │       ├── quiz_command.dart
│       │       ├── import_command.dart
│       │       ├── history_command.dart
│       │       └── clear_command.dart
│       └── exceptions/
│           └── quiz_exceptions.dart   # Custom exception classes
├── test/
│   ├── helpers.dart                   # Shared test fixtures
│   └── src/
│       ├── database/
│       │   └── database_test.dart     # Full database layer tests
│       ├── service/
│       │   ├── quiz_engine_test.dart  # QuizService + QuizEngine tests
│       │   ├── answer_shuffler_test.dart
│       │   └── markdown_parser_test.dart
│       └── models/
│           └── models_test.dart       # Model serialisation tests
├── scripts/
│   └── check_coverage.sh             # Enforces ≥90% coverage
├── Dockerfile                        # Multi-stage production image
├── docker-compose.yml                # Dev / test / build services
├── pubspec.yaml
├── analysis_options.yaml
└── README.md
```

---

## Architecture Decisions

| Decision | Choice | Rationale |
|---|---|---|
| Database | `sqlite3` package | Direct SQLite access, no code generation required |
| CLI | `args` package | Standard, well-maintained argument parsing |
| Testing | `test` package | Idiomatic Dart testing framework |
| Distribution | `dart compile exe` | Single native binary, no runtime needed |
| Non-repetition | Cycle columns | `usage_cycle` + `times_used` ensure questions exhaust before repeating |

---

## Production Deployment

1. **Build the native executable:**
   ```bash
   dart compile exe lib/main.dart -o bin/quiz_engine
   ```

2. **Copy to target host** — the binary is self-contained on the same OS/arch.

3. **Initialise the database** — the database is created automatically on first run.

4. **Import questions:**
   ```bash
   ./bin/quiz_engine import --dir /path/to/questions/
   ```

5. **Run quizzes:**
   ```bash
   ./bin/quiz_engine quiz --questions 100
   ```

> **Note:** The `sqlite3` native library must be present on the target system.
> Install with: `apt install libsqlite3-dev` (Debian/Ubuntu) or `brew install sqlite` (macOS).
