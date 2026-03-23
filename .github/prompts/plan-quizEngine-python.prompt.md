# Python/SQLite Quiz Engine for GH-200 Certification

## System Architecture Overview

### Directory Structure
```
quiz-engine/
├── quiz_engine/
│   ├── __init__.py
│   ├── main.py           # Entry point, orchestrates quiz flow
│   ├── database.py       # SQLite CRUD operations, schema
│   ├── quiz.py           # QuizEngine class (load, submit, finalize, score)
│   ├── history.py        # History query and formatting
│   ├── models.py         # Data classes: Question, QuizSession, QuizResponse
│   ├── utils.py          # Helpers: markdown parsing, answer shuffling, time
│   └── cli.py            # User input prompts, progress display, formatting
├── scripts/
│   ├── import_questions.py   # Parse MD → validate → insert into DB
│   ├── view_history.py       # Query sessions, format output, export CSV/JSON
│   ├── clear_database.py     # Truncate questions table
│   └── clear_history.py      # Delete quiz sessions & responses
├── requirements.txt      # Python dependencies
├── setup.sh              # Create venv, install deps - bash shell
├── setup.bat             # Create venv, install deps - Windows batch file
├── setup.ps1             # Create venv, install deps - Windows PowerShell script
├── README.md             # Setup, usage, operation docs
└── .gitignore
```

### Database Schema

#### Table: questions
Stores all GH-200 practice questions imported from markdown files.

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
Captures quiz metadata and overall score per quiz attempt.

```sql
CREATE TABLE quiz_sessions (
    session_id TEXT PRIMARY KEY,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP,
    num_questions INTEGER NOT NULL,
    num_correct INTEGER DEFAULT 0,
    percentage_correct REAL DEFAULT 0.0,
    time_taken_seconds INTEGER,
    UNIQUE(session_id)
);
CREATE INDEX idx_sessions_date ON quiz_sessions(started_at DESC);
```

#### Table: quiz_responses
Records individual question responses within a quiz session.

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

### Phase 1: Foundation (Setup & Database)
**Timeline:** 1-2 hours

**Objective:** Create project structure, SQLite schema, data models, and initialization scripts.

**Tasks:**
1. Set up directory structure and create `__init__.py` files.
2. Create `requirements.txt` with 5 core dependencies:
   - typer[all]==0.9.0
   - rich==13.7.0
   - pydantic==2.5.0
   - python-dateutil==2.8.2
   - sqlite3 (built-in)
3. Write `database.py`:
   - `init_database()` → creates 3 tables with indexes
   - `get_questions_by_id(ids)` → fetch specific questions
   - `count_questions()` → total question count
   - `delete_all_questions()` → truncate questions table
   - **`get_current_cycle()`** → determine MIN(usage_cycle) for questions not yet exhausted in this cycle
   - **`get_random_questions(n, difficulty=None, section=None)`** → SQL query that:
     1. Determines current_cycle from `get_current_cycle()`
     2. Queries: `SELECT ... WHERE usage_cycle = current_cycle [AND difficulty/section filters] ORDER BY RANDOM() LIMIT n`
     3. Returns questions WITHOUT correct_answer/explanation
     4. **NEVER returns same question twice until all questions in current cycle are used**
   - **`mark_question_used(question_id)`** → increment times_used, update last_used_at, check if cycle exhausted
   - **`advance_questions_to_next_cycle()`** → after all questions at current cycle used, increment usage_cycle for those questions, reset them into active pool
4. Write `models.py`:
   - Pydantic `Question` class (id, question_text, options A-E, correct_answer, explanation, section, difficulty)
   - Pydantic `QuizSession` class (session_id, started_at, ended_at, num_questions, num_correct, etc.)
   - Pydantic `QuizResponse` class (session_id, question_id, user_answer, is_correct, time_taken)
   - **CRITICAL:** Ensure Question model does NOT include correct_answer or explanation in __repr__ or public display
5. Create `setup.sh` to automate venv creation and dependency installation.
6. Test database initialization: `python -m quiz_engine.main --init` should succeed.

**Success Criteria:**
- SQLite file created at `quiz_engine/quiz.db`
- Schema initialized with 3 tables, indexes, foreign keys
- Models importable and validated with Pydantic
- No errors during setup

---

### Phase 2: Core Quiz Logic (Question Loading, Timing, Scoring)
**Timeline:** 2-3 hours

**Objective:** Implement interactive quiz flow with randomization, per-question timing, and scoring.

**Tasks:**
1. Write `utils.py`:
   - `shuffle_answers(options)` → randomize A-E, track correct answer position, return ONLY (shuffled_options_list, answer_mapping)
   - `get_random_questions(n, difficulty=None, section=None)` → SQL RANDOM() query, **NEVER fetch explanation or correct_answer in query result**
   - `format_question_display(question, shuffled_options)` → colored terminal output with Rich, **DISPLAY ONLY question_text and shuffled options A-E, NO correct answers**
   - `format_answer_feedback(is_correct)` → brief emoji/text feedback ("✓ Saved!" or "✗ Saved!"), **NO correct answer shown**
   - `calculate_score(num_correct, num_total)` → percentage calculation
   - `markdown_to_questions(file_path)` → parse gh-200-iteration-*.md, extract explanations
2. Write `quiz.py` with `QuizEngine` class:
   - `__init__(session_id, config: QuizConfig)` → initialize session with user-provided config
   - `load_questions()` → fetch random questions via SQL **using cycle-aware logic, WITHOUT correct_answer/explanation**, store in memory
   - `submit_answer(question_idx, user_answer, time_taken)` → record response, calculate correct/incorrect internally, persist to DB **only session_id, question_id, user_answer, is_correct, time_taken**
   - `finalize()` → calculate final score, close session timestamp, save to quiz_sessions table, **mark all used questions as used via `mark_question_used()`, check if current cycle exhausted, auto-advance to next cycle if needed**
   - `get_session_review()` → **NEW METHOD** fetch all questions WITH correct_answer and explanation for review-only display
   - `get_results()` → return session summary (correct count, percentage, time) WITHOUT answer details
3. Write `cli.py`:
   - `prompt_config()` → ask user for (num_questions=100, seconds_per=60, total_minutes=90)
   - `display_question(question, options, question_num, total)` → formatted display with countdown timer, **CLEAR INSTRUCTION: "A-E to answer, ENTER to skip" NO HINTS**
   - `get_answer_input()` → validate A-E input with retry logic, accept only A-E or Skip, **NO feedback on correctness during input**
   - `display_answer_feedback(is_correct, time_taken)` → minimal feedback (icon only: ✓ or ✗), **NEVER show correct answer here**
   - `display_final_review(session, all_questions, responses)` → **NEW: show all responses with shuffled question options, user answer (marked ✓/✗), correct answer, and explanation for EACH question**
   - `display_results(session)` → show score, time, percentage **WITHOUT answer details**
   - `prompt_continue()` → ask user to retake, review answers, or exit
4. Write `main.py`:
   - Entry point orchestrating: initialize DB → prompt config → loop through questions → finalize → display score/time → **OFFER ANSWER REVIEW** → ask retake/exit
   - Timer logic: countdown per question (Ctrl+C to skip), global time warning (10 min remaining)
   - **CRITICAL FLOW:**
     1. Load questions (no answers)
     2. Display each question (no correct answer visible)
     3. Get user input (A-E or Skip)
     4. After last question → finalize
     5. Show summary: "X/Y correct, Z%, time: MM:SS"
     6. Prompt: "View answer key? (y/n)"
     7. If yes → display_final_review() with ALL correct answers and explanations
     8. Prompt: "Take another quiz? (y/n)" or Exit
5. Test full quiz flow: `python -m quiz_engine.main --questions 5` (5-question quiz for test)
6. **Test non-repetition cycle:**
   - Load 10 questions, take 2 quizzes of 5 questions each
   - Verify: First quiz uses 5 from cycle 1, second quiz uses different 5 from cycle 1
   - Verify: After 2nd quiz, all 10 marked used, cycle incremented to 2
   - Take 3rd quiz: should load from cycle 2 (questions available again)
   - Log confirms no duplicates within or across cycles

**Success Criteria:**
- Quiz loads 100 random questions (or user-specified count)
- **NO questions repeat until entire question pool exhausted at current cycle**
- All answers randomized and correctly tracked
- Per-question timer functional (visual countdown in Rich)
- Score calculation accurate (e.g., 8/10 = 80%)
- Session persisted to DB with UUID
- User can retake quiz
- Database fields (usage_cycle, times_used, last_used_at) properly maintained
- Cycle auto-advances when all questions exhausted

---

### Phase 3: Data Management (Import, History, Clear Operations)
**Timeline:** 1-2 hours

**Objective:** Populate questions from markdown, query history, and safely clear data.

**Tasks:**
1. Write `import_questions.py` script:
   - Parse files matching `gh-200-iteration-*.md`
   - Extract questions via regex or markdown parser (pyparsing or custom)
   - Validate: 4-5 options per question, exactly one correct answer (A-E)
   - Batch insert into questions table with conflict handling (UNIQUE constraint)
   - Report: X questions imported, Y skipped (duplicates), Z errors
   - Usage: `python scripts/import_questions.py --file md/gh-200-iteration-1.md`
2. Write `history.py`:
   - `get_all_sessions()` → query quiz_sessions with pagination
   - `get_session_details(session_id)` → retrieve responses WITH correct answers and explanations
   - `format_session_history(session)` → markdown table: date, duration, score, Q count
   - `format_session_review(session_id)` → **NEW: format complete answer key with incorrect answers FIRST (by section), then correct answers, each with: user_answer (✓/✗), correct_answer, explanation, section**
   - `export_to_csv(sessions, file_path)` → write history to CSV (with or without answers)
   - `export_to_json(sessions, file_path)` → write history to JSON (with or without answers)
3. Write `scripts/view_history.py`:
   - Display all quiz sessions in table format
   - Filters (optional): `--session-id UUID`, `--start-date`, `--end-date`
   - Display modes:
     - `--summary`: Show only score/time (default)
     - `--review`: Show full answer key with correct answers and explanations for selected session
   - Export options: `--export csv` or `--export json`, `--include-answers` (default: summary only)
   - Usage: `python scripts/view_history.py --session-id abc123def456 --review` (show full answer key)
   - Usage: `python scripts/view_history.py --export json --start-date 2025-01-01` (export summaries)
4. Write `scripts/clear_database.py`:
   - Truncate questions table with confirmation prompt
   - Usage: `python scripts/clear_database.py --confirm`
5. Write `scripts/clear_history.py`:
   - Delete specific session: `--session-id UUID`
   - Delete all history: `--all`
   - Delete sessions older than X days: `--before 30`
   - Usage: `python scripts/clear_history.py --session-id abc123 --confirm`
6. Test each operation with sample data

**Success Criteria:**
- 100+ questions imported and queryable
- History shows all quiz sessions with scores
- Incorrect answers listed first, then correct answers
- CSV/JSON export working
- Clear operations require confirmation and succeed without errors

---

### Phase 4: CLI Polish & README
**Timeline:** 1-2 hours

**Objective:** Enhance user experience and document the system.

**Tasks:**
1. Add Rich formatting to all CLI output:
   - Colored headers, progress bars, styled tables
   - Error messages in red, success in green
   - Clear visual separation between sections
2. Implement graceful error handling:
   - Network timeouts (none expected, but future-proof)
   - Invalid database state (auto-repair)
   - User input validation with retries
   - Keyboard interrupt handling (Ctrl+C) with session save
3. Add verbose logging (optional `--verbose` flag):
   - Log DB queries, timings, imports
   - Help with debugging
4. Create comprehensive `README.md`:
   - **Getting Started:** Python 3.9+ requirement, venv setup, `setup.sh` usage
   - **Configuration:** Sensible defaults (100 questions, 60 sec/Q, 90 min)
   - **Taking Quizzes:** How to run `python -m quiz_engine.main`, what to expect
   - **Importing Questions:** How to add markdown files, validation rules
   - **Reviewing History:** How to view past quiz results, export to CSV/JSON
   - **Clearing Data:** How to safely clear questions or history
   - **Troubleshooting:** Common issues (no questions, DB locked, etc.)
   - **Architecture:** Diagram or description of system components
5. Add `--help` and `--version` flags to all scripts
6. Test end-to-end workflow for a "new user":
   - Install Python
   - Run setup.sh
   - Import questions
   - Take a quiz
   - View history

**Success Criteria:**
- All CLI commands execute cleanly with helpful output
- README covers all major operations
- First-time user can complete full workflow in <15 minutes
- No unhandled exceptions in normal usage

---

## Answer Concealment Policy (CRITICAL)

**During Quiz:**
- ❌ NEVER display correct_answer field on screen
- ❌ NEVER show explanations during quiz
- ❌ NEVER indicate whether user's answer is correct (only icon: ✓ or ✗ saved)
- ✓ Show only: question_text, shuffled options (A-E), timer

**After Quiz Finalization:**
- ✓ Show summary: "X/Y correct, Z%, time: MM:SS"
- ✓ Prompt user: "Review answer key? (y/n)"
- ✓ IF yes → display_final_review() shows ALL answers with explanations
- ✓ Incorrect answers displayed FIRST (grouped by section), then correct answers

**Data Layer:**
- SELECT queries during quiz MUST use SELECT id, question_text, option_a, option_b, option_c, option_d, option_e (NO correct_answer, NO explanation)
- Correct answer verification happens in Python logic only (never exposed to CLI)
- Explanations fetched only on explicit review request

**Query Examples:**
```sql
-- DURING QUIZ (✓ safe)
SELECT id, question_text, option_a, option_b, option_c, option_d, option_e
FROM questions ORDER BY RANDOM() LIMIT 100;

-- DURING ANSWER SUBMISSION (✓ safe - verify internally in Python)
SELECT correct_answer FROM questions WHERE id = ? (used only in business logic, never printed)

-- DURING REVIEW (✓ safe - user opted in)
SELECT id, question_text, option_a, option_b, option_c, option_d, option_e,
       correct_answer, explanation, section
FROM questions WHERE id IN (...) JOIN quiz_responses ...
```

---

## Core Features & Design Decisions

### 1. Question Randomization & Non-Repetition Cycling
- **Approach:** SQL `ORDER BY RANDOM()` with LIMIT N, filtered by `usage_cycle` to prevent repetition until all questions seen
- **Rationale:** Scalable, DB-level optimization, ensures spaced repetition before repeating questions
- **Non-Repetition Algorithm:**
  1. Track each question's `usage_cycle` (starts at 1) and `times_used` count
  2. Determine the current global cycle = MIN(usage_cycle) across all unused questions in the pool
  3. Query: `SELECT ... WHERE usage_cycle = current_cycle ORDER BY RANDOM() LIMIT n`
  4. When a question is used in a quiz, increment `times_used` and update `last_used_at`
  5. After each quiz, check if all questions at current cycle have been used
  6. When all questions at current cycle are exhausted, increment `usage_cycle` for those questions to cycle them back into pool
  7. This ensures NO question repeats until ALL questions have been seen at least once
- **Implementation:** `get_random_questions(n, difficulty, section)` in database.py with cycle-aware SELECT logic

### 2. Non-Repetition Question Cycling
- **Objective:** Prevent question repetition until all questions have been seen at least once
- **Mechanism:**
  - Track `usage_cycle` (integer starting at 1) for each question
  - When loading quiz questions, query only questions WHERE `usage_cycle = current_global_cycle` (min used cycle)
  - After quiz finalization, mark each used question with `times_used++` and `last_used_at=now`
  - When ALL questions at the current cycle have `times_used > 0`, increment `usage_cycle` for those questions to cycle them back into the pool
  - Next quiz automatically uses questions from the NEW cycle (which now have usage_cycle incremented)
- **Example Flow (100 questions):**
  1. Quiz 1: Load 50 random questions from cycle 1; mark them used
  2. Quiz 2: Load 50 more random questions from cycle 1 (different ones); mark them used
  3. All 100 now used. System auto-increments usage_cycle to 2 for all questions.
  4. Quiz 3: Load 50 random from cycle 2 (all questions available again, fresh cycle)
  5. Repeat indefinitely without seeing question repeats until full cycle exhausted
- **Benefits:**
  - Ensures true randomization without duplication in learning sessions
  - Supports spaced repetition by cycling questions back after all seen
  - DB-driven: scales to 1M+ questions without performance penalty

### 3. Answer Shuffling & Concealment
- **Per-Question Timer:** Countdown display (Rich progress bar), user can skip
- **Global Limit:** Total time enforced at finalize (warn at 10 min remaining)
- **Handling:** Unanswered questions auto-marked as wrong (fail-safe)
- **Review Phase:** Full answer key with explanations shown ONLY after quiz complete + user opts-in to review

### 4. History Storage & Display
- **Query Time:** Answers and explanations fetched from questions table only when explicitly reviewing or exporting
- **Summary Display:** Quiz summaries NEVER include correct answers (score/time only)
- **Review Display:** Full answer key shown ONLY in dedicated review (incorrect answers FIRST by section, then correct answers)
- **Format:** Incorrect answers listed FIRST (by section), then correct answers, each with explanation
- **Rationale:** User sees their weaknesses first, facilitates learning, enforces spaced repetition
- **Retention:** Unlimited history (can be cleared manually)

### 5. Sensible Defaults
- **Questions:** 100 (realistic exam prep volume)
- **Time per Q:** 60 seconds (GitHub Actions questions typically 1-2 min)
- **Total Quiz:** 90 minutes (covers 100 questions at 54 sec avg)
- **Rationale:** Aligns with exam conditions, reduces decision paralysis

### 6. Import Format
- **Input:** Markdown files (gh-200-iteration-*.md)
- **Parsing:** Regex or custom markdown parser (simple structure assumed)
- **Validation:** Duplicate detection, answer count validation
- **Conflict:** UNIQUE constraint prevents duplicate questions

### 7. Database Choice
- **SQLite:** Zero-config, file-based, ACID transactions, suitable for <100k questions
- **Alternatives considered:** PostgreSQL (overkill for local), in-memory (no persistence)

### 8. Python Stack
- **Typer:** Type-safe CLI, modern, intuitive defaults
- **Rich:** Terminal colors, progress bars, formatted tables
- **Pydantic:** Data validation, type hints, structured errors
- **python-dateutil:** Flexible datetime parsing and arithmetic

### 9. Session Persistence
- **UUID:** Each quiz session gets unique identifier (uuid4)
- **Timestamps:** started_at, ended_at, time_taken_seconds tracked
- **Atomicity:** Quiz response saved per-question (allows crash recovery)
- **Index:** Fast history queries on session_id and date

### 10. Error Handling Strategy
- **DB Errors:** Rollback, notify user, suggest troubleshooting
- **Input Validation:** Retry with helpful prompts
- **Timeout:** Skip question, unanswered=wrong, continue
- **Corrupt Data:** Validate on load, auto-repair if possible

### 11. Extensibility
- **Future:** Add difficulty filtering, section filtering, timed mode variations
- **Hook:** `get_random_questions(n, difficulty, section)` supports filtering

### 12. Testing Strategy
- **Unit Tests:** database.py (CRUD), utils.py (shuffling, parsing)
- **Integration Tests:** quiz.py (load → submit → finalize flow)
- **Manual Tests:** CLI end-to-end, history export, clear operations

---

## Python Dependencies

```
typer[all]==0.9.0           # Modern CLI framework with type safety
rich==13.7.0                # Terminal formatting, colors, progress bars
pydantic==2.5.0             # Data validation and type hints
python-dateutil==2.8.2      # Flexible datetime utilities
sqlite3                     # Built-in SQL database (no install needed)
uuid                        # Built-in unique identifiers (no install needed)
```

Total install size: ~50MB (uncompressed)

---

## CLI Operations & Examples

### 1. Take a Quiz (Interactive Flow)
```bash
python -m quiz_engine.main
```

**Interactive Flow:**
```
1. SETUP
   "How many questions? [100]: " → User input: 50
   "Seconds per question? [60]: " → User input: 45
   "Total time in minutes? [90]: " → User input: 60

2. QUIZ BEGINS
   ┌─────────────────────────────────────────────────────────────┐
   │ Question 1 of 50                            ⏱️ 00:45         │
   ├─────────────────────────────────────────────────────────────┤
   │ Which of the following describes GitHub Actions?             │
   │                                                              │
   │ A) A CI/CD automation platform                              │
   │ B) A version control system                                 │
   │ C) A project management tool                                │
   │ D) A deployment service                                     │
   │ E) A code editor                                            │
   │                                                              │
   │ Enter your answer (A-E) or press ENTER to skip: A          │
   └─────────────────────────────────────────────────────────────┘

3. CONTINUE THROUGH ALL QUESTIONS
   (Same flow: display → input →  next question)

4. QUIZ ENDS
   ┌─────────────────────────────────────────────────────────────┐
   │                        QUIZ COMPLETE                         │
   ├─────────────────────────────────────────────────────────────┤
   │ Score: 34 / 50 correct                                       │
   │ Percentage: 68%                                              │
   │ Time: 32:15                                                  │
   │                                                              │
   │ Would you like to review the answer key? (y/n): y          │
   └─────────────────────────────────────────────────────────────┘

5. ANSWER REVIEW (IF USER SELECTED YES)
   ┌─────────────────────────────────────────────────────────────┐
   │                      ANSWER KEY REVIEW                       │
   ├─────────────────────────────────────────────────────────────┤
   │
   │ ✗ INCORRECT ANSWERS (6 total)
   │ ─────────────────────────────────────────────────────────────
   │
   │ Question 2: Which of the following...
   │   Your answer: B - A version control system
   │   Correct answer: A - A CI/CD automation platform
   │   Explanation: GitHub Actions is GitHub's native CI/CD...
   │   Section: Fundamentals
   │
   │ Question 7: What is a workflow file...
   │   Your answer: C - A build configuration
   │   Correct answer: D - A YAML file that defines automation
   │   Explanation: Workflow files are YAML files stored in...
   │   Section: Workflow Structure
   │
   │ [... more incorrect answers ...]
   │
   │ ✓ CORRECT ANSWERS (44 total)
   │ ─────────────────────────────────────────────────────────────
   │
   │ Question 1: Which of the following describes GitHub Actions?
   │   Your answer: A - A CI/CD automation platform ✓
   │   Explanation: GitHub Actions is GitHub's native CI/CD...
   │   Section: Fundamentals
   │
   │ [... more correct answers ...]
   │
   └─────────────────────────────────────────────────────────────┘

6. RETAKE OR EXIT
   "Take another quiz? (y/n): "
   → y: Return to step 1 (SETUP)
   → n: Exit application
```

**Key Points:**
- Answers are NEVER shown during questions
- User must explicitly opt-in to "review answer key"
- Review shows all answers grouped: incorrect first, then correct
- Each answer includes: user response, correct answer, explanation, section

---

### 2. Import Questions from Markdown
```bash
python scripts/import_questions.py --file md/gh-200-iteration-1.md
python scripts/import_questions.py --dir md/  # Import all files
```
**Output:** "Imported 50 questions, 3 skipped (duplicates), 0 errors"

---

### 3. View Quiz History (Summary)
```bash
python scripts/view_history.py
python scripts/view_history.py --session-id abc123def456 --summary
python scripts/view_history.py --start-date 2025-01-01 --export json
```
**Output:** Table showing date, duration, score, Q count (NO answers shown)

### 3b. Review Quiz Answers
```bash
python scripts/view_history.py --session-id abc123def456 --review
python scripts/view_history.py --session-id abc123def456 --review --export json
```
**Output:** Full answer key with: user_answer (✓/✗), correct_answer, explanation, section for each question (incorrect first, then correct)

---

### 4. Clear All Questions
```bash
python scripts/clear_database.py --confirm
```
**Output:** "Deleted 150 questions. Database ready for import."

---

### 5. Clear Quiz History
```bash
python scripts/clear_history.py --session-id abc123 --confirm
python scripts/clear_history.py --all --confirm
python scripts/clear_history.py --before 30 --confirm  # Delete sessions older than 30 days
```
**Output:** "Deleted 1 session and 45 responses."

---

## README.md Structure

### Sections (9 total)

1. **Overview** – What it is, why it exists, who it's for
2. **Getting Started** – Python version, venv setup, installation steps
3. **Configuration** – Default values, customization options
4. **Taking Quizzes** – How to launch, quiz flow (no answers during), answer review at end
5. **Quiz Flow Explained** – Step-by-step: load → question display → answer input → finalize → review option → retake/exit
6. **Adding Questions** – Markdown format, import process, validation, explanation field
7. **Reviewing Quiz Results** – Viewing quiz history summaries, detailed answer reviews with explanations
8. **Clearing Data** – Removing questions or history safely
9. **Troubleshooting & FAQ** – Common issues and solutions

---

## Success Criteria

### Functional Requirements
- ✓ Load 100+ random questions from SQLite (WITHOUT correct answers)
- ✓ **NEVER repeat a question in subsequent quizzes until ALL questions have been used at least once** (usage_cycle tracking)
- ✓ Auto-cycle questions back into pool after all exhausted at current cycle
- ✓ Randomize answers per question, conceal correct answer during display
- ✓ Per-question and global timers
- ✓ Calculate and display score (initially WITHOUT answer details)
- ✓ Persist session and responses to DB
- ✓ Import questions from markdown with explanations
- ✓ View quiz history summaries (scores/times, no answers)
- ✓ Review answers with full explanation ONLY after quiz complete + user opt-in
- ✓ View quiz history with incorrect-first ordering in review mode
- ✓ Clear questions and history safely
- ✓ All CLI commands functional and well-documented

### Non-Functional Requirements
- ✓ Performance: Load 100 questions + display first in <1 second
- ✓ Usability: New user completes full workflow in <15 minutes
- ✓ Reliability: No crashes on normal usage; graceful error handling
- ✓ Maintainability: Clean code, documented functions, extensible architecture
- ✓ Compatibility: Python 3.9+, Windows/Mac/Linux, no system dependencies

---

## Non-Repetition Question Cycling (Implementation Reference)

### Overview
The quiz engine prevents question repetition using a "usage cycle" mechanism. Questions are never repeated until **all questions have been used at least once**, at which point they cycle back into the pool fresh.

### How It Works

#### 1. Database Fields (per question)
```
usage_cycle       INTEGER DEFAULT 1       # Cycle number this question is in
times_used        INTEGER DEFAULT 0       # How many times question was used
last_used_at      TIMESTAMP               # When question was last used
```

#### 2. Current Cycle Determination
```python
def get_current_cycle():
    """Find the minimum usage_cycle currently in use."""
    cursor.execute("SELECT MIN(usage_cycle) FROM questions")
    return cursor.fetchone()[0] or 1
```

#### 3. Loading Questions (Cycle-Aware)
```python
def get_random_questions(n, difficulty=None, section=None):
    """Load n random questions from current cycle only."""
    current_cycle = get_current_cycle()

    query = "SELECT id, question_text, option_a, option_b, option_c, option_d, option_e "
    query += "FROM questions WHERE usage_cycle = ? "

    if difficulty:
        query += "AND difficulty = ? "
    if section:
        query += "AND section = ? "

    query += "ORDER BY RANDOM() LIMIT ?"

    # This ensures ONLY questions from current cycle selected
    # NO repeats until entire cycle exhausted
```

#### 4. Marking Questions Used (During Quiz Finalization)
```python
def mark_question_used(question_id):
    """Increment usage counter and update timestamp."""
    cursor.execute("""
        UPDATE questions
        SET times_used = times_used + 1, last_used_at = CURRENT_TIMESTAMP
        WHERE id = ?
    """, (question_id,))

    # Check if this question was the last one in its cycle
    cursor.execute("""
        SELECT COUNT(*) FROM questions
        WHERE usage_cycle = (SELECT usage_cycle FROM questions WHERE id = ?)
        AND times_used = 0
    """, (question_id,))

    remaining = cursor.fetchone()[0]
    if remaining == 0:
        # All questions in this cycle have been used
        auto_advance_cycle()
```

#### 5. Auto-Cycling to Next Cycle
```python
def advance_questions_to_next_cycle():
    """Push all used questions to next cycle."""
    current_cycle = get_current_cycle()

    cursor.execute("""
        UPDATE questions
        SET usage_cycle = usage_cycle + 1
        WHERE usage_cycle = ? AND times_used > 0
    """, (current_cycle,))

    # Now questions are ready for re-use in a fresh cycle
```

### Example Trace (10 Questions, 5 per Quiz)

**Initial State:**
```
Q1-Q10: usage_cycle=1, times_used=0
current_cycle = 1
```

**Quiz 1 (5 questions):**
```
SELECT ... WHERE usage_cycle=1 ORDER BY RANDOM() LIMIT 5
→ Returns: Q3, Q7, Q1, Q9, Q5

After Quiz 1:
Q1: times_used=1
Q3: times_used=1
Q5: times_used=1
Q7: times_used=1
Q9: times_used=1
Q2,Q4,Q6,Q8,Q10: times_used=0
current_cycle = 1 (still have unused questions)
```

**Quiz 2 (5 questions):**
```
SELECT ... WHERE usage_cycle=1 ORDER BY RANDOM() LIMIT 5
→ Returns: Q2, Q4, Q6, Q8, Q10 (guaranteed different from Quiz 1!)

After Quiz 2:
ALL Q1-Q10: times_used=1
remaining in cycle 1 = 0
→ TRIGGER: advance_questions_to_next_cycle()

After cycle advance:
Q1-Q10: usage_cycle=2, times_used=1
current_cycle = 2
```

**Quiz 3 (5 questions):**
```
SELECT ... WHERE usage_cycle=2 ORDER BY RANDOM() LIMIT 5
→ Returns: Q4, Q1, Q9, Q6, Q2 (can repeat now, fresh cycle!)

After Quiz 3:
Q1: times_used=2, usage_cycle=2
Q2: times_used=2, usage_cycle=2
... (cycle continues)
```

### Key Guarantees
1. **No duplicates within a cycle:** Until all questions exhausted, question IDs never repeat
2. **Automatic cycling:** When all used, system automatically resets for next cycle
3. **Transparent to user:** Happens automatically during `quiz.finalize()`
4. **Scales infinitely:** Works for 1 question or 1M questions

### Testing the Feature
```bash
# Create test with 10 questions
python scripts/import_questions.py --file test_10.md

# Quiz 1: 5 questions
python -m quiz_engine.main --questions 5
→ Logs: Q[x,y,z,a,b] selected from cycle 1

# Quiz 2: 5 different questions
python -m quiz_engine.main --questions 5
→ Logs: Q[p,q,r,s,t] selected from cycle 1 (different!)

# Quiz 3: Cycle auto-advanced, can repeat
python -m quiz_engine.main --questions 5
→ Logs: Q[x,p,z,q,b] selected from cycle 2 (some repeats OK now)
```

---

## Implementation Notes

- **Testing first:** Write unit tests for database and utils before CLI integration
- **Defensive coding:** Validate all user input, handle edge cases (zero questions, network lag, etc.)
- **User experience:** Make error messages helpful and actionable
- **Documentation:** Inline comments explain WHY, not WHAT; self-documenting code preferred
- **Version control:** Commit after each phase, tag releases
- **Non-repetition:** The cycling mechanism is the core feature preventing question duplicates—test thoroughly!
- **Future roadmap:** Difficulty filters, section filters, performance analytics, web UI
