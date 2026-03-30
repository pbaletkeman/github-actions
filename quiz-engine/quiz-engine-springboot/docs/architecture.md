# Quiz Engine — Spring Boot — Architecture

> See [README.md](README.md) for full setup and usage documentation.

- [Quiz Engine — Spring Boot — Architecture](#quiz-engine--spring-boot--architecture)
  - [Sequence Diagram — Quiz Flow (Web + REST)](#sequence-diagram--quiz-flow-web--rest)
  - [Class Diagram](#class-diagram)
  - [Entity Relationship Diagram](#entity-relationship-diagram)
  - [Data Flow Diagram](#data-flow-diagram)

---

## Sequence Diagram — Quiz Flow (Web + REST)

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant WebCtrl as WebController (Thymeleaf)
    participant RestCtrl as QuizRestController
    participant Svc as QuizService
    participant Repo as Spring Data JPA Repos
    participant DB as SQLite / H2

    alt Web UI Flow
        User->>Browser: Open http://localhost:8080
        Browser->>WebCtrl: GET /
        WebCtrl-->>Browser: index.html (Thymeleaf)
        User->>Browser: Click "Start Quiz" (10 questions)
        Browser->>WebCtrl: POST /quiz/start
        WebCtrl->>Svc: startSession(10)
        Svc->>Repo: QuestionRepository.findNextQuestions(10)
        Repo->>DB: JPQL ORDER BY usageCycle, timesUsed
        DB-->>Repo: List~Question~
        Svc->>Repo: SessionRepository.save(session)
        Repo->>DB: INSERT quiz_sessions
        WebCtrl-->>Browser: quiz.html with first question

        loop For each question
            User->>Browser: Select answer + submit
            Browser->>WebCtrl: POST /quiz/{sessionId}/answer
            WebCtrl->>Svc: recordAnswer(sessionId, qId, answer)
            Svc->>Repo: ResponseRepository.save(response)
            Svc->>Repo: QuestionRepository.incrementUsage(qId)
            WebCtrl-->>Browser: next question or results.html
        end
    end

    alt REST API Flow
        User->>RestCtrl: POST /api/quiz/start (JSON)
        RestCtrl->>Svc: startSession(count)
        Svc->>Repo: fetch + persist
        RestCtrl-->>User: 201 { sessionId, questions }
        User->>RestCtrl: POST /api/quiz/{id}/answer
        RestCtrl->>Svc: recordAnswer(...)
        RestCtrl-->>User: 200 { correct, correctAnswer }
    end
```

---

## Class Diagram

```mermaid
classDiagram
    class Question {
        <<Entity>>
        +Long id
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
        +LocalDateTime createdAt
        +getters() / setters()
    }

    class QuizSession {
        <<Entity>>
        +Long id
        +String sessionUuid
        +LocalDateTime startTime
        +LocalDateTime endTime
        +int totalQuestions
        +int correctAnswers
        +double getScorePercent()
        +List~QuizResponse~ responses
    }

    class QuizResponse {
        <<Entity>>
        +Long id
        +String responseUuid
        +QuizSession session
        +Question question
        +String selectedAnswer
        +boolean isCorrect
        +LocalDateTime answeredAt
    }

    class QuestionRepository {
        <<JpaRepository>>
        +findNextQuestions(int count) List~Question~
        +incrementUsage(Long id) void
        +deleteAllQuestions() void
    }

    class SessionRepository {
        <<JpaRepository>>
        +findBySessionUuid(String uuid) Optional~QuizSession~
        +findAllByOrderByStartTimeDesc() List~QuizSession~
    }

    class ResponseRepository {
        <<JpaRepository>>
        +findBySessionUuid(String uuid) List~QuizResponse~
    }

    class QuizService {
        -QuestionRepository questionRepo
        -SessionRepository sessionRepo
        -ResponseRepository responseRepo
        -AnswerShuffler shuffler
        +startSession(int count) SessionStartResult
        +recordAnswer(String sessionId, Long qId, String ans) AnswerResult
        +finishSession(String sessionId) QuizSession
    }

    class ImportService {
        -QuestionRepository questionRepo
        -MarkdownParser parser
        +importFile(String path) int
        +importDirectory(String path) int
    }

    class HistoryService {
        -SessionRepository sessionRepo
        -ResponseRepository responseRepo
        +getAllSessions() List~QuizSession~
        +getDetail(String uuid) SessionDetail
        +exportJson(List~QuizSession~) String
        +exportCsv(List~QuizSession~) String
    }

    class WebController {
        <<Controller>>
        -QuizService quizService
        -ImportService importService
        -HistoryService historyService
        +index(Model) String
        +startQuiz(int count, Model) String
        +showQuestion(String sessionId, int idx, Model) String
        +submitAnswer(String sessionId, AnswerForm, Model) String
        +results(String sessionId, Model) String
        +history(Model) String
    }

    class QuizRestController {
        <<RestController>>
        -QuizService quizService
        +startSession(StartRequest) ResponseEntity
        +submitAnswer(String id, AnswerRequest) ResponseEntity
        +finishSession(String id) ResponseEntity
        +getHistory() ResponseEntity
    }

    class ImportRestController {
        <<RestController>>
        -ImportService importService
        +importFile(MultipartFile) ResponseEntity
    }

    class QuizCli {
        <<Command Picocli>>
        -QuizService quizService
        -ImportService importService
        -HistoryService historyService
        +quiz(int questions)
        +importCmd(String file, String dir)
        +history(String sessionId)
        +clear(boolean confirm)
    }

    QuizService --> QuestionRepository
    QuizService --> SessionRepository
    QuizService --> ResponseRepository
    QuizService --> AnswerShuffler
    ImportService --> QuestionRepository
    HistoryService --> SessionRepository
    HistoryService --> ResponseRepository
    WebController --> QuizService
    WebController --> ImportService
    WebController --> HistoryService
    QuizRestController --> QuizService
    ImportRestController --> ImportService
    QuizCli --> QuizService
    QuizCli --> ImportService
    QuizCli --> HistoryService
    QuizSession "1" --o "many" QuizResponse
    Question "1" --o "many" QuizResponse
```

---

## Entity Relationship Diagram

```mermaid
erDiagram
    QUESTIONS {
        BIGINT id PK
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
        BIGINT id PK
        TEXT session_uuid
        TEXT start_time
        TEXT end_time
        INTEGER total_questions
        INTEGER correct_answers
        REAL score_percent
    }

    QUIZ_RESPONSES {
        BIGINT id PK
        TEXT response_uuid
        BIGINT session_id FK
        BIGINT question_id FK
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
        B([Browser\nhttp://localhost:8080]) --> WC[WebController\nThymeleaf MVC]
        R([REST Client\nJSON]) --> RC[QuizRestController]
        C([CLI stdin\nPicocli]) --> CLI[QuizCli]
        M([Markdown File\nPOST /api/import]) --> IC[ImportRestController]
    end

    subgraph Controller Layer
        WC --> QS[QuizService]
        RC --> QS
        CLI --> QS
        WC --> IS[ImportService]
        IC --> IS
        WC --> HS[HistoryService]
        CLI --> IS
        CLI --> HS
    end

    subgraph Service Layer
        QS --> QREPO[QuestionRepository\nSpring Data JPA]
        QS --> SREPO[SessionRepository]
        QS --> RREPO[ResponseRepository]
        IS --> QREPO
        HS --> SREPO
        HS --> RREPO
    end

    subgraph Data Layer
        QREPO --> JPA[Hibernate / JPA]
        SREPO --> JPA
        RREPO --> JPA
        JPA -->|prod| SQLITE[(SQLite\nquiz_engine.db)]
        JPA -->|test| H2[(H2 In-Memory\ntestdb)]
    end

    subgraph Output
        WC -->|Thymeleaf| HTML([HTML Pages\nquiz.html / results.html])
        RC -->|JSON| JSON_OUT([REST JSON\nResponseEntity])
        CLI -->|stdout| STDOUT([Console\nSystem.out])
    end
```
