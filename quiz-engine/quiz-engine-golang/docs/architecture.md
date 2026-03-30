# Quiz Engine — Go — Architecture

> See [README.md](README.md) for full setup and usage documentation.

- [Quiz Engine — Go — Architecture](#quiz-engine--go--architecture)
  - [Sequence Diagram — Quiz Command Flow](#sequence-diagram--quiz-command-flow)
  - [Class Diagram](#class-diagram)
  - [Entity Relationship Diagram](#entity-relationship-diagram)
  - [Data Flow Diagram](#data-flow-diagram)


---

## Sequence Diagram — Quiz Command Flow

```mermaid
sequenceDiagram
    actor User
    participant CLI as cmd/root.go (Cobra)
    participant Cmd as cmd/quiz.go
    participant Svc as QuizService
    participant DB as database.go
    participant SQLite as SQLite File

    User->>CLI: quiz_engine quiz --questions 10
    CLI->>Cmd: cobra.Command.Execute()
    Cmd->>Svc: StartSession(db, count: 10)
    Svc->>DB: FetchNextQuestions(10)
    DB->>SQLite: SELECT * FROM questions ORDER BY usage_cycle, times_used
    SQLite-->>DB: []Question
    DB-->>Svc: []Question
    Svc->>DB: CreateSession()
    DB->>SQLite: INSERT INTO quiz_sessions
    SQLite-->>DB: sessionID
    Svc-->>Cmd: Session + []Question

    loop For each question
        Cmd->>User: fatih/color + tablewriter display
        User->>Cmd: stdin: A/B/C/D
        Cmd->>Svc: RecordResponse(sessionID, questionID, answer)
        Svc->>DB: InsertResponse()
        DB->>SQLite: INSERT INTO quiz_responses
        Svc->>DB: UpdateQuestionUsage(questionID)
        DB->>SQLite: UPDATE questions SET times_used++
    end

    Svc->>DB: FinalizeSession(sessionID, score)
    DB->>SQLite: UPDATE quiz_sessions SET score, end_time
    Cmd->>User: Display final score (color/table)
```

---

## Class Diagram

```mermaid
classDiagram
    class Question {
        +string ID
        +string QuestionText
        +string OptionA
        +string OptionB
        +string OptionC
        +string OptionD
        +string CorrectAnswer
        +string Explanation
        +string Category
        +int UsageCycle
        +int TimesUsed
    }

    class QuizSession {
        +string ID
        +time.Time StartTime
        +time.Time EndTime
        +int TotalQuestions
        +int CorrectAnswers
        +float64 ScorePercent()
    }

    class QuizResponse {
        +string ID
        +string SessionID
        +string QuestionID
        +string SelectedAnswer
        +bool IsCorrect
        +time.Time AnsweredAt
    }

    class Database {
        +*sql.DB db
        +Open(path string) error
        +Initialize() error
        +FetchNextQuestions(count int) []Question
        +CreateSession() (string, error)
        +InsertResponse(r QuizResponse) error
        +UpdateQuestionUsage(id string) error
        +FinalizeSession(id string, score int) error
        +GetHistory() []QuizSession
        +GetResponses(sessionID string) []QuizResponse
        +ImportQuestion(q Question) error
        +Close() error
    }

    class QuizService {
        -db *Database
        +StartSession(count int) (*QuizSession, []Question, error)
        +RecordResponse(sessionID, qID, answer string) (bool, error)
        +FinalizeSession(sessionID string) (*QuizSession, error)
    }

    class ImportService {
        -db *Database
        +ImportFile(path string) (int, error)
        +ImportDirectory(path string) (int, error)
    }

    class HistoryService {
        -db *Database
        +ListSessions() ([]QuizSession, error)
        +SessionDetail(id string) (*QuizSession, []QuizResponse, error)
        +ExportJSON(sessions []QuizSession) string
        +ExportCSV(sessions []QuizSession) string
    }

    class MarkdownParser {
        +Parse(content string) ([]Question, error)
        +ParseFile(path string) ([]Question, error)
    }

    class AnswerShuffler {
        +Shuffle(options []string) []string
        +MapAnswer(original string, shuffled []string, orig []string) string
    }

    class RootCmd {
        +cobra.Command
        +string dbFlag
        +Execute() error
    }

    class QuizCmd {
        +cobra.Command
        +int questionsFlag
        +bool noShuffleFlag
        +RunE(cmd, args)
    }

    class ImportCmd {
        +cobra.Command
        +string fileFlag
        +string dirFlag
        +RunE(cmd, args)
    }

    class HistoryCmd {
        +cobra.Command
        +string sessionIDFlag
        +string exportFlag
        +RunE(cmd, args)
    }

    QuizService --> Database
    QuizService --> AnswerShuffler
    ImportService --> Database
    ImportService --> MarkdownParser
    HistoryService --> Database
    RootCmd --> Database : opens db
    QuizCmd --> QuizService
    ImportCmd --> ImportService
    HistoryCmd --> HistoryService
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
        DATETIME created_at
    }

    QUIZ_SESSIONS {
        TEXT id PK
        DATETIME start_time
        DATETIME end_time
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
        DATETIME answered_at
    }

    QUIZ_SESSIONS ||--o{ QUIZ_RESPONSES : "contains"
    QUESTIONS ||--o{ QUIZ_RESPONSES : "referenced by"
```

---

## Data Flow Diagram

```mermaid
flowchart TD
    subgraph Input
        U([User stdin]) --> ROOT[cmd/root.go\nCobra Execute]
        M([Markdown File]) --> IMP[cmd/import.go]
    end

    subgraph Command Layer
        ROOT --> ROUTE{Cobra Router}
        ROUTE -->|quiz| QC[cmd/quiz.go]
        ROUTE -->|import| IMP
        ROUTE -->|history| HC[cmd/history.go]
        ROUTE -->|clear| CC[cmd/clear.go]
    end

    subgraph Service Layer
        QC --> QS[QuizService\n+ AnswerShuffler]
        IMP --> IS[ImportService\n+ MarkdownParser]
        HC --> HS[HistoryService]
    end

    subgraph Data Layer
        QS --> DB[(database.go\nmattn/go-sqlite3)]
        IS --> DB
        HS --> DB
        CC --> DB
        DB --> FILE[(quiz_engine.db\nSQLite File)]
    end

    subgraph Output
        QS --> QOUT([Quiz Results\nfatih/color + tablewriter])
        HS --> HOUT([History Table\nor JSON/CSV export])
        IS --> IOUT([Import summary\ncount printed])
    end
```
