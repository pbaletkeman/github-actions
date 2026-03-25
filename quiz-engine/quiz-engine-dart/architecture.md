# Architecture — quiz-engine-dart

> Part of the [Quiz Engine multi-language collection](../README.md)

---

## System Overview

### 1000 ft View

A high-level picture of the Dart package structure and dependencies.

```mermaid
graph TD
    User["👤 User (Terminal)"]
    Entry["bin/quiz_engine.dart\nentry point"]
    CLI["lib/src/cli/\ncommands/, formatter.dart, prompts.dart"]
    Service["lib/src/service/\nquiz_engine.dart, quiz_service.dart,\nhistory_service.dart, import_service.dart,\nanswer_shuffler.dart, markdown_parser.dart"]
    DBLayer["lib/src/database/\ndatabase.dart"]
    Models["lib/src/models/\nQuestion, QuizSession, QuizResponse"]
    Exceptions["lib/src/exceptions/\nquiz_exceptions.dart"]
    SQLite[("SQLite Database\nquiz_engine.db")]
    MD["📄 Markdown Files\n(question source)"]

    User -->|"dart run / native binary"| Entry
    Entry --> CLI
    CLI --> Service
    Service --> DBLayer
    Service --> Models
    DBLayer --> SQLite
    MD -->|"import --file / --dir"| CLI
    Service --> Exceptions
```

**Description:** Pure Dart app compiling to a native binary; `sqlite3` package provides synchronous SQLite access.

---

## Sequence Diagram

### Taking a Quiz Session

How the `quiz` command executes through Dart classes.

```mermaid
sequenceDiagram
    actor User
    participant CMD as cli/commands/quiz_command.dart
    participant Engine as service/quiz_engine.dart
    participant SVC as service/quiz_service.dart
    participant DB as database/database.dart
    participant SQLite as SQLite (sqlite3)

    User->>CMD: quiz_engine quiz --questions 10
    CMD->>Engine: startQuiz(10)
    Engine->>SVC: createSession(10)
    SVC->>DB: insertSession(session)
    DB->>SQLite: INSERT INTO quiz_sessions
    SQLite-->>DB: lastInsertRowId
    Engine->>DB: getCycleAwareQuestions(sessionId, 10)
    DB->>SQLite: SELECT questions ORDER BY usage_count ASC
    SQLite-->>DB: ResultSet
    DB-->>Engine: List<Question>
    loop Each Question
        Engine->>Engine: AnswerShuffler.shuffle(question)
        Engine-->>CMD: ShuffledQuestion
        CMD->>User: formatter.displayQuestion()
        User-->>CMD: answer
        CMD->>Engine: recordResponse(sessionId, qId, answer, timeTaken)
        Engine->>DB: insertResponse(response)
        DB->>SQLite: INSERT INTO quiz_responses
        Engine->>DB: incrementUsage(qId)
    end
    Engine->>SVC: finalizeSession(sessionId, score)
    SVC->>DB: updateSession(sessionId, score)
    Engine-->>CMD: QuizResult
    CMD-->>User: formatter.displayResult()
```

**Description:** Synchronous SQLite calls via the `sqlite3` package; Dart `List` and `Map` carry data between layers.

---

## ER Diagram

### Database Schema

SQLite tables created by `database.dart` on first run.

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

**Description:** Tables created with `CREATE TABLE IF NOT EXISTS`; IDs generated via `uuid` package as TEXT.

---

## Class Diagram

### Core Dart Classes

Key classes and their relationships across the `lib/src` directory tree.

```mermaid
classDiagram
    class Question {
        +id: String
        +text: String
        +optionA: String
        +optionB: String
        +optionC: String
        +optionD: String
        +correctAnswer: String
        +explanation: String
        +usageCount: int
        +cycleNumber: int
        +fromRow(Row) Question$
        +toMap() Map
    }

    class QuizSession {
        +id: String
        +startedAt: DateTime
        +completedAt: DateTime?
        +totalQuestions: int
        +correctAnswers: int
        +score: double
        +grade: String
    }

    class QuizResponse {
        +id: String
        +sessionId: String
        +questionId: String
        +selectedAnswer: String
        +isCorrect: bool
        +timeTakenSeconds: int
    }

    class Database {
        -_db: sqlite3.Database
        +getCycleAwareQuestions(sessionId, limit) List~Question~
        +insertSession(session) void
        +updateSession(sessionId, score) void
        +insertResponse(response) void
        +incrementUsage(qId) void
    }

    class QuizEngine {
        -_db: Database
        +startQuiz(numQ) QuizResult
        +recordResponse(sessionId, qId, answer, time) void
    }

    class QuizService {
        -_db: Database
        +createSession(numQ) QuizSession
        +finalizeSession(sessionId, score) void
    }

    class HistoryService {
        -_db: Database
        +getSessions() List~QuizSession~
        +exportJson(sessionId) String
        +exportCsv(sessionId) String
    }

    class AnswerShuffler {
        +shuffle(Question q) ShuffledQuestion$
    }

    class MarkdownParser {
        +parseFile(String path) List~Question~
        +parseDirectory(String dir) List~Question~
    }

    QuizEngine --> QuizService
    QuizEngine --> AnswerShuffler
    QuizEngine --> Database
    QuizService --> Database
    HistoryService --> Database
    MarkdownParser --> Question
```

**Description:** Dart uses factory constructors (`fromRow`) for model hydration; `Database` wraps all `sqlite3` calls.

---

## Data Flow Diagram

### Question Import and Quiz Flow

How data moves from Markdown files through the Dart package layers.

```mermaid
flowchart LR
    A["📄 Markdown File"] -->|"File.readAsStringSync"| B["MarkdownParser\nparseFile()"]
    B -->|"List<Question>"| C["import_command.dart\nimportQuestions()"]
    C -->|"database.insertQuestion()"| D[("SQLite\nquestions table")]

    D -->|"getCycleAwareQuestions()"| E["Database\ncycle-aware SELECT"]
    E -->|"List<Question>"| F["AnswerShuffler\nshuffle()"]
    F -->|"ShuffledQuestion"| G["prompts.dart\ndisplayQuestion()"]
    G -->|"stdin.readLineSync"| H["QuizEngine\nrecordResponse()"]
    H -->|"database.insertResponse()"| I[("SQLite\nquiz_responses")]
    H -->|"database.incrementUsage()"| D
    I -->|"COUNT correct"| J["QuizService\nfinalizeSession()"]
    J -->|"score + grade"| K["formatter.dart\ndisplayResult()"]
```

**Description:** All SQLite operations are synchronous; the Dart native binary has no external runtime dependency.
