# Architecture — quiz-engine-rust

> Part of the [Quiz Engine multi-language collection](../README.md)

---

## System Overview

### 1000 ft View

A high-level picture of the Rust crate's modules and async runtime.

```mermaid
graph TD
    User["👤 User (Terminal)"]
    Main["main.rs\nclap dispatcher"]
    CLI["src/cli/\nmod.rs, formatter.rs,\nprompts.rs, commands/"]
    Service["src/service/\nquiz_engine.rs, quiz_service.rs,\nhistory_service.rs, import_service.rs,\nanswer_shuffler.rs, markdown_parser.rs"]
    DB["src/db/\nconnection.rs, repositories/\n(question, session, response)"]
    Models["src/models/\nQuestion, QuizSession, QuizResponse"]
    Tokio["tokio async runtime"]
    SQLx["sqlx (async SQLite)"]
    DB_File[("SQLite Database\nquiz_engine.db")]
    MD["📄 Markdown Files\n(question source)"]

    User -->|"clap commands"| Main
    Main --> CLI
    CLI --> Service
    Service --> DB
    DB --> SQLx
    SQLx --> Tokio
    Tokio --> DB_File
    Service --> Models
    MD -->|"--file"| CLI
```

**Description:** Fully async via tokio; sqlx provides compile-time-checked queries against SQLite.

---

## Sequence Diagram

### Taking a Quiz Session

The async request flow from `quiz` command through tokio to SQLite.

```mermaid
sequenceDiagram
    actor User
    participant CMD as cli/commands/quiz.rs
    participant Engine as service/quiz_engine.rs
    participant SVC as service/quiz_service.rs
    participant QRepo as db/repositories/question_repo.rs
    participant SRepo as db/repositories/session_repo.rs
    participant RRepo as db/repositories/response_repo.rs
    participant DB as SQLite (sqlx)

    User->>CMD: ./quiz_engine quiz --questions 10
    CMD->>SVC: create_session(10).await
    SVC->>SRepo: insert(session).await
    SRepo->>DB: sqlx::query INSERT INTO quiz_sessions
    DB-->>SRepo: session_id
    CMD->>QRepo: get_cycle_aware(session_id, 10).await
    QRepo->>DB: sqlx::query SELECT ORDER BY usage_count
    DB-->>QRepo: Vec<Question>
    QRepo-->>CMD: questions
    loop Each Question
        CMD->>Engine: shuffle_answers(question)
        Engine-->>CMD: ShuffledQuestion
        CMD->>User: println! prompt
        User-->>CMD: answer
        CMD->>RRepo: insert_response(response).await
        RRepo->>DB: sqlx::query INSERT INTO quiz_responses
        CMD->>QRepo: increment_usage(q_id).await
        QRepo->>DB: sqlx::query UPDATE usage_count
    end
    CMD->>SRepo: finalize(session_id, score).await
    SRepo->>DB: sqlx::query UPDATE quiz_sessions
    SRepo-->>CMD: QuizSession
    CMD-->>User: formatter::print_result
```

**Description:** All database calls are `async fn`; the tokio runtime drives the entire execution graph.

---

## ER Diagram

### Database Schema

SQLite tables created by migrations in `migrations/001_create_tables.sql`.

```mermaid
erDiagram
    questions {
        TEXT id PK
        TEXT text
        TEXT option_a
        TEXT option_b
        TEXT option_c
        TEXT option_d
        TEXT correct_answer
        TEXT explanation
        TEXT section
        TEXT difficulty
        INTEGER usage_count
        INTEGER cycle_number
        TEXT created_at
        TEXT source_file
    }
    quiz_sessions {
        TEXT id PK
        TEXT started_at
        TEXT completed_at
        INTEGER total_questions
        INTEGER correct_answers
        REAL score
        TEXT grade
    }
    quiz_responses {
        TEXT id PK
        TEXT session_id FK
        TEXT question_id FK
        TEXT selected_answer
        INTEGER is_correct
        INTEGER time_taken_seconds
        TEXT answered_at
    }

    quiz_sessions ||--o{ quiz_responses : "contains"
    questions ||--o{ quiz_responses : "answered in"
```

**Description:** Migrations applied via `sqlx::migrate!` macro at startup; UUIDs are stored as TEXT.

---

## Class Diagram

### Core Rust Structs and Traits

Key structs, traits, and their module relationships.

```mermaid
classDiagram
    namespace models {
        class Question {
            +id: String
            +text: String
            +option_a: String
            +option_b: String
            +option_c: String
            +option_d: String
            +correct_answer: String
            +usage_count: i32
            +cycle_number: i32
        }
        class QuizSession {
            +id: String
            +started_at: DateTime
            +completed_at: Option~DateTime~
            +total_questions: i32
            +correct_answers: i32
            +score: f64
            +grade: String
        }
        class QuizResponse {
            +id: String
            +session_id: String
            +question_id: String
            +selected_answer: String
            +is_correct: bool
            +time_taken_seconds: i32
        }
    }

    namespace service {
        class QuizEngine {
            +run_quiz(pool, num_q) Future~QuizSession~
            +shuffle_answers(q) ShuffledQuestion
        }
        class AnswerShuffler {
            +shuffle(q Question) ShuffledQuestion
        }
        class MarkdownParser {
            +parse(content str) Vec~Question~
        }
        class ImportService {
            +import_file(pool, path) Future~usize~
        }
        class HistoryService {
            +get_sessions(pool) Future~Vec~
        }
    }

    namespace db_repositories {
        class QuestionRepo {
            +get_cycle_aware(pool, session_id, limit) Future~Vec~
            +increment_usage(pool, id) Future~()~
        }
        class SessionRepo {
            +insert(pool, session) Future~()~
            +finalize(pool, id, score) Future~QuizSession~
        }
        class ResponseRepo {
            +insert(pool, response) Future~()~
            +find_by_session(pool, id) Future~Vec~
        }
    }

    QuizEngine --> AnswerShuffler
    QuizEngine --> QuestionRepo
    QuizEngine --> SessionRepo
    QuizEngine --> ResponseRepo
    ImportService --> MarkdownParser
    ImportService --> QuestionRepo
    HistoryService --> SessionRepo
    HistoryService --> ResponseRepo
```

**Description:** Rust uses trait objects and `sqlx::Pool` passed by reference; no global state.

---

## Data Flow Diagram

### Question Import and Quiz Flow

How data flows through async functions across Rust modules.

```mermaid
flowchart LR
    A["📄 Markdown File"] -->|"fs::read_to_string.await"| B["MarkdownParser\nparse()"]
    B -->|"Vec<Question>"| C["ImportService\nimport_file().await"]
    C -->|"sqlx INSERT batch"| D[("SQLite\nquestions table")]

    D -->|"sqlx SELECT cycle-aware"| E["QuestionRepo\nget_cycle_aware().await"]
    E -->|"Vec<Question>"| F["AnswerShuffler\nshuffle()"]
    F -->|"ShuffledQuestion"| G["cli/prompts.rs\nprompt user"]
    G -->|"user answer"| H["ResponseRepo\ninsert().await"]
    H -->|"sqlx INSERT"| I[("SQLite\nquiz_responses")]
    H -->|"sqlx UPDATE"| D
    I -->|"aggregate"| J["SessionRepo\nfinalize().await"]
    J -->|"score + grade"| K["cli/formatter.rs\nprint results"]
```

**Description:** All I/O is non-blocking; `sqlx` macro-checks SQL at compile time against the database schema.
