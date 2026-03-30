# Architecture — Quiz Engine C# / .NET 8

> Part of the [Quiz Engine multi-language collection](../../README.md)

- [Architecture — Quiz Engine C# / .NET 8](#architecture--quiz-engine-c--net-8)
  - [Sequence Diagram — Taking a Quiz Session](#sequence-diagram--taking-a-quiz-session)
  - [Class Diagram](#class-diagram)
  - [Entity Relationship Diagram](#entity-relationship-diagram)
  - [Data Flow Diagram](#data-flow-diagram)

---

## Sequence Diagram — Taking a Quiz Session

```mermaid
sequenceDiagram
    actor User
    participant CMD as QuizCommand (CLI)
    participant SVC as QuizService (Service)
    participant REPO as QuestionRepository (Data)
    participant DB as SQLite via EF Core

    User->>CMD: dotnet run -- quiz --questions 10
    CMD->>SVC: StartSessionAsync(numQuestions: 10)
    SVC->>DB: INSERT INTO QuizSessions
    DB-->>SVC: QuizSession entity
    SVC->>REPO: GetCycleAwareQuestionsAsync(sessionId, 10)
    REPO->>DB: SELECT WHERE usage_cycle = MIN(usage_cycle) LIMIT 10
    DB-->>REPO: List<Question>
    REPO-->>SVC: questions[]
    SVC-->>CMD: QuizSession + Question[]

    loop For each Question
        CMD->>User: Spectre.Console selection prompt
        User-->>CMD: selected answer (A–D)
        CMD->>SVC: RecordResponseAsync(sessionId, questionId, answer)
        SVC->>DB: INSERT INTO QuizResponses
        SVC->>DB: UPDATE Questions SET times_used = times_used + 1
    end

    CMD->>SVC: FinalizeSessionAsync(sessionId)
    SVC->>DB: UPDATE QuizSessions SET ended_at, score
    SVC-->>CMD: FinalScore (correct, total, percentage, grade)
    CMD-->>User: Spectre.Console results panel + explanations
```

---

## Class Diagram

```mermaid
classDiagram
    class Question {
        +Guid Id
        +string Text
        +string OptionA
        +string OptionB
        +string OptionC
        +string OptionD
        +string CorrectAnswer
        +string Explanation
        +string Section
        +string Difficulty
        +string SourceFile
        +int UsageCycle
        +int TimesUsed
        +DateTime CreatedAt
    }

    class QuizSession {
        +Guid Id
        +DateTime StartedAt
        +DateTime? EndedAt
        +int NumQuestions
        +int NumCorrect
        +double PercentageCorrect
        +int TimeTakenSeconds
    }

    class QuizResponse {
        +Guid Id
        +Guid SessionId
        +Guid QuestionId
        +string UserAnswer
        +bool IsCorrect
        +int TimeTakenSeconds
        +DateTime AnsweredAt
    }

    class IQuestionRepository {
        <<interface>>
        +GetCycleAwareQuestionsAsync(id, n) List~Question~
        +GetByIdAsync(id) Question
        +AddRangeAsync(questions) void
        +DeleteAllAsync() void
    }

    class ISessionRepository {
        <<interface>>
        +CreateAsync(session) QuizSession
        +FinalizeAsync(id, score) void
        +GetRecentAsync(count) List~QuizSession~
        +GetByIdAsync(id) QuizSession
    }

    class IResponseRepository {
        <<interface>>
        +AddAsync(response) void
        +GetBySessionAsync(sessionId) List~QuizResponse~
    }

    class QuizService {
        -IQuestionRepository _questions
        -ISessionRepository _sessions
        -IResponseRepository _responses
        -AnswerShuffler _shuffler
        +StartSessionAsync(n) QuizSession
        +RecordResponseAsync(sessionId, qId, answer) void
        +FinalizeSessionAsync(sessionId) FinalScore
    }

    class ImportService {
        -IQuestionRepository _questions
        -MarkdownParser _parser
        +ImportFileAsync(path) int
        +ImportDirectoryAsync(dir) int
    }

    class HistoryService {
        -ISessionRepository _sessions
        -IResponseRepository _responses
        +GetRecentSessionsAsync(count, sort, order) List~QuizSession~
        +GetSessionDetailAsync(id) SessionDetail
    }

    class AnswerShuffler {
        +Shuffle(question) ShuffledQuestion
        +MapDisplayToOriginal(shuffled, letter) string
    }

    class MarkdownParser {
        +ParseSimpleFormat(text) List~Question~
        +ParseAnswerKeyFormat(text) List~Question~
        +ParseFile(path) List~Question~
    }

    QuizResponse --> QuizSession : sessionId FK
    QuizResponse --> Question : questionId FK
    QuizService --> IQuestionRepository
    QuizService --> ISessionRepository
    QuizService --> IResponseRepository
    QuizService --> AnswerShuffler
    ImportService --> IQuestionRepository
    ImportService --> MarkdownParser
    HistoryService --> ISessionRepository
    HistoryService --> IResponseRepository
    IQuestionRepository <|.. QuestionRepository
    ISessionRepository <|.. SessionRepository
    IResponseRepository <|.. ResponseRepository
```

---

## Entity Relationship Diagram

```mermaid
erDiagram
    Questions {
        string Id PK
        string Text
        string OptionA
        string OptionB
        string OptionC
        string OptionD
        string CorrectAnswer
        string Explanation
        string Section
        string Difficulty
        string SourceFile
        int UsageCycle
        int TimesUsed
        datetime CreatedAt
    }

    QuizSessions {
        string Id PK
        datetime StartedAt
        datetime EndedAt
        int NumQuestions
        int NumCorrect
        float PercentageCorrect
        int TimeTakenSeconds
    }

    QuizResponses {
        string Id PK
        string SessionId FK
        string QuestionId FK
        string UserAnswer
        bool IsCorrect
        int TimeTakenSeconds
        datetime AnsweredAt
    }

    QuizSessions ||--o{ QuizResponses : "has"
    Questions ||--o{ QuizResponses : "answered in"
```

---

## Data Flow Diagram

```mermaid
flowchart TD
    A([User]) -->|"run quiz / import / history"| B[QuizEngine.CLI\nProgram.cs]

    B -->|"quiz command"| C[QuizCommand]
    B -->|"import command"| D[ImportCommand]
    B -->|"history command"| E[HistoryCommand]
    B -->|"clear command"| F[ClearCommand]

    C -->|"StartSessionAsync\nRecordResponseAsync\nFinalizeSessionAsync"| G[QuizService]
    D -->|"ImportFileAsync\nImportDirectoryAsync"| H[ImportService]
    E -->|"GetRecentSessionsAsync\nGetSessionDetailAsync"| I[HistoryService]
    F -->|"DeleteAllAsync"| J[(SQLite DB)]

    G -->|"GetCycleAwareQuestions\nIncrementUsage"| K[QuestionRepository]
    G -->|"CreateSession\nFinalizeSession"| L[SessionRepository]
    G -->|"AddResponse"| M[ResponseRepository]
    H -->|"ParseFile"| N[MarkdownParser]
    H -->|"AddRangeAsync"| K
    I -->|"GetRecentSessions"| L
    I -->|"GetBySession"| M

    G -->|"Shuffle"| O[AnswerShuffler]

    K --> J
    L --> J
    M --> J

    N -->|"parsed questions"| H
    O -->|"shuffled options"| G

    G -->|"session + questions"| C
    C -->|"formatted output"| P[Spectre.Console]
    P -->|"rich terminal UI"| A
```
