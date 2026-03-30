# Quiz Engine — Dart — Architecture

> See [README.md](README.md) for full setup and usage documentation.

- [Quiz Engine — Dart — Architecture](#quiz-engine--dart--architecture)
  - [Sequence Diagram — Quiz Command Flow](#sequence-diagram--quiz-command-flow)
  - [Class Diagram](#class-diagram)
  - [Entity Relationship Diagram](#entity-relationship-diagram)
  - [Data Flow Diagram](#data-flow-diagram)

---

## Sequence Diagram — Quiz Command Flow

```mermaid
sequenceDiagram
    actor User
    participant CLI as main.dart (CLI Runner)
    participant Cmd as QuizCommand
    participant Svc as QuizService
    participant Engine as QuizEngine
    participant Repo as AppDatabase
    participant DB as SQLite

    User->>CLI: quiz_engine quiz --questions 10
    CLI->>Cmd: parse args & dispatch
    Cmd->>Svc: startSession(questionCount: 10)
    Svc->>Repo: fetchNextQuestions(count: 10)
    Repo->>DB: SELECT with usage_cycle ordering
    DB-->>Repo: List<Question>
    Repo-->>Svc: List<Question>
    Svc->>Engine: createSession(questions)
    Engine-->>Svc: QuizSession (persisted)

    loop For each question
        Engine->>CLI: displayQuestion(question, shuffledOptions)
        CLI->>User: render options via formatter
        User->>CLI: stdin answer (A/B/C/D)
        CLI->>Engine: recordResponse(sessionId, questionId, answer)
        Engine->>Repo: insertResponse(QuizResponse)
        Repo->>DB: INSERT quiz_responses
        Engine->>Repo: updateUsage(questionId)
        Repo->>DB: UPDATE questions (times_used, usage_cycle)
    end

    Engine->>Repo: updateSession(sessionId, score)
    Repo->>DB: UPDATE quiz_sessions
    Engine->>CLI: displayResults(score, total)
    CLI->>User: render final score
```

---

## Class Diagram

```mermaid
classDiagram
    class Question {
        +String id
        +String questionText
        +List~String~ options
        +String correctAnswer
        +String? explanation
        +int usageCycle
        +int timesUsed
        +Question.fromMap(Map)
        +Map toMap()
    }

    class QuizSession {
        +String id
        +DateTime startTime
        +DateTime? endTime
        +int totalQuestions
        +int correctAnswers
        +double get scorePercent
        +QuizSession.fromMap(Map)
        +Map toMap()
    }

    class QuizResponse {
        +String id
        +String sessionId
        +String questionId
        +String selectedAnswer
        +bool isCorrect
        +DateTime answeredAt
        +QuizResponse.fromMap(Map)
        +Map toMap()
    }

    class AppDatabase {
        +Database _db
        +Future~void~ open(String path)
        +Future~void~ initialize()
        +Future~List~Question~~ fetchNextQuestions(int count)
        +Future~QuizSession~ insertSession(QuizSession)
        +Future~void~ insertResponse(QuizResponse)
        +Future~void~ updateSessionScore(String id, int correct)
        +Future~void~ updateQuestionUsage(String id)
        +Future~List~QuizSession~~ fetchHistory()
        +Future~List~QuizResponse~~ fetchResponses(String sessionId)
        +Future~int~ importQuestion(Question)
        +Future~void~ close()
    }

    class QuizService {
        -AppDatabase _db
        -AnswerShuffler _shuffler
        +Future~QuizSession~ startSession(int questionCount)
        +Future~void~ recordResponse(String sessionId, String questionId, String answer)
        +Future~QuizSession~ finishSession(String sessionId)
        +Future~List~QuizSession~~ getHistory()
    }

    class QuizEngine {
        -QuizService _service
        -List~Question~ _questions
        -QuizSession _session
        +Future~void~ run()
        +void displayQuestion(int index, Question q, List~String~ shuffled)
        +String promptAnswer(int optionCount)
    }

    class AnswerShuffler {
        +List~String~ shuffle(List~String~ options)
        +String mapShuffled(String original, List~String~ shuffled, List~String~ origOrder)
    }

    class MarkdownParser {
        +List~Question~ parseFile(String path)
        +List~Question~ parseContent(String content)
    }

    class ImportService {
        -AppDatabase _db
        -MarkdownParser _parser
        +Future~int~ importFile(String path)
        +Future~int~ importDirectory(String path)
    }

    class HistoryService {
        -AppDatabase _db
        +Future~List~QuizSession~~ allSessions()
        +Future~QuizSession~ sessionDetail(String id)
        +Future~List~QuizResponse~~ sessionResponses(String id)
        +String exportJson(List~QuizSession~ sessions)
        +String exportCsv(List~QuizSession~ sessions)
    }

    class QuizCommand {
        +ArgParser argParser
        +Future~void~ run(ArgResults args, AppDatabase db)
    }

    class ImportCommand {
        +ArgParser argParser
        +Future~void~ run(ArgResults args, AppDatabase db)
    }

    class HistoryCommand {
        +ArgParser argParser
        +Future~void~ run(ArgResults args, AppDatabase db)
    }

    class ClearCommand {
        +ArgParser argParser
        +Future~void~ run(ArgResults args, AppDatabase db)
    }

    QuizEngine --> QuizService
    QuizService --> AppDatabase
    QuizService --> AnswerShuffler
    ImportService --> AppDatabase
    ImportService --> MarkdownParser
    HistoryService --> AppDatabase
    QuizSession "1" --o "many" QuizResponse : contains
    QuizSession "many" --o "many" Question : references via responses
    QuizCommand --> QuizEngine
    ImportCommand --> ImportService
    HistoryCommand --> HistoryService
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

    QUIZ_SESSIONS ||--o{ QUIZ_RESPONSES : "has"
    QUESTIONS ||--o{ QUIZ_RESPONSES : "referenced by"
```

---

## Data Flow Diagram

```mermaid
flowchart TD
    subgraph Input
        A([User via stdin]) --> B[main.dart CLI Runner]
        F([Markdown File]) --> I[import command]
    end

    subgraph CLI Layer
        B --> C{Command Router\nargs package}
        C -->|quiz| D[QuizCommand]
        C -->|import| I[ImportCommand]
        C -->|history| H[HistoryCommand]
        C -->|clear| K[ClearCommand]
    end

    subgraph Service Layer
        D --> E[QuizEngine]
        E --> S[QuizService]
        I --> IS[ImportService]
        IS --> MP[MarkdownParser]
        H --> HS[HistoryService]
    end

    subgraph Data Layer
        S --> DB[(AppDatabase\nSQLite)]
        IS --> DB
        HS --> DB
        K --> DB
    end

    subgraph Output
        DB --> S
        S --> E
        E --> R([Quiz Results\nstdout/Rich UI])
        HS --> EXP([History\nstdout / JSON / CSV])
    end
```
