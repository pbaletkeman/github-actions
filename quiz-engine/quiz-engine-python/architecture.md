# Architecture — quiz-engine-python

> Part of the [Quiz Engine multi-language collection](../README.md)

---

## System Overview

### 1000 ft View

A high-level picture of the system's components and external dependencies.

```mermaid
graph TD
    User["👤 User (Terminal)"]
    CLI["CLI Layer\n(Typer commands)"]
    Logic["Business Logic\n(quiz.py, history.py)"]
    DB_Layer["Database Layer\n(database.py)"]
    DB[("SQLite Database\nquiz_engine.db")]
    MD["📄 Markdown Files\n(question source)"]
    Scripts["scripts/\nimport_questions.py\nview_history.py\nclear_database.py"]

    User -->|"quiz / import / history / clear"| CLI
    User -->|"utility scripts"| Scripts
    CLI --> Logic
    Scripts --> Logic
    Logic --> DB_Layer
    DB_Layer --> DB
    MD -->|"--file / --dir"| Scripts
```

**Description:** Typer CLI delegates to business logic modules; all state persisted in a local SQLite file.

---

## Sequence Diagram

### Taking a Quiz Session

How a `quiz` command executes from start to finish.

```mermaid
sequenceDiagram
    actor User
    participant CLI as cli.py (Typer)
    participant Quiz as quiz.py
    participant DB as database.py
    participant SQLite as SQLite

    User->>CLI: python -m quiz_engine.main --questions 10
    CLI->>Quiz: run_quiz(num_questions=10)
    Quiz->>DB: create_session()
    DB->>SQLite: INSERT INTO quiz_sessions
    SQLite-->>DB: session_id
    Quiz->>DB: get_cycle_questions(session_id, limit=10)
    DB->>SQLite: SELECT questions ORDER BY usage_count
    SQLite-->>DB: rows
    DB-->>Quiz: List[Question]
    loop Each Question
        Quiz->>Quiz: shuffle_answers(question)
        Quiz-->>CLI: display question
        CLI->>User: prompt for answer
        User-->>CLI: A/B/C/D
        CLI->>Quiz: record_response(session_id, q_id, answer)
        Quiz->>DB: insert_response(...)
        DB->>SQLite: INSERT INTO quiz_responses
    end
    Quiz->>DB: finalize_session(session_id, score)
    DB->>SQLite: UPDATE quiz_sessions SET score=...
    Quiz-->>CLI: session result
    CLI-->>User: display score and grade
```

**Description:** Session lifecycle managed by `quiz.py`; `database.py` wraps all SQLite interactions.

---

## ER Diagram

### Database Schema

The three SQLite tables and their relationships.

```mermaid
erDiagram
    QUESTIONS {
        text id PK
        text text
        text option_a
        text option_b
        text option_c
        text option_d
        text correct_answer
        text explanation
        text section
        text difficulty
        integer usage_count
        integer cycle_number
        text created_at
        text source_file
    }
    QUIZ_SESSIONS {
        text id PK
        text started_at
        text completed_at
        integer total_questions
        integer correct_answers
        real score
        text grade
    }
    QUIZ_RESPONSES {
        text id PK
        text session_id FK
        text question_id FK
        text selected_answer
        integer is_correct
        integer time_taken_seconds
        text answered_at
    }

    QUIZ_SESSIONS ||--o{ QUIZ_RESPONSES : "contains"
    QUESTIONS ||--o{ QUIZ_RESPONSES : "answered in"
```

**Description:** Three normalized tables; responses join sessions to questions.

---

## Class Diagram

### Core Python Classes and Modules

Key classes and their relationships across the `quiz_engine` package.

```mermaid
classDiagram
    class Question {
        +id: str
        +text: str
        +options: dict
        +correct_answer: str
        +explanation: str
        +usage_count: int
        +cycle_number: int
    }

    class QuizSession {
        +id: str
        +started_at: datetime
        +completed_at: datetime
        +total_questions: int
        +correct_answers: int
        +score: float
        +grade: str
    }

    class QuizResponse {
        +id: str
        +session_id: str
        +question_id: str
        +selected_answer: str
        +is_correct: bool
        +time_taken: int
    }

    class Database {
        +conn: sqlite3.Connection
        +get_cycle_questions(session_id, limit) List~Question~
        +create_session() str
        +insert_response(response) None
        +finalize_session(session_id, score) None
    }

    class QuizEngine {
        +db: Database
        +run_quiz(num_questions) QuizSession
        +shuffle_answers(q) tuple
    }

    class HistoryEngine {
        +db: Database
        +get_sessions() List~QuizSession~
        +export_json(session_id) str
        +export_csv(session_id) str
    }

    class MarkdownParser {
        +parse_file(path) List~Question~
        +parse_directory(dir) List~Question~
    }

    QuizEngine --> Database
    QuizEngine --> Question
    HistoryEngine --> Database
    MarkdownParser --> Question
    Database --> QuizSession
    Database --> QuizResponse
```

**Description:** Pydantic-based models flow through a lightweight service and database module structure.

---

## Data Flow Diagram

### Question Import and Quiz Flow

How data moves from Markdown source files through the system.

```mermaid
flowchart LR
    A["📄 Markdown File"] -->|"parse_file()"| B["MarkdownParser"]
    B -->|"List[Question]"| C["import_questions.py"]
    C -->|"INSERT"| D[("SQLite\nquestions table")]

    D -->|"SELECT cycle-aware"| E["Database\nget_cycle_questions()"]
    E -->|"List[Question]"| F["QuizEngine\nshuffle_answers()"]
    F -->|"shuffled question"| G["cli.py\ndisplay & prompt"]
    G -->|"user answer"| H["QuizEngine\nrecord_response()"]
    H -->|"INSERT"| I[("SQLite\nquiz_responses")]
    H -->|"UPDATE usage_count"| D
    I -->|"aggregate"| J["Database\nfinalize_session()"]
    J -->|"score + grade"| K["Rich Console\ndisplay results"]
```

**Description:** Import populates questions once; the quiz loop reads, shuffles, records, and finalizes each session.
