# Go/Golang/SQLite Quiz Engine for GH-200 Certification

## System Architecture Overview

### Directory Structure
```
quiz-engine/
├── main.go                   # Entry point, orchestrates quiz flow
├── cmd/
│   ├── quiz/main.go         # Quiz command entry point
│   ├── import/main.go        # Import questions command
│   ├── history/main.go       # View history command
│   └── clear/main.go         # Clear data command
├── internal/
│   ├── database/
│   │   ├── db.go            # SQLite connection, migrations
│   │   ├── question.go       # Question CRUD operations
│   │   ├── session.go        # Session CRUD operations
│   │   └── response.go       # Response CRUD operations
│   ├── models/
│   │   ├── question.go       # Question struct
│   │   ├── session.go        # QuizSession struct
│   │   └── response.go       # QuizResponse struct
│   ├── engine/
│   │   ├── quiz.go           # QuizEngine struct and methods
│   │   ├── shuffler.go       # Answer randomization
│   │   └── utils.go          # Helper functions
│   ├── cli/
│   │   ├── formatter.go      # Terminal formatting, colors
│   │   ├── prompts.go        # User input collection
│   │   └── display.go        # Question/result display
│   ├── parser/
│   │   └── markdown.go       # Markdown question parsing
│   └── service/
│       ├── quiz_service.go   # Business logic orchestration
│       ├── history_service.go # Query and format history
│       └── config.go         # Configuration management
├── go.mod                     # Go module definition
├── go.sum                     # Dependency lock file
├── Makefile                   # Build, test, run targets
├── README.md                  # Documentation
└── .gitignore
```

### Database Schema (Using go-sqlite3 or gorm/sqlite)

#### Table: questions
```sql
CREATE TABLE questions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    question_text TEXT NOT NULL,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT NOT NULL,
    option_d TEXT NOT NULL,
    option_e TEXT,
    correct_answer TEXT NOT NULL,
    explanation TEXT,
    section TEXT,
    difficulty TEXT,
    source_file TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usage_cycle INTEGER DEFAULT 1,
    times_used INTEGER DEFAULT 0,
    last_used_at TIMESTAMP,
    UNIQUE(question_text, correct_answer)
);
CREATE INDEX idx_questions_section ON questions(section);
CREATE INDEX idx_questions_difficulty ON questions(difficulty);
CREATE INDEX idx_questions_usage_cycle ON questions(usage_cycle);
```

#### Table: quiz_sessions
```sql
CREATE TABLE quiz_sessions (
    session_id TEXT PRIMARY KEY,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP,
    num_questions INTEGER NOT NULL,
    num_correct INTEGER DEFAULT 0,
    percentage_correct REAL DEFAULT 0.0,
    time_taken_seconds INTEGER
);
CREATE INDEX idx_sessions_date ON quiz_sessions(started_at DESC);
```

#### Table: quiz_responses
```sql
CREATE TABLE quiz_responses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    session_id TEXT NOT NULL,
    question_id INTEGER NOT NULL,
    user_answer TEXT NOT NULL,
    is_correct INTEGER DEFAULT 0,
    time_taken_seconds INTEGER,
    FOREIGN KEY (session_id) REFERENCES quiz_sessions(session_id),
    FOREIGN KEY (question_id) REFERENCES questions(id),
    UNIQUE(session_id, question_id)
);
CREATE INDEX idx_responses_session ON quiz_responses(session_id);
```

---

## Implementation Plan

### Phase 1: Project Setup & Database Layer
**Timeline:** 2-2.5 hours

**Objective:** Initialize Go project, setup SQLite database, define data models and DAOs.

**Tasks:**

1. **Create Go Project Structure:**
   ```bash
   go mod init github.com/pbaletkeman/quiz-engine
   mkdir -p cmd/{quiz,import,history,clear}
   mkdir -p internal/{database,models,engine,cli,parser,service}
   ```

2. **Define `go.mod` Dependencies:**
   - `github.com/mattn/go-sqlite3` (SQLite driver) or GORM with SQLite
   - `github.com/google/uuid` (UUID generation)
   - `github.com/spf13/cobra` (CLI framework)
   - `github.com/fatih/color` (Terminal colors)
   - `github.com/olekueleye/tablewriter` (Table formatting)

3. **Create `internal/models/` Structs:**
   ```go
   // Question struct
   type Question struct {
       ID              int64
       QuestionText    string
       OptionA         string
       OptionB         string
       OptionC         string
       OptionD         string
       OptionE         *string
       CorrectAnswer   string    // Hidden during quiz
       Explanation     *string   // Hidden during quiz
       Section         *string
       Difficulty      *string
       SourceFile      *string
       CreatedAt       time.Time
       UsageCycle      int64
       TimesUsed       int64
       LastUsedAt      *time.Time
   }

   // QuizSession struct
   type QuizSession struct {
       SessionID           string
       StartedAt           time.Time
       EndedAt             *time.Time
       NumQuestions        int64
       NumCorrect          int64
       PercentageCorrect   float64
       TimeTakenSeconds    *int64
   }

   // QuizResponse struct
   type QuizResponse struct {
       ID              int64
       SessionID       string
       QuestionID      int64
       UserAnswer      string
       IsCorrect       bool
       TimeTakenSeconds *int64
   }
   ```

4. **Create `internal/database/db.go`:**
   ```go
   type DB struct {
       conn *sql.DB
   }

   func NewDB(dbPath string) (*DB, error) {
       conn, err := sql.Open("sqlite3", dbPath)
       if err != nil {
           return nil, err
       }
       db := &DB{conn: conn}
       if err := db.Init(); err != nil {
           return nil, err
       }
       return db, nil
   }

   func (d *DB) Init() error {
       // Execute CREATE TABLE statements
       // Return error if any statement fails
   }

   func (d *DB) Close() error {
       return d.conn.Close()
   }
   ```

5. **Implement DAOs in `internal/database/`:**
   - `question.go`: GetRandomQuestions (cycle-aware), GetByID, CountAll, DeleteAll, MarkUsed, GetCurrentCycle, AdvanceCycle
   - `session.go`: CreateSession, GetSession, UpdateSession, ListSessions
   - `response.go`: SaveResponse, GetSessionResponses, CountCorrect

6. **Cycle-Aware Query in `question.go`:**
   ```go
   func (d *DB) GetRandomQuestions(ctx context.Context, n int, difficulty, section string) ([]Question, error) {
       // Query current cycle
       currentCycle := d.GetCurrentCycle(ctx)
       
       // Build query with WHERE usage_cycle = ? filter
       query := `
           SELECT id, question_text, option_a, option_b, option_c, option_d, option_e, section, difficulty
           FROM questions
           WHERE usage_cycle = ?
       `
       if difficulty != "" {
           query += " AND difficulty = ?"
       }
       if section != "" {
           query += " AND section = ?"
       }
       query += " ORDER BY RANDOM() LIMIT ?"
       
       // Execute query WITHOUT fetching correct_answer or explanation
       return scanQuestions(rows)
   }

   func (d *DB) MarkQuestionUsed(ctx context.Context, questionID int64) error {
       // UPDATE questions SET times_used = times_used + 1, last_used_at = NOW WHERE id = ?
       // Check if cycle exhausted, call AdvanceCycle if needed
   }

   func (d *DB) AdvanceCycle(ctx context.Context) error {
       // UPDATE questions SET usage_cycle = usage_cycle + 1 WHERE usage_cycle = ? AND times_used > 0
   }
   ```

7. **Test Database:**
   - Write integration test: create DB, insert questions, verify schema
   - `go test ./internal/database -v`

**Success Criteria:**
- SQLite file created and readable
- All three tables initialized with indexes
- DAOs functional for CRUD operations
- Cycle-aware queries tested and verified
- No questions fetched with correct_answer/explanation

---

### Phase 2: Quiz Engine & Core Logic
**Timeline:** 2-2.5 hours

**Objective:** Implement quiz flow, answer shuffling, scoring.

**Tasks:**

1. **Create `internal/engine/quiz.go`:**
   ```go
   type QuizEngine struct {
       sessionID    string
       questions    []Question
       responses    []QuizResponse
       db           *database.DB
       config       QuizConfig
   }

   type QuizConfig struct {
       NumQuestions   int
       SecondsPerQ    int
       TotalSeconds   int
   }

   func NewQuizEngine(db *database.DB, config QuizConfig) (*QuizEngine, error) {
       sessionID := uuid.New().String()
       return &QuizEngine{
           sessionID: sessionID,
           db:        db,
           config:    config,
       }, nil
   }

   func (qe *QuizEngine) LoadQuestions(ctx context.Context) error {
       // Load questions via cycle-aware DAO query
       // Verify correct_answer and explanation NOT in struct
   }

   func (qe *QuizEngine) SubmitAnswer(ctx context.Context, qIdx int, answer string, timeTaken int) error {
       // Verify user answer against stored correct answer
       // Create QuizResponse (is_correct computed here, not exposed to user)
       // Persist response to DB
   }

   func (qe *QuizEngine) Finalize(ctx context.Context) error {
       // Calculate final score
       // Mark all questions used
       // Auto-advance cycle if exhausted
       // Update session in DB
       // Return final stats
   }

   func (qe *QuizEngine) GetSessionReview(ctx context.Context) ([]QuestionWithAnswer, error) {
       // Fetch questions WITH correct_answer and explanation for review display
       // Match with user responses
   }
   ```

2. **Create `internal/engine/shuffler.go`:**
   ```go
   type ShuffledOptions struct {
       Options      []string
       AnswerMap    map[string]string // Shuffled position to original
       CorrectPos   string
   }

   func ShuffleAnswers(options []string, correctAnswer string) ShuffledOptions {
       // Randomize order
       // Track mapping
       // Return shuffled + mapping
   }
   ```

3. **Create `internal/service/quiz_service.go`:**
   - Wrapper around QuizEngine and DB
   - Public methods: StartQuiz, SubmitAnswer, CompleteQuiz, GetReview

4. **Create `internal/cli/` for formatting:**
   - `formatter.go` → table/box output with `color` package
   - `display.go` → question display (NEVER show correct_answer)
   - `prompts.go` → get user input

5. **Create `cmd/quiz/main.go`:**
   - Orchestrate quiz flow: load questions → loop answers → finalize → show score → ask review → ask repeat
   - Per-question timer with countdown
   - Global timer with warnings

6. **Test Quiz Flow:**
   - `go test ./internal/engine -v`
   - Run: `go run cmd/quiz/main.go --questions 5`
   - Verify cycle mechanics with 10 questions, 2 quizzes of 5

**Success Criteria:**
- Quiz loads questions without exposing correct answers
- Answers shuffled each quiz
- Scoring accurate
- Non-repetition cycle working (test with 10Q, 2 quizzes)
- Session persisted to DB
- Review shows all answers when requested

---

### Phase 3: Data Management (Import, History, Clear)
**Timeline:** 1.5-2 hours

**Objective:** Parse markdown, manage history, clear operations.

**Tasks:**

1. **Create `internal/parser/markdown.go`:**
   ```go
   type QuestionParser struct{}

   func (p *QuestionParser) ParseFile(filePath string) ([]models.Question, error) {
       // Read markdown file
       // Extract questions via regex
       // Validate 4-5 options, correct answer
       // Return Question slices
   }
   ```

2. **Create `cmd/import/main.go`:**
   - Parse markdown files
   - Batch insert with conflict handling
   - Report counts: imported, skipped, errors
   - Usage: `go run cmd/import/main.go --file questions.md`

3. **Create `cmd/history/main.go`:**
   - List all sessions with filters (date range, session ID)
   - Display modes: summary, review (with answers)
   - Export to CSV/JSON
   - Usage examples:
     ```bash
     go run cmd/history/main.go --summary
     go run cmd/history/main.go --session-id UUID --review
     go run cmd/history/main.go --export json --start-date 2025-01-01
     ```

4. **Create `cmd/clear/main.go`:**
   - Clear questions: `--questions --confirm`
   - Clear history: `--history --all --confirm`
   - Clear before date: `--history --before 30 --confirm`

5. **Test Each Operation:**
   - `go test ./cmd/... -v`
   - Manual test: import → take quiz → view history → export

**Success Criteria:**
- 100+ questions imported
- History queryable with filters
- Export works (CSV/JSON)
- Clear requires confirmation
- No data loss without --confirm flag

---

### Phase 4: CLI Polish & Deployment
**Timeline:** 1.5-2 hours

**Objective:** Polish CLI, add help, build executable, document.

**Tasks:**

1. **Enhance Cobra CLI:**
   - Add `--help` to all commands
   - Add `--version` flag
   - Error handling with colored output (red for errors, green for success)
   - Usage examples in help text

2. **Create `Makefile`:**
   ```makefile
   build:
       go build -o bin/quiz-engine cmd/quiz/main.go
       go build -o bin/quiz-import cmd/import/main.go
       go build -o bin/quiz-history cmd/history/main.go
       go build -o bin/quiz-clear cmd/clear/main.go

   run:
       go run cmd/quiz/main.go

   test:
       go test ./... -v -cover

   clean:
       rm -rf bin/
   ```

3. **Add Logging (optional):**
   - `go.uber.org/zap` for structured logging
   - Verbose mode: `--verbose` flag

4. **Create Comprehensive `README.md`:**
   - Getting Started (Go 1.21+)
   - Build instructions
   - Running quizzes
   - Importing questions
   - Viewing history
   - Clearing data
   - Troubleshooting
   - Architecture overview

5. **Cross-Platform Build:**
   ```bash
   # Windows
   GOOS=windows GOARCH=amd64 go build -o quiz-engine.exe
   
   # macOS
   GOOS=darwin GOARCH=amd64 go build -o quiz-engine-macos
   
   # Linux
   GOOS=linux GOARCH=amd64 go build -o quiz-engine-linux
   ```

6. **End-to-End Test:**
   - Build executable
   - Import questions
   - Take quiz
   - View history
   - Clear data
   - No errors

**Success Criteria:**
- All CLI commands polished
- Executable builds for Windows/Mac/Linux
- README comprehensive
- First-time user success in <15 minutes
- Proper error messages

---

## Core Design Decisions

### 1. Go Concurrency Model
- **Approach:** Goroutines for independent tasks (future websocket support)
- **Rationale:** Go's lightweight concurrency excellent for I/O-bound operations
- **Current:** Sequential quiz flow, but structured for async expansion

### 2. SQLite with Pragmatic Approach
- **sqlite3 driver:** `github.com/mattn/go-sqlite3` (pure C bindings)
- **Alternative:** GORM with SQLite (more verbose but type-safe)
- **Choice:** Direct sql/database for minimal dependencies

### 3. Non-Repetition Cycling
- **Identical to Python version:**
  - Track usage_cycle and times_used for each question
  - Query: `WHERE usage_cycle = current_cycle`
  - Auto-advance when cycle exhausted
  - Ensures no repeats until full cycle seen

### 4. Answer Shuffling & Concealment
- **During Quiz:** Only fetch id, question_text, options A-E (NO correct_answer)
- **Verification:** Correct answer checked in Go business logic only
- **Review:** Full questions with answers fetched ONLY on explicit review request

### 5. CLI Framework Choice
- **Cobra:** Standard Go CLI framework
- **Rationale:** Rich ecosystem, built-in help/completion, scales well
- **Alternative considered:** Flag package (too verbose)

### 6. Terminal Formatting
- **fatih/color:** ANSI color output
- **tablewriter:** ASCII table formatting
- **Rationale:** Minimal, performant, widely used

### 7. Distribution Strategy
- **Single Binary:** No runtime dependencies (SQLite embedded)
- **Build:** Go's cross-compilation: Windows/Mac/Linux from single system
- **Deployment:** `make build` produces executables

### 8. Error Handling
- **Pattern:** Return errors explicitly (no panics in production)
- **Context:** Use context.Context for timeouts and cancellation
- **Logging:** Structured logging with optional verbose mode

---

## CLI Examples

```bash
# Build all executables
make build

# Take a quiz (100 questions, 60 sec per Q)
./bin/quiz-engine quiz --questions 100

# Take a quick quiz (20 questions)
./bin/quiz-engine quiz -q 20

# Import questions from markdown
./bin/quiz-engine import --file questions.md

# View all quiz history
./bin/quiz-engine history

# View specific session with answers
./bin/quiz-engine history --session-id abc123def456 --review

# Export history to CSV
./bin/quiz-engine history --export csv --output history.csv

# Export history to JSON with answers
./bin/quiz-engine history --export json --include-answers

# View sessions from last 7 days
./bin/quiz-engine history --since 7d

# Clear all questions (requires confirmation)
./bin/quiz-engine clear --questions --confirm

# Clear all history (requires confirmation)
./bin/quiz-engine clear --history --all --confirm

# Clear history from before 30 days ago
./bin/quiz-engine clear --history --before 30d --confirm
```

---

## Success Criteria

### Functional Requirements
- ✓ Load random questions filtered by cycle (no repeats until cycle exhausted)
- ✓ Answers shuffled per question
- ✓ Correct answers verified internally (never exposed during quiz)
- ✓ Per-question timer with countdown display
- ✓ Global timer with warnings
- ✓ Session persisted with all stats
- ✓ History queryable with multiple filters
- ✓ Full answer review available after quiz (user can opt-in)
- ✓ Import/clear operations working

### Non-Functional Requirements
- ✓ Performance: Load 100 questions + display first question <500ms
- ✓ Single binary: No external dependencies at runtime
- ✓ Cross-platform: Native executables for Windows/Mac/Linux
- ✓ Usability: Complete workflow in <15 minutes
- ✓ Error handling: Graceful failures with helpful messages
- ✓ Code quality: <5% test coverage requirement (well-tested DAOs)

---

## Implementation Notes

- **Database Path:** `./quiz.db` by default (configurable via flag)
- **Session Storage:** UUID identifies each quiz session
- **Answer Format:** Store A-E as string (not int)
- **Thread Safety:** SQLite serialized mode (default) ensures transaction safety
- **Migrations:** Schema version 1 (can add versioning later)
- **Cycle Logic:** Identical to Python and other implementations
- **Concealment:** Regex can be used for markdown parsing or standard string matching
- **Future:** Could add web UI (Gin), REST API, or export formats

