# Dart/Drift Quiz Engine for GH-200 Certification

## System Architecture Overview

### Directory Structure
```
quiz_engine/
├── pubspec.yaml                            # Dart dependencies
├── lib/
│   ├── main.dart                           # Entry point
│   ├── src/
│   │   ├── models/
│   │   │   ├── question.dart               # Question class (Drift)
│   │   │   ├── quiz_session.dart           # QuizSession class (Drift)
│   │   │   └── quiz_response.dart          # QuizResponse class (Drift)
│   │   ├── database/
│   │   │   ├── database.dart               # Drift AppDatabase
│   │   │   ├── daos/
│   │   │   │   ├── question_dao.dart
│   │   │   │   ├── session_dao.dart
│   │   │   │   └── response_dao.dart
│   │   │   └── migrations.dart             # Schema versioning
│   │   ├── service/
│   │   │   ├── quiz_engine.dart            # Core quiz logic
│   │   │   ├── quiz_service.dart           # Business logic
│   │   │   ├── history_service.dart        # History queries
│   │   │   ├── import_service.dart         # Markdown import
│   │   │   ├── markdown_parser.dart        # MD file parsing
│   │   │   ├── answer_shuffler.dart        # Answer randomization
│   │   │   └── quiz_utils.dart             # Helper utilities
│   │   ├── cli/
│   │   │   ├── commands/
│   │   │   │   ├── quiz_command.dart
│   │   │   │   ├── import_command.dart
│   │   │   │   ├── history_command.dart
│   │   │   │   └── clear_command.dart
│   │   │   ├── formatter.dart              # Table/box formatting
│   │   │   └── prompts.dart                # Interactive prompts
│   │   └── exceptions/
│   │       └── quiz_exceptions.dart        # Custom exceptions
├── test/
│   ├── src/
│   │   ├── database/
│   │   │   ├── question_dao_test.dart
│   │   │   └── database_test.dart
│   │   ├── service/
│   │   │   ├── quiz_engine_test.dart
│   │   │   └── answer_shuffler_test.dart
│   │   └── models/
│   │       └── models_test.dart
├── analysis_options.yaml                  # Linting rules
├── Dockerfile               # Container image for production deployment
├── docker-compose.yml       # Multi-container orchestration for dev/test
└── README.md                               # Documentation
```

### Docker & Containerization

#### Dockerfile (Production - Multi-stage)
```dockerfile
# Build stage
FROM google/dart:3.0 as builder

WORKDIR /app

COPY pubspec.* .
RUN dart pub get

COPY . .
RUN dart compile exe lib/main.dart -o bin/quiz-engine

# Runtime stage
FROM alpine:latest

WORKDIR /app

RUN apk add --no-cache libc6-compat sqlite-libs

COPY --from=builder /app/bin/quiz-engine .

# Create non-root user
RUN addgroup -g 1000 dartuser && adduser -D -u 1000 -G dartuser dartuser
RUN chown -R dartuser:dartuser /app
USER dartuser

ENTRYPOINT ["./quiz-engine"]
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
    working_dir: /app
    command: dart run lib/main.dart --help
    environment:
      - PUB_CACHE=/app/.pub-cache
    stdin_open: true
    tty: true

  quiz-engine-test:
    build: .
    container_name: quiz-engine-test
    volumes:
      - .:/app
    working_dir: /app
    command: bash -c "dart pub get && dart run test && dart run test --coverage=coverage && format_coverage --lcov --in=coverage/test_coverage.json --out=coverage/coverage.lcov && dart run scripts/check_coverage.sh"
    environment:
      - PUB_CACHE=/app/.pub-cache

  quiz-engine-build:
    build: .
    container_name: quiz-engine-build
    volumes:
      - .:/app
    working_dir: /app
    command: dart compile exe lib/main.dart -o bin/quiz-engine-release
```

#### Getting Started with Docker

**Quick Start (5 steps):**

1. **Build the image:**
   ```bash
   docker build -t quiz-engine:latest .
   ```

2. **Run development mode:**
   ```bash
   docker run -it quiz-engine:latest dart run lib/main.dart quiz --questions 10
   ```

3. **Run tests with coverage threshold:**
   ```bash
   docker-compose up quiz-engine-test
   ```

4. **Build native executable:**
   ```bash
   docker-compose up quiz-engine-build
   ```

5. **Run compiled binary directly:**
   ```bash
   docker run -it quiz-engine:latest ./quiz-engine quiz --questions 10
   ```

**Build & Push:**
```bash
# Build multi-arch
docker buildx build --platform linux/amd64,linux/arm64 -t myregistry/quiz-engine:1.0 .

# Push to registry
docker push myregistry/quiz-engine:1.0
```

**Container Configuration:**
- Multi-stage build: Dart SDK build + minimal Alpine runtime
- Compiled native executable (single binary, no runtime dependency)
- Drift database compiled into executable
- Non-root user (dartuser) for security
- Coverage verification with genhtml/lcov
- Pub cache volume for faster dependency resolution

### Database Schema (Drift)

#### Question Table
```dart
class Questions extends Table {
  IntColumn get id => integer().autoIncrement()();

  TextColumn get questionText =>
      text().withLength(min: 1, max: 500)();

  TextColumn get optionA =>
      text().withLength(min: 1, max: 200)();

  TextColumn get optionB =>
      text().withLength(min: 1, max: 200)();

  TextColumn get optionC =>
      text().withLength(min: 1, max: 200)();

  TextColumn get optionD =>
      text().withLength(min: 1, max: 200)();

  TextColumn get optionE =>
      text().withLength(min: 0, max: 200).nullable()();

  TextColumn get correctAnswer =>
      text().withLength(min: 1, max: 1)();

  TextColumn get explanation =>
      text().withLength(min: 0, max: 1000).nullable()();

  TextColumn get section =>
      text().withLength(min: 0, max: 100).nullable()();

  TextColumn get difficulty =>
      text().withLength(min: 0, max: 50).nullable()();

  TextColumn get sourceFile =>
      text().withLength(min: 0, max: 255).nullable()();

  IntColumn get usageCycle =>
      integer().withDefault(const Constant(1))();

  IntColumn get timesUsed =>
      integer().withDefault(const Constant(0))();

  DateTimeColumn get lastUsedAt =>
      dateTime().nullable()();

  DateTimeColumn get createdAt =>
      dateTime().withDefault(currentDateAndTime)();
}
```

#### QuizSession Table
```dart
class QuizSessions extends Table {
  TextColumn get sessionId =>
      text().withLength(min: 36, max: 36)();

  DateTimeColumn get startedAt =>
      dateTime().withDefault(currentDateAndTime)();

  DateTimeColumn get endedAt =>
      dateTime().nullable()();

  IntColumn get numQuestions => integer()();

  IntColumn get numCorrect =>
      integer().withDefault(const Constant(0))();

  RealColumn get percentageCorrect =>
      real().withDefault(const Constant(0.0))();

  IntColumn get timeTakenSeconds =>
      integer().nullable()();

  @override
  Set<Column> get primaryKey => {sessionId};
}
```

#### QuizResponse Table
```dart
class QuizResponses extends Table {
  IntColumn get id => integer().autoIncrement()();

  TextColumn get sessionId =>
      text().withLength(min: 36, max: 36)();

  IntColumn get questionId => integer()();

  TextColumn get userAnswer =>
      text().withLength(min: 1, max: 1)();

  IntColumn get isCorrect =>
      integer().withDefault(const Constant(0))();

  IntColumn get timeTakenSeconds =>
      integer().nullable()();

  @override
  List<Set<Column>> get uniqueKeys => [
    {sessionId, questionId}
  ];
}
```

---

## Implementation Plan

### Phase 1: Project Setup & Drift Configuration
**Timeline:** 1.5-2 hours

**Objective:** Initialize Dart project, setup Drift ORM, define entities.

**Tasks:**

1. **Create Dart Project:**
   ```bash
   dart create quiz_engine
   cd quiz_engine
   ```

2. **Update `pubspec.yaml` Dependencies:**
   ```yaml
   name: quiz_engine
   description: GitHub Actions Quiz Engine
   version: 1.0.0
   publish_to: none

   environment:
     sdk: '>=3.0.0 <4.0.0'

   dependencies:
     args: ^2.4.0
     drift: ^2.13.0
     sqlite3_flutter_libs: ^0.5.0
     path_provider: ^2.1.0
     path: ^1.8.0

   dev_dependencies:
     drift_dev: ^2.13.0
     build_runner: ^2.4.0
     test: ^1.24.0
   ```

3. **Create Drift Database Class:**
   - `lib/src/database/database.dart`
   - Define `AppDatabase` extending `GeneratedDatabase`
   - Include migrations and schema management

4. **Define Table Classes:**
   - `lib/src/models/question.dart` → `Questions` table
   - `lib/src/models/quiz_session.dart` → `QuizSessions` table
   - `lib/src/models/quiz_response.dart` → `QuizResponses` table

5. **Generate Drift Code:**
   ```bash
   dart run build_runner build
   ```
   - Generates `database.g.dart` with type-safe queries
   - Auto-generates DAOs

6. **Create DAO Classes:**
   - `QuestionDao` with cycle-aware query methods
   - `SessionDao` for session CRUD
   - `ResponseDao` for response tracking

7. **Test database initialization:**
   - `dart run lib/main.dart --init`
   - SQLite database created successfully

**Success Criteria:**
- Dart project structure created
- Drift code generation successful
- SQLite database initializes with schema
- All entities properly mapped
- DAOs functional

---

### Phase 2: Service Layer & Quiz Logic
**Timeline:** 2-2.5 hours

**Objective:** Implement core quiz engine, services, utility classes.

**Tasks:**

1. **Create `QuizEngine` Class:**
   ```dart
   class QuizEngine {
     final String sessionId;
     final QuizService _quizService;
     late List<Question> _currentQuestions;
     late QuizSessionData _session;

     QuizEngine(this.sessionId, this._quizService);

     Future<void> loadQuestions(int count) async {
       _currentQuestions = await _quizService.getRandomQuestions(count);
     }

     Future<void> submitAnswer(
       int questionIndex,
       String userAnswer,
       int timeTaken,
     ) async {
       final question = _currentQuestions[questionIndex];
       final isCorrect = _verifyAnswer(userAnswer, question.correctAnswer);

       await _quizService.saveResponse(
         sessionId: sessionId,
         questionId: question.id,
         userAnswer: userAnswer,
         isCorrect: isCorrect ? 1 : 0,
         timeTaken: timeTaken,
       );
     }

     Future<void> finalize() async {
       // Mark questions used
       for (final question in _currentQuestions) {
         await _quizService.markQuestionUsed(question.id);
       }

       // Auto-advance cycle if exhausted
       await _quizService.advanceCycleIfNeeded();

       // Update session stats
       final numCorrect = await _quizService.countCorrectAnswers(sessionId);
       final percentage = (numCorrect / _currentQuestions.length) * 100;

       await _quizService.updateSession(
         sessionId: sessionId,
         numCorrect: numCorrect,
         percentage: percentage,
         endedAt: DateTime.now(),
       );
     }

     bool _verifyAnswer(String shuffled, String correct) {
       return shuffled == correct;
     }
   }
   ```

2. **Create `QuizService` Class:**
   - Wrapper around Drift DAOs
   - Orchestrate business logic
   - `getRandomQuestions()`, `markQuestionUsed()`, `advanceCycleIfNeeded()`

3. **Create `AnswerShuffler` Class:**
   - `shuffleAnswers(Question)` → randomized list + position map
   - Preserve shuffled answer for verification

4. **Create `MarkdownParser` Class:**
   - `parseFile(File)` → extract questions from markdown
   - Regex parsing or markdown package

5. **Create Utility Classes:**
   - `HistoryService` for queries
   - `ImportService` for batch import
   - `QuizUtils` for scoring, formatting

6. **Test service layer:**
   ```bash
   dart test test/src/service/quiz_engine_test.dart
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

### Phase 3: CLI Implementation with args + mason_logger
**Timeline:** 1.5-2 hours

**Objective:** Build interactive CLI using `args` package and `mason_logger`.

**Tasks:**

1. **Create Command Structure:**
   - `lib/src/cli/commands/` directory
   - Each command is a separate file
   - Extend `Command` abstract class or use Function pattern

2. **Implement Quiz Command:**
   ```dart
   Future<int> quizCommand(List<String> args) async {
     // Parse arguments
     final parser = ArgParser()
       ..addOption('questions', defaultsTo: '100', abbr: 'q')
       ..addOption('seconds-per', defaultsTo: '60', abbr: 's');

     final results = parser.parse(args);
     final numQuestions = int.parse(results['questions'] as String);

     // Interactive quiz flow
     final quizService = QuizService();
     final sessionId = uuid.v4();

     stdout.write('Starting quiz...\n');

     // Present questions
     for (var i = 0; i < numQuestions; i++) {
       // Display question with timer
       // Get user input
       // Submit answer
     }

     // Display results and offer review
   }
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
   ```dart
   Future<void> main(List<String> args) async {
     final parser = ArgParser()
       ..addCommand('quiz')
       ..addCommand('import')
       ..addCommand('history')
       ..addCommand('clear')
       ..addFlag('help', abbr: 'h');

     final results = parser.parse(args);

     if (results['help'] as bool) {
       print(parser.usage);
       return;
     }

     final command = results.command?.name;
     switch (command) {
       case 'quiz':
         exit(await quizCommand(results.command!.rest));
       case 'import':
         exit(await importCommand(results.command!.rest));
       case 'history':
         exit(await historyCommand(results.command!.rest));
       case 'clear':
         exit(await clearCommand(results.command!.rest));
       default:
         print('Unknown command: $command');
         exit(1);
     }
   }
   ```

7. **Create Column Formatter:**
   ```dart
   class Formatter {
     static String boxed(String title, String content) {
       // Use box_drawing_chars for pretty output
       return '╔═══════════════════════════════════╗\n'
              '║ $title\n'
              '╚═══════════════════════════════════╝\n'
              '$content';
     }

     static String table(List<List<String>> rows) {
       // Format as ASCII table
     }
   }
   ```

8. **Test CLI commands:**
   ```bash
   dart run lib/main.dart quiz
   dart run lib/main.dart import --file questions.md
   ```

**Success Criteria:**
- All CLI commands execute correctly
- Interactive prompts work smoothly
- Pretty-printed output formatted
- Error handling graceful
- No unhandled exceptions

---

### Phase 4: Unit Testing & Coverage Enforcement
**Timeline:** 2-3 hours

**Objective:** Achieve >90% unit test coverage. The CI script must fail if coverage drops below 90%.

**Add to `pubspec.yaml` dev_dependencies:**
```yaml
dev_dependencies:
  test: ^1.24.0
  coverage: ^1.6.0
  mocktail: ^1.0.0
  lints: ^3.0.0
```

**Run tests with coverage:**
```bash
# Run tests and collect coverage
dart pub global activate coverage
dart run test --coverage=coverage

# Convert to LCOV format
dart pub global run coverage:format_coverage \
  --lcov \
  --in=coverage \
  --out=coverage/lcov.info \
  --report-on=lib

# Generate HTML report (requires lcov installed)
genhtml coverage/lcov.info --output-directory coverage/html
open coverage/html/index.html
```

**Enforce 90% threshold (add to `Makefile` or CI script):**
```bash
#!/bin/bash
# scripts/check_coverage.sh
COVERAGE=$(lcov --summary coverage/lcov.info 2>&1 | grep "lines" | awk '{print $2}' | tr -d '%')
echo "Line coverage: ${COVERAGE}%"
if (( $(echo "$COVERAGE < 90" | bc -l) )); then
  echo "ERROR: Coverage ${COVERAGE}% is below the required 90%"
  exit 1
fi
echo "Coverage check passed: ${COVERAGE}%"
```

**Tasks:**

1. **Create `test/helpers.dart` — shared fixtures:**
   ```dart
   import 'package:quiz_engine/database/database.dart';

   /// Creates an in-memory Drift database for test isolation.
   QuizDatabase openTestDatabase() {
     return QuizDatabase.forTesting();
   }

   QuestionCompanion sampleQuestion({
     String questionText = 'What is CI?',
     String optionA = 'Continuous Integration',
     String optionB = 'Code Import',
     String optionC = 'Compile',
     String optionD = 'Configure',
     String correctAnswer = 'A',
   }) =>
       QuestionCompanion.insert(
         questionText: questionText,
         optionA: optionA,
         optionB: optionB,
         optionC: optionC,
         optionD: optionD,
         correctAnswer: correctAnswer,
       );
   ```

2. **Write `test/dao/question_dao_test.dart` (target: >92%):**
   ```dart
   import 'package:test/test.dart';
   import '../helpers.dart';

   void main() {
     late QuizDatabase db;

     setUp(() => db = openTestDatabase());
     tearDown(() async => await db.close());

     test('inserts and retrieves a question', () async {
       await db.questionsDao.insert(sampleQuestion());
       final all = await db.questionsDao.getAllQuestions();
       expect(all.length, equals(1));
       expect(all.first.questionText, equals('What is CI?'));
     });

     test('getRandomQuestions omits correctAnswer', () async {
       await db.questionsDao.insert(sampleQuestion());
       final questions = await db.questionsDao.getRandomQuestions(1);
       // The projection must not include correctAnswer
       expect(() => questions.first.correctAnswer, throwsNoSuchMethodError);
     });

     test('advances cycle when all questions used', () async {
       final id = await db.questionsDao.insert(sampleQuestion());
       await db.questionsDao.markQuestionUsed(id);
       await db.questionsDao.advanceCycleIfExhausted();
       expect(await db.questionsDao.getCurrentCycle(), equals(2));
     });

     test('skips duplicate on insert', () async {
       final q = sampleQuestion();
       await db.questionsDao.insertIfNotExists(q);
       await db.questionsDao.insertIfNotExists(q);
       final count = await db.questionsDao.countQuestions();
       expect(count, equals(1));
     });

     test('getRandomQuestions respects current cycle', () async {
       final id = await db.questionsDao.insert(sampleQuestion());
       await db.questionsDao.markQuestionUsed(id);
       await db.questionsDao.advanceCycleIfExhausted();
       final questions = await db.questionsDao.getRandomQuestions(1);
       expect(questions.first.usageCycle, equals(2));
     });
   }
   ```

3. **Write `test/service/quiz_engine_test.dart` (target: >92%):**
   ```dart
   void main() {
     late QuizDatabase db;
     late QuizEngine engine;

     setUp(() async {
       db = openTestDatabase();
       await db.questionsDao.insert(sampleQuestion());
       await db.questionsDao.insert(
         sampleQuestion(questionText: 'Q2', correctAnswer: 'B'),
       );
       engine = QuizEngine(db.questionsDao, db.sessionsDao, numQuestions: 2);
     });

     tearDown(() async => await db.close());

     test('loadQuestions returns requested count', () async {
       await engine.loadQuestions();
       expect(engine.questions.length, equals(2));
     });

     test('submitAnswer increments score on correct answer', () async {
       await engine.loadQuestions();
       await engine.submitAnswer(0, 'A', timeTaken: 10);
       expect(engine.numCorrect, equals(1));
     });

     test('submitAnswer does not score wrong answer', () async {
       await engine.loadQuestions();
       await engine.submitAnswer(0, 'B', timeTaken: 10);
       expect(engine.numCorrect, equals(0));
     });

     test('finalizeQuiz persists session to database', () async {
       await engine.loadQuestions();
       await engine.submitAnswer(0, 'A', timeTaken: 5);
       await engine.submitAnswer(1, 'B', timeTaken: 5);
       final session = await engine.finalizeQuiz();
       expect(session.sessionId, isNotEmpty);
       final saved = await db.sessionsDao.getSession(session.sessionId);
       expect(saved, isNotNull);
     });
   }
   ```

4. **Write `test/utils/answer_shuffler_test.dart` (target: >95%):**
   ```dart
   void main() {
     test('shuffle preserves all original options', () {
       final options = ['Alpha', 'Beta', 'Gamma', 'Delta'];
       final result = shuffleAnswers(options, 'A');
       expect(result.shuffledOptions.toSet(), equals(options.toSet()));
     });

     test('shuffle maps correct answer to new position', () {
       final options = ['Alpha', 'Beta', 'Gamma', 'Delta'];
       final result = shuffleAnswers(options, 'A'); // A = 'Alpha'
       expect(result.shuffledOptions[result.correctShuffledIndex], equals('Alpha'));
     });

     test('shuffle returns 4 options', () {
       final result = shuffleAnswers(['A', 'B', 'C', 'D'], 'C');
       expect(result.shuffledOptions.length, equals(4));
     });
   }
   ```

5. **Coverage target summary:**

| File | Test File | Target |
|---|---|---|
| `lib/dao/question_dao.dart` | `test/dao/question_dao_test.dart` | >92% |
| `lib/service/quiz_engine.dart` | `test/service/quiz_engine_test.dart` | >92% |
| `lib/utils/answer_shuffler.dart` | `test/utils/answer_shuffler_test.dart` | >95% |
| `lib/utils/markdown_parser.dart` | `test/utils/markdown_parser_test.dart` | >90% |
| `lib/service/history_service.dart` | `test/service/history_service_test.dart` | >90% |

6. **Build Release Executable:**
   ```bash
   dart compile exe lib/main.dart -o bin/quiz_engine
   ```
   - Single executable file (no Dart runtime dependency)

7. **Write Comprehensive README** with testing section:
   - `dart run test --coverage=coverage && scripts/check_coverage.sh` — must show ≥90%

8. **Final Testing:**
   - Full end-to-end workflow: Import → Quiz → History → Retake
   - Verify cycle mechanics and non-repetition
   - Cross-platform execution

**Success Criteria:**
- `dart run test --coverage=coverage` passes with all tests green
- `scripts/check_coverage.sh` **exits 0 only at ≥90% line coverage**
- LCOV HTML report generated at `coverage/html/index.html`
- Executable compiles successfully
- Single-file distribution (no dependencies)
- Full documentation provided
- Works on Windows/Mac/Linux

---

## Dependencies Summary
- **drift** (2.13.0) - Type-safe ORM
- **drift_dev** (2.13.0) - Code generation
- **args** (2.4.0) - CLI argument parsing
- **sqlite3_flutter_libs** (0.5.0) - SQLite support
- **path_provider** (2.1.0) - Platform-aware paths
- **test** (1.24.0) - Testing framework

---

## Core Design Decisions

### 1. Drift for ORM
- **Type-Safe:** Compile-time query validation
- **Reactive:** Built-in reactive streams (Optional)
- **Code Generation:** Reduces boilerplate via `build_runner`
- **SQLite:** Native SQLite support with better performance

### 2. Command-Line Argument Parsing
- **args package:** Standard, maintained
- **Subcommands:** quiz, import, history, clear
- **Flags & Options:** Flexible configuration

### 3. Single Executable Distribution
- **Dart compile exe:** Creates standalone binary
- **No Runtime Needed:** Self-contained executable
- **Cross-Platform:** Native support for Windows/Mac/Linux

### 4. Functional Command Structure
- **Stateless:** Each command function independent
- **Composable:** Easy to combine commands
- **Testable:** Mock dependencies easily

### 5. Non-Repetition with Drift Queries
- **Cycle Tracking:** usageCycle, timesUsed columns
- **Type-Safe Queries:** Drift generates safe SQL
- **Performance:** Single database access for cycle determination

---

## CLI Examples

```bash
# Build
dart compile exe lib/main.dart -o bin/quiz_engine

# Take a quiz
./bin/quiz_engine quiz --questions 100 --seconds-per 60

# Import questions
./bin/quiz_engine import --file questions.md
./bin/quiz_engine import --dir ./md/

# View history (in development; use dart run)
dart run lib/main.dart history
dart run lib/main.dart history --session-id <uuid> --review
dart run lib/main.dart history --export json

# Clear data
dart run lib/main.dart clear --questions --confirm
dart run lib/main.dart clear --history --all --confirm
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
- ✓ Performance: Load questions + display <1 second
- ✓ Usability: Full workflow <15 minutes
- ✓ Reliability: Graceful error handling, transactional integrity
- ✓ Maintainability: Clean architecture, testable
- ✓ Compatibility: Dart 3+, Windows/Mac/Linux (native executables)
- ✓ Distribution:** Single executable, no dependencies

---

## Implementation Notes

- **Drift Code Generation:** Run `build_runner` after schema changes
- **Database Migrations:** Use Drift's schema versioning
- **Testing:** Use `test` package for unit/integration tests
- **Error Handling:** Create custom exception classes
- **Formatting:** Use ANSI escape codes for colors/styling
- **Async/Await:** Drift queries are async by default
- **Future:** Add web interface with Shelf framework, REST API
