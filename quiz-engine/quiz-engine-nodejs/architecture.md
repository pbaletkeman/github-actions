# Architecture — quiz-engine-nodejs

> Part of the [Quiz Engine multi-language collection](../README.md)

---

## System Overview

### 1000 ft View

A high-level picture of the system's components and external dependencies.

```mermaid
graph TD
    User["👤 User (Terminal)"]
    CLI["CLI Layer\n(yargs commands)"]
    Service["Service Layer\n(QuizEngine, QuizService,\nHistoryService, ImportService)"]
    Data["Data Layer\n(TypeORM Repositories)"]
    DB[("SQLite Database\nquiz_engine.db")]
    MD["📄 Markdown Files\n(question source)"]

    User -->|"quiz / import / history / clear"| CLI
    CLI --> Service
    Service --> Data
    Data --> DB
    MD -->|"--file / --dir"| CLI
```

**Description:** User commands flow through the CLI to service logic, persisted via TypeORM into SQLite.

---

## Sequence Diagram

### Taking a Quiz Session

How a `quiz` command executes from start to finish.

```mermaid
sequenceDiagram
    actor User
    participant CLI as QuizCommand
    participant Engine as QuizEngine
    participant Service as QuizService
    participant Repo as QuestionRepository
    participant DB as SQLite

    User->>CLI: npm run dev -- quiz --questions 10
    CLI->>Service: startSession(10)
    Service->>DB: INSERT INTO quiz_sessions
    Service->>Repo: findUnseenQuestions(sessionId)
    Repo->>DB: SELECT questions (cycle-aware)
    DB-->>Repo: question rows
    Repo-->>Service: Question[]
    Service-->>CLI: QuizSession + questions
    loop Each Question
        CLI->>User: display question + shuffled options
        User->>CLI: answer (A/B/C/D)
        CLI->>Engine: recordResponse(questionId, answer)
        Engine->>DB: INSERT INTO quiz_responses
    end
    CLI->>Service: finalizeSession(sessionId)
    Service-->>CLI: score, grade, summary
    CLI-->>User: display results
```

**Description:** Session creation, cycle-aware question selection, response recording, and final score display.

---

## ER Diagram

### Database Schema

The three SQLite tables and their relationships.

```mermaid
erDiagram
    QUESTIONS {
        string id PK
        string text
        string option_a
        string option_b
        string option_c
        string option_d
        string correct_answer
        string explanation
        string section
        string difficulty
        int usage_count
        int cycle_number
        datetime created_at
        string source_file
    }
    QUIZ_SESSIONS {
        string id PK
        datetime started_at
        datetime completed_at
        int total_questions
        int correct_answers
        float score
        string grade
    }
    QUIZ_RESPONSES {
        string id PK
        string session_id FK
        string question_id FK
        string selected_answer
        boolean is_correct
        int time_taken_seconds
        datetime answered_at
    }

    QUIZ_SESSIONS ||--o{ QUIZ_RESPONSES : "contains"
    QUESTIONS ||--o{ QUIZ_RESPONSES : "answered in"
```

**Description:** Each session has many responses; each response links to one question.

---

## Class Diagram

### Core TypeScript Classes and Interfaces

Key classes, interfaces, and their relationships in the service and data layers.

```mermaid
classDiagram
    class QuizEngine {
        +startSession(numQ: number) QuizSession
        +getNextQuestion(sessionId: string) Question
        +submitAnswer(sessionId, questionId, answer, timeTaken) QuizResponse
        +finalizeSession(sessionId: string) QuizSession
    }

    class QuizService {
        +createSession(numQ: number) QuizSession
        +getHistory() QuizSession[]
        +getSessionDetail(id: string) QuizSession
    }

    class HistoryService {
        +getSessions() QuizSession[]
        +exportJson(sessionId: string) string
        +exportCsv(sessionId: string) string
    }

    class ImportService {
        +importFile(path: string) number
        +importDirectory(dir: string) number
    }

    class MarkdownParser {
        +parse(content: string) Question[]
    }

    class AnswerShuffler {
        +shuffle(question: Question) ShuffledQuestion
    }

    class QuestionRepository {
        +findCycleAware(sessionId: string, limit: number) Question[]
        +incrementUsage(id: string) void
    }

    class SessionRepository {
        +create(session: QuizSession) QuizSession
        +findById(id: string) QuizSession
    }

    class ResponseRepository {
        +create(response: QuizResponse) QuizResponse
        +findBySession(sessionId: string) QuizResponse[]
    }

    QuizEngine --> QuizService
    QuizEngine --> AnswerShuffler
    QuizService --> QuestionRepository
    QuizService --> SessionRepository
    QuizService --> ResponseRepository
    ImportService --> MarkdownParser
    ImportService --> QuestionRepository
    HistoryService --> SessionRepository
    HistoryService --> ResponseRepository
```

**Description:** Service layer orchestrates repositories; `AnswerShuffler` and `MarkdownParser` are pure utilities.

---

## Data Flow Diagram

### Question Import and Quiz Flow

How data moves from a Markdown file through import to a completed quiz session.

```mermaid
flowchart LR
    A["📄 Markdown File"] -->|"read"| B["MarkdownParser\nparse()"]
    B -->|"Question[]"| C["ImportService\nimportFile()"]
    C -->|"INSERT"| D[("SQLite\nquestions table")]

    D -->|"SELECT cycle-aware"| E["QuestionRepository\nfindCycleAware()"]
    E -->|"Question[]"| F["AnswerShuffler\nshuffle()"]
    F -->|"shuffled options"| G["QuizCommand\ndisplay"]
    G -->|"user answer"| H["QuizEngine\nsubmitAnswer()"]
    H -->|"INSERT response"| I[("SQLite\nquiz_responses")]
    H -->|"UPDATE usage_count"| D
    I -->|"aggregate"| J["QuizService\nfinalizeSession()"]
    J -->|"score + grade"| K["ConsoleFormatter\ndisplay results"]
```

**Description:** Markdown is parsed on import; questions flow through shuffling, user interaction, and persistence on each quiz run.
