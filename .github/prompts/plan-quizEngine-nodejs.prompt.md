# Node.js/TypeORM Quiz Engine for GH-200 Certification

## System Architecture Overview

### Directory Structure
```
quiz_engine/
├── package.json                            # npm dependencies
├── tsconfig.json                           # TypeScript configuration
├── src/
│   ├── main.ts                             # Entry point
│   ├── models/
│   │   ├── Question.ts                     # Question entity (TypeORM)
│   │   ├── QuizSession.ts                  # QuizSession entity (TypeORM)
│   │   └── QuizResponse.ts                 # QuizResponse entity (TypeORM)
│   ├── database/
│   │   ├── database.ts                     # TypeORM AppDataSource
│   │   ├── repositories/
│   │   │   ├── QuestionRepository.ts       # Custom question queries
│   │   │   ├── SessionRepository.ts        # Session CRUD
│   │   │   └── ResponseRepository.ts       # Response tracking
│   │   └── migrations/
│   │       └── 1_InitialSchema.ts          # Schema versioning
│   ├── service/
│   │   ├── QuizEngine.ts                   # Core quiz logic
│   │   ├── QuizService.ts                  # Business logic
│   │   ├── HistoryService.ts               # History queries
│   │   ├── ImportService.ts                # Markdown import
│   │   ├── MarkdownParser.ts               # MD file parsing
│   │   ├── AnswerShuffler.ts               # Answer randomization
│   │   └── QuizUtils.ts                    # Helper utilities
│   ├── cli/
│   │   ├── commands/
│   │   │   ├── QuizCommand.ts
│   │   │   ├── ImportCommand.ts
│   │   │   ├── HistoryCommand.ts
│   │   │   └── ClearCommand.ts
│   │   ├── Formatter.ts                    # Table/box formatting
│   │   └── Prompts.ts                      # Interactive prompts
│   └── exceptions/
│       └── QuizExceptions.ts               # Custom exceptions
├── test/
│   ├── database/
│   │   ├── QuestionRepository.test.ts
│   │   └── database.test.ts
│   ├── service/
│   │   ├── QuizEngine.test.ts
│   │   └── AnswerShuffler.test.ts
│   └── models/
│       └── models.test.ts
├── .env                                    # Environment configuration
└── README.md                               # Documentation
```

### Database Schema (TypeORM)

#### Question Entity
```typescript
@Entity('questions')
export class Question {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ type: 'varchar', length: 500 })
  questionText: string;

  @Column({ type: 'varchar', length: 200 })
  optionA: string;

  @Column({ type: 'varchar', length: 200 })
  optionB: string;

  @Column({ type: 'varchar', length: 200 })
  optionC: string;

  @Column({ type: 'varchar', length: 200 })
  optionD: string;

  @Column({ type: 'varchar', length: 200, nullable: true })
  optionE?: string;

  @Column({ type: 'varchar', length: 1 })
  correctAnswer: string;

  @Column({ type: 'varchar', length: 1000, nullable: true })
  explanation?: string;

  @Column({ type: 'varchar', length: 100, nullable: true })
  section?: string;

  @Column({ type: 'varchar', length: 50, nullable: true })
  difficulty?: string;

  @Column({ type: 'varchar', length: 255, nullable: true })
  sourceFile?: string;

  @Column({ type: 'integer', default: 1 })
  usageCycle: number;

  @Column({ type: 'integer', default: 0 })
  timesUsed: number;

  @Column({ type: 'datetime', nullable: true })
  lastUsedAt?: Date;

  @CreateDateColumn()
  createdAt: Date;

  @OneToMany(() => QuizResponse, response => response.question)
  responses: QuizResponse[];
}
```

#### QuizSession Entity
```typescript
@Entity('quiz_sessions')
export class QuizSession {
  @PrimaryColumn({ type: 'varchar', length: 36 })
  sessionId: string;

  @CreateDateColumn()
  startedAt: Date;

  @Column({ type: 'datetime', nullable: true })
  endedAt?: Date;

  @Column({ type: 'integer' })
  numQuestions: number;

  @Column({ type: 'integer', default: 0 })
  numCorrect: number;

  @Column({ type: 'decimal', precision: 5, scale: 2, default: 0 })
  percentageCorrect: number;

  @Column({ type: 'integer', nullable: true })
  timeTakenSeconds?: number;

  @OneToMany(() => QuizResponse, response => response.session)
  responses: QuizResponse[];
}
```

#### QuizResponse Entity
```typescript
@Entity('quiz_responses')
export class QuizResponse {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ type: 'varchar', length: 36 })
  sessionId: string;

  @Column({ type: 'integer' })
  questionId: number;

  @Column({ type: 'varchar', length: 1 })
  userAnswer: string;

  @Column({ type: 'integer', default: 0 })
  isCorrect: number;

  @Column({ type: 'integer', nullable: true })
  timeTakenSeconds?: number;

  @ManyToOne(() => QuizSession, session => session.responses)
  @JoinColumn({ name: 'sessionId' })
  session: QuizSession;

  @ManyToOne(() => Question, question => question.responses)
  @JoinColumn({ name: 'questionId' })
  question: Question;

  @Unique(['sessionId', 'questionId'])
  unique_session_question: void;
}
```

---

## Implementation Plan

### Phase 1: Project Setup & TypeORM Configuration
**Timeline:** 1.5-2 hours

**Objective:** Initialize Node.js project, setup TypeORM, define entities.

**Tasks:**

1. **Create Node.js Project:**
   ```bash
   npm init -y
   npm install -D typescript ts-node @types/node
   npm install typeorm sqlite3 uuid
   ```

2. **Update `package.json` Dependencies:**
   ```json
   {
     "name": "quiz-engine",
     "version": "1.0.0",
     "type": "module",
     "scripts": {
       "build": "tsc",
       "start": "node dist/main.js",
       "dev": "ts-node src/main.ts",
       "test": "jest",
       "typeorm": "typeorm"
     },
     "dependencies": {
       "typeorm": "^0.3.16",
       "sqlite3": "^5.1.6",
       "uuid": "^9.0.0",
       "yargs": "^17.7.2",
       "chalk": "^5.3.0",
       "cli-table3": "^0.6.3"
     },
     "devDependencies": {
       "typescript": "^5.2.2",
       "ts-node": "^10.9.1",
       "@types/node": "^20.3.1",
       "jest": "^29.5.0",
       "@types/jest": "^29.5.2"
     }
   }
   ```

3. **Create TypeORM Configuration:**
   - `src/database/database.ts` - Initialize AppDataSource
   - Configure SQLite connection
   - Setup entity paths

4. **Define Entity Classes:**
   - `src/models/Question.ts` → Question entity
   - `src/models/QuizSession.ts` → QuizSession entity
   - `src/models/QuizResponse.ts` → QuizResponse entity

5. **Generate Database Schema:**
   ```bash
   npm run typeorm -- migration:generate ./src/database/migrations/InitialSchema -d ./src/database/database.ts
   npm run typeorm -- migration:run -d ./src/database/database.ts
   ```

6. **Create Repository Classes:**
   - `QuestionRepository` with cycle-aware query methods
   - `SessionRepository` for session CRUD
   - `ResponseRepository` for response tracking

7. **Test database initialization:**
   ```bash
   npm run dev -- --init
   ```
   - SQLite database created successfully

**Success Criteria:**
- Node.js project structure created
- TypeORM entities compile without errors
- SQLite database initializes with schema
- All entities properly mapped
- Custom repositories functional

---

### Phase 2: Service Layer & Quiz Logic
**Timeline:** 2-2.5 hours

**Objective:** Implement core quiz engine, services, utility classes.

**Tasks:**

1. **Create `QuizEngine` Class:**
   ```typescript
   export class QuizEngine {
     async loadQuestions(count: number): Promise<Question[]> { }
     async submitAnswers(sessionId: string, answers: string[]): Promise<void> { }
     async finalizeSession(sessionId: string): Promise<QuizSession> { }
   }
   ```

2. **Create `QuizService` Class:**
   - Wrapper around TypeORM repositories
   - Orchestrate business logic
   - `getRandomQuestions()`, `markQuestionUsed()`, `advanceCycleIfNeeded()`

3. **Create `AnswerShuffler` Class:**
   - `shuffleAnswers(question: Question)` → randomized list + position map
   - Preserve shuffled answer for verification

4. **Create `MarkdownParser` Class:**
   - `parseFile(filePath: string)` → extract questions from markdown
   - Regex parsing for question format

5. **Create Utility Classes:**
   - `HistoryService` for queries
   - `ImportService` for batch import
   - `QuizUtils` for scoring, formatting

6. **Test service layer:**
   ```bash
   npm test -- src/service/QuizEngine.test.ts
   ```
   - Verify cycle-aware question selection
   - Test score calculation
   - Verify answer shuffling

**Success Criteria:**
- QuizEngine orchestrates correct flow
- Cycle-aware question selection works
- Session stats calculated correctly
- All services tested and passing
- Batch import functional

---

### Phase 3: CLI Implementation with Yargs & Chalk
**Timeline:** 1.5-2 hours

**Objective:** Build interactive CLI using `yargs` package and `chalk` for colors.

**Tasks:**

1. **Create Command Structure:**
   - `src/cli/commands/` directory
   - Each command is a separate file
   - Export command configuration for yargs

2. **Implement Quiz Command:**
   ```typescript
   export const quizCommand = {
     command: 'quiz',
     describe: 'Take a quiz',
     builder: (yargs) => yargs.option('questions', { alias: 'q', type: 'number', default: 100 }),
     handler: async (argv) => { }
   };
   ```

3. **Implement Import Command:**
   - `importCommand()` → parse markdown files
   - Batch insert via ImportService

4. **Implement History Command:**
   - `historyCommand()` → view sessions
   - `--review` flag for full answer key
   - `--export` option for CSV/JSON

5. **Implement Clear Command:**
   - `clearCommand()` → delete data
   - Confirmation prompts
   - `--confirm` flag

6. **Create CLI Entry Point:**
   ```typescript
   async function main() {
     const yargs = require('yargs/yargs');
     yargs(process.argv.slice(2))
       .command(quizCommand)
       .command(importCommand)
       .demandCommand()
       .strict()
       .argv;
   }
   ```

7. **Create Table Formatter:**
   ```typescript
   export class Formatter {
     static table(data: any[]): string { }
     static box(text: string): string { }
   }
   ```

8. **Test CLI commands:**
   ```bash
   npm run dev quiz --questions 100
   npm run dev import --file questions.md
   ```

**Success Criteria:**
- All CLI commands execute correctly
- Interactive prompts work smoothly
- Pretty-printed output formatted with chalk
- Error handling graceful
- No unhandled promise rejections

---

### Phase 4: Testing & Packaging
**Timeline:** 1-1.5 hours

**Objective:** Comprehensive testing, compile to executable.

**Tasks:**

1. **Write Unit Tests:**
   ```bash
   npm test
   ```
   - Test repositories: CRUD operations
   - Test services: Business logic
   - Test utilities: Shuffling, parsing

2. **Write Integration Tests:**
   - Full quiz flow: load → submit → finalize
   - Cycle mechanics verification
   - Non-repetition across quizzes

3. **Build Release Binary:**
   ```bash
   npm run build
   pkg dist/main.js --output bin/quiz_engine
   ```
   - Single executable file using `pkg`
   - No Node.js runtime dependency

4. **Write Comprehensive README:**
   - **Getting Started:** Node.js 18+ requirement
   - **Installation:** `npm install && npm run build`
   - **Running Quizzes:** `npm run dev quiz` or `./bin/quiz_engine quiz`
   - **CLI Commands:** quiz, import, history, clear
   - **Configuration:** `.env` file settings
   - **Architecture:** TypeORM, yargs CLI structure
   - **Testing:** How to run tests with Jest

5. **Final Testing:**
   - Full end-to-end workflow
   - Create → Import → Take Quiz → View History → Retake
   - Verify cycle mechanics
   - Test packaged executable

**Success Criteria:**
- All tests passing with Jest
- Executable compiles successfully with `pkg`
- Single executable works (no Node.js needed)
- Full documentation provided
- Works on Windows/Mac/Linux

---

## Dependencies Summary
- **typeorm** (0.3.16) - ORM with decorators
- **sqlite3** (5.1.6) - SQLite driver
- **yargs** (17.7.2) - CLI argument parsing
- **chalk** (5.3.0) - Terminal colors
- **cli-table3** (0.6.3) - Table formatting
- **uuid** (9.0.0) - Session ID generation
- **jest** (29.5.0) - Testing framework
- **typescript** (5.2.2) - Type safety

---

## Core Design Decisions

### 1. TypeORM for ORM
- **Decorator-Based:** Clean entity definitions with TypeScript decorators
- **Type-Safe:** Full TypeScript support with IntelliSense
- **Relations:** Automatic foreign key management
- **Repositories:** Custom repo methods for complex queries

### 2. Yargs for CLI
- **Subcommands:** Structured command hierarchy
- **Type Validation:** Built-in type coercion and validation
- **Help Generation:** Automatic --help and --version
- **Environment Integration:** Reads process.env and argv

### 3. Chalk for Output
- **Terminal Colors:** Simple ANSI color management
- **Cross-Platform:** Works on Windows, Mac, Linux
- **No Dependencies:** Pure JavaScript implementation

### 4. Async/Await Throughout
- **Non-Blocking:** All I/O is async by default
- **Promise-Based:** Cleaner error handling than callbacks
- **Parallel Operations:** Multiple queries execute concurrently

### 5. npm pkg for Distribution
- **Single Binary:** Packages Node.js + app into one exe
- **Cross-Platform:** Can build for Windows/Mac/Linux
- **No Runtime Needed:** End users don't need Node.js installed

---

## CLI Examples

```bash
# Build
npm run build && npm install -g pkg
pkg dist/main.js --output bin/quiz_engine

# Take a quiz
npm run dev quiz --questions 100 --seconds-per 60
./bin/quiz_engine quiz --questions 100

# Import questions
npm run dev import --file questions.md
npm run dev import --dir ./md/

# View history
npm run dev history
npm run dev history --session-id <uuid> --review
npm run dev history --export json

# Clear data
npm run dev clear --questions --confirm
npm run dev clear --history --all --confirm
```

---

## Success Criteria

### Functional Requirements
- ✓ Load 100+ random questions without showing answers
- ✓ NEVER repeat question until cycle exhausted
- ✓ Answers randomized and verified correctly
- ✓ Session persisted with full stats
- ✓ Import/history/clear operations work
- ✓ All CLI commands functional

### Non-Functional Requirements
- ✓ Performance: Load questions + display <500ms
- ✓ Usability: Full workflow <15 minutes
- ✓ Reliability: Graceful error handling, transactional integrity
- ✓ Maintainability: Clean architecture, testable
- ✓ Compatibility: Node.js 18+, Windows/Mac/Linux (native executables)
- ✓ Distribution: Single executable, no dependencies

---

## Implementation Notes

- **TypeORM Decorators:** Entities use @Entity, @Column, @PrimaryGeneratedColumn
- **Repository Pattern:** Custom repositories extend Repository<Entity>
- **Migrations:** Use TypeORM CLI to generate and run migrations
- **Testing:** Jest with ts-jest for TypeScript support
- **Error Handling:** Try-catch for async/await, centralized error logger
- **CLI Parsing:** Yargs handles subcommands and validation
- **Data Validation:** Leverage TypeScript types at compile-time
- **Future:** Express.js REST API, GraphQL server, Docker containerization
