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
│   ├── database_tests.rs
│   ├── service_tests.rs
│   └── integration_tests.rs
├── migrations/
│   └── 2024-03-23-000000_create_tables/
│       └── up.sql
└── README.md                               # Documentation
```

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

### Phase 4: Testing & Packaging
**Timeline:** 1-1.5 hours

**Objective:** Comprehensive testing, compile to release binary.

**Tasks:**

1. **Write Unit Tests:**
   ```bash
   cargo test
   ```
   - Test repositories: CRUD operations
   - Test services: Business logic
   - Test utilities: Shuffling, parsing

2. **Write Integration Tests:**
   - Full quiz flow: load → submit → finalize
   - Cycle mechanics verification
   - Non-repetition across quizzes

3. **Build Release Binary:**
   ```bash
   cargo build --release
   ```
   - Single executable in `target/release/quiz_engine`
   - ~10MB binary size
   - No runtime dependencies

4. **Cross-Compilation (Optional):**
   ```bash
   cargo install cross
   cross build --release --target x86_64-pc-windows-gnu
   cross build --release --target x86_64-apple-darwin
   ```

5. **Write Comprehensive README:**
   - **Getting Started:** Rust 1.70+ requirement
   - **Installation:** `cargo build --release`
   - **Running Quizzes:** `./target/release/quiz_engine quiz`
   - **CLI Commands:** quiz, import, history, clear
   - **Configuration:** Environment variables via `.env`
   - **Architecture:** Diesel ORM, Clap CLI, async Tokio
   - **Testing:** How to run tests with `cargo test`

6. **Final Testing:**
   - Full end-to-end workflow
   - Create → Import → Take Quiz → View History → Retake
   - Verify cycle mechanics
   - Cross-platform execution
   - Performance profiling

**Success Criteria:**
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
