# Architecture — quiz-engine-springboot

> Part of the [Quiz Engine multi-language collection](../README.md)

---

## System Overview

### 1000 ft View

A high-level picture of the Spring Boot application with both web and CLI interfaces.

```mermaid
graph TD
    WebUser["🌐 Web Browser\nhttp://localhost:8080"]
    CLIUser["👤 CLI User (Terminal)"]

    Controller["QuizController\n(REST API)"]
    Thymeleaf["Thymeleaf Templates\nindex.html, quiz.html,\nhistory.html"]

    CLICommands["cli/\nQuizCommand, ImportCommand,\nHistoryCommand, ClearCommand\n(Picocli)"]

    Service["service/\nQuizEngine, QuizService,\nHistoryService, ImportService"]
    Util["util/\nAnswerShuffler, MarkdownParser, QuizUtils"]

    Repo["repository/\nQuestionRepository, SessionRepository,\nResponseRepository\n(Spring Data JPA)"]

    DB[("H2 in-memory (test)\nSQLite (production)")]

    MD["📄 Markdown Files\n(question source)"]

    WebUser --> Thymeleaf
    WebUser --> Controller
    CLIUser --> CLICommands
    Controller --> Service
    CLICommands --> Service
    Thymeleaf --> Controller
    Service --> Util
    Service --> Repo
    Repo --> DB
    MD -->|"REST POST /api/import\nor CLI import"| Service
```

**Description:** Spring Boot exposes REST + Thymeleaf web UI alongside a Picocli CLI backed by Spring Data JPA.

---

## Sequence Diagram

### Web Quiz Flow (REST API)

A browser-based quiz session from start to answer submission.

```mermaid
sequenceDiagram
    actor Browser
    participant Ctrl as QuizController
    participant Engine as QuizEngine
    participant SVC as QuizService
    participant QRepo as QuestionRepository
    participant SRepo as SessionRepository
    participant DB as H2/SQLite (JPA)

    Browser->>Ctrl: POST /api/quiz/start {"numQuestions": 10}
    Ctrl->>Engine: startSession(10)
    Engine->>SVC: createSession(10)
    SVC->>SRepo: save(new QuizSession)
    SRepo->>DB: INSERT quiz_sessions
    DB-->>SRepo: QuizSession (with id)
    Engine->>QRepo: findCycleAware(sessionId, 10)
    QRepo->>DB: JPQL ORDER BY usageCount
    DB-->>QRepo: List<Question>
    Engine-->>Ctrl: StartQuizResponse (sessionId, questions)
    Ctrl-->>Browser: 200 JSON

    Browser->>Ctrl: POST /api/quiz/{sessionId}/answer {"questionIndex": 0, "answer": "A", "timeTaken": 15}
    Ctrl->>Engine: submitAnswer(sessionId, qIndex, answer, timeTaken)
    Engine->>DB: INSERT quiz_responses
    Engine->>DB: UPDATE question.usageCount
    Engine-->>Ctrl: AnswerResult (correct, explanation)
    Ctrl-->>Browser: 200 JSON
```

**Description:** REST endpoints delegate to service beans; JPA manages transaction boundaries.

---

## ER Diagram

### Database Schema

JPA entities mapped to H2 (test) and SQLite (production) by Spring Data.

```mermaid
erDiagram
    QUESTIONS {
        VARCHAR id PK
        TEXT text
        VARCHAR option_a
        VARCHAR option_b
        VARCHAR option_c
        VARCHAR option_d
        VARCHAR correct_answer
        TEXT explanation
        VARCHAR section
        VARCHAR difficulty
        INT usage_count
        INT cycle_number
        TIMESTAMP created_at
        VARCHAR source_file
    }
    QUIZ_SESSIONS {
        VARCHAR id PK
        TIMESTAMP started_at
        TIMESTAMP completed_at
        INT total_questions
        INT correct_answers
        DOUBLE score
        VARCHAR grade
    }
    QUIZ_RESPONSES {
        VARCHAR id PK
        VARCHAR session_id FK
        VARCHAR question_id FK
        VARCHAR selected_answer
        BOOLEAN is_correct
        INT time_taken_seconds
        TIMESTAMP answered_at
    }

    QUIZ_SESSIONS ||--o{ QUIZ_RESPONSES : "contains"
    QUESTIONS ||--o{ QUIZ_RESPONSES : "answered in"
```

**Description:** JPA `@Entity` annotations drive DDL auto-generation; `@OneToMany` navigates session to responses.

---

## Class Diagram

### Core Spring Boot Classes

Key beans and their dependency injection relationships.

```mermaid
classDiagram
    class QuizController {
        +startQuiz(StartQuizRequest) ResponseEntity
        +submitAnswer(sessionId, AnswerRequest) ResponseEntity
        +getHistory() ResponseEntity
        +importQuestions(ImportRequest) ResponseEntity
    }

    class QuizCommand {
        +run() void
    }

    class ImportCommand {
        +run() void
    }

    class QuizEngine {
        +startSession(numQ) QuizSession
        +submitAnswer(sessionId, qId, answer, time) AnswerResult
        +finalizeSession(sessionId) QuizSession
    }

    class QuizService {
        +createSession(numQ) QuizSession
        +getSession(id) QuizSession
    }

    class HistoryService {
        +getSessions() List~QuizSession~
        +getSessionDetail(id) QuizSession
    }

    class ImportService {
        +importContent(content, source) int
    }

    class MarkdownParser {
        +parse(content) List~Question~
    }

    class AnswerShuffler {
        +shuffle(q) ShuffledQuestion
    }

    class QuestionRepository {
        +findCycleAware(sessionId, pageable) List~Question~
    }

    class SessionRepository {
        +findAllOrderByStartedAtDesc() List~QuizSession~
    }

    class ResponseRepository {
        +findBySessionId(id) List~QuizResponse~
    }

    QuizController --> QuizEngine
    QuizController --> HistoryService
    QuizController --> ImportService
    QuizCommand --> QuizEngine
    ImportCommand --> ImportService
    QuizEngine --> QuizService
    QuizEngine --> AnswerShuffler
    QuizEngine --> QuestionRepository
    QuizEngine --> ResponseRepository
    QuizService --> SessionRepository
    ImportService --> MarkdownParser
    ImportService --> QuestionRepository
    HistoryService --> SessionRepository
    HistoryService --> ResponseRepository
```

**Description:** All beans managed by Spring IoC; `@Transactional` on service methods ensures consistency.

---

## Data Flow Diagram

### Question Import and Quiz Flow (Web)

How data flows through the Spring layers during import and a web quiz session.

```mermaid
flowchart LR
    A["📄 Markdown Content\n(POST body or file)"] -->|"importContent()"| B["MarkdownParser\nparse()"]
    B -->|"List<Question>"| C["ImportService\nimportContent()"]
    C -->|"JPA saveAll()"| D[("H2 / SQLite\nQUESTIONS table")]

    D -->|"JPQL cycle-aware"| E["QuestionRepository\nfindCycleAware()"]
    E -->|"List<Question>"| F["AnswerShuffler\nshuffle()"]
    F -->|"ShuffledQuestion[]"| G["QuizController\nStartQuizResponse JSON"]
    G -->|"browser answer"| H["QuizEngine\nsubmitAnswer()"]
    H -->|"JPA save()"| I[("H2 / SQLite\nQUIZ_RESPONSES")]
    H -->|"JPA save() usageCount++"| D
    I -->|"JPA aggregate"| J["QuizService\nfinalizeSession()"]
    J -->|"score + grade JSON"| K["Thymeleaf / REST\nrender result"]
```

**Description:** Spring Data JPA repositories abstract all SQL; Thymeleaf renders server-side HTML for the web UI.
