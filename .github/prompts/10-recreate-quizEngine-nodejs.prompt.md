# Recreate quiz-engine-nodejs from Scratch

> **Purpose:** This prompt is a complete, self-contained specification for recreating the `quiz-engine-nodejs` project. Every section below contains the full detail needed to build the project from zero — no prior context required.

---

## 1. Project Structure

Every file and directory under `quiz-engine-nodejs/`:

```
quiz-engine-nodejs/
├── .env                              # Runtime environment variables (DATABASE_PATH, NODE_ENV, LOG_LEVEL)
├── .gitignore                        # Ignores node_modules, dist, coverage, *.db files
├── Dockerfile                        # Multi-stage build: node:20-alpine, non-root user, ENTRYPOINT node dist/main.js
├── README.md                         # Top-level quick-start README
├── architecture.md                   # Mermaid diagrams: sequence, ER, class, data-flow (root copy)
├── docker-compose.yml                # Three services: quiz-engine, quiz-engine-test, quiz-engine-lint
├── jest.config.ts                    # Jest config: ts-jest preset, coverage thresholds, collectCoverageFrom
├── package.json                      # NPM manifest: scripts, dependencies, devDependencies
├── package-lock.json                 # Locked dependency tree
├── tsconfig.json                     # TypeScript: ES2020, commonjs, strict, experimentalDecorators
│
├── build.sh                          # Bash: npm install && npm run build
├── build.ps1                         # PowerShell: $ErrorActionPreference=Stop; npm install; npm run build
├── build.bat                         # Windows CMD: npm install + npm run build with ERRORLEVEL checks
│
├── quiz.sh                           # Bash: node dist/main.js quiz --questions <N> (default 10)
├── quiz.ps1                          # PowerShell: param([int]$Questions=10); node dist\main.js quiz ...
├── quiz.bat                          # Windows CMD: optional arg for question count
│
├── import.sh                         # Bash: detects file vs dir, calls import --file / --dir
├── import.ps1                        # PowerShell: -Path param, detects file vs dir
├── import.bat                        # Windows CMD: detects file vs dir
│
├── history.sh                        # Bash: node dist/main.js history
├── history.ps1                       # PowerShell: node dist\main.js history
├── history.bat                       # Windows CMD: node dist\main.js history
│
├── docs/
│   ├── README.md                     # Full documentation (see Section 5)
│   └── architecture.md              # Architecture diagrams with Mermaid (see Section 10)
│
├── src/
│   ├── main.ts                       # CLI entry point — dotenv, yargs setup, DB init, command wiring
│   │
│   ├── models/
│   │   ├── Question.ts               # TypeORM @Entity('questions') — all columns + OneToMany to QuizResponse
│   │   ├── QuizSession.ts            # TypeORM @Entity('quiz_sessions') — session stats + OneToMany
│   │   └── QuizResponse.ts          # TypeORM @Entity('quiz_responses') — per-answer record + ManyToOne
│   │
│   ├── database/
│   │   ├── database.ts               # AppDataSource (SQLite), initializeDatabase() runs migrations
│   │   ├── migrations/
│   │   │   └── 1_InitialSchema.ts    # Creates questions, quiz_sessions, quiz_responses tables + index
│   │   └── repositories/
│   │       ├── QuestionRepository.ts # CRUD + getRandomQuestions (cycle-aware) + markUsed + advanceCycle
│   │       ├── SessionRepository.ts  # CRUD + findRecent(limit) + findAll ordered DESC
│   │       └── ResponseRepository.ts # CRUD + findBySessionWithQuestion (JOIN) + countCorrectBySession
│   │
│   ├── service/
│   │   ├── AnswerShuffler.ts         # Fisher-Yates shuffle of options; returns ShuffleResult
│   │   ├── HistoryService.ts         # getRecentSessions, getAllSessions, getSessionReview, exportSessions
│   │   ├── ImportService.ts          # importFile(path) + importDirectory(dirPath) orchestration
│   │   ├── MarkdownParser.ts         # parseMarkdownFile / parseMarkdownContent — two formats (see Section 6)
│   │   ├── QuizEngine.ts             # startSession, submitAnswer, finalizeSession — top-level quiz loop
│   │   ├── QuizService.ts            # importQuestions (dedup), getRandomQuestions, clearAll, etc.
│   │   └── QuizUtils.ts             # Static helpers: calculatePercentage, getGrade, formatDuration, etc.
│   │
│   ├── cli/
│   │   ├── Formatter.ts              # table(), box(), question(), reviewSection(), quizResult()
│   │   ├── Prompts.ts                # inquirer wrappers: selectAnswer, confirm, pressEnterToContinue
│   │   └── commands/
│   │       ├── ClearCommand.ts       # clear command — --questions / --history / --all / --confirm
│   │       ├── HistoryCommand.ts     # history command — --session-id / --review / --export / --limit
│   │       ├── ImportCommand.ts      # import command — --file / --dir (one required)
│   │       └── QuizCommand.ts        # quiz command — --questions (default 10) / --seconds-per (default 0)
│   │
│   └── exceptions/
│       └── QuizExceptions.ts         # QuizException base + 6 typed subclasses
│
└── test/
    ├── setup.ts                      # createTestDataSource() (in-memory SQLite) + sampleQuestion fixtures
    ├── unit/
    │   ├── AnswerShuffler.test.ts
    │   ├── HistoryService.test.ts
    │   ├── MarkdownParser.test.ts
    │   ├── QuestionRepository.test.ts
    │   ├── QuizEngine.test.ts
    │   ├── QuizExceptions.test.ts
    │   ├── QuizService.test.ts
    │   ├── QuizUtils.test.ts
    │   └── ResponseRepository.test.ts
    └── integration/
        └── quiz.workflow.test.ts     # 13-step end-to-end workflow (import → quiz → history → clear)
```

---

## 2. Language, Runtime, and Dependencies

### Runtime

| Tool | Minimum Version |
|------|----------------|
| Node.js | 18+ (Docker image uses `node:20-alpine`) |
| npm | 9+ |

### TypeScript Compiler Options (`tsconfig.json`)

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "commonjs",
    "lib": ["ES2020"],
    "outDir": "./dist",
    "rootDir": "./src",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "forceConsistentCasingInFileNames": true,
    "experimentalDecorators": true,
    "emitDecoratorMetadata": true,
    "resolveJsonModule": true,
    "declaration": true,
    "declarationMap": true,
    "sourceMap": true
  },
  "include": ["src/**/*"],
  "exclude": ["node_modules", "dist", "test"]
}
```

> **Critical:** `experimentalDecorators` and `emitDecoratorMetadata` are both required for TypeORM decorators (`@Entity`, `@Column`, etc.) to work at runtime.

### Production Dependencies (`package.json`)

| Package | Declared Version | Locked Version | Purpose |
|---------|-----------------|----------------|---------|
| `chalk` | `^4.1.2` | `4.1.2` | Coloured terminal output |
| `cli-table3` | `^0.6.3` | `0.6.5` | Tabular CLI output |
| `dotenv` | `^17.3.1` | `17.3.1` | `.env` file loading |
| `inquirer` | `^8.2.6` | `8.2.7` | Interactive prompts (list, confirm, input) |
| `reflect-metadata` | `^0.1.14` | `0.1.14` | Required by TypeORM decorator metadata |
| `sqlite3` | `^5.1.7` | `5.1.7` | SQLite3 native driver for TypeORM |
| `typeorm` | `^0.3.20` | `0.3.28` | ORM — entities, repositories, migrations |
| `uuid` | `^9.0.1` | `9.0.1` | UUIDv4 generation for session IDs |
| `yargs` | `^17.7.2` | `17.7.2` | CLI framework — commands, options, help |

### Development Dependencies (`package.json`)

| Package | Declared Version | Locked Version | Purpose |
|---------|-----------------|----------------|---------|
| `@types/dotenv` | `^6.1.1` | — | Type stubs for dotenv |
| `@types/inquirer` | `^8.2.10` | — | Type stubs for inquirer |
| `@types/jest` | `^29.5.12` | — | Type stubs for jest |
| `@types/node` | `^20.12.7` | — | Node.js type stubs |
| `@types/uuid` | `^9.0.8` | — | Type stubs for uuid |
| `@types/yargs` | `^17.0.32` | — | Type stubs for yargs |
| `jest` | `^29.7.0` | `29.7.0` | Test runner |
| `ts-jest` | `^29.1.4` | `29.4.6` | TypeScript transform for Jest |
| `ts-node` | `^10.9.2` | `10.9.2` | TypeScript execution (dev/typeorm CLI) |
| `typescript` | `^5.4.5` | `5.9.3` | TypeScript compiler |

### NPM Scripts (`package.json`)

```json
{
  "build":         "tsc",
  "start":         "node dist/main.js",
  "dev":           "ts-node --project tsconfig.json src/main.ts",
  "test":          "jest",
  "test:watch":    "jest --watch",
  "test:coverage": "jest --coverage",
  "test:ci":       "jest --coverage --ci --runInBand",
  "typeorm":       "typeorm-ts-node-commonjs"
}
```

---

## 3. Database Schema

### Overview

SQLite database, path defaults to `./quiz_engine.db` (overridden via `DATABASE_PATH` env var). Schema is managed by a single TypeORM migration: `1_InitialSchema.ts`. TypeORM is configured with `synchronize: false`; migrations are the source of truth.

### Table: `questions`

| Column | Type | Constraints | Default | Notes |
|--------|------|-------------|---------|-------|
| `id` | `integer` | PRIMARY KEY, AUTO INCREMENT | — | Internal ID |
| `questionText` | `varchar(2000)` | NOT NULL | — | Full question body |
| `optionA` | `varchar(500)` | NOT NULL | — | Option A text |
| `optionB` | `varchar(500)` | NOT NULL | — | Option B text |
| `optionC` | `varchar(500)` | NOT NULL | — | Option C text |
| `optionD` | `varchar(500)` | NOT NULL | — | Option D text |
| `optionE` | `varchar(500)` | NULLABLE | — | Optional 5th option |
| `correctAnswer` | `varchar(1)` | NOT NULL | — | Single letter A–E |
| `explanation` | `varchar(2000)` | NULLABLE | — | Post-answer explanation |
| `section` | `varchar(100)` | NULLABLE | — | Topic/section name |
| `difficulty` | `varchar(50)` | NULLABLE | — | e.g. Easy / Medium / Hard |
| `sourceFile` | `varchar(255)` | NULLABLE | — | Originating markdown filename |
| `usageCycle` | `integer` | NOT NULL | `1` | Current cycle number |
| `timesUsed` | `integer` | NOT NULL | `0` | Times used this cycle |
| `lastUsedAt` | `datetime` | NULLABLE | — | Timestamp of last use |
| `createdAt` | `datetime` | NOT NULL | `datetime('now')` | Insert timestamp |

### Table: `quiz_sessions`

| Column | Type | Constraints | Default | Notes |
|--------|------|-------------|---------|-------|
| `sessionId` | `varchar(36)` | PRIMARY KEY | — | UUIDv4 string |
| `startedAt` | `datetime` | NOT NULL | `datetime('now')` | Session start time |
| `endedAt` | `datetime` | NULLABLE | — | Session end time |
| `numQuestions` | `integer` | NOT NULL | — | Total questions in session |
| `numCorrect` | `integer` | NOT NULL | `0` | Count of correct answers |
| `percentageCorrect` | `decimal(5,2)` | NOT NULL | `0` | Score as percentage |
| `timeTakenSeconds` | `integer` | NULLABLE | — | Elapsed seconds |

### Table: `quiz_responses`

| Column | Type | Constraints | Default | Notes |
|--------|------|-------------|---------|-------|
| `id` | `integer` | PRIMARY KEY, AUTO INCREMENT | — | Internal ID |
| `sessionId` | `varchar(36)` | NOT NULL, FK → `quiz_sessions.sessionId` ON DELETE CASCADE | — | Links to session |
| `questionId` | `integer` | NOT NULL, FK → `questions.id` ON DELETE CASCADE | — | Links to question |
| `userAnswer` | `varchar(1)` | NOT NULL | — | User's selected letter A–E |
| `isCorrect` | `integer` | NOT NULL | `0` | `1` = correct, `0` = incorrect |
| `timeTakenSeconds` | `integer` | NULLABLE | — | Per-question timing |
| `answeredAt` | `datetime` | NOT NULL | `datetime('now')` | Answer timestamp |

### Index

| Name | Table | Columns | Unique |
|------|-------|---------|--------|
| `UQ_session_question` | `quiz_responses` | `(sessionId, questionId)` | YES |

### Relationships

- `quiz_sessions` **1 → many** `quiz_responses` (via `sessionId`, CASCADE DELETE)
- `questions` **1 → many** `quiz_responses` (via `questionId`, CASCADE DELETE)

### Migration Commands

```bash
# Run pending migrations (auto-run on app start via initializeDatabase())
npm run typeorm migration:run

# Revert last migration
npm run typeorm migration:revert

# Generate a new migration from entity changes
npm run typeorm migration:generate -- -n AddNewColumn
```

---

## 4. CLI Commands

The entry point is `node dist/main.js` (or `npm run dev --` for development). The script name registered with yargs is `quiz-engine`.

### Global

```
quiz-engine <command> [options]
  --help     Show help
  --version  Show version
```

---

### `import` — Load questions from Markdown

```
quiz-engine import [--file <path>] [--dir <path>]
```

**Exactly one of `--file` or `--dir` must be provided** (validated by `.check()`).

| Flag | Alias | Type | Required | Description |
|------|-------|------|----------|-------------|
| `--file` | `-f` | string | one-of | Path to a single `.md` or `.markdown` file |
| `--dir` | `-d` | string | one-of | Path to a directory; all `.md`/`.markdown` files imported |

**Example invocations:**

```bash
# Single file
node dist/main.js import --file questions.md
node dist/main.js import -f ./quiz-source\ material/01-questions.md

# Directory
node dist/main.js import --dir ./quiz-source\ material/
node dist/main.js import -d ./questions/
```

**Expected output (single file):**

```
📥 Importing from: questions.md
  ✅ Imported: 42, Skipped (duplicates): 0
```

**Expected output (directory):**

```
📥 Importing from directory: ./questions/
  ✅ ./questions/file1.md: 20 imported, 0 skipped
  ✅ ./questions/file2.md: 0 imported, 5 skipped

Total: 20 imported, 5 skipped
```

---

### `quiz` — Take a quiz

```
quiz-engine quiz [--questions <n>] [--seconds-per <s>]
```

| Flag | Alias | Type | Default | Description |
|------|-------|------|---------|-------------|
| `--questions` | `-q` | number | `10` | Number of questions per session |
| `--seconds-per` | `-s` | number | `0` | Seconds per question (0 = unlimited) |

**Example invocations:**

```bash
node dist/main.js quiz                    # 10 questions, no timer
node dist/main.js quiz --questions 20    # 20 questions
node dist/main.js quiz -q 5 -s 30        # 5 questions, 30 seconds each
```

**Expected output flow:**

```
🎯 Starting Quiz...
Loading 10 questions...

Question 1 of 10
────────────────────────────────────────────────────────────
Which trigger event is used to run a workflow on a schedule?

  A) on: push
  B) on: workflow_dispatch
  C) on: schedule
  D) on: timer

? Your answer: (Use arrow keys)

[After all questions:]

📝 Answer Review
────────────────────────────────────────────────────────────
✓ Q1: Which trigger event is used to run a workflow...
✗ Q2: ...
   Your answer: A | Correct: C) on: schedule
   💡 on: schedule is the correct trigger.

📊 Quiz Results
────────────────────────────────────────────────
Score:    8 / 10
Percent:  80%
Grade:    B
Status:   ✅ PASSED
Duration: 2m 15s
────────────────────────────────────────────────
Session ID: a1b2c3d4-...
```

---

### `history` — View quiz history

```
quiz-engine history [--session-id <uuid>] [--review] [--export <format>] [--limit <n>]
```

| Flag | Alias | Type | Default | Description |
|------|-------|------|---------|-------------|
| `--session-id` | `-i` | string | — | UUID of a specific session to inspect |
| `--review` | `-r` | boolean | `false` | Show full answer-by-answer review for a session |
| `--export` | `-e` | string | — | Export format: `json` or `csv` (choices enforced) |
| `--limit` | `-l` | number | `10` | Number of recent sessions to show |

**Example invocations:**

```bash
node dist/main.js history                                    # Last 10 sessions (table)
node dist/main.js history --limit 25                         # Last 25 sessions
node dist/main.js history --session-id a1b2c3d4-...          # Session detail (table)
node dist/main.js history --session-id a1b2c3d4-... --review # Full answer review
node dist/main.js history --export json                      # Writes quiz-history.json
node dist/main.js history --export csv                       # Writes quiz-history.csv
```

**Expected output — list view:**

```
📊 Recent Quiz Sessions (last 10):
┌───────────────┬──────────────────────┬──────────────┬─────────────┬──────────┬────────────┬────────┬────────┬──────────┐
│ sessionId     │ startedAt            │ endedAt      │ numQuestions│ numCorrect│ percentage │ grade  │ passed │ duration │
├───────────────┼──────────────────────┼──────────────┼─────────────┼──────────┼────────────┼────────┼────────┼──────────┤
│ a1b2c3d4-...  │ 1/15/2025, 10:00 AM  │ ...          │ 10          │ 8         │ 80         │ B      │ true   │ 2m 15s   │
└───────────────┴──────────────────────┴──────────────┴─────────────┴──────────┴────────────┴────────┴────────┴──────────┘
```

**Expected output — review view:**

```
📋 Review: Session a1b2c3d4-...
[summary table]

Answer Key:
  ✅ Q1: You answered C, Correct: C
  ❌ Q2: You answered A, Correct: B
     💡 Explanation text here
```

---

### `clear` — Remove stored data

```
quiz-engine clear [--questions] [--history] [--all] [--confirm]
```

**Exactly one of `--questions`, `--history`, or `--all` must be provided** (validated by `.check()`).

| Flag | Type | Default | Description |
|------|------|---------|-------------|
| `--questions` | boolean | `false` | Delete all questions from the database |
| `--history` | boolean | `false` | Delete all session history (sessions + responses) |
| `--all` | boolean | `false` | Delete everything (questions + sessions + responses) |
| `--confirm` | boolean | `false` | Skip the interactive confirmation prompt |

**Example invocations:**

```bash
node dist/main.js clear --questions --confirm   # No prompt
node dist/main.js clear --history               # Interactive prompt
node dist/main.js clear --all --confirm         # Wipe everything
```

**Deletion order** (enforced in `QuizService.clearAll()`): responses → sessions → questions.

---

## 5. Documentation — `docs/README.md`

Full heading structure and content of `docs/README.md`:

```
# Quiz Engine — Node.js — Full Documentation
> Part of the Quiz Engine multi-language collection

## Table of Contents
  - Overview
    - Features
  - Project Structure
  - Prerequisites
  - Installation
  - Script Reference
    - Build Scripts
      - build.bat (Windows CMD)
      - build.ps1 (PowerShell)
      - build.sh (Bash / macOS / Linux)
    - Quiz Scripts
      - quiz.bat / quiz.ps1 / quiz.sh
    - Import Scripts
      - import.bat / import.ps1 / import.sh
    - History Scripts
      - history.bat / history.ps1 / history.sh
  - CLI Commands
    - import — Load questions from Markdown
    - quiz — Take a quiz
    - history — View past sessions
    - clear — Remove stored data
    - Global Options (yargs)
  - Docker Setup
    - Building
    - Running Interactively
    - Environment Variables
    - Docker Compose Services
  - Configuration
  - Question File Format
  - TypeORM Migrations
  - Testing
    - Coverage Thresholds (jest.config.js)
  - Dependencies
  - Architecture
```

### Key content from `docs/README.md`

**Features list:**
- Interactive CLI quiz with shuffled answers via `inquirer`
- SQLite persistence with TypeORM entities and repositories
- Non-repetition cycle tracking — `usageCycle` + `timesUsed` columns
- Markdown import — parse and load `.md` question files
- Session history — browse results with JSON/CSV export
- 122 Jest tests with ≥90% line/statement/function coverage, ≥85% branch coverage
- TypeScript strict mode; single `npm run build` compilation step

**Prerequisites table:**

| Tool | Version | Download |
|------|---------|----------|
| Node.js | 18+ | https://nodejs.org/ |
| npm | 9+ | Included with Node.js |
| Docker (optional) | 20.10+ | https://docs.docker.com/get-docker/ |

**Installation code block:**

```bash
npm install
npm run build
node dist/main.js --help
```

**Coverage thresholds table:**

| Metric | Threshold |
|--------|-----------|
| Lines | ≥90% |
| Statements | ≥90% |
| Functions | ≥90% |
| Branches | ≥85% |

**Dependencies table:**

| Package | Purpose |
|---------|---------|
| `typeorm` | ORM with entity + repository pattern |
| `better-sqlite3` | SQLite driver (sync, fast) |
| `yargs` | CLI framework |
| `inquirer` | Interactive prompts |
| `typescript` | Type safety |
| `ts-node` | TypeScript execution (dev) |
| `jest` + `ts-jest` | Testing + TypeScript support |
| `uuid` | Session ID generation |

> Note: The docs mention `better-sqlite3` but the actual installed driver is `sqlite3` (v5.1.7). Both are compatible with TypeORM; use `sqlite3`.

**Architecture reference:** `docs/architecture.md` contains Mermaid diagrams for: sequence (quiz command flow), ER diagram, class diagram, data flow diagram.

---

## 6. Question File Formats

The `MarkdownParser` (`src/service/MarkdownParser.ts`) supports two distinct formats detected at parse time.

### Format 1: Simple Format (inline `**Answer: X**`)

Headers use `## Q<N>` syntax. Question text is a blockquote (`>`). Options are list items `- A) ...`.

**Syntax:**

```markdown
## Q<N>
> Question text here
- A) Option A text
- B) Option B text
- C) Option C text
- D) Option D text
**Answer: A**
> Optional explanation text (blockquote after the answer line)
```

**Sample Question 1 (Simple format):**

```markdown
## Q1
> What does CI stand for?
- A) Continuous Integration
- B) Code Import
- C) Compiler Install
- D) Content Index
**Answer: A**
CI stands for Continuous Integration.
```

**Sample Question 2 (Simple format with 5 options):**

```markdown
## Q2
> Five option question?
- A) Alpha
- B) Beta
- C) Gamma
- D) Delta
- E) Epsilon
**Answer: E**
```

**Multi-question simple file:**

```markdown
## Q1
> What does CI stand for?
- A) Continuous Integration
- B) Code Import
- C) Compiler Install
- D) Content Index
**Answer: A**

## Q2
> What does CD stand for?
- A) Code Deploy
- B) Continuous Delivery
- C) Content Delivery
- D) Code Distribution
**Answer: B**
```

---

### Format 2: Structured / Answer-Key Table Format (GH-200 format)

Headers use `### Question <N> — <Section Name>` syntax. Metadata fields use `**Key**: Value` syntax. The answer is stored in a separate `## Answer Key` table at the bottom of the file. Questions with `**Answer Type**: many` are **skipped** (only `one` type is imported).

**Syntax:**

```markdown
### Question <N> — <Section Name>

**Difficulty**: Easy|Medium|Hard
**Answer Type**: one
**Topic**: <topic name>

**Question**:
Question text here

- A) Option A text
- B) Option B text
- C) Option C text
- D) Option D text

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1  | A         | Explanation text here. | source.md | Easy |
```

**Sample Question 1 (Structured format):**

```markdown
### Question 1 — Workflow Trigger Events

**Difficulty**: Easy
**Answer Type**: one
**Topic**: schedule trigger

**Question**:
Which trigger event is used to run a workflow on a recurring time-based schedule?

- A) `on: timer`
- B) `on: cron`
- C) `on: schedule`
- D) `on: workflow_dispatch`
```

**Sample Question 2 (Structured format with Scenario):**

```markdown
### Question 2 — Contextual Information

**Difficulty**: Medium
**Answer Type**: one
**Topic**: secrets context usage

**Scenario**:
Your team reviews a workflow and finds several usages of the `secrets` context.
You need to identify which usages are valid.

**Question**:
Which location in a workflow file can reference the `secrets` context?

- A) `jobs.<job_id>.steps[*].env`
- B) `jobs.<job_id>.steps[*].with`
- C) `jobs.<job_id>.strategy.matrix`
- D) `jobs.<job_id>.steps[*].run` (via expression `${{ secrets.MY_SECRET }}`)
```

**Answer Key table (placed at end of file):**

```markdown
## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1  | C         | `on: schedule` is the correct trigger. `cron` is the value of the `schedule` key, not the trigger name itself. | 05-Workflow-Trigger-Events.md | Easy |
| 2  | A         | `secrets` is available in `steps[*].env`. | 02-Contextual-Information.md | Medium |
```

**Parser rules:**
- Answer key table takes priority over inline `**Answer: X**` when both exist
- Multi-answer rows (e.g., `A, C`) in the answer key are skipped
- `**Answer Type**: many` questions are skipped entirely
- Only `**Answer Type**: one` (or absent Answer Type) questions are imported
- Section extracted from `### Question N — <Section>` header
- Difficulty extracted from `**Difficulty**: <value>` metadata line
- `**Scenario**:` blocks are prepended to the question text with a blank line separator

---

## 7. Unit Test Coverage

### Tool

**Jest** (`jest@29.7.0`) with **ts-jest** (`ts-jest@29.4.6`) transform.

### Configuration File

**`jest.config.ts`** (TypeScript jest config at project root)

### Threshold Configuration

The thresholds are set under the `coverageThreshold.global` property in `jest.config.ts`:

```typescript
coverageThreshold: {
  global: {
    lines:      90,   // ≥90%
    functions:  90,   // ≥90%
    branches:   85,   // ≥85%
    statements: 90,   // ≥90%
  },
},
```

### Coverage Collection

```typescript
collectCoverageFrom: [
  'src/**/*.ts',
  '!src/main.ts',              // excluded — CLI entry point
  '!src/cli/**/*.ts',          // excluded — CLI layer (Formatter, Prompts, commands)
  '!src/**/*.d.ts',            // excluded — declaration files
  '!src/database/database.ts', // excluded — DataSource init
  '!src/database/migrations/**/*.ts', // excluded — migration files
],
```

Coverage is collected **only** from the service layer, repositories, models, and exceptions.

### Test Match Pattern

```typescript
testMatch: ['**/test/**/*.test.ts']
```

### Test Scripts

```bash
npm test                  # Run all tests
npm run test:coverage     # Run tests + generate coverage report
npm run test:watch        # Watch mode
npm run test:ci           # Coverage + --ci + --runInBand (for CI pipelines)
```

### Coverage Output

```typescript
coverageReporters: ['text', 'text-summary', 'html', 'lcov'],
coverageDirectory: 'coverage',
```

### Test Files

| File | What it tests |
|------|--------------|
| `test/unit/AnswerShuffler.test.ts` | Fisher-Yates shuffle, correct index, displayToOriginal map |
| `test/unit/HistoryService.test.ts` | getRecentSessions, getSessionReview, exportSessions (JSON/CSV) |
| `test/unit/MarkdownParser.test.ts` | Simple format, structured format, scenario, 5-option, answer key, edge cases |
| `test/unit/QuestionRepository.test.ts` | CRUD, getRandomQuestions, markUsed, advanceCycleIfExhausted |
| `test/unit/QuizEngine.test.ts` | startSession, submitAnswer, finalizeSession, error cases |
| `test/unit/QuizExceptions.test.ts` | All 6 exception classes — message and name |
| `test/unit/QuizService.test.ts` | importQuestion (dedup), importQuestions batch, clearAll |
| `test/unit/QuizUtils.test.ts` | calculatePercentage, getGrade, isPassing, formatDuration |
| `test/unit/ResponseRepository.test.ts` | save, findBySession, countCorrectBySession |
| `test/integration/quiz.workflow.test.ts` | 13-step end-to-end: import → quiz → history → clear → export |

### Test Setup Helper (`test/setup.ts`)

```typescript
export async function createTestDataSource(): Promise<DataSource> {
  const ds = new DataSource({
    type: 'sqlite',
    database: ':memory:',  // in-memory SQLite for isolation
    synchronize: true,     // auto-create schema in tests
    logging: false,
    entities: [Question, QuizSession, QuizResponse],
  });
  await ds.initialize();
  return ds;
}
```

---

## 8. Scripts

All scripts are located at the project root (`quiz-engine-nodejs/`). Each script checks for `dist/main.js` before running (except build scripts).

### Build Scripts

#### `build.sh` — Bash / macOS / Linux

**Purpose:** Install dependencies and compile TypeScript to `dist/`

```bash
./build.sh
```

**Content:**
```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Node.js - Build ==="
echo "Installing dependencies..."
npm install
echo "Compiling TypeScript..."
npm run build
echo "Build successful! Output: dist/"
```

#### `build.ps1` — PowerShell (Windows/cross-platform)

**Purpose:** Same as build.sh with `$ErrorActionPreference = "Stop"` for fail-fast

```powershell
.\build.ps1
```

**Content:**
```powershell
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Node.js - Build ===" -ForegroundColor Cyan
Write-Host "Installing dependencies..." -ForegroundColor Yellow
npm install
Write-Host "Compiling TypeScript..." -ForegroundColor Yellow
npm run build
Write-Host "Build successful! Output: dist\" -ForegroundColor Green
```

#### `build.bat` — Windows CMD

**Purpose:** Same as build.sh with `ERRORLEVEL` checks

```bat
build.bat
```

---

### Quiz Scripts

#### `quiz.sh` — Bash

```bash
./quiz.sh          # 10 questions (default)
./quiz.sh 20       # 20 questions
```

Takes an optional positional argument for question count. Exits with error if `dist/main.js` not found.

#### `quiz.ps1` — PowerShell

```powershell
.\quiz.ps1              # 10 questions
.\quiz.ps1 -Questions 20  # 20 questions (note: param name is $Questions / -N not shown)
```

**Param:** `[int]$Questions = 10`

#### `quiz.bat` — Windows CMD

```bat
quiz.bat        # 10 questions (default via node dist\main.js quiz)
quiz.bat 20     # 20 questions
```

---

### Import Scripts

#### `import.sh` — Bash

```bash
./import.sh questions.md       # Single file
./import.sh ./questions/       # Directory
```

Detects file vs directory with `-d "$1"` / `[ -f ... ]`. Exits if no argument provided.

#### `import.ps1` — PowerShell

```powershell
.\import.ps1 -Path questions.md    # Single file
.\import.ps1 -Path .\questions\    # Directory
```

**Param:** `[string]$Path = ""`

#### `import.bat` — Windows CMD

```bat
import.bat questions.md        # Single file
import.bat .\questions\        # Directory
```

Uses `if exist "%~1\"` to detect directory.

---

### History Scripts

#### `history.sh` — Bash

```bash
./history.sh
```

Simply executes: `node dist/main.js history`

#### `history.ps1` — PowerShell

```powershell
.\history.ps1
```

#### `history.bat` — Windows CMD

```bat
history.bat
```

---

## 9. Docker Setup

### `Dockerfile`

```dockerfile
FROM node:20-alpine

WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm ci --only=production

# Copy TypeScript and source
COPY tsconfig.json .
COPY src/ ./src/

# Build TypeScript
RUN npm install -D typescript ts-node @types/node && npm run build

# Create non-root user
RUN addgroup -g 1000 nodegroup && adduser -D -u 1000 -G nodegroup nodeuser
RUN chown -R nodeuser:nodegroup /app
USER nodeuser

# Expose default port (can be overridden)
EXPOSE 3000

# Run
ENTRYPOINT ["node", "dist/main.js"]
CMD ["--help"]
```

**Notes:**
- Base image: `node:20-alpine`
- Two-phase install: production deps first via `npm ci --only=production`, then dev deps for build
- Non-root user: `nodeuser` (uid 1000) in `nodegroup` (gid 1000)
- Default entrypoint passes `--help` if no command given

### `docker-compose.yml`

```yaml
version: '3.8'

services:
  quiz-engine:
    build: .
    container_name: quiz-engine-dev
    volumes:
      - .:/app
      - /app/node_modules
    working_dir: /app
    command: npm run dev -- --help
    environment:
      - NODE_ENV=development
    stdin_open: true
    tty: true

  quiz-engine-test:
    build: .
    container_name: quiz-engine-test
    volumes:
      - .:/app
      - /app/node_modules
    working_dir: /app
    command: npm run test:coverage
    environment:
      - NODE_ENV=test

  quiz-engine-lint:
    build: .
    container_name: quiz-engine-lint
    volumes:
      - .:/app
      - /app/node_modules
    working_dir: /app
    command: npm run build
```

### Service Summary

| Service | Container Name | Command | Use Case |
|---------|---------------|---------|----------|
| `quiz-engine` | `quiz-engine-dev` | `npm run dev -- --help` | Interactive development CLI (stdin_open + tty) |
| `quiz-engine-test` | `quiz-engine-test` | `npm run test:coverage` | Run Jest with coverage |
| `quiz-engine-lint` | `quiz-engine-lint` | `npm run build` | TypeScript compilation check |

### Volume Strategy

Both `quiz-engine` and `quiz-engine-test` mount the project root at `/app` with an anonymous volume at `/app/node_modules` to prevent host modules from overwriting the container's modules.

### Environment Variables

| Variable | Default (`.env`) | Description |
|----------|-----------------|-------------|
| `DATABASE_PATH` | `./quiz_engine.db` | SQLite database file path |
| `NODE_ENV` | `development` | Controls TypeORM logging (logs when `development`) |
| `LOG_LEVEL` | `info` | Application log level |

The `.env` file:

```ini
NODE_ENV=development
DATABASE_PATH=./quiz_engine.db
LOG_LEVEL=info
```

### Docker Usage Examples

```bash
# Build image
docker build -t quiz-engine-node:latest .

# Show help
docker run --rm quiz-engine-node:latest --help

# Interactive quiz with persistent database
docker run -it \
  -v quiz-node-data:/data \
  -e DATABASE_PATH=/data/quiz.db \
  quiz-engine-node:latest quiz --questions 10

# Import a local file
docker run -it \
  -v quiz-node-data:/data \
  -v "$(pwd)/questions.md:/tmp/questions.md" \
  -e DATABASE_PATH=/data/quiz.db \
  quiz-engine-node:latest import --file /tmp/questions.md

# Run via docker-compose
docker-compose up quiz-engine          # Dev CLI
docker-compose up quiz-engine-test     # Run tests with coverage
docker-compose up quiz-engine-lint     # TypeScript build check
```

---

## 10. Architecture Decisions

### Repository Pattern

Each of the three entities (`Question`, `QuizSession`, `QuizResponse`) has a dedicated repository class that wraps TypeORM's `Repository<T>`. Repositories encapsulate all query logic; services never call TypeORM APIs directly.

```
src/database/repositories/
├── QuestionRepository.ts   # cycle-aware random selection, markUsed, advanceCycleIfExhausted
├── SessionRepository.ts    # findRecent(limit), findAll DESC, findById
└── ResponseRepository.ts   # findBySessionWithQuestion (JOIN), countCorrectBySession
```

### Service Layer

Three domain services coordinate repository operations:

- **`QuizEngine`** — stateful quiz session orchestration (startSession → submitAnswer → finalizeSession)
- **`QuizService`** — lower-level CRUD orchestration; used by CLI commands and QuizEngine
- **`ImportService`** — file/directory import pipeline (MarkdownParser → QuizService.importQuestion)
- **`HistoryService`** — read-only session/response aggregation + export

### TypeORM with Raw SQL via QueryBuilder

The project uses TypeORM's `EntityManager`/`Repository` API for simple CRUD but drops to `createQueryBuilder()` for complex operations:

- Random question selection with cycle filtering: `WHERE q.timesUsed < q.usageCycle ORDER BY RANDOM()`
- Batch cycle advancement: `UPDATE questions SET usageCycle = <n+1>`
- JOIN fetch for review: `leftJoinAndSelect('r.question', 'q')`

`synchronize: false` is enforced in production; schema changes are always made via TypeORM migrations.

### Non-Repetition Cycle Tracking

Two integer columns on `questions` implement round-robin non-repetition:

- `usageCycle` — the current "round" number (starts at 1, increments when all questions are used)
- `timesUsed` — how many times used in the current round

A question is available when `timesUsed < usageCycle`. After all questions in a cycle are used, `advanceCycleIfExhausted()` increments `usageCycle` for all rows, making them available again.

### Dependency Injection via Constructor (Manual DI)

No DI container is used. `DataSource` is injected into every repository and service via constructor parameters. The CLI `main.ts` wires everything together using a yargs middleware pattern:

```typescript
const withDataSource = (handler: Function) => async (argv: object) => {
  await handler({ ...argv, dataSource: AppDataSource });
};
```

This passes the initialized `AppDataSource` to every command handler without the command knowing about database initialization.

### Answer Shuffling

`AnswerShuffler.ts` implements Fisher-Yates (Knuth) shuffle on the option array. It returns:
- `shuffledOptions` — display-order option texts
- `correctShuffledIndex` — index (0-based) of the correct answer in the shuffled array
- `displayToOriginal` — map from shuffled display letter (A–E) to original option letter

This allows the quiz to present options in random order while still tracking the correct answer and mapping user selections back to the original answer key.

### Pure Utility Classes

`QuizUtils` and `AnswerShuffler` are pure functions / static methods with no state or dependencies — easily testable in isolation. `MarkdownParser` exports module-level functions (not a class) for the same reason.

### Exception Hierarchy

```
QuizException (base, extends Error)
├── QuestionNotFoundError  — id lookup miss
├── SessionNotFoundError   — sessionId lookup miss
├── InsufficientQuestionsError — not enough questions in DB
├── InvalidAnswerError     — answer not in A–E
├── ParseError             — markdown parse failure
└── DatabaseError          — generic DB error wrapper
```

All exceptions set both `message` and `name` properties.

### CLI Layer Separation

The CLI layer (`src/cli/`) is entirely excluded from test coverage collection. `Formatter.ts` and `Prompts.ts` are pure output/input utilities. Command files contain only argument wiring and output formatting — no business logic. This keeps the CLI layer thin and makes service-layer coverage meaningful.
