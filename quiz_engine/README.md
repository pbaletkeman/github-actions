# Quiz Engine — Node.js / TypeORM

A **TypeScript + TypeORM** CLI quiz engine for studying the GitHub Actions GH-200 certification exam.

---

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
- [Installation](#installation)
- [CLI Commands](#cli-commands)
- [Architecture](#architecture)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Docker](#docker)
- [API Documentation](#api-documentation)

---

## Features

- 🧠 **Cycle-aware question selection** — never repeats a question until all questions have been seen
- 🔀 **Answer shuffling** — randomizes option order while tracking the correct answer
- 📥 **Markdown import** — parse questions from `.md` files
- 📊 **Session history** — track scores over time, review answers, export to JSON/CSV
- ✅ **Jest test suite** — 122 tests, ≥90% coverage enforced
- 🐳 **Docker** — production, test, and lint containers

---

## Getting Started

**Prerequisites:** Node.js 18+, npm

```bash
# Install dependencies
npm install

# Build TypeScript
npm run build

# Run the CLI
npm run dev -- --help
# or: node dist/main.js --help
```

---

## Installation

```bash
cd quiz_engine
npm install
npm run build
```

---

## CLI Commands

### `quiz` — Take a quiz

```bash
# Take a 10-question quiz (default)
npm run dev -- quiz

# Take a 100-question quiz
npm run dev -- quiz --questions 100

# Take a quiz with a time limit (60 seconds per question)
npm run dev -- quiz -q 50 --seconds-per 60
```

**Options:**
| Flag | Alias | Type | Default | Description |
|------|-------|------|---------|-------------|
| `--questions` | `-q` | number | 10 | Number of questions per session |
| `--seconds-per` | `-s` | number | 0 | Seconds per question (0 = unlimited) |

---

### `import` — Import questions from Markdown

```bash
# Import from a single file
npm run dev -- import --file ./quiz/gh-200-iteration-1.md

# Import from a directory
npm run dev -- import --dir ./quiz/
```

**Markdown format supported:**

```markdown
## Q1
> What does CI stand for?
- A) Continuous Integration
- B) Code Import
- C) Compiler Install
- D) Content Index
**Answer: A**
Optional explanation text here.
```

Also supports the extended GH-200 format with `**Difficulty**`, `**Topic**`, `**Scenario**`, etc.

**Options:**
| Flag | Alias | Description |
|------|-------|-------------|
| `--file` | `-f` | Path to a single `.md` file |
| `--dir` | `-d` | Path to a directory of `.md` files |

---

### `history` — View quiz history

```bash
# Show last 10 sessions
npm run dev -- history

# Show last 20 sessions
npm run dev -- history --limit 20

# Review answers for a specific session
npm run dev -- history --session-id <uuid> --review

# Export all sessions to JSON
npm run dev -- history --export json

# Export all sessions to CSV
npm run dev -- history --export csv
```

**Options:**
| Flag | Alias | Description |
|------|-------|-------------|
| `--session-id` | `-i` | Session UUID to inspect |
| `--review` | `-r` | Show full answer key for a session |
| `--export` | `-e` | Export format: `json` or `csv` |
| `--limit` | `-l` | Number of recent sessions (default: 10) |

---

### `clear` — Clear stored data

```bash
# Clear all questions (with confirmation prompt)
npm run dev -- clear --questions

# Clear session history (with confirmation prompt)
npm run dev -- clear --history

# Clear all data without prompt
npm run dev -- clear --all --confirm
```

**Options:**
| Flag | Description |
|------|-------------|
| `--questions` | Delete all questions from the database |
| `--history` | Delete all session history |
| `--all` | Delete everything (questions + sessions + responses) |
| `--confirm` | Skip the confirmation prompt |

---

## Architecture

```
quiz_engine/
├── src/
│   ├── main.ts                             # CLI entry point (yargs)
│   ├── models/
│   │   ├── Question.ts                     # TypeORM entity
│   │   ├── QuizSession.ts                  # TypeORM entity
│   │   └── QuizResponse.ts                 # TypeORM entity
│   ├── database/
│   │   ├── database.ts                     # AppDataSource (TypeORM)
│   │   ├── repositories/
│   │   │   ├── QuestionRepository.ts       # Cycle-aware queries
│   │   │   ├── SessionRepository.ts        # Session CRUD
│   │   │   └── ResponseRepository.ts       # Response tracking
│   │   └── migrations/
│   │       └── 1_InitialSchema.ts          # Database migration
│   ├── service/
│   │   ├── QuizEngine.ts                   # Session lifecycle, scoring
│   │   ├── QuizService.ts                  # Business logic wrapper
│   │   ├── HistoryService.ts               # History/export queries
│   │   ├── ImportService.ts                # Batch markdown import
│   │   ├── MarkdownParser.ts               # MD file parsing
│   │   ├── AnswerShuffler.ts               # Fisher-Yates shuffle
│   │   └── QuizUtils.ts                    # Helpers: scoring, formatting
│   ├── cli/
│   │   ├── commands/
│   │   │   ├── QuizCommand.ts              # `quiz` command
│   │   │   ├── ImportCommand.ts            # `import` command
│   │   │   ├── HistoryCommand.ts           # `history` command
│   │   │   └── ClearCommand.ts             # `clear` command
│   │   ├── Formatter.ts                    # Table/box/result formatting
│   │   └── Prompts.ts                      # inquirer interactive prompts
│   └── exceptions/
│       └── QuizExceptions.ts               # Custom error classes
├── test/
│   ├── setup.ts                            # In-memory SQLite, sample data
│   ├── unit/
│   │   ├── QuestionRepository.test.ts
│   │   ├── QuizEngine.test.ts
│   │   ├── AnswerShuffler.test.ts
│   │   ├── MarkdownParser.test.ts
│   │   ├── HistoryService.test.ts
│   │   ├── QuizService.test.ts
│   │   ├── QuizUtils.test.ts
│   │   ├── QuizExceptions.test.ts
│   │   └── ResponseRepository.test.ts
│   └── integration/
│       └── quiz.workflow.test.ts           # Full load → answer → finalize flow
├── jest.config.ts                          # Jest config with ≥90% thresholds
├── tsconfig.json                           # TypeScript configuration
├── package.json                            # Dependencies and scripts
├── Dockerfile                              # Production image (node:20-alpine)
├── docker-compose.yml                      # Dev / test / lint services
├── .env                                    # Environment config
└── README.md
```

---

## Database Schema

### `questions`

| Column | Type | Description |
|--------|------|-------------|
| `id` | INTEGER PK | Auto-increment |
| `questionText` | VARCHAR(2000) | Question body |
| `optionA`–`optionE` | VARCHAR(500) | Answer options |
| `correctAnswer` | VARCHAR(1) | `A`–`E` |
| `explanation` | VARCHAR(2000) | Optional explanation |
| `section` | VARCHAR(100) | Topic/section |
| `difficulty` | VARCHAR(50) | Easy/Medium/Hard |
| `sourceFile` | VARCHAR(255) | Origin markdown file |
| `usageCycle` | INTEGER | Current cycle number (starts at 1) |
| `timesUsed` | INTEGER | Times answered in current cycle |
| `lastUsedAt` | DATETIME | Last answered timestamp |
| `createdAt` | DATETIME | Insert timestamp |

### `quiz_sessions`

| Column | Type | Description |
|--------|------|-------------|
| `sessionId` | VARCHAR(36) PK | UUID |
| `startedAt` | DATETIME | Session start |
| `endedAt` | DATETIME | Session end |
| `numQuestions` | INTEGER | Questions in session |
| `numCorrect` | INTEGER | Correct answers |
| `percentageCorrect` | DECIMAL(5,2) | Score percentage |
| `timeTakenSeconds` | INTEGER | Total duration |

### `quiz_responses`

| Column | Type | Description |
|--------|------|-------------|
| `id` | INTEGER PK | Auto-increment |
| `sessionId` | VARCHAR(36) FK | Links to `quiz_sessions` |
| `questionId` | INTEGER FK | Links to `questions` |
| `userAnswer` | VARCHAR(1) | Answer submitted (`A`–`E`) |
| `isCorrect` | INTEGER | 1 = correct, 0 = wrong |
| `timeTakenSeconds` | INTEGER | Time to answer |
| `answeredAt` | DATETIME | Answer timestamp |

---

## Testing

```bash
# Run all tests
npm test

# Run with coverage report (enforces ≥90% thresholds)
npm run test:coverage

# Watch mode
npm run test:watch

# CI mode (no watch, strict coverage)
npm run test:ci
```

**Coverage thresholds (enforced by Jest):**

| Metric | Threshold |
|--------|-----------|
| Lines | 90% |
| Statements | 90% |
| Functions | 90% |
| Branches | 85% |

The test suite will **fail automatically** if coverage drops below these thresholds.

---

## Docker

### Quick Start

```bash
# Build image
docker build -t quiz-engine:latest .

# Run dev mode
docker-compose up quiz-engine

# Run tests with coverage
docker-compose up quiz-engine-test

# Run build/lint check
docker-compose up quiz-engine-lint
```

### Interactive quiz in Docker

```bash
docker run -it quiz-engine:latest npm run dev -- quiz --questions 10
```

---

## API Documentation

### `QuizEngine`

Core quiz session management.

#### `startSession(numQuestions: number): Promise<QuizState>`

Starts a new quiz session, loads randomized questions with shuffled answers.

- Throws `InsufficientQuestionsError` if not enough questions available.

#### `submitAnswer(state, questionIndex, displayAnswer, timeTakenSeconds?): Promise<boolean>`

Records an answer for a question.

- `displayAnswer`: Letter `A`–`E` in shuffled display order.
- Returns `true` if correct.
- Throws `InvalidAnswerError` for letters outside `A`–`E`.
- Mutates `state.numCorrect` and `state.questions[i].isCorrect`.

#### `finalizeSession(state: QuizState): Promise<QuizSession>`

Closes the session, calculates final score and duration.

- Returns the persisted `QuizSession` entity.
- Advances usage cycle if all questions have been seen.

---

### `AnswerShuffler`

#### `shuffleAnswers(question: Question): ShuffleResult`

Shuffles answer options using Fisher-Yates.

**Returns:**
```typescript
{
  shuffledOptions: string[];       // Options in display order
  correctShuffledIndex: number;    // Index of correct answer
  displayToOriginal: Record<string, string>; // Maps display letter to original
}
```

---

### `MarkdownParser`

#### `parseMarkdownFile(filePath: string): ParsedQuestion[]`

Parses a markdown file and returns an array of questions.

Throws `ParseError` if:
- File not found
- Question missing required options A–D
- Question missing `**Answer: X**` line

#### `parseMarkdownContent(content: string, sourceFile?): ParsedQuestion[]`

Same as above but operates on a raw string.

---

### `HistoryService`

#### `getRecentSessions(limit: number): Promise<SessionSummary[]>`

Returns the most recent `limit` sessions ordered by date descending.

#### `getAllSessions(): Promise<SessionSummary[]>`

Returns all sessions.

#### `getSessionReview(sessionId: string): Promise<SessionReview | null>`

Returns detailed review with all answered questions, user answers, and correct answers.

#### `exportSessions(format: 'json' | 'csv'): Promise<string>`

Exports all sessions as a JSON array or CSV string.

---

### `QuizUtils`

#### `calculatePercentage(numCorrect, numQuestions): number`

Returns score as a percentage rounded to 2 decimal places.

#### `getGrade(percentage): string`

Returns letter grade: `A` (≥90), `B` (≥80), `C` (≥70), `D` (≥60), `F` (<60).

#### `isPassing(percentage): boolean`

Returns `true` if percentage ≥ 70.

#### `formatDuration(seconds): string`

Formats seconds into human-readable string: `45s`, `2m 30s`, `1h 5m 10s`.

---

## Dependencies

| Package | Version | Purpose |
|---------|---------|---------|
| `typeorm` | ^0.3.20 | ORM with decorators |
| `sqlite3` | ^5.1.7 | SQLite driver |
| `yargs` | ^17.7.2 | CLI argument parsing |
| `chalk` | ^4.1.2 | Terminal colors |
| `cli-table3` | ^0.6.3 | Table formatting |
| `inquirer` | ^8.2.6 | Interactive prompts |
| `uuid` | ^9.0.1 | Session ID generation |
| `dotenv` | latest | Environment variables |
| `reflect-metadata` | ^0.1.14 | TypeORM decorator support |
| `typescript` | ^5.4.5 | Type safety |
| `jest` + `ts-jest` | ^29.x | Testing framework |
