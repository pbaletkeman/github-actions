# Quiz Engine — Java — Architecture

> See [README.md](README.md) for full setup and usage documentation.

- [Quiz Engine — Java — Architecture](#quiz-engine--java--architecture)
  - [Sequence Diagram — Quiz Command Flow](#sequence-diagram--quiz-command-flow)
  - [Class Diagram](#class-diagram)
  - [Entity Relationship Diagram](#entity-relationship-diagram)
  - [Data Flow Diagram](#data-flow-diagram)


---

## Sequence Diagram — Quiz Command Flow

```mermaid
sequenceDiagram
    actor User
    participant CLI as Main.java (picocli)
    participant Cmd as QuizCommand
    participant Svc as QuizService
    participant DAO as QuestionDao / SessionDao / ResponseDao
    participant DB as SQLite (JDBC)

    User->>CLI: java -jar quiz-engine.jar quiz --questions 10
    CLI->>Cmd: picocli dispatch
    Cmd->>Svc: startQuiz(10)
    Svc->>DAO: QuestionDao.getNextQuestions(10)
    DAO->>DB: SELECT * FROM questions ORDER BY usage_cycle, times_used
    DB-->>DAO: List<Question>
    DAO-->>Svc: List<Question>
    Svc->>DAO: SessionDao.insertSession(session)
    DAO->>DB: INSERT INTO quiz_sessions
    DB-->>DAO: sessionId (UUID)
    Svc-->>Cmd: Session + List<Question>

    loop For each question
        Cmd->>User: Print question + shuffled options
        User->>Cmd: stdin: A/B/C/D
        Cmd->>Svc: recordAnswer(sessionId, questionId, answer)
        Svc->>DAO: ResponseDao.insertResponse(response)
        DAO->>DB: INSERT INTO quiz_responses
        Svc->>DAO: QuestionDao.incrementUsage(questionId)
        DAO->>DB: UPDATE questions SET times_used = times_used + 1
    end

    Svc->>DAO: SessionDao.finalizeSession(sessionId, score)
    DAO->>DB: UPDATE quiz_sessions SET correct_answers, end_time
    Cmd->>User: Print final score and summary
```

---

## Class Diagram

```mermaid
classDiagram
    class Question {
        +String id
        +String questionText
        +String optionA
        +String optionB
        +String optionC
        +String optionD
        +String correctAnswer
        +String explanation
        +String category
        +int usageCycle
        +int timesUsed
        +getters() / setters()
    }

    class QuizSession {
        +String id
        +LocalDateTime startTime
        +LocalDateTime endTime
        +int totalQuestions
        +int correctAnswers
        +double getScorePercent()
        +getters() / setters()
    }

    class QuizResponse {
        +String id
        +String sessionId
        +String questionId
        +String selectedAnswer
        +boolean isCorrect
        +LocalDateTime answeredAt
        +getters() / setters()
    }

    class DatabaseManager {
        -Connection connection
        +connect(String dbPath) void
        +initializeSchema() void
        +getConnection() Connection
        +close() void
    }

    class QuestionDao {
        -DatabaseManager dbManager
        +getNextQuestions(int count) List~Question~
        +insertQuestion(Question q) void
        +incrementUsage(String id) void
        +deleteAll() void
    }

    class SessionDao {
        -DatabaseManager dbManager
        +insertSession(QuizSession s) String
        +finalizeSession(String id, int correct) void
        +getAllSessions() List~QuizSession~
        +getSession(String id) Optional~QuizSession~
    }

    class ResponseDao {
        -DatabaseManager dbManager
        +insertResponse(QuizResponse r) void
        +getResponsesBySession(String sessionId) List~QuizResponse~
    }

    class QuizService {
        -QuestionDao questionDao
        -SessionDao sessionDao
        -ResponseDao responseDao
        -AnswerShuffler shuffler
        +startQuiz(int count) QuizSession
        +recordAnswer(String sessionId, String questionId, String answer) boolean
        +finishQuiz(String sessionId) QuizSession
    }

    class ImportService {
        -QuestionDao questionDao
        -MarkdownParser parser
        +importFile(String path) int
        +importDirectory(String path) int
    }

    class HistoryService {
        -SessionDao sessionDao
        -ResponseDao responseDao
        +getSessions() List~QuizSession~
        +getDetail(String id) SessionDetail
        +exportJson(List~QuizSession~) String
        +exportCsv(List~QuizSession~) String
    }

    class MarkdownParser {
        +parse(String content) List~Question~
        +parseFile(Path path) List~Question~
    }

    class AnswerShuffler {
        +shuffle(List~String~ opts) List~String~
        +mapAnswer(String orig, List shuffled, List orig) String
    }

    class MainCommand {
        <<picocli>>
        +String dbOption
        +run() void
    }

    class QuizCommand {
        <<picocli>>
        +int questions
        +boolean noShuffle
        +run() void
    }

    class ImportCommand {
        <<picocli>>
        +String file
        +String dir
        +run() void
    }

    class HistoryCommand {
        <<picocli>>
        +String sessionId
        +String export
        +run() void
    }

    class ClearCommand {
        <<picocli>>
        +boolean questions
        +boolean history
        +boolean all
        +boolean confirm
        +run() void
    }

    DatabaseManager <-- QuestionDao
    DatabaseManager <-- SessionDao
    DatabaseManager <-- ResponseDao
    QuizService --> QuestionDao
    QuizService --> SessionDao
    QuizService --> ResponseDao
    QuizService --> AnswerShuffler
    ImportService --> QuestionDao
    ImportService --> MarkdownParser
    HistoryService --> SessionDao
    HistoryService --> ResponseDao
    MainCommand --> DatabaseManager
    QuizCommand --> QuizService
    ImportCommand --> ImportService
    HistoryCommand --> HistoryService
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
        U([User stdin]) --> MAIN[Main.java\npicocli CommandLine]
        M([Markdown File]) --> ICMD[ImportCommand]
    end

    subgraph Command Layer
        MAIN --> ROUTE{picocli\nCommand Router}
        ROUTE -->|quiz| QCMD[QuizCommand]
        ROUTE -->|import| ICMD
        ROUTE -->|history| HCMD[HistoryCommand]
        ROUTE -->|clear| CCMD[ClearCommand]
    end

    subgraph Service Layer
        QCMD --> QS[QuizService\n+ AnswerShuffler]
        ICMD --> IS[ImportService\n+ MarkdownParser]
        HCMD --> HS[HistoryService]
    end

    subgraph DAO Layer
        QS --> QDAO[QuestionDao]
        QS --> SDAO[SessionDao]
        QS --> RDAO[ResponseDao]
        IS --> QDAO
        HS --> SDAO
        HS --> RDAO
        CCMD --> QDAO
        CCMD --> SDAO
    end

    subgraph Data Layer
        QDAO --> DBM[DatabaseManager\nJDBC Connection]
        SDAO --> DBM
        RDAO --> DBM
        DBM --> FILE[(quiz_engine.db\nSQLite)]
    end

    subgraph Output
        QS --> QOUT([Quiz Results\nSystem.out])
        HS --> HOUT([History\nSystem.out / JSON / CSV])
        IS --> IOUT([Import Summary\nSystem.out])
    end
```
