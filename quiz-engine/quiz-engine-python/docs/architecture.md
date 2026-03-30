# Quiz Engine — Python — Architecture

> See [README.md](README.md) for full setup and usage documentation.

- [Quiz Engine — Python — Architecture](#quiz-engine--python--architecture)
  - [Sequence Diagram — Quiz Command Flow](#sequence-diagram--quiz-command-flow)
  - [Class Diagram](#class-diagram)
  - [Entity Relationship Diagram](#entity-relationship-diagram)
  - [Data Flow Diagram](#data-flow-diagram)


---

## Sequence Diagram — Quiz Command Flow

```mermaid
sequenceDiagram
    actor User
    participant CLI as main.py (Typer)
    participant Cmd as quiz callback
    participant Svc as QuizService
    participant Repo as Database (DAOs)
    participant DB as SQLite (stdlib)

    User->>CLI: python -m quiz_engine.main quiz --questions 10
    CLI->>Cmd: Typer dispatch quiz()
    Cmd->>Svc: start_session(question_count=10)
    Svc->>Repo: get_next_questions(count=10)
    Repo->>DB: SELECT * FROM questions ORDER BY usage_cycle, times_used
    DB-->>Repo: List[Question]
    Repo-->>Svc: List[Question]
    Svc->>Repo: create_session()
    Repo->>DB: INSERT INTO quiz_sessions
    DB-->>Repo: session_id (UUID)
    Svc-->>Cmd: QuizSession + List[Question]

    loop For each question
        Cmd->>User: Rich.print question + shuffled options
        User->>Cmd: stdin answer (A/B/C/D)
        Cmd->>Svc: record_response(session_id, question_id, answer)
        Svc->>Repo: insert_response(QuizResponse)
        Repo->>DB: INSERT INTO quiz_responses
        Svc->>Repo: update_usage(question_id)
        Repo->>DB: UPDATE questions SET times_used = times_used + 1
    end

    Svc->>Repo: finalize_session(session_id, correct_count)
    Repo->>DB: UPDATE quiz_sessions SET correct_answers, end_time
    Cmd->>User: Rich panel — final score summary
```

---

## Class Diagram

```mermaid
classDiagram
    class Question {
        <<Pydantic BaseModel>>
        +str id
        +str question_text
        +str option_a
        +str option_b
        +str option_c
        +str option_d
        +str correct_answer
        +str explanation
        +str category
        +int usage_cycle
        +int times_used
        +datetime created_at
    }

    class QuizSession {
        <<Pydantic BaseModel>>
        +str id
        +datetime start_time
        +Optional~datetime~ end_time
        +int total_questions
        +int correct_answers
        +float score_percent
    }

    class QuizResponse {
        <<Pydantic BaseModel>>
        +str id
        +str session_id
        +str question_id
        +str selected_answer
        +bool is_correct
        +datetime answered_at
    }

    class Database {
        -sqlite3.Connection _conn
        +connect(db_path: str) None
        +initialize() None
        +get_next_questions(count: int) list~Question~
        +create_session(session: QuizSession) str
        +insert_response(response: QuizResponse) None
        +update_usage(question_id: str) None
        +finalize_session(session_id: str, correct: int) None
        +get_all_sessions() list~QuizSession~
        +get_responses(session_id: str) list~QuizResponse~
        +import_question(question: Question) None
        +close() None
    }

    class QuizService {
        -db: Database
        -shuffler: AnswerShuffler
        +start_session(count: int) tuple~QuizSession, list~Question~~
        +record_response(session_id, q_id, answer: str) bool
        +finish_session(session_id: str) QuizSession
    }

    class ImportService {
        -db: Database
        -parser: MarkdownParser
        +import_file(path: str) int
        +import_directory(path: str) int
    }

    class HistoryService {
        -db: Database
        +get_sessions() list~QuizSession~
        +get_detail(session_id: str) dict
        +export_json(sessions: list) str
        +export_csv(sessions: list) str
    }

    class MarkdownParser {
        +parse_file(path: str) list~Question~
        +parse_content(content: str) list~Question~
    }

    class AnswerShuffler {
        +shuffle(options: list~str~) list~str~
        +map_answer(original, shuffled, orig_order) str
    }

    class MainApp {
        <<Typer App>>
        +quiz(questions, no_shuffle, db)
        +import_cmd(file, dir, db)
        +history(session_id, review, export, db)
        +clear(questions, history, all, confirm, db)
    }

    MainApp --> Database
    MainApp --> QuizService
    MainApp --> ImportService
    MainApp --> HistoryService
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
        U([User stdin]) --> APP[main.py\nTyper App]
        M([Markdown File]) --> ICMD[import callback]
    end

    subgraph CLI Layer
        APP --> ROUTE{Typer\nCommand Dispatch}
        ROUTE -->|quiz| QCMD[quiz callback]
        ROUTE -->|import| ICMD
        ROUTE -->|history| HCMD[history callback]
        ROUTE -->|clear| CCMD[clear callback]
    end

    subgraph Service Layer
        QCMD --> QS[QuizService\n+ AnswerShuffler]
        ICMD --> IS[ImportService\n+ MarkdownParser]
        HCMD --> HS[HistoryService]
    end

    subgraph Data Layer
        QS --> DB[Database\nsqlite3 stdlib]
        IS --> DB
        HS --> DB
        CCMD --> DB
        DB --> FILE[(quiz_engine.db\nSQLite File)]
    end

    subgraph Output
        QS --> QOUT([Quiz Results\nRich Console])
        HS --> HOUT([History\nRich Table / JSON / CSV])
        IS --> IOUT([Import Summary\nRich Console])
    end
```
