# Architecture — quiz-engine-csharp

> Part of the [Quiz Engine multi-language collection](../README.md)

---

## System Overview

### 1000 ft View

A high-level picture of the four-project .NET solution and its layers.

```mermaid
graph TD
    User["👤 User (Terminal)"]
    CLI["QuizEngine.CLI\nSystem.CommandLine + Spectre.Console"]
    Service["QuizEngine.Service\nQuizService, HistoryService,\nImportService, MarkdownParser,\nAnswerShuffler"]
    Data["QuizEngine.Data\nEF Core Repositories\n(IQuestionRepository, ISessionRepository,\nIResponseRepository)"]
    Entities["QuizEngine.Entities\nQuestion, QuizSession, QuizResponse"]
    DB[("SQLite Database\nquiz_engine.db")]
    MD["📄 Markdown Files\n(question source)"]

    User -->|"quiz / import / history / clear"| CLI
    CLI --> Service
    Service --> Data
    Data --> Entities
    Data --> DB
    MD -->|"import --file / --dir"| CLI
```

**Description:** Four-layer solution — CLI, Service, Data, Entities — with EF Core managing SQLite persistence.

---

## Sequence Diagram

### Taking a Quiz Session

The request flow from `quiz` command to displayed results.

```mermaid
sequenceDiagram
    actor User
    participant CMD as QuizCommand
    participant SVC as QuizService
    participant Engine as QuizEngine (Service)
    participant Repo as QuestionRepository
    participant DB as SQLite (EF Core)

    User->>CMD: dotnet run -- quiz --questions 10
    CMD->>SVC: StartSessionAsync(10)
    SVC->>DB: INSERT QuizSession
    DB-->>SVC: QuizSession entity
    SVC->>Repo: GetCycleAwareQuestionsAsync(sessionId, 10)
    Repo->>DB: LINQ query (usage_count ASC)
    DB-->>Repo: IList<Question>
    Repo-->>SVC: questions
    SVC-->>CMD: QuizSession + Question[]
    loop Each Question
        CMD->>User: Spectre.Console prompt
        User-->>CMD: answer
        CMD->>Engine: RecordResponseAsync(sessionId, questionId, answer)
        Engine->>DB: INSERT QuizResponse
        Engine->>DB: UPDATE Question.UsageCount
    end
    CMD->>SVC: FinalizeSessionAsync(sessionId)
    SVC-->>CMD: score, grade
    CMD-->>User: Spectre result panel
```

**Description:** EF Core handles all persistence; Spectre.Console renders rich terminal output.

---

## ER Diagram

### Database Schema

Entity Framework Core entities mapped to SQLite tables.

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
        int UsageCount
        int CycleNumber
        datetime CreatedAt
        string SourceFile
    }
    QuizSessions {
        string Id PK
        datetime StartedAt
        datetime CompletedAt
        int TotalQuestions
        int CorrectAnswers
        double Score
        string Grade
    }
    QuizResponses {
        string Id PK
        string SessionId FK
        string QuestionId FK
        string SelectedAnswer
        bool IsCorrect
        int TimeTakenSeconds
        datetime AnsweredAt
    }

    QuizSessions ||--o{ QuizResponses : "contains"
    Questions ||--o{ QuizResponses : "answered in"
```

**Description:** EF Core navigational properties map these three entities; IDs are GUIDs.

---

## Class Diagram

### Solution Class Structure

Key classes across the four .NET projects and their dependencies.

```mermaid
classDiagram
    class QuizCommand {
        +ExecuteAsync(options) Task
    }

    class ImportCommand {
        +ExecuteAsync(options) Task
    }

    class HistoryCommand {
        +ExecuteAsync(options) Task
    }

    class QuizService {
        +StartSessionAsync(numQ) Task~QuizSession~
        +FinalizeSessionAsync(id) Task~QuizSession~
    }

    class HistoryService {
        +GetSessionsAsync() Task~List~
        +ExportJsonAsync(id) Task~string~
        +ExportCsvAsync(id) Task~string~
    }

    class ImportService {
        +ImportFileAsync(path) Task~int~
        +ImportDirectoryAsync(dir) Task~int~
    }

    class MarkdownParser {
        +Parse(content) List~Question~
    }

    class AnswerShuffler {
        +Shuffle(question) ShuffledQuestion
    }

    class QuestionRepository {
        +GetCycleAwareAsync(sessionId, limit) Task~List~
        +IncrementUsageAsync(id) Task
    }

    class SessionRepository {
        +CreateAsync(s) Task~QuizSession~
        +GetByIdAsync(id) Task~QuizSession~
    }

    class ResponseRepository {
        +CreateAsync(r) Task~QuizResponse~
        +GetBySessionAsync(id) Task~List~
    }

    QuizCommand --> QuizService
    QuizCommand --> AnswerShuffler
    ImportCommand --> ImportService
    HistoryCommand --> HistoryService
    QuizService --> QuestionRepository
    QuizService --> SessionRepository
    QuizService --> ResponseRepository
    ImportService --> MarkdownParser
    ImportService --> QuestionRepository
    HistoryService --> SessionRepository
    HistoryService --> ResponseRepository
```

**Description:** CLI commands depend on service interfaces; repositories depend on `QuizEngineDbContext`.

---

## Data Flow Diagram

### Question Import and Quiz Flow

How data moves through the solution layers during import and quiz.

```mermaid
flowchart LR
    A["📄 Markdown File"] -->|"File.ReadAllText"| B["MarkdownParser\nParse()"]
    B -->|"IList<Question>"| C["ImportService\nImportFileAsync()"]
    C -->|"DbContext.Questions.AddRange"| D[("SQLite\nQuestions table")]

    D -->|"LINQ cycle-aware query"| E["QuestionRepository\nGetCycleAwareAsync()"]
    E -->|"List<Question>"| F["AnswerShuffler\nShuffle()"]
    F -->|"ShuffledQuestion"| G["QuizCommand\nSpectre prompt"]
    G -->|"user answer"| H["QuizService\nRecordResponseAsync()"]
    H -->|"INSERT QuizResponse"| I[("SQLite\nQuizResponses table")]
    H -->|"UPDATE UsageCount"| D
    I -->|"GROUP BY session"| J["QuizService\nFinalizeSessionAsync()"]
    J -->|"Score, Grade"| K["ConsoleFormatter\nRender result panel"]
```

**Description:** EF Core change-tracking handles batch inserts; Spectre.Console renders all terminal output.
