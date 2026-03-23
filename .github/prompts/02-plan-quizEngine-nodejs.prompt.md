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
│   ├── setup.ts                            # Global fixtures (in-memory SQLite, sample data)
│   ├── unit/
│   │   ├── QuestionRepository.test.ts      # CRUD, cycle mechanics, duplicate handling
│   │   ├── QuizEngine.test.ts              # Session lifecycle, scoring, finalization
│   │   ├── AnswerShuffler.test.ts          # Shuffle correctness, correct-answer mapping
│   │   ├── MarkdownParser.test.ts          # File parsing, validation, error cases
│   │   └── HistoryService.test.ts          # History queries, export formatting
│   └── integration/
│       └── quiz.workflow.test.ts           # Full load → answer → finalize flow
├── jest.config.ts                          # Jest config with >90% coverage thresholds
├── coverage/                               # Generated HTML/LCOV coverage reports
├── Dockerfile               # Container image for production deployment
├── docker-compose.yml       # Multi-container orchestration for dev/test
├── .env                                    # Environment configuration
└── README.md                               # Documentation
```

### Docker & Containerization

#### Dockerfile (Production)
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
RUN npm run build

# Create non-root user
RUN addgroup -g 1000 node && adduser -D -u 1000 -G node nodeuser
RUN chown -R nodeuser:node /app
USER nodeuser

# Expose default port (can be overridden)
EXPOSE 3000

# Run
ENTRYPOINT ["node", "dist/main.js"]
CMD ["--help"]
```

#### docker-compose.yml (Development)
```yaml
version: '3.8'

services:
  quiz-engine:
    build: .
    container_name: quiz-engine-dev
    volumes:
      - .:/app
      - /app/node_modules  # Exclude node_modules from mount
    working_dir: /app
    command: npm run dev
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
    command: npm run lint
```

#### Getting Started with Docker

**Quick Start (5 steps):**

1. **Build the image:**
   ```bash
   docker build -t quiz-engine:latest .
   ```

2. **Run development mode:**
   ```bash
   docker-compose up quiz-engine
   ```

3. **Run tests with coverage:**
   ```bash
   docker-compose up quiz-engine-test
   ```

4. **Run linting:**
   ```bash
   docker-compose up quiz-engine-lint
   ```

5. **Execute quiz interactively:**
   ```bash
   docker run -it quiz-engine:latest npm run dev -- quiz --questions 10
   ```

**Build & Push:**
```bash
# Build multi-arch
docker buildx build --platform linux/amd64,linux/arm64 -t myregistry/quiz-engine:1.0 .

# Push to registry
docker push myregistry/quiz-engine:1.0
```

**Container Configuration:**
- Node 20-alpine base image (minimal footprint)
- Non-root user (nodeuser) for security
- Volume mounts for live development
- node_modules excluded from bind mounts for performance
- Development, test, and lint services defined

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

### Phase 4: Unit Testing & Coverage Enforcement
**Timeline:** 2-3 hours

**Objective:** Achieve >90% unit test coverage across all non-CLI source modules using Jest.

**Coverage Configuration (`jest.config.ts`):**
```typescript
import type { Config } from 'jest';

const config: Config = {
  preset: 'ts-jest',
  testEnvironment: 'node',
  testMatch: ['**/test/**/*.test.ts'],
  collectCoverageFrom: [
    'src/**/*.ts',
    '!src/main.ts',           // Exclude CLI entry point
    '!src/cli/**/*.ts',       // Exclude CLI command wiring
    '!src/**/*.d.ts',
  ],
  coverageThreshold: {
    global: {
      lines: 90,
      functions: 90,
      branches: 85,
      statements: 90,
    },
  },
  coverageReporters: ['text', 'text-summary', 'html', 'lcov'],
  coverageDirectory: 'coverage',
};

export default config;
```

**Add to `package.json` scripts:**
```json
"scripts": {
  "test": "jest",
  "test:watch": "jest --watch",
  "test:coverage": "jest --coverage",
  "test:ci": "jest --coverage --ci --runInBand"
}
```

**Tasks:**

1. **Create `test/setup.ts` — shared fixtures:**
   ```typescript
   import Database from 'better-sqlite3';
   import { QuestionRepository } from '../src/database/repositories/QuestionRepository';

   export function createTestRepo() {
     const db = new Database(':memory:');
     const repo = new QuestionRepository(db);
     repo.initSchema();
     return repo;
   }

   export const sampleQuestion = {
     questionText: 'What is CI/CD?',
     optionA: 'Continuous Integration',
     optionB: 'Code Import',
     optionC: 'Compiler Install',
     optionD: 'Content Index',
     correctAnswer: 'A',
   };
   ```

2. **Write `test/unit/QuestionRepository.test.ts` (target: >92%):**
   ```typescript
   import { createTestRepo, sampleQuestion } from '../setup';

   describe('QuestionRepository', () => {
     it('creates schema tables on init', () => {
       const repo = createTestRepo();
       expect(repo.getTableNames()).toContain('questions');
       expect(repo.getTableNames()).toContain('quiz_sessions');
     });

     it('inserts a question and retrieves it by ID', () => {
       const repo = createTestRepo();
       repo.insert(sampleQuestion);
       const all = repo.getAll();
       expect(all).toHaveLength(1);
       expect(all[0].questionText).toBe('What is CI/CD?');
     });

     it('omits correctAnswer in getRandomQuestions result', () => {
       const repo = createTestRepo();
       repo.insert(sampleQuestion);
       const questions = repo.getRandomQuestions(1);
       expect((questions[0] as any).correctAnswer).toBeUndefined();
     });

     it('advances cycle when all questions marked used', () => {
       const repo = createTestRepo();
       repo.insert(sampleQuestion);
       repo.markUsed(1);
       repo.advanceCycleIfExhausted();
       expect(repo.getCurrentCycle()).toBe(2);
     });

     it('skips duplicate on insert', () => {
       const repo = createTestRepo();
       repo.insert(sampleQuestion);
       repo.insert(sampleQuestion); // duplicate
       expect(repo.count()).toBe(1);
     });
   });
   ```

3. **Write `test/unit/AnswerShuffler.test.ts` (target: >95%):**
   ```typescript
   import { shuffleAnswers } from '../../src/service/AnswerShuffler';

   describe('AnswerShuffler', () => {
     const options = ['Alpha', 'Beta', 'Gamma', 'Delta'];

     it('returns all original options after shuffling', () => {
       const result = shuffleAnswers(options, 'A');
       expect(new Set(result.shuffledOptions)).toEqual(new Set(options));
     });

     it('maps correct answer to its new shuffled index', () => {
       const result = shuffleAnswers(options, 'A'); // A = 'Alpha'
       expect(result.shuffledOptions[result.correctShuffledIndex]).toBe('Alpha');
     });

     it('always returns 4 options', () => {
       const result = shuffleAnswers(options, 'B');
       expect(result.shuffledOptions).toHaveLength(4);
     });
   });
   ```

4. **Write `test/unit/MarkdownParser.test.ts` (target: >92%):**
   ```typescript
   import * as fs from 'fs';
   import * as path from 'path';
   import * as os from 'os';
   import { parseMarkdownFile } from '../../src/service/MarkdownParser';

   describe('MarkdownParser', () => {
     it('parses a valid markdown file into question objects', () => {
       const file = path.join(os.tmpdir(), `test-${Date.now()}.md`);
       fs.writeFileSync(file, [
         '## Q1',
         '> What does CI stand for?',
         '- A) Continuous Integration',
         '- B) Code Import',
         '- C) Compiler Install',
         '- D) Content Index',
         '**Answer: A**',
       ].join('\n'));
       const questions = parseMarkdownFile(file);
       expect(questions).toHaveLength(1);
       expect(questions[0].correctAnswer).toBe('A');
       fs.unlinkSync(file);
     });

     it('throws on missing answer line', () => {
       const file = path.join(os.tmpdir(), `test-${Date.now()}.md`);
       fs.writeFileSync(file, '## Q1\n> No answer here.');
       expect(() => parseMarkdownFile(file)).toThrow();
       fs.unlinkSync(file);
     });
   });
   ```

5. **Write `test/unit/QuizEngine.test.ts` (target: >92%):**
   ```typescript
   describe('QuizEngine', () => {
     it('scores correct answer', () => {
       const repo = createTestRepo();
       repo.insert(sampleQuestion);
       const engine = new QuizEngine(repo, { numQuestions: 1 });
       engine.loadQuestions();
       engine.submitAnswer(0, engine.questions[0].correctShuffledIndex, 10);
       expect(engine.numCorrect).toBe(1);
     });

     it('does not score wrong answer', () => {
       const repo = createTestRepo();
       repo.insert(sampleQuestion);
       const engine = new QuizEngine(repo, { numQuestions: 1 });
       engine.loadQuestions();
       engine.submitAnswer(0, 99, 10);
       expect(engine.numCorrect).toBe(0);
     });

     it('finalizes and persists session to DB', () => {
       const repo = createTestRepo();
       repo.insert(sampleQuestion);
       const engine = new QuizEngine(repo, { numQuestions: 1 });
       engine.loadQuestions();
       engine.submitAnswer(0, 0, 10);
       const session = engine.finalize();
       expect(repo.getSession(session.sessionId)).not.toBeNull();
     });
   });
   ```

6. **Run coverage and enforce threshold:**
   ```bash
   npm run test:coverage

   # Build fails automatically if coverage < 90%:
   # FAIL - "global" coverage threshold for lines (90%) not met: 87%

   # On success:
   # Lines   : 93.2% ( 280/300 )  ✓
   # Functions: 91.0% ( 82/90 )   ✓
   # Statements: 93.0% ( 280/301 ) ✓
   # HTML report: ./coverage/index.html
   ```

7. **Build Release Binary:**
   ```bash
   npm run build
   pkg dist/main.js --output bin/quiz_engine
   ```
   - Single executable file using `pkg`
   - No Node.js runtime dependency

8. **Write Comprehensive README:**
   - **Getting Started:** Node.js 18+ requirement
   - **Installation:** `npm install && npm run build`
   - **Running Quizzes:** `npm run dev quiz` or `./bin/quiz_engine quiz`
   - **CLI Commands:** quiz, import, history, clear
   - **Testing:** `npm run test:coverage` — must show ≥90% coverage
   - **Architecture:** TypeORM, yargs CLI structure

9. **Final Testing:**
   - Full end-to-end workflow: Import → Quiz → History → Retake
   - Verify cycle mechanics (no repeats until all seen)
   - Test packaged executable on target platforms

**Success Criteria:**
- `npm run test:coverage` passes with ≥90% lines, functions, statements
- Jest build **fails automatically** if coverage drops below threshold
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
- ✓ Maintainability: Clean architecture, fully testable
- ✓ **Test Coverage: `npm run test:coverage` enforces ≥90% lines/functions/statements**
- ✓ **Coverage Gate: Jest build fails automatically below 90% threshold**
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
