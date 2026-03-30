# Quiz Engine — Node.js — Architecture

> See [README.md](README.md) for full setup and usage documentation.

- [Quiz Engine — Node.js — Architecture](#quiz-engine--nodejs--architecture)
  - [Sequence Diagram — Quiz Command Flow](#sequence-diagram--quiz-command-flow)
  - [Class Diagram](#class-diagram)
  - [Entity Relationship Diagram](#entity-relationship-diagram)
  - [Data Flow Diagram](#data-flow-diagram)

---

## Sequence Diagram — Quiz Command Flow

```mermaid
sequenceDiagram
    actor User
    participant CLI as main.ts (yargs)
    participant Cmd as quiz.ts (Command)
    participant Svc as QuizService
    participant QR as QuestionRepository
    participant SR as SessionRepository
    participant RR as ResponseRepository
    participant DB as SQLite (TypeORM)

    User->>CLI: node dist/main.js quiz --questions 10
    CLI->>Cmd: yargs handler(argv)
    Cmd->>Svc: startSession(count: 10)
    Svc->>QR: getNextQuestions(10)
    QR->>DB: find({ order: { usageCycle, timesUsed } })
    DB-->>QR: Question[]
    QR-->>Svc: Question[]
    Svc->>SR: createSession(session)
    SR->>DB: INSERT INTO quiz_sessions
    DB-->>SR: QuizSession (with id)
    Svc-->>Cmd: { session, questions }

    loop For each question
        Cmd->>User: inquirer.prompt(question + shuffled options)
        User->>Cmd: selected answer
        Cmd->>Svc: recordResponse(sessionId, questionId, answer)
        Svc->>RR: save(QuizResponse)
        RR->>DB: INSERT INTO quiz_responses
        Svc->>QR: updateUsage(questionId)
        QR->>DB: UPDATE questions SET timesUsed++
    end

    Svc->>SR: finalizeSession(sessionId, score)
    SR->>DB: UPDATE quiz_sessions SET correctAnswers, endTime
    Cmd->>User: Display score summary
```

---

## Class Diagram

```mermaid
classDiagram
    class Question {
        <<Entity>>
        +string id
        +string questionText
        +string optionA
        +string optionB
        +string optionC
        +string optionD
        +string correctAnswer
        +string explanation
        +string category
        +number usageCycle
        +number timesUsed
        +Date createdAt
    }

    class QuizSession {
        <<Entity>>
        +string id
        +Date startTime
        +Date endTime
        +number totalQuestions
        +number correctAnswers
        +number scorePercent
        +QuizResponse[] responses
    }

    class QuizResponse {
        <<Entity>>
        +string id
        +string sessionId
        +string questionId
        +string selectedAnswer
        +boolean isCorrect
        +Date answeredAt
    }

    class QuestionRepository {
        <<TypeORM Repository>>
        +getNextQuestions(count: number) Promise~Question[]~
        +updateUsage(id: string) Promise~void~
        +importQuestion(q: Partial~Question~) Promise~Question~
        +deleteAll() Promise~void~
    }

    class SessionRepository {
        <<TypeORM Repository>>
        +createSession(s: Partial~QuizSession~) Promise~QuizSession~
        +finalizeSession(id: string, correct: number) Promise~void~
        +getAllSessions() Promise~QuizSession[]~
        +getSession(id: string) Promise~QuizSession | null~
    }

    class ResponseRepository {
        <<TypeORM Repository>>
        +saveResponse(r: Partial~QuizResponse~) Promise~QuizResponse~
        +getBySession(sessionId: string) Promise~QuizResponse[]~
    }

    class QuizService {
        -questionRepo: QuestionRepository
        -sessionRepo: SessionRepository
        -responseRepo: ResponseRepository
        -shuffler: AnswerShuffler
        +startSession(count: number) Promise~SessionStart~
        +recordResponse(sessionId, qId, answer) Promise~boolean~
        +finishSession(sessionId: string) Promise~QuizSession~
    }

    class ImportService {
        -questionRepo: QuestionRepository
        -parser: MarkdownParser
        +importFile(path: string) Promise~number~
        +importDirectory(path: string) Promise~number~
    }

    class HistoryService {
        -sessionRepo: SessionRepository
        -responseRepo: ResponseRepository
        +getSessions() Promise~QuizSession[]~
        +getDetail(id: string) Promise~SessionDetail~
        +exportJson(sessions: QuizSession[]) string
        +exportCsv(sessions: QuizSession[]) string
    }

    class MarkdownParser {
        +parseFile(path: string) Promise~Question[]~
        +parseContent(content: string) Question[]
    }

    class AnswerShuffler {
        +shuffle(options: string[]) string[]
        +mapAnswer(original, shuffled, origOrder) string
    }

    class QuizExceptions {
        <<namespace>>
        +NoDatabaseError
        +NoQuestionsError
        +SessionNotFoundError
        +ImportError
    }

    QuizService --> QuestionRepository
    QuizService --> SessionRepository
    QuizService --> ResponseRepository
    QuizService --> AnswerShuffler
    ImportService --> QuestionRepository
    ImportService --> MarkdownParser
    HistoryService --> SessionRepository
    HistoryService --> ResponseRepository
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
        U([User stdin\ninquirer prompts]) --> MAIN[main.ts\nyargs setup]
        M([Markdown File]) --> ICMD[importCmd.ts]
    end

    subgraph Command Layer
        MAIN --> ROUTE{yargs\nCommand Router}
        ROUTE -->|quiz| QCMD[quiz.ts]
        ROUTE -->|import| ICMD
        ROUTE -->|history| HCMD[history.ts]
        ROUTE -->|clear| CCMD[clear.ts]
    end

    subgraph Service Layer
        QCMD --> QS[QuizService\n+ AnswerShuffler]
        ICMD --> IS[ImportService\n+ MarkdownParser]
        HCMD --> HS[HistoryService]
    end

    subgraph Repository Layer
        QS --> QR[QuestionRepository]
        QS --> SR[SessionRepository]
        QS --> RR[ResponseRepository]
        IS --> QR
        HS --> SR
        HS --> RR
        CCMD --> QR
        CCMD --> SR
    end

    subgraph Data Layer
        QR --> ORM[TypeORM\nDataSource]
        SR --> ORM
        RR --> ORM
        ORM --> DB[(quiz_engine.db\nbetter-sqlite3)]
    end

    subgraph Output
        QS --> QOUT([Quiz Results\nConsole / inquirer])
        HS --> HOUT([History\nConsole / JSON / CSV])
        IS --> IOUT([Import Count\nConsole])
    end
```
