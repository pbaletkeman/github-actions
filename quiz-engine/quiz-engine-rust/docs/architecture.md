# Quiz Engine — Rust — Architecture

> See [README.md](README.md) for full setup and usage documentation.

- [Quiz Engine — Rust — Architecture](#quiz-engine--rust--architecture)
  - [Sequence Diagram — Quiz Command Flow](#sequence-diagram--quiz-command-flow)
  - [Class Diagram](#class-diagram)
  - [Entity Relationship Diagram](#entity-relationship-diagram)
  - [Data Flow Diagram](#data-flow-diagram)


---

## Sequence Diagram — Quiz Command Flow

```mermaid
sequenceDiagram
    actor User
    participant CLI as main.rs (Tokio)
    participant Cmd as cli/quiz.rs
    participant Svc as QuizService
    participant DB as db.rs (sqlx)
    participant SQLite as SQLite File

    User->>CLI: quiz_engine quiz --questions 10
    CLI->>Cmd: #[tokio::main] dispatch
    Cmd->>Svc: start_session(pool, count=10).await
    Svc->>DB: get_next_questions(pool, 10).await
    DB->>SQLite: sqlx::query! SELECT ORDER BY usage_cycle, times_used
    SQLite-->>DB: Vec<Question>
    DB-->>Svc: Vec<Question>
    Svc->>DB: create_session(pool, &session).await
    DB->>SQLite: INSERT INTO quiz_sessions
    SQLite-->>DB: session_id (UUID)
    Svc-->>Cmd: (QuizSession, Vec<Question>)

    loop For each question
        Cmd->>User: print question + shuffled options
        User->>Cmd: stdin answer (A/B/C/D)
        Cmd->>Svc: record_response(pool, session_id, q_id, answer).await
        Svc->>DB: insert_response(pool, &response).await
        DB->>SQLite: INSERT INTO quiz_responses
        Svc->>DB: update_usage(pool, question_id).await
        DB->>SQLite: UPDATE questions SET times_used = times_used + 1
    end

    Svc->>DB: finalize_session(pool, session_id, correct).await
    DB->>SQLite: UPDATE quiz_sessions SET correct_answers, end_time
    Cmd->>User: print final score
```

---

## Class Diagram

```mermaid
classDiagram
    class Question {
        <<Serde Deserialize>>
        +String id
        +String question_text
        +String option_a
        +String option_b
        +String option_c
        +String option_d
        +String correct_answer
        +String explanation
        +String category
        +i32 usage_cycle
        +i32 times_used
        +NaiveDateTime created_at
    }

    class QuizSession {
        <<Serde Serialize>>
        +String id
        +NaiveDateTime start_time
        +Option~NaiveDateTime~ end_time
        +i32 total_questions
        +i32 correct_answers
        +fn score_percent() f64
    }

    class QuizResponse {
        <<Serde Serialize>>
        +String id
        +String session_id
        +String question_id
        +String selected_answer
        +bool is_correct
        +NaiveDateTime answered_at
    }

    class Database {
        <<sqlx functions>>
        +get_next_questions(pool, count) Vec~Question~
        +create_session(pool, session) String
        +insert_response(pool, response) ()
        +update_usage(pool, id) ()
        +finalize_session(pool, id, correct) ()
        +get_all_sessions(pool) Vec~QuizSession~
        +get_responses(pool, session_id) Vec~QuizResponse~
        +import_question(pool, question) ()
        +initialize(pool) ()
    }

    class QuizService {
        +start_session(pool Arc~Pool~, count usize) (QuizSession, Vec~Question~)
        +record_response(pool, session_id, q_id, answer) bool
        +finish_session(pool, session_id) QuizSession
    }

    class ImportService {
        +import_file(pool, path) usize
        +import_directory(pool, path) usize
    }

    class HistoryService {
        +get_sessions(pool) Vec~QuizSession~
        +get_detail(pool, id) SessionDetail
        +export_json(sessions) String
        +export_csv(sessions) String
    }

    class MarkdownParser {
        +parse_file(path Path) Vec~Question~
        +parse_content(content String) Vec~Question~
    }

    class AnswerShuffler {
        +shuffle(options Vec~String~) Vec~String~
        +map_answer(original, shuffled, orig_order) String
    }

    class Cli {
        <<Clap Parser>>
        +String db
        +Commands subcommand
    }

    class Commands {
        <<Clap Subcommand>>
        +Quiz(QuizArgs)
        +Import(ImportArgs)
        +History(HistoryArgs)
        +Clear(ClearArgs)
    }

    Cli --> Commands
    Commands --> QuizService
    Commands --> ImportService
    Commands --> HistoryService
    QuizService --> Database
    QuizService --> AnswerShuffler
    ImportService --> Database
    ImportService --> MarkdownParser
    HistoryService --> Database
    QuizSession "1" --o "many" QuizResponse
    Question "1" --o "many" QuizResponse
```

---

## Entity Relationship Diagram

```mermaid
erDiagram
    QUESTIONS {
        TEXT id PK
        TEXT question_text
        TEXT option_a
        TEXT option_b
        TEXT option_c
        TEXT option_d
        TEXT correct_answer
        TEXT explanation
        TEXT category
        INTEGER usage_cycle
        INTEGER times_used
        TEXT created_at
    }

    QUIZ_SESSIONS {
        TEXT id PK
        TEXT start_time
        TEXT end_time
        INTEGER total_questions
        INTEGER correct_answers
        REAL score_percent
    }

    QUIZ_RESPONSES {
        TEXT id PK
        TEXT session_id FK
        TEXT question_id FK
        TEXT selected_answer
        INTEGER is_correct
        TEXT answered_at
    }

    QUIZ_SESSIONS ||--o{ QUIZ_RESPONSES : "contains"
    QUESTIONS ||--o{ QUIZ_RESPONSES : "referenced by"
```

---

## Data Flow Diagram

```mermaid
flowchart TD
    subgraph Input
        U([User stdin]) --> MAIN[main.rs\nTokio runtime]
        M([Markdown File]) --> ICMD[cli/import.rs]
    end

    subgraph CLI Layer
        MAIN --> CLAP{Clap Parser\ncommand dispatch}
        CLAP -->|quiz| QCMD[cli/quiz.rs]
        CLAP -->|import| ICMD
        CLAP -->|history| HCMD[cli/history.rs]
        CLAP -->|clear| CCMD[cli/clear.rs]
    end

    subgraph Async Service Layer
        QCMD --> QS[QuizService\n.await]
        ICMD --> IS[ImportService\n.await]
        HCMD --> HS[HistoryService\n.await]
    end

    subgraph Data Layer
        QS --> POOL[sqlx Pool\nArc~SqlitePool~]
        IS --> POOL
        HS --> POOL
        CCMD --> POOL
        POOL --> FILE[(quiz_engine.db\nSQLite File)]
    end

    subgraph Output
        QS --> QOUT([Quiz Results\nprintln! / colored])
        HS --> HOUT([History\nstdout / JSON / CSV])
        IS --> IOUT([Import Summary\nprintln!])
    end
```
