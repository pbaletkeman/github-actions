# Rust/Diesel Quiz Engine for GH-200 Certification

## System Architecture Overview

### Directory Structure
```
quiz_engine/
├── Cargo.toml                              # Rust dependencies
├── Cargo.lock                              # Dependency lock file
├── src/
│   ├── main.rs                             # Entry point
│   ├── models/
│   │   ├── question.rs                     # Question struct (Diesel)
│   │   ├── quiz_session.rs                 # QuizSession struct (Diesel)
│   │   └── quiz_response.rs                # QuizResponse struct (Diesel)
│   ├── schema.rs                           # Diesel schema auto-generated
│   ├── database/
│   │   ├── mod.rs                          # Database module
│   │   ├── connection.rs                   # SQLite connection management
│   │   ├── repositories/
│   │   │   ├── mod.rs
│   │   │   ├── question_repo.rs            # Question queries
│   │   │   ├── session_repo.rs             # Session CRUD
│   │   │   └── response_repo.rs            # Response tracking
│   │   └── migrations/
│   │       └── 2024-03-23-create_tables   # Schema versioning
│   ├── service/
│   │   ├── mod.rs
│   │   ├── quiz_engine.rs                  # Core quiz logic
│   │   ├── quiz_service.rs                 # Business logic
│   │   ├── history_service.rs              # History queries
│   │   ├── import_service.rs               # Markdown import
│   │   ├── markdown_parser.rs              # MD file parsing
│   │   ├── answer_shuffler.rs              # Answer randomization
│   │   └── quiz_utils.rs                   # Helper utilities
│   ├── cli/
│   │   ├── mod.rs
│   │   ├── commands/
│   │   │   ├── mod.rs
│   │   │   ├── quiz.rs                     # Quiz subcommand
│   │   │   ├── import.rs                   # Import subcommand
│   │   │   ├── history.rs                  # History subcommand
│   │   │   └── clear.rs                    # Clear subcommand
│   │   ├── formatter.rs                    # Table/box formatting
│   │   └── prompts.rs                      # Interactive prompts
│   └── error.rs                            # Custom error types
├── tests/
│   ├── database_tests.rs             # Integration tests for repository layer
│   ├── service_tests.rs              # Integration tests for QuizEngine service
│   └── integration_tests.rs          # Full workflow: load → answer → finalize
├── migrations/
│   └── 2024-03-23-000000_create_tables/
│       └── up.sql
├── Dockerfile               # Container image for production deployment
├── docker-compose.yml       # Multi-container orchestration for dev/test
└── README.md                               # Documentation
```

### Docker & Containerization

#### Dockerfile (Production - Multi-stage)
```dockerfile
# Build stage
FROM rust:1.75 as builder

WORKDIR /app

RUN apt-get update && apt-get install -y libsqlite3-dev && rm -rf /var/lib/apt/lists/*

COPY Cargo.* .
RUN mkdir src && echo "fn main() {}" > src/main.rs && cargo build --release && rm -rf src

COPY . .
RUN cargo build --release

# Runtime stage
FROM debian:bookworm-slim

WORKDIR /app

RUN apt-get update && apt-get install -y libsqlite3-0 && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/release/quiz_engine .

# Create non-root user
RUN useradd -m -u 1000 rustuser && chown -R rustuser:rustuser /app
USER rustuser

ENTRYPOINT ["./quiz_engine"]
CMD ["--help"]
```

#### docker-compose.yml (Development)
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
    stdin_open: true
    tty: true

  quiz-engine-test:
    build: .
    container_name: quiz-engine-test
    volumes:
      - .:/app
      - cargo-cache:/usr/local/cargo/registry
    working_dir: /app
    command: bash -c "cargo test && cargo tarpaulin --fail-under 90 --out Html"
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

#### Getting Started with Docker

**Quick Start (5 steps):**

1. **Build the image:**
   ```bash
   docker build -t quiz-engine:latest .
   ```

2. **Run development mode:**
   ```bash
   docker run -it quiz-engine:latest cargo run -- quiz --questions 10
   ```

3. **Run tests with Tarpaulin (code coverage):**
   ```bash
   docker-compose up quiz-engine-test
   ```

4. **Build optimized release binary:**
   ```bash
   docker-compose up quiz-engine-build
   ```

5. **Run compiled executable:**
   ```bash
   docker run -it quiz-engine:latest ./quiz_engine quiz --questions 10
   ```

**Build & Push:**
```bash
# Build multi-arch
docker buildx build --platform linux/amd64,linux/arm64 -t myregistry/quiz-engine:1.0 .

# Push to registry
docker push myregistry/quiz-engine:1.0
```

**Container Configuration:**
- Multi-stage build: Rust full SDK build + minimal Debian runtime
- SQLite support with libsqlite3-dev during build
- Diesel ORM pre-compiled migrations
- Non-root user (rustuser) for security
- Cargo cache volume for faster rebuilds
- Tarpaulin for code coverage with 90% threshold
- Single static binary optimized for size and performance

### Database Schema (Diesel)

#### Question Table (migrations/up.sql)
```sql
CREATE TABLE questions (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  question_text TEXT NOT NULL CHECK(length(question_text) <= 500),
  option_a TEXT NOT NULL CHECK(length(option_a) <= 200),
  option_b TEXT NOT NULL CHECK(length(option_b) <= 200),
  option_c TEXT NOT NULL CHECK(length(option_c) <= 200),
  option_d TEXT NOT NULL CHECK(length(option_d) <= 200),
  option_e TEXT CHECK(length(option_e) <= 200),
  correct_answer TEXT NOT NULL CHECK(length(correct_answer) = 1),
  explanation TEXT CHECK(length(explanation) <= 1000),
  section TEXT CHECK(length(section) <= 100),
  difficulty TEXT CHECK(length(difficulty) <= 50),
  source_file TEXT CHECK(length(source_file) <= 255),
  usage_cycle INTEGER NOT NULL DEFAULT 1,
  times_used INTEGER NOT NULL DEFAULT 0,
  last_used_at DATETIME,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_usage_cycle ON questions(usage_cycle);
CREATE INDEX idx_section ON questions(section);
```

#### QuizSession Table
```sql
CREATE TABLE quiz_sessions (
  session_id TEXT PRIMARY KEY CHECK(length(session_id) = 36),
  started_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ended_at DATETIME,
  num_questions INTEGER NOT NULL,
  num_correct INTEGER NOT NULL DEFAULT 0,
  percentage_correct REAL NOT NULL DEFAULT 0.0,
  time_taken_seconds INTEGER
);

CREATE INDEX idx_started_at ON quiz_sessions(started_at);
```

#### QuizResponse Table
```sql
CREATE TABLE quiz_responses (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  session_id TEXT NOT NULL CHECK(length(session_id) = 36),
  question_id INTEGER NOT NULL,
  user_answer TEXT NOT NULL CHECK(length(user_answer) = 1),
  is_correct INTEGER NOT NULL DEFAULT 0,
  time_taken_seconds INTEGER,
  FOREIGN KEY (session_id) REFERENCES quiz_sessions(session_id),
  FOREIGN KEY (question_id) REFERENCES questions(id),
  UNIQUE(session_id, question_id)
);

CREATE INDEX idx_session_id ON quiz_responses(session_id);
CREATE INDEX idx_question_id ON quiz_responses(question_id);
```

#### Rust Model Struct (schema.rs - auto-generated)
```rust
#[derive(Insertable, Queryable, Serialize, Deserialize)]
#[diesel(table_name = questions)]
pub struct Question {
    pub id: i32,
    pub question_text: String,
    pub option_a: String,
    pub option_b: String,
    pub option_c: String,
    pub option_d: String,
    pub option_e: Option<String>,
    pub correct_answer: String,
    pub explanation: Option<String>,
    pub section: Option<String>,
    pub difficulty: Option<String>,
    pub source_file: Option<String>,
    pub usage_cycle: i32,
    pub times_used: i32,
    pub last_used_at: Option<NaiveDateTime>,
    pub created_at: NaiveDateTime,
}

#[derive(Insertable, Queryable, Serialize, Deserialize)]
#[diesel(table_name = quiz_sessions)]
pub struct QuizSession {
    pub session_id: String,
    pub started_at: NaiveDateTime,
    pub ended_at: Option<NaiveDateTime>,
    pub num_questions: i32,
    pub num_correct: i32,
    pub percentage_correct: f64,
    pub time_taken_seconds: Option<i32>,
}

#[derive(Insertable, Queryable, Serialize, Deserialize)]
#[diesel(table_name = quiz_responses)]
pub struct QuizResponse {
    pub id: i32,
    pub session_id: String,
    pub question_id: i32,
    pub user_answer: String,
    pub is_correct: i32,
    pub time_taken_seconds: Option<i32>,
}
```

---

## Implementation Plan

### Phase 1: Project Setup & Diesel Configuration
**Timeline:** 1.5-2 hours

**Objective:** Initialize Rust project, setup Diesel ORM, define entities.

**Tasks:**

1. **Create Rust Project:**
   ```bash
   cargo new --name quiz_engine quiz_engine
   cd quiz_engine
   ```

2. **Update `Cargo.toml` Dependencies:**
   ```toml
   [package]
   name = "quiz_engine"
   version = "0.1.0"
   edition = "2021"

   [dependencies]
   diesel = { version = "2.1", features = ["sqlite"] }
   dotenvy = "0.15"
   uuid = { version = "1.3", features = ["v4", "serde"] }
   serde = { version = "1.0", features = ["derive"] }
   serde_json = "1.0"
   clap = { version = "4.3", features = ["derive"] }
   chrono = { version = "0.4", features = ["serde"] }
   tokio = { version = "1", features = ["full"] }
   rand = "0.8"
   regex = "1.5"
   anyhow = "1.0"
   thiserror = "1.0"

   [dev-dependencies]
   assert_cmd = "2.0"
   predicates = "3.0"
   ```

3. **Setup Diesel:**
   ```bash
   cargo install diesel_cli --no-default-features --features sqlite
   diesel setup
   ```

4. **Create Diesel Migrations:**
   ```bash
   diesel migration generate create_tables
   ```
   - Create `migrations/2024-03-23-create_tables/up.sql` with schema
   - Create `migrations/2024-03-23-create_tables/down.sql` for rollback

5. **Generate Diesel Schema:**
   ```bash
   diesel migration run
   ```
   - Auto-generates `src/schema.rs`

6. **Define Model Structs:**
   - `src/models/question.rs` → Question struct
   - `src/models/quiz_session.rs` → QuizSession struct
   - `src/models/quiz_response.rs` → QuizResponse struct

7. **Create Repository Modules:**
   - `QuestionRepository` with cycle-aware query methods
   - `SessionRepository` for session CRUD
   - `ResponseRepository` for response tracking

8. **Test database initialization:**
   ```bash
   cargo run -- --init
   ```
   - SQLite database created successfully

**Success Criteria:**
- Rust project structure created
- Diesel migrations run successfully
- SQLite database initializes with schema
- All models properly defined
- Repositories compile without errors

---

### Phase 2: Service Layer & Quiz Logic
**Timeline:** 2-2.5 hours

**Objective:** Implement core quiz engine, services, utility classes.

**Tasks:**

1. **Create `QuizEngine` Struct:**
   ```rust
   pub struct QuizEngine {
       db: DbConn,
   }

   impl QuizEngine {
       pub async fn load_questions(&self, count: usize) -> Result<Vec<Question>> { }
       pub async fn submit_answers(&self, session_id: &str, answers: &[String]) -> Result<()> { }
       pub async fn finalize_session(&self, session_id: &str) -> Result<QuizSession> { }
   }
   ```

2. **Create `QuizService` Module:**
   - Wrapper around Diesel repositories
   - Orchestrate business logic
   - `get_random_questions()`, `mark_question_used()`, `advance_cycle_if_needed()`

3. **Create `AnswerShuffler` Module:**
   - `shuffle_answers(question: &Question)` → randomized Vec + position map
   - Preserve shuffled answer for verification

4. **Create `MarkdownParser` Module:**
   - `parse_file(path: &Path)` → extract questions from markdown
   - Regex parsing for question format

5. **Create Utility Modules:**
   - `HistoryService` for queries
   - `ImportService` for batch import
   - `QuizUtils` for scoring, formatting

6. **Test service layer:**
   ```bash
   cargo test service
   ```
   - Verify cycle-aware question selection
   - Test score calculation
   - Verify answer shuffling

**Success Criteria:**
- QuizEngine orchestrates correct flow
- Cycle-aware question selection works
- Session stats calculated correctly
- All services tested and passing
- Batch import functional

---

### Phase 3: CLI Implementation with Clap
**Timeline:** 1.5-2 hours

**Objective:** Build interactive CLI using `clap` derive macros.

**Tasks:**

1. **Create Command Structure with Clap:**
   ```rust
   #[derive(Parser)]
   #[command(name = "quiz_engine")]
   #[command(about = "GH-200 Certification Quiz Engine", long_about = None)]
   struct Cli {
       #[command(subcommand)]
       command: Commands,
   }

   #[derive(Subcommand)]
   enum Commands {
       Quiz { #[arg(short, long, default_value = "100")] questions: usize },
       Import { #[arg(short, long)] file: String },
       History { #[arg(long)] session_id: Option<String> },
       Clear { #[arg(long)] confirm: bool },
   }
   ```

2. **Implement Quiz Command:**
   - Load questions via QuizService
   - Present interactive quiz
   - Collect answers
   - Calculate score
   - Persist session

3. **Implement Import Command:**
   - `import --file questions.md`
   - Parse markdown file
   - Batch insert via ImportService

4. **Implement History Command:**
   - View past sessions
   - `--session-id` flag for details
   - `--review` option for answer key
   - `--export` for CSV/JSON

5. **Implement Clear Command:**
   - `--questions` delete questions
   - `--history` delete sessions
   - `--all` delete all data
   - `--confirm` flag required for safety

6. **Create CLI Entry Point:**
   ```rust
   #[tokio::main]
   async fn main() -> Result<()> {
       let cli = Cli::parse();
       match cli.command {
           Commands::Quiz { questions } => { }
           Commands::Import { file } => { }
           Commands::History { session_id } => { }
           Commands::Clear { confirm } => { }
       }
       Ok(())
   }
   ```

7. **Create Formatter Module:**
   - Table formatting using Unicode box-drawing
   - Pretty-printed output

8. **Test CLI commands:**
   ```bash
   cargo run -- quiz --questions 100
   cargo run -- import --file questions.md
   cargo run -- history
   ```

**Success Criteria:**
- All CLI commands execute correctly
- Interactive prompts work smoothly
- Pretty-printed output formatted
- Error handling graceful
- No panics in normal operation

---

### Phase 4: Unit Testing & Coverage Enforcement
**Timeline:** 2-3 hours

**Objective:** Achieve >90% unit test coverage. `cargo tarpaulin` must fail if coverage drops below 90%.

**Install and configure `cargo-tarpaulin`:**
```bash
cargo install cargo-tarpaulin

# Run with threshold enforcement (exits 1 if below 90%)
cargo tarpaulin \
  --out Html \
  --output-dir coverage \
  --exclude-files "src/main.rs" "src/cli/**" \
  --fail-under 90

# Or with llvm-cov (faster):
cargo install cargo-llvm-cov
cargo llvm-cov \
  --html \
  --output-dir coverage \
  --ignore-filename-regex "main\.rs|cli/" \
  --fail-under-lines 90
```

**Add to `Cargo.toml`:**
```toml
[dev-dependencies]
tempfile = "3"
wildmatch = "2"

[profile.test]
opt-level = 0
debug = true
```

**Tasks:**

1. **Write unit tests inside `src/database/repositories/question_repo.rs` (target: >92%):**
   ```rust
   #[cfg(test)]
   mod tests {
       use super::*;
       use crate::database::connection::establish_test_connection;

       fn setup() -> SqliteConnection {
           let mut conn = establish_test_connection(); // returns :memory: connection
           run_migrations(&mut conn).expect("migrations failed");
           conn
       }

       #[test]
       fn test_insert_and_retrieve_question() {
           let mut conn = setup();
           let new_q = NewQuestion {
               question_text: "What is CI?",
               option_a: "Continuous Integration",
               option_b: "Code Import",
               option_c: "Compile",
               option_d: "Configure",
               correct_answer: "A",
               ..Default::default()
           };
           let id = QuestionRepo::insert(&mut conn, new_q).expect("insert failed");
           let questions = QuestionRepo::get_all(&mut conn).expect("get failed");
           assert_eq!(questions.len(), 1);
           assert_eq!(questions[0].question_text, "What is CI?");
           let _ = id;
       }

       #[test]
       fn test_get_random_questions_omits_correct_answer() {
           let mut conn = setup();
           insert_sample(&mut conn);
           let questions = QuestionRepo::get_random_for_quiz(&mut conn, 1)
               .expect("query failed");
           // QuizQuestion projection must not include correct_answer
           // This is enforced at the type level: QuizQuestion has no correct_answer field
           assert_eq!(questions.len(), 1);
       }

       #[test]
       fn test_advance_cycle_when_all_used() {
           let mut conn = setup();
           let id = insert_sample(&mut conn);
           QuestionRepo::mark_used(&mut conn, id).unwrap();
           QuestionRepo::advance_cycle_if_exhausted(&mut conn).unwrap();
           assert_eq!(QuestionRepo::get_current_cycle(&mut conn).unwrap(), 2);
       }

       #[test]
       fn test_insert_skips_duplicate() {
           let mut conn = setup();
           let q = sample_new_question();
           QuestionRepo::insert_if_not_exists(&mut conn, q.clone()).unwrap();
           QuestionRepo::insert_if_not_exists(&mut conn, q).unwrap();
           assert_eq!(QuestionRepo::count(&mut conn).unwrap(), 1);
       }
   }
   ```

2. **Write unit tests inside `src/service/answer_shuffler.rs` (target: >95%):**
   ```rust
   #[cfg(test)]
   mod tests {
       use super::*;
       use std::collections::HashSet;

       #[test]
       fn test_shuffle_preserves_all_options() {
           let options = vec![
               "Alpha".to_string(), "Beta".to_string(),
               "Gamma".to_string(), "Delta".to_string(),
           ];
           let result = shuffle_answers(&options, "A");
           let original: HashSet<_> = options.iter().collect();
           let shuffled: HashSet<_> = result.shuffled_options.iter().collect();
           assert_eq!(original, shuffled);
       }

       #[test]
       fn test_shuffle_maps_correct_answer_to_new_position() {
           let options = vec![
               "Alpha".to_string(), "Beta".to_string(),
               "Gamma".to_string(), "Delta".to_string(),
           ];
           let result = shuffle_answers(&options, "A"); // A = "Alpha"
           assert_eq!(result.shuffled_options[result.correct_shuffled_index], "Alpha");
       }

       #[test]
       fn test_shuffle_returns_four_options() {
           let options = vec![
               "A".to_string(), "B".to_string(),
               "C".to_string(), "D".to_string(),
           ];
           let result = shuffle_answers(&options, "C");
           assert_eq!(result.shuffled_options.len(), 4);
       }
   }
   ```

3. **Write unit tests inside `src/service/markdown_parser.rs` (target: >90%):**
   ```rust
   #[cfg(test)]
   mod tests {
       use super::*;
       use std::io::Write;
       use tempfile::NamedTempFile;

       #[test]
       fn test_parse_valid_markdown_file() {
           let mut file = NamedTempFile::new().unwrap();
           writeln!(file, "## Q1").unwrap();
           writeln!(file, "> What is CI?").unwrap();
           writeln!(file, "- A) Continuous Integration").unwrap();
           writeln!(file, "- B) Code Import").unwrap();
           writeln!(file, "- C) Compile").unwrap();
           writeln!(file, "- D) Configure").unwrap();
           writeln!(file, "**Answer: A**").unwrap();

           let questions = parse_markdown_file(file.path()).unwrap();
           assert_eq!(questions.len(), 1);
           assert_eq!(questions[0].correct_answer, "A");
       }

       #[test]
       fn test_parse_fails_on_missing_answer_line() {
           let mut file = NamedTempFile::new().unwrap();
           writeln!(file, "## Q1\n> No answer here.").unwrap();
           let result = parse_markdown_file(file.path());
           assert!(result.is_err());
       }

       #[test]
       fn test_parse_fails_on_invalid_answer_letter() {
           let mut file = NamedTempFile::new().unwrap();
           writeln!(file, "**Answer: Z**").unwrap();
           let result = parse_markdown_file(file.path());
           assert!(result.is_err());
       }
   }
   ```

4. **Write integration test `tests/service_tests.rs` (target: QuizEngine >92%):**
   ```rust
   use quiz_engine::database::connection::establish_test_connection;
   use quiz_engine::database::repositories::QuestionRepo;
   use quiz_engine::service::quiz_engine::QuizEngine;

   #[test]
   fn test_submit_correct_answer_increases_score() {
       let mut conn = establish_test_connection();
       quiz_engine::database::migrations::run(&mut conn).unwrap();
       QuestionRepo::insert(&mut conn, sample_question()).unwrap();
       let mut engine = QuizEngine::new(&mut conn, 1);
       engine.load_questions().unwrap();
       engine.submit_answer(0, "A", 10).unwrap();
       assert_eq!(engine.num_correct(), 1);
   }

   #[test]
   fn test_finalize_persists_session() {
       let mut conn = establish_test_connection();
       quiz_engine::database::migrations::run(&mut conn).unwrap();
       QuestionRepo::insert(&mut conn, sample_question()).unwrap();
       let mut engine = QuizEngine::new(&mut conn, 1);
       engine.load_questions().unwrap();
       engine.submit_answer(0, "A", 5).unwrap();
       let session = engine.finalize().unwrap();
       assert!(!session.session_id.is_empty());
   }
   ```

5. **Coverage target summary:**

| Module | Test Location | Target |
|---|---|---|
| `database/repositories/question_repo` | `#[cfg(test)]` inline | >92% |
| `service/answer_shuffler` | `#[cfg(test)]` inline | >95% |
| `service/markdown_parser` | `#[cfg(test)]` inline | >90% |
| `service/quiz_engine` | `tests/service_tests.rs` | >92% |
| `service/history_service` | `tests/service_tests.rs` | >90% |

6. **Build Release Binary:**
   ```bash
   cargo build --release
   # Binary at: target/release/quiz_engine (~10MB)
   ```

7. **Cross-Compilation (Optional):**
   ```bash
   cargo install cross
   cross build --release --target x86_64-pc-windows-gnu
   cross build --release --target x86_64-apple-darwin
   ```

8. **Write Comprehensive README** with testing section:
   - `cargo tarpaulin --fail-under 90` — must show ≥90% coverage
   - HTML coverage report at `coverage/tarpaulin-report.html`

9. **Final Testing:**
   - Full end-to-end workflow: Import → Quiz → History → Retake
   - Verify cycle mechanics and non-repetition
   - Performance profiling

**Success Criteria:**
- `cargo tarpaulin --fail-under 90` **exits 0 (fails build below 90%)**
- Or `cargo llvm-cov --fail-under-lines 90` as alternative
- HTML coverage report generated in `coverage/`
- All tests passing with `cargo test`
- Release binary compiles successfully
- Single executable works (no dependencies)
- Binary size ~10MB
- Full documentation provided
- Works on Windows/Mac/Linux

---

## Dependencies Summary
- **diesel** (2.1) - Type-safe ORM
- **tokio** (1.0) - Async runtime
- **clap** (4.3) - CLI argument parsing with derive macros
- **uuid** (1.3) - Session ID generation
- **chrono** (0.4) - DateTime handling
- **serde** (1.0) - Serialization framework
- **rand** (0.8) - Random answer shuffling
- **regex** (1.5) - Markdown parsing
- **anyhow/thiserror** (1.0) - Error handling

---

## Core Design Decisions

### 1. Diesel for ORM
- **Type-Safe:** Compile-time query validation
- **Query Builder:** Expressive SQL in Rust
- **No Runtime Reflection:** Zero-cost abstractions
- **Migrations:** Version control for schema changes

### 2. Clap for CLI
- **Derive Macros:** Declarative command structure
- **Type Validation:** Automatic type coercion and validation
- **Subcommands:** Structured command hierarchy
- **Auto Help:** Automatic `--help` generation

### 3. Tokio for Async
- **Async/Await:** Modern Rust concurrency
- **Non-Blocking I/O:** All database queries async
- **Parallel Operations:** Support for concurrent quizzes

### 4. Error Handling
- **Custom Error Types:** `thiserror` for ergonomic errors
- **Result Types:** Composable error propagation with `?`
- **No Panics:** Production-quality error handling

### 5. Release Binary Distribution
- **Standalone Exe:** No runtime or dependencies needed
- **Cross-Platform:** Compile for Windows/Mac/Linux
- **Small Size:** ~10MB, 100x smaller than Java

---

## CLI Examples

```bash
# Build
cargo build --release
./target/release/quiz_engine --help

# Take a quiz
./target/release/quiz_engine quiz --questions 100
cargo run -- quiz --questions 50

# Import questions
./target/release/quiz_engine import --file questions.md

# View history
./target/release/quiz_engine history
./target/release/quiz_engine history --session-id <uuid>
./target/release/quiz_engine history --session-id <uuid> --review

# Clear data
./target/release/quiz_engine clear --questions --confirm
./target/release/quiz_engine clear --history --all --confirm
```

---

## Success Criteria

### Functional Requirements
- ✓ Load 100+ random questions without showing answers
- ✓ NEVER repeat question until cycle exhausted
- ✓ Answers randomized and verified correctly
- ✓ Session persisted with full stats
- ✓ Import/history/clear operations work
- ✓ All CLI commands functional

### Non-Functional Requirements
- ✓ Performance: Load questions + display <100ms
- ✓ Usability: Full workflow <15 minutes
- ✓ Reliability: No panics, graceful error handling
- ✓ Maintainability: Clean separation of concerns
- ✓ Compatibility: Rust 1.70+, Windows/Mac/Linux
- ✓ Distribution: Single executable, no dependencies
- ✓ Binary Size: ~10MB

---

## Implementation Notes

- **Diesel Migrations:** Run `diesel migration run` after schema changes
- **Query Safety:** Diesel compile-time checks prevent SQL errors
- **Async Database:** Use Diesel pooling with Tokio runtime
- **Error Handling:** Use custom error enums for domain errors
- **Testing:** Use `#[test]` and integration test files
- **Logging:** Consider adding `tracing` crate for structured logging
- **Documentation:** Cargo doc comments on public APIs
- **Future:** Warp web framework for REST API, GraphQL server, WebAssembly target
