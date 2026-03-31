# Quiz Engine — Node.js — Full Documentation

> Part of the [Quiz Engine multi-language collection](../../README.md)

- [Quiz Engine — Node.js — Full Documentation](#quiz-engine--nodejs--full-documentation)
  - [Overview](#overview)
    - [Features](#features)
  - [Project Structure](#project-structure)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Script Reference](#script-reference)
    - [Build Scripts](#build-scripts)
      - [`build.bat` (Windows CMD)](#buildbat-windows-cmd)
      - [`build.ps1` (PowerShell)](#buildps1-powershell)
      - [`build.sh` (Bash / macOS / Linux)](#buildsh-bash--macos--linux)
    - [Quiz Scripts](#quiz-scripts)
      - [`quiz.bat` / `quiz.ps1` / `quiz.sh`](#quizbat--quizps1--quizsh)
    - [Import Scripts](#import-scripts)
      - [`import.bat` / `import.ps1` / `import.sh`](#importbat--importps1--importsh)
    - [History Scripts](#history-scripts)
      - [`history.bat` / `history.ps1` / `history.sh`](#historybat--historyps1--historysh)
  - [CLI Commands](#cli-commands)
    - [`import` — Load questions from Markdown](#import--load-questions-from-markdown)
    - [`quiz` — Take a quiz](#quiz--take-a-quiz)
    - [`history` — View past sessions](#history--view-past-sessions)
    - [`clear` — Remove stored data](#clear--remove-stored-data)
    - [Global Options (yargs)](#global-options-yargs)
  - [Docker Setup](#docker-setup)
    - [Building](#building)
    - [Running Interactively](#running-interactively)
    - [Environment Variables](#environment-variables)
    - [Docker Compose Services](#docker-compose-services)
  - [Configuration](#configuration)
  - [Question File Format](#question-file-format)
  - [TypeORM Migrations](#typeorm-migrations)
  - [Testing](#testing)
    - [Coverage Thresholds (`jest.config.js`)](#coverage-thresholds-jestconfigjs)
  - [Dependencies](#dependencies)
  - [Architecture](#architecture)


---

A command-line quiz engine for GH-200 GitHub Actions certification preparation built with TypeScript, TypeORM, SQLite, yargs CLI, and inquirer prompts. 122 tests with ≥90% coverage enforced.

---

## Overview

### Features

- **Interactive CLI quiz** with shuffled answers via `inquirer`
- **SQLite persistence** with TypeORM entities and repositories
- **Non-repetition cycle tracking** — `usageCycle` + `timesUsed` columns
- **Markdown import** — parse and load `.md` question files
- **Session history** — browse results with JSON/CSV export
- **122 Jest tests** with ≥90% line/statement/function coverage, ≥85% branch coverage
- TypeScript strict mode; single `npm run build` compilation step

---

## Project Structure

```
quiz-engine-nodejs/
├── src/
│   ├── main.ts                              # CLI entry point (yargs setup)
│   ├── models/
│   │   ├── Question.ts                      # TypeORM entity
│   │   ├── QuizSession.ts
│   │   └── QuizResponse.ts
│   ├── database/
│   │   ├── repositories/
│   │   │   ├── QuestionRepository.ts
│   │   │   ├── SessionRepository.ts
│   │   │   └── ResponseRepository.ts
│   │   └── migrations/
│   │       └── 1_InitialSchema.ts           # TypeORM migration
│   ├── service/
│   │   ├── QuizService.ts
│   │   ├── ImportService.ts
│   │   └── HistoryService.ts
│   ├── cli/
│   │   └── commands/
│   │       ├── quiz.ts
│   │       ├── importCmd.ts
│   │       ├── history.ts
│   │       └── clear.ts
│   └── exceptions/
│       └── QuizExceptions.ts
├── test/
│   ├── unit/
│   │   ├── service/
│   │   ├── repositories/
│   │   └── models/
│   └── integration/
├── dist/                                    # TypeScript compiled output
├── Dockerfile
├── docker-compose.yml
├── package.json
├── tsconfig.json
├── jest.config.js
├── .env.example                             # Environment variables template
├── build.bat / build.ps1 / build.sh
├── quiz.bat / quiz.ps1 / quiz.sh
├── import.bat / import.ps1 / import.sh
├── history.bat / history.ps1 / history.sh
└── README.md
```

---

## Prerequisites

| Tool | Version | Download |
|------|---------|----------|
| Node.js | 18+ | https://nodejs.org/ |
| npm | 9+ | Included with Node.js |
| Docker (optional) | 20.10+ | https://docs.docker.com/get-docker/ |

Verify:
```bash
node --version    # v18+
npm --version     # 9+
```

---

## Installation

```bash
# Install packages
npm install

# Compile TypeScript
npm run build

# Verify
node dist/main.js --help
```

---

## Script Reference

### Build Scripts

#### `build.bat` (Windows CMD)

```bat
build.bat
```

- Runs `npm install` and `npm run build` (`tsc`)
- Output: `dist/` directory

#### `build.ps1` (PowerShell)

```powershell
.\build.ps1
```

- `$ErrorActionPreference = "Stop"` for fail-fast behavior
- Runs `npm ci` + `npm run build`
- Prints progress in cyan/green

#### `build.sh` (Bash / macOS / Linux)

```bash
./build.sh
```

- Runs `npm ci && npm run build`
- Uses `set -e` for fail-fast behavior

---

### Quiz Scripts

#### `quiz.bat` / `quiz.ps1` / `quiz.sh`

```bash
quiz.bat 10             # Windows CMD — 10 questions
.\quiz.ps1 -N 10        # PowerShell  — 10 questions
./quiz.sh 10            # Bash        — 10 questions
```

These scripts:
1. Verify `dist/main.js` exists (run build if missing)
2. Run `node dist/main.js quiz --questions <N>`
3. Use `--questions 10` as default

---

### Import Scripts

#### `import.bat` / `import.ps1` / `import.sh`

```bash
import.bat questions.md          # Windows CMD single file
import.bat .\questions\          # Windows CMD directory
.\import.ps1 -Path questions.md  # PowerShell single file
./import.sh questions.md         # Bash single file
./import.sh ./questions/         # Bash directory
```

- Detects file vs directory
- Calls `node dist/main.js import --file <path>` or `--dir <path>`

---

### History Scripts

#### `history.bat` / `history.ps1` / `history.sh`

```bash
history.bat
.\history.ps1
./history.sh
```

All execute `node dist/main.js history`.

---

## CLI Commands

### `import` — Load questions from Markdown

```bash
node dist/main.js import --file questions.md
node dist/main.js import --dir ./questions/
```

| Option | Description |
|--------|-------------|
| `--file` | Path to a single `.md` file |
| `--dir` | Path to a directory |

---

### `quiz` — Take a quiz

```bash
node dist/main.js quiz                      # 10 questions
node dist/main.js quiz --questions 20
node dist/main.js quiz --no-shuffle
```

| Option | Default | Description |
|--------|---------|-------------|
| `--questions` | 10 | Number of questions per session |
| `--no-shuffle` | false | Disable answer option shuffling |

---

### `history` — View past sessions

```bash
node dist/main.js history
node dist/main.js history --session-id <uuid>
node dist/main.js history --session-id <uuid> --review
node dist/main.js history --export json
node dist/main.js history --export csv
```

| Option | Description |
|--------|-------------|
| `--session-id` | Filter to a specific session UUID |
| `--review` | Show full answer review for the session |
| `--export json\|csv` | Export all history |

---

### `clear` — Remove stored data

```bash
node dist/main.js clear --questions --confirm
node dist/main.js clear --history --confirm
node dist/main.js clear --all --confirm
```

### Global Options (yargs)

| Option | Default | Description |
|--------|---------|-------------|
| `--db` | `./quiz_engine.db` | Path to SQLite database |
| `--help` | — | Show help |
| `--version` | — | Show version |

---

## Docker Setup

### Building

```bash
docker build -t quiz-engine-node:latest .
docker run --rm quiz-engine-node:latest --help
```

### Running Interactively

```bash
# Interactive quiz
docker run -it \
  -v quiz-node-data:/data \
  -e QUIZ_DB_PATH=/data/quiz.db \
  quiz-engine-node:latest quiz --questions 10

# Import local file
docker run -it \
  -v quiz-node-data:/data \
  -v "$(pwd)/questions.md:/tmp/questions.md" \
  quiz-engine-node:latest import --file /tmp/questions.md
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `QUIZ_DB_PATH` | `./quiz_engine.db` | SQLite database path |
| `NODE_ENV` | `production` | Environment mode |

### Docker Compose Services

```bash
docker-compose up quiz-engine      # Run CLI
docker-compose up quiz-engine-test # Run Jest with coverage
docker-compose up quiz-engine-build # Compile TypeScript
```

| Service | Description |
|---------|-------------|
| `quiz-engine` | Interactive CLI |
| `quiz-engine-test` | Jest with coverage checking |
| `quiz-engine-build` | `npm ci && npm run build` |

---

## Configuration

Create a `.env` from `.env.example`:

```bash
cp .env.example .env
```

```ini
# .env
QUIZ_DB_PATH=./quiz_engine.db
NODE_ENV=development
```

TypeORM picks up `QUIZ_DB_PATH` for the database file location.

---

## Question File Format

```markdown
# Quiz Title

**Iteration**: 1
**Total Questions**: 2

---

## Questions

---

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

---

### Question 2 — Contextual Information

**Difficulty**: Medium
**Answer Type**: many
**Topic**: secrets context usage

**Scenario**:
Your team reviews a workflow and finds several usages of the `secrets` context.
You need to identify which usages are valid.

**(Select all that apply)**
Which locations in a workflow file can reference the `secrets` context?

- A) `jobs.<job_id>.steps[*].env`
- B) `jobs.<job_id>.steps[*].with`
- C) `jobs.<job_id>.strategy.matrix`
- D) `jobs.<job_id>.steps[*].run` (via expression `${{ secrets.MY_SECRET }}`)

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | C | `on: schedule` is the correct trigger. `cron` is the value of the `schedule` key, not the trigger name itself. | 05-Workflow-Trigger-Events.md | Easy |
| 2 | A, B, D | `secrets` is available in `steps[*].env`, `steps[*].with`, and `steps[*].run`. It is NOT available in `strategy.matrix`. | 02-Contextual-Information.md | Medium |
```

---

## TypeORM Migrations

The migration `1_InitialSchema.ts` creates the `questions`, `quiz_sessions`, and `quiz_responses` tables with all indexes.

```bash
# Run pending migrations
npm run typeorm migration:run

# Revert last migration
npm run typeorm migration:revert

# Generate a new migration from entity changes
npm run typeorm migration:generate -- -n AddNewColumn
```

TypeORM is configured with `synchronize: false` in production to prevent accidental schema changes. Migrations are the source of truth.

---

## Testing

```bash
# Run all 122 tests
npm test

# With coverage report
npm run test:coverage

# Watch mode (development)
npm run test:watch

# Single file
npx jest src/service/QuizService.test.ts
```

### Coverage Thresholds (`jest.config.js`)

| Metric | Threshold |
|--------|-----------|
| Lines | ≥90% |
| Statements | ≥90% |
| Functions | ≥90% |
| Branches | ≥85% |

Coverage failure causes `npm test` to exit non-zero (CI-aware).

---

## Dependencies

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

---

## Architecture

See [architecture.md](architecture.md) for full sequence, class, ER, and data flow diagrams.
