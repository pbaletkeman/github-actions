# Quiz Engine — Rust

A high-performance, memory-safe CLI quiz engine for GH-200 GitHub Actions certification preparation.

Built with **Rust 1.70+**, **sqlx** (async SQLite), **clap** (CLI), **criterion** (benchmarks), and **tokio** (async runtime).

## Features

- 📚 Load and randomize questions from Markdown files
- 🔄 Cycle-aware question selection — never repeat until all questions used
- 🔀 Answer shuffling — options randomized on each quiz
- 💾 Persistent sessions — all quiz history stored in SQLite
- 📊 Detailed stats — score, grade, time taken
- 🔍 History review — replay sessions with answer key
- 🦀 Zero-cost safety — Rust's borrow checker prevents entire bug classes
- 🚀 Single binary — no runtime dependencies, ~10MB executable

## Quick Start

```bash
# Build (debug)
cargo build

# Build (release — optimized, ~10MB binary)
cargo build --release

# Run (debug)
cargo run -- --help

# Run (release)
./target/release/quiz_engine --help
```

## CLI Commands

### Take a Quiz

```bash
# Default: 10 questions
cargo run -- quiz

# Custom question count
cargo run -- quiz --questions 50
./target/release/quiz_engine quiz --questions 100
```

### Import Questions

Import questions from a Markdown file:

```bash
cargo run -- import --file questions.md
./target/release/quiz_engine import --file /path/to/questions.md
```

**Expected Markdown format:**

```markdown
## Q1
> What is Continuous Integration?
- A) Continuous Integration
- B) Code Import
- C) Compile
- D) Configure
**Answer: A**
> Explanation: CI automates integration of code changes.

## Q2
> What does CD stand for?
- A) Continuous Delivery
- B) Code Deploy
- C) Compile
- D) Configure
**Answer: A**
```

### View History

```bash
# List all sessions
cargo run -- history

# View specific session
cargo run -- history --session-id <uuid>

# Review answers for a session
cargo run -- history --session-id <uuid> --review
```

### Clear Data

```bash
# Clear all questions (requires --confirm)
cargo run -- clear --questions --confirm

# Clear history only
cargo run -- clear --history --confirm

# Clear everything
cargo run -- clear --all --confirm
```

## Database

The engine uses SQLite via `sqlx`. The default database file is `./quiz_engine.db`.

Override with the `DATABASE_URL` environment variable:

```bash
export DATABASE_URL=sqlite:/path/to/my.db
cargo run -- quiz --questions 10
```

Or pass `--db` directly:

```bash
cargo run -- --db sqlite:./custom.db quiz --questions 5
```

Migrations run automatically on startup.

## Project Structure

```
quiz-engine-rust/
├── Cargo.toml                    # Dependencies + release profile
├── src/
│   ├── main.rs                   # CLI entry point (clap)
│   ├── lib.rs                    # Library root
│   ├── error.rs                  # Custom error types (thiserror)
│   ├── models/
│   │   ├── question.rs           # Question struct (sqlx::FromRow)
│   │   ├── quiz_session.rs       # QuizSession struct
│   │   └── quiz_response.rs      # QuizResponse struct
│   ├── db/
│   │   ├── connection.rs         # sqlx connection pool
│   │   └── repositories/
│   │       ├── question_repo.rs  # Question CRUD + cycle logic
│   │       ├── session_repo.rs   # Session CRUD
│   │       └── response_repo.rs  # Response recording
│   ├── service/
│   │   ├── quiz_engine.rs        # Core quiz orchestration
│   │   ├── quiz_service.rs       # Business logic wrapper
│   │   ├── history_service.rs    # History queries
│   │   ├── import_service.rs     # Batch import
│   │   ├── markdown_parser.rs    # Markdown question parser
│   │   ├── answer_shuffler.rs    # Answer randomization
│   │   └── quiz_utils.rs         # Scoring helpers
│   └── cli/
│       ├── commands/
│       │   ├── quiz.rs           # quiz subcommand
│       │   ├── import.rs         # import subcommand
│       │   ├── history.rs        # history subcommand
│       │   └── clear.rs          # clear subcommand
│       ├── formatter.rs          # Table/box formatting
│       └── prompts.rs            # Interactive prompts
├── migrations/
│   └── 001_create_tables.sql     # Schema migrations
├── tests/
│   ├── database_tests.rs         # Repository layer tests
│   ├── service_tests.rs          # Service layer tests
│   └── integration_tests.rs      # End-to-end workflow tests
├── benches/
│   └── quiz_bench.rs             # Criterion benchmarks
├── Dockerfile                    # Multi-stage production build
└── docker-compose.yml            # Dev/test services
```

## Testing

### Run All Tests

```bash
cargo test
```

### Run with Output

```bash
cargo test -- --nocapture
```

### Run Specific Test File

```bash
cargo test --test database_tests
cargo test --test service_tests
cargo test --test integration_tests
```

### Code Coverage (optional)

```bash
# Install tarpaulin
cargo install cargo-tarpaulin

# Run with 90% threshold
cargo tarpaulin --fail-under 90 --out Html --output-dir coverage \
    --exclude-files "src/main.rs" "src/cli/**"
```

## Benchmarks

```bash
# Run all benchmarks
cargo bench

# Run a specific benchmark
cargo bench -- shuffle_answers

# View HTML report
open target/criterion/report/index.html
```

Benchmark targets:
- `shuffle_answers` — answer randomization speed
- `calculate_percentage` — score calculation
- `grade_from_percentage` — grade lookup
- `db_insert_question` — single insert latency
- `db_get_random_10_questions` — bulk random query from 100-question pool

## Release Build

The release profile is optimized for size and speed:

```toml
[profile.release]
opt-level = 3      # Maximum optimization
lto = true         # Link-time optimization
codegen-units = 1  # Single codegen unit (better optimization)
panic = "abort"    # Smaller binary, no unwinding
strip = true       # Strip debug symbols
```

Build the release binary:

```bash
cargo build --release
ls -lh target/release/quiz_engine
```

## Docker

```bash
# Build image
docker build -t quiz-engine:latest .

# Run help
docker run --rm quiz-engine:latest

# Interactive quiz
docker run -it quiz-engine:latest ./quiz_engine quiz --questions 10

# Docker Compose: run tests
docker-compose up quiz-engine-test

# Docker Compose: release build
docker-compose up quiz-engine-build
```

## Dependencies

| Crate | Version | Purpose |
|-------|---------|---------|
| `sqlx` | 0.8 | Async SQLite ORM with compile-time safety |
| `tokio` | 1 | Async runtime |
| `clap` | 4.5 | CLI argument parsing (derive macros) |
| `uuid` | 1.11 | Session ID generation (v4) |
| `serde` | 1.0 | Serialization framework |
| `chrono` | 0.4 | DateTime handling |
| `rand` | 0.8 | Answer shuffling |
| `regex` | 1.11 | Markdown parsing |
| `anyhow` | 1.0 | Error context propagation |
| `thiserror` | 2.0 | Custom error types |
| `dotenvy` | 0.15 | `.env` file support |
| `criterion` | 0.5 | Performance benchmarks |
| `tempfile` | 3.14 | Temporary files in tests |

## Design Decisions

1. **sqlx over Diesel** — Async-first, no code generation required, cleaner ergonomics
2. **Cycle-aware selection** — Questions cycle through ensuring full coverage before repeats
3. **Shuffled answers** — Prevents answer pattern memorization
4. **No panics** — All errors propagated with `?` operator using custom `QuizError` enum
5. **Single binary** — `panic = "abort"` + `lto = true` + `strip = true` for minimal runtime footprint
