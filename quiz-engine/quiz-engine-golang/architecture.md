# Architecture — quiz-engine-golang

> Part of the [Quiz Engine multi-language collection](../README.md)

---

## System Overview

### 1000 ft View

A high-level picture of the Go application's packages and dependencies.

```mermaid
graph TD
    User["👤 User (Terminal)"]
    CMD["cmd/\ncobra commands\n(quiz, import, history, clear)"]
    Engine["internal/engine\nquiz.go, shuffler.go"]
    Service["internal/service\nquiz_service.go, history_service.go"]
    DB["internal/database\ndb.go, question.go,\nsession.go, response.go"]
    Parser["internal/parser\nmarkdown.go"]
    Models["internal/models\nQuestion, QuizSession, QuizResponse"]
    SQLite[("SQLite Database\nquiz_engine.db")]
    MD["📄 Markdown Files\n(question source)"]

    User -->|"cobra commands"| CMD
    CMD --> Engine
    CMD --> Service
    Engine --> DB
    Service --> DB
    CMD --> Parser
    Parser --> Models
    DB --> Models
    DB --> SQLite
    MD -->|"--file"| Parser
```

**Description:** Cobra commands delegate to engine/service packages; all persistence via `go-sqlite3`.

---

## Sequence Diagram

### Taking a Quiz Session

How the `quiz` command flows through the Go packages.

```mermaid
sequenceDiagram
    actor User
    participant CMD as cmd/quiz.go
    participant SVC as service/quiz_service.go
    participant Engine as engine/quiz.go
    participant DB as database/session.go
    participant QDB as database/question.go
    participant SQLite as SQLite

    User->>CMD: ./quiz-engine quiz --questions 10
    CMD->>SVC: StartSession(10)
    SVC->>DB: CreateSession()
    DB->>SQLite: INSERT INTO quiz_sessions
    SQLite-->>DB: sessionID
    SVC->>QDB: GetCycleAwareQuestions(sessionID, 10)
    QDB->>SQLite: SELECT questions ORDER BY usage_count ASC
    SQLite-->>QDB: rows
    QDB-->>SVC: []models.Question
    SVC-->>CMD: session + questions
    loop Each Question
        CMD->>Engine: ShuffleAnswers(question)
        Engine-->>CMD: shuffled options
        CMD->>User: tablewriter display
        User-->>CMD: answer
        CMD->>DB: RecordResponse(sessionID, qID, answer)
        DB->>SQLite: INSERT INTO quiz_responses
    end
    CMD->>SVC: FinalizeSession(sessionID, correct)
    SVC->>SQLite: UPDATE quiz_sessions SET score=...
    SVC-->>CMD: result
    CMD-->>User: display score + grade
```

**Description:** All database calls go through `internal/database` package functions; cobra handles CLI parsing.

---

## ER Diagram

### Database Schema

The three SQLite tables managed by `internal/database`.

```mermaid
erDiagram
    questions {
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
        datetime created_at
        text source_file
    }
    quiz_sessions {
        text id PK
        datetime started_at
        datetime completed_at
        integer total_questions
        integer correct_answers
        real score
        text grade
    }
    quiz_responses {
        text id PK
        text session_id FK
        text question_id FK
        text selected_answer
        integer is_correct
        integer time_taken_seconds
        datetime answered_at
    }

    quiz_sessions ||--o{ quiz_responses : "contains"
    questions ||--o{ quiz_responses : "answered in"
```

**Description:** Schema created with `CREATE TABLE IF NOT EXISTS` statements in `db.go`.

---

## Class Diagram

### Package Structure and Types

Go structs, interfaces, and their package relationships.

```mermaid
classDiagram
    namespace models {
        class Question {
            +ID string
            +Text string
            +OptionA string
            +OptionB string
            +OptionC string
            +OptionD string
            +CorrectAnswer string
            +Explanation string
            +UsageCount int
            +CycleNumber int
        }
        class QuizSession {
            +ID string
            +StartedAt time.Time
            +CompletedAt time.Time
            +TotalQuestions int
            +CorrectAnswers int
            +Score float64
            +Grade string
        }
        class QuizResponse {
            +ID string
            +SessionID string
            +QuestionID string
            +SelectedAnswer string
            +IsCorrect bool
            +TimeTaken int
        }
    }

    namespace engine {
        class QuizRunner {
            +Run(db, numQ int) error
        }
        class Shuffler {
            +Shuffle(q Question) ShuffledQ
        }
    }

    namespace service {
        class QuizService {
            +StartSession(numQ int) QuizSession
            +FinalizeSession(id, correct int) QuizSession
        }
        class HistoryService {
            +GetSessions() []QuizSession
            +ExportJSON(id string) string
        }
    }

    QuizRunner --> QuizService
    QuizRunner --> Shuffler
    QuizService --> Question
    QuizService --> QuizSession
    QuizService --> QuizResponse
```

**Description:** Go uses struct composition over inheritance; packages enforce encapsulation via exported types.

---

## Data Flow Diagram

### Question Import and Quiz Flow

How data moves through `internal/` packages during import and quiz execution.

```mermaid
flowchart LR
    A["📄 Markdown File"] -->|"os.ReadFile"| B["parser/markdown.go\nParseFile()"]
    B -->|"[]models.Question"| C["cmd/import.go\nimportQuestions()"]
    C -->|"db.Exec INSERT"| D[("SQLite\nquestions table")]

    D -->|"SELECT cycle-aware"| E["database/question.go\nGetCycleAware()"]
    E -->|"[]Question"| F["engine/shuffler.go\nShuffle()"]
    F -->|"ShuffledQuestion"| G["cli/display.go\ntablewriter render"]
    G -->|"user answer"| H["database/response.go\nInsertResponse()"]
    H -->|"db.Exec INSERT"| I[("SQLite\nquiz_responses")]
    H -->|"db.Exec UPDATE"| D
    I -->|"COUNT / SUM"| J["service/quiz_service.go\nFinalizeSession()"]
    J -->|"score + grade"| K["cli/formatter.go\nPrint results"]
```

**Description:** `internal/parser` reads Markdown; all database writes use parameterized `database/sql` statements.
