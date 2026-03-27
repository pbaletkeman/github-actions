# Quiz Engine — Rust

> Part of the [Quiz Engine multi-language collection](../README.md)

A high-performance, memory-safe CLI quiz engine for GH-200 GitHub Actions certification preparation.

Built with **Rust 1.70+**, **sqlx** (async SQLite), **clap** (CLI), **criterion** (benchmarks), and **tokio** (async runtime).

## Prerequisites

### Required Software

- **Rust 1.70+** - [Install Rust](https://www.rust-lang.org/tools/install)
- **Cargo** - Included with Rust
- **SQLite** - Required for database support
- **C Compiler** - Required for building SQLite bindings
  - **Windows**: Visual C++ Build Tools or MinGW-w64
  - **macOS**: Xcode Command Line Tools
  - **Linux**: GCC or Clang

### Verifying Prerequisites

```bash
# Check Rust version
rustc --version

# Check Cargo version
cargo --version

# Check SQLite (if installed separately)
sqlite3 --version
```

## Installing Rust

### Windows Installation

1. Download [rustup-init.exe](https://www.rust-lang.org/tools/install)
2. Run the installer and follow the prompts
3. Select `1) Proceed with installation`
4. Close terminal and restart
5. Verify installation:
   ```cmd
   rustc --version
   cargo --version
   ```

### macOS Installation

```bash
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh

# Follow the prompts, then activate Rust:
source $HOME/.cargo/env

# Verify installation
rustc --version
cargo --version
```

### Linux Installation

```bash
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh

# Follow the prompts, then activate Rust:
source $HOME/.cargo/env

# Verify installation
rustc --version
cargo --version
```

## Setting Up Build Environment

### Windows Setup

#### Option 1: Using Visual C++ Build Tools

1. Download [Visual C++ Build Tools](https://visualstudio.microsoft.com/downloads/)
2. Run installer and select "Desktop development with C++"
3. Complete installation and restart

#### Option 2: Using MinGW-w64

1. Download [MinGW-w64](https://www.mingw-w64.org/)
2. Extract to folder (e.g., `C:\mingw`)
3. Add to PATH and update Rust:
   ```cmd
   rustup default stable-gnu
   ```

### macOS Setup

```bash
# Install Xcode Command Line Tools
xcode-select --install

# Rust will use clang automatically
```

### Linux Setup

#### Debian/Ubuntu

```bash
sudo apt-get update
sudo apt-get install build-essential sqlite3 libsqlite3-dev
```

#### Fedora/RHEL

```bash
sudo dnf groupinstall "Development Tools"
sudo dnf install sqlite sqlite-devel
```

## Installed Dependencies

The project uses the following Rust crates (automatically downloaded):

- **tokio** - Async runtime
- **sqlx** - SQL toolkit with SQLite support
- **clap** - Command-line argument parser
- **serde** - Serialization framework
- **criterion** - Benchmarking framework
- **uuid** - UUID generation

These are defined in `Cargo.toml` and installed automatically via `cargo build`.

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

### 1. Clone or Navigate to Project

```bash
cd quiz-engine-rust
```

### 2. Build the Project

```bash
# Debug build (faster compile, larger binary, debug info)
cargo build

# Release build (optimized, ~10MB binary, faster execution)
cargo build --release
```

**Note**: First build may take 2-5 minutes as dependencies are downloaded and compiled.

### 3. Locate the Executable

- **Debug**: `target/debug/quiz_engine`
- **Release**: `target/release/quiz_engine`

### 4. Verify Build Success

```bash
# Using debug binary
cargo run -- --help

# Using release binary
./target/release/quiz_engine --help
```

## Building the Project

### Full Build

```bash
# Debug build (default)
cargo build

# Release build (optimized)
cargo build --release

# Verbose build output
cargo build --verbose

# Check for errors without building
cargo check
```

### Clean Build

```bash
# Remove build artifacts
cargo clean

# Rebuild from scratch
cargo clean && cargo build --release
```

### Build Troubleshooting

**Error**: `error[E0514]: found crate ... compiled by an incompatible version`
- **Solution**: Run `cargo clean && cargo build`

**Error**: `error: linking with ... failed: exit status: 1`
- **Solution**: Ensure C compiler is installed (see Setup Build Environment section)

**Error**: `error: could not compile sqlx`
- **Solution**: Install SQLite development files for your OS

## Running the Project

### Using Cargo (Debug)

```bash
# Run default quiz (10 questions)
cargo run -- quiz

# Run with custom question count
cargo run -- quiz --questions 50

# Show help
cargo run -- --help
```

### Using Compiled Binary (Release)

```bash
# Windows
.\target\release\quiz_engine --help
.\target\release\quiz_engine quiz --questions 20

# macOS/Linux
./target/release/quiz_engine --help
./target/release/quiz_engine quiz --questions 20
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

### Run Tests with Output

```bash
# Show println! and other output
cargo test -- --nocapture

# Run tests sequentially (default is parallel)
cargo test -- --test-threads=1
```

### Run Specific Test

```bash
# Run a single test by name
cargo test test_quiz_creation

# Run all tests in a specific file
cargo test --test integration_tests

# Run tests matching a pattern
cargo test history --lib
```

### Run Tests by Module

```bash
# Database/repository tests
cargo test --test database_tests

# Business logic tests
cargo test --test service_tests

# End-to-end workflow tests
cargo test --test integration_tests
```

### Test Coverage

```bash
# Install tarpaulin (code coverage tool)
cargo install cargo-tarpaulin

# Generate coverage report
cargo tarpaulin --out Html
```

## Benchmarking

Run performance benchmarks using Criterion:

```bash
cargo bench

# Benchmark specific function
cargo bench quiz_engine
```

## Code Coverage (optional)

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
