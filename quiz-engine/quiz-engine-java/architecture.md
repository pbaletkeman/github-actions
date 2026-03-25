# Architecture — quiz-engine-java

> Part of the [Quiz Engine multi-language collection](../README.md)

---

## System Overview

### 1000 ft View

A high-level picture of the Maven project's layers and external dependencies.

```mermaid
graph TD
    User["👤 User (Terminal)"]
    Main["QuizEngineApp.java\n(entry point + Picocli dispatcher)"]
    CLI["cli/\nQuizCLI, ConsoleFormatter"]
    Service["service/\nQuizEngine, QuizService, HistoryService"]
    Util["util/\nMarkdownParser, AnswerShuffler, QuizUtils"]
    DAO["dao/\nQuestionDAO, SessionDAO, ResponseDAO"]
    DBMGR["DatabaseManager\n(JDBC connection pool)"]
    DB[("SQLite Database\nquiz_engine.db")]
    MD["📄 Markdown Files\n(question source)"]

    User -->|"java -jar quiz-engine.jar"| Main
    Main --> CLI
    CLI --> Service
    CLI --> Util
    Service --> DAO
    Util --> DAO
    DAO --> DBMGR
    DBMGR --> DB
    MD -->|"import"| CLI
```

**Description:** Plain Java with JDBC; no ORM — all SQL hand-written in DAO classes.

---

## Sequence Diagram

### Taking a Quiz Session

How a `quiz` command flows through the Java class hierarchy.

```mermaid
sequenceDiagram
    actor User
    participant Main as QuizEngineApp
    participant CLI as QuizCLI
    participant Engine as QuizEngine
    participant SVC as QuizService
    participant DAO as QuestionDAO / SessionDAO
    participant DB as SQLite (JDBC)

    User->>Main: java -jar quiz-engine.jar quiz
    Main->>CLI: dispatch(args)
    CLI->>Engine: startQuiz(numQuestions)
    Engine->>SVC: createSession()
    SVC->>DAO: SessionDAO.insert(session)
    DAO->>DB: INSERT INTO quiz_sessions
    DB-->>DAO: generated key
    Engine->>DAO: QuestionDAO.getCycleAware(sessionId, limit)
    DAO->>DB: SELECT questions ORDER BY usage_count
    DB-->>DAO: ResultSet
    DAO-->>Engine: List<Question>
    loop Each Question
        Engine->>Engine: AnswerShuffler.shuffle(question)
        Engine-->>CLI: display shuffled question
        CLI->>User: System.out formatted prompt
        User-->>CLI: answer
        CLI->>Engine: recordResponse(sessionId, qId, answer, timeTaken)
        Engine->>DAO: ResponseDAO.insert(response)
    end
    Engine->>SVC: finalizeSession(sessionId, correct)
    SVC->>DAO: SessionDAO.update(session)
    Engine-->>CLI: QuizResult
    CLI-->>User: ConsoleFormatter output
```

**Description:** Picocli dispatches commands to `QuizCLI`; all SQL executed via raw `PreparedStatement`.

---

## ER Diagram

### Database Schema

SQLite tables created by `DatabaseManager.initializeSchema()`.

```mermaid
erDiagram
    QUESTIONS {
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
    QUIZ_SESSIONS {
        TEXT id PK
        TEXT started_at
        TEXT completed_at
        INTEGER total_questions
        INTEGER correct_answers
        REAL score
        TEXT grade
    }
    QUIZ_RESPONSES {
        TEXT id PK
        TEXT session_id FK
        TEXT question_id FK
        TEXT selected_answer
        INTEGER is_correct
        INTEGER time_taken_seconds
        TEXT answered_at
    }

    QUIZ_SESSIONS ||--o{ QUIZ_RESPONSES : "contains"
    QUESTIONS ||--o{ QUIZ_RESPONSES : "answered in"
```

**Description:** Schema initialised from `resources/schema.sql` on first run via `DatabaseManager`.

---

## Class Diagram

### Core Java Classes

Key classes and their relationships across the `com.quizengine` package tree.

```mermaid
classDiagram
    class QuizEngineApp {
        +main(String[] args)
    }

    class QuizCLI {
        +run() void
    }

    class QuizEngine {
        -quizService: QuizService
        -answerShuffler: AnswerShuffler
        +startQuiz(int numQ) QuizResult
        +recordResponse(String sessionId, String qId, String answer, int timeTaken)
    }

    class QuizService {
        +createSession() QuizSession
        +finalizeSession(String id, int correct) void
    }

    class HistoryService {
        +getSessions() List~QuizSession~
        +exportJson(String id) String
        +exportCsv(String id) String
    }

    class MarkdownParser {
        +parseFile(Path path) List~Question~
    }

    class AnswerShuffler {
        +shuffle(Question q) ShuffleResult
    }

    class QuestionDAO {
        +save(Question q) void
        +getCycleAware(String sessionId, int limit) List~Question~
        +incrementUsage(String id) void
    }

    class SessionDAO {
        +insert(QuizSession s) void
        +update(QuizSession s) void
        +findAll() List~QuizSession~
    }

    class ResponseDAO {
        +insert(QuizResponse r) void
        +findBySession(String sessionId) List~QuizResponse~
    }

    class DatabaseManager {
        +getConnection() Connection
        +initializeSchema() void
    }

    QuizEngineApp --> QuizCLI
    QuizCLI --> QuizEngine
    QuizCLI --> HistoryService
    QuizEngine --> QuizService
    QuizEngine --> AnswerShuffler
    QuizEngine --> QuestionDAO
    QuizEngine --> ResponseDAO
    QuizService --> SessionDAO
    MarkdownParser --> QuestionDAO
    QuestionDAO --> DatabaseManager
    SessionDAO --> DatabaseManager
    ResponseDAO --> DatabaseManager
```

**Description:** All DAO classes share a single `DatabaseManager` connection; no dependency injection framework used.

---

## Data Flow Diagram

### Question Import and Quiz Flow

How data moves through the Maven project's layers.

```mermaid
flowchart LR
    A["📄 Markdown File"] -->|"Files.readString"| B["MarkdownParser\nparseFile()"]
    B -->|"List<Question>"| C["ImportCommand\nCallable.call()"]
    C -->|"PreparedStatement INSERT"| D[("SQLite\nQUESTIONS table")]

    D -->|"SELECT cycle-aware"| E["QuestionDAO\ngetCycleAware()"]
    E -->|"List<Question>"| F["AnswerShuffler\nshuffle()"]
    F -->|"ShuffleResult"| G["ConsoleFormatter\ndisplayQuestion()"]
    G -->|"readLine answer"| H["QuizEngine\nrecordResponse()"]
    H -->|"PreparedStatement INSERT"| I[("SQLite\nQUIZ_RESPONSES table")]
    H -->|"PreparedStatement UPDATE"| D
    I -->|"COUNT correct"| J["QuizService\nfinalizeSession()"]
    J -->|"score + grade"| K["ConsoleFormatter\ndisplayResult()"]
```

**Description:** All SQL uses `PreparedStatement` for parameterized queries; `DatabaseManager` provides the `Connection`.
