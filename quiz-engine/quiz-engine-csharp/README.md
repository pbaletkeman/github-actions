# Quiz Engine — C# / .NET 8

> Part of the [Quiz Engine multi-language collection](../README.md)

A command-line quiz engine for GH-200 GitHub Actions certification preparation. Built with .NET 8, Entity Framework Core, Spectre.Console, and System.CommandLine.

## Features

- 📚 **Question Management**: Import questions from Markdown files
- 🔀 **Answer Shuffling**: Randomizes answer order per-question to prevent memorization
- 🔄 **Cycle-Aware**: Never repeats a question until all have been seen (usage cycles)
- 💾 **Persistence**: SQLite database via Entity Framework Core
- 📊 **History**: View past quiz sessions and detailed results
- 🎨 **Rich CLI**: Formatted output using Spectre.Console

## Project Structure

```
quiz-engine-csharp/
├── QuizEngine.sln                    # Solution file
├── QuizEngine.Entities/              # Entity models
│   ├── Question.cs
│   ├── QuizSession.cs
│   └── QuizResponse.cs
├── QuizEngine.Data/                  # EF Core data access
│   ├── QuizEngineDbContext.cs
│   ├── IQuestionRepository.cs
│   ├── QuestionRepository.cs
│   ├── ISessionRepository.cs
│   ├── SessionRepository.cs
│   ├── IResponseRepository.cs
│   └── ResponseRepository.cs
├── QuizEngine.Service/               # Business logic
│   ├── QuizService.cs
│   ├── HistoryService.cs
│   ├── ImportService.cs
│   ├── MarkdownParser.cs
│   └── AnswerShuffler.cs
├── QuizEngine.CLI/                   # CLI entry point
│   ├── Program.cs
│   ├── Prompts.cs
│   ├── Commands/
│   │   ├── QuizCommand.cs
│   │   ├── ImportCommand.cs
│   │   ├── HistoryCommand.cs
│   │   └── ClearCommand.cs
│   └── Formatters/
│       └── ConsoleFormatter.cs
├── QuizEngine.Tests/                 # xUnit tests
│   ├── DatabaseFixture.cs
│   ├── RepositoryTests.cs
│   ├── QuizEngineTests.cs
│   ├── AnswerShufflerTests.cs
│   ├── MarkdownParserTests.cs
│   └── ServiceTests.cs
├── Dockerfile                        # Multi-stage build
├── docker-compose.yml                # Container orchestration
└── README.md
```

## Prerequisites

- .NET 8 SDK: https://dotnet.microsoft.com/download/dotnet/8
- Or Docker (no local .NET required)

## Quick Start

### 1. Build

```bash
cd quiz-engine-csharp
dotnet build
```

### 2. Import Questions

Create a Markdown file with questions in this format:

```markdown
## Question 1

**Q: What does CI stand for?**

- A) Continuous Integration
- B) Code Integration
- C) Complete Infrastructure
- D) Cloud Infrastructure

**Answer: A**

**Explanation:** CI stands for Continuous Integration.

Section: GitHub Actions
Difficulty: easy
```

Then import:

```bash
dotnet run --project QuizEngine.CLI -- import --file questions.md

# Or import an entire directory
dotnet run --project QuizEngine.CLI -- import --dir ./questions/
```

### 3. Take a Quiz

```bash
# Take a 10-question quiz (default)
dotnet run --project QuizEngine.CLI -- quiz

# Custom number of questions
dotnet run --project QuizEngine.CLI -- quiz --questions 20

# Filter by difficulty
dotnet run --project QuizEngine.CLI -- quiz --questions 10 --difficulty easy

# Filter by section
dotnet run --project QuizEngine.CLI -- quiz --questions 10 --section "GitHub Actions"

# Skip explanations
dotnet run --project QuizEngine.CLI -- quiz --no-explanation
```

### 4. View History

```bash
# List recent sessions
dotnet run --project QuizEngine.CLI -- history

# Show last N sessions
dotnet run --project QuizEngine.CLI -- history --count 5

# Show session details
dotnet run --project QuizEngine.CLI -- history --session-id <uuid>
```

### 5. Clear Data

```bash
# Clear all questions
dotnet run --project QuizEngine.CLI -- clear --questions --confirm

# Clear history
dotnet run --project QuizEngine.CLI -- clear --history --confirm

# Clear everything
dotnet run --project QuizEngine.CLI -- clear --all --confirm
```

## Running Tests

```bash
# Run all tests
dotnet test

# Run with coverage enforcement (>= 90% required)
dotnet test /p:CollectCoverage=true \
            /p:CoverletOutputFormat=lcov \
            /p:CoverletOutput=./coverage/ \
            /p:Threshold=90 \
            /p:ThresholdType=line \
            /p:ExcludeByFile="**/Program.cs;**/Migrations/**"
```

## Docker

### Build and Run

```bash
# Build the image
docker build -t quiz-engine:latest .

# Run interactively
docker run -it quiz-engine:latest --help

# Run with persistent data volume
docker run -it -v quiz-data:/data quiz-engine:latest quiz --questions 10
```

### Docker Compose

```bash
# Run the CLI
docker-compose up quiz-engine

# Run tests
docker-compose up quiz-engine-test

# Build only
docker-compose up quiz-engine-build
```

## Architecture

### Repository Pattern

Data access is abstracted behind interfaces (`IQuestionRepository`, `ISessionRepository`, `IResponseRepository`), making it easy to test and swap implementations.

### Cycle-Aware Question Selection

Questions have a `UsageCycle` field. The engine always selects questions from the minimum cycle, ensuring no question is repeated until all others have been shown. After all questions in a cycle are used, they advance to the next cycle.

### Answer Shuffling

`AnswerShuffler` randomizes the order of answer options for each question and tracks the mapping from shuffled letters to original letters, ensuring correct scoring.

### Database

Uses SQLite via Entity Framework Core. The database file is created automatically at startup (`quiz.db` by default, configurable via `QUIZ_DB_PATH` environment variable).

## Dependencies

| Package | Version | Purpose |
|---|---|---|
| Microsoft.EntityFrameworkCore | 8.0.0 | ORM |
| Microsoft.EntityFrameworkCore.Sqlite | 8.0.0 | SQLite provider |
| Spectre.Console | 0.49.1 | Rich terminal output |
| System.CommandLine | 2.0.0-beta4 | CLI argument parsing |
| xUnit | 2.9.3 | Test framework |
| coverlet | 6.0.2 | Code coverage |
| Moq | 4.20.70 | Mocking for tests |
