# Java/SQLite Quiz Engine for GH-200 Certification

## System Architecture Overview

### Directory Structure
```
quiz-engine/
├── src/
│   ├── main/java/com/quizengine/
│   │   ├── QuizEngineApp.java           # Entry point, orchestrates quiz flow
│   │   ├── model/
│   │   │   ├── Question.java            # Entity: question data
│   │   │   ├── QuizSession.java         # Entity: session metadata
│   │   │   └── QuizResponse.java        # Entity: individual responses
│   │   ├── dao/
│   │   │   ├── QuestionDAO.java         # Database operations: questions
│   │   │   ├── SessionDAO.java          # Database operations: sessions
│   │   │   └── DatabaseManager.java     # Connection pooling, schema init
│   │   ├── service/
│   │   │   ├── QuizEngine.java          # Core quiz logic (load, submit, finalize)
│   │   │   ├── QuizService.java         # Business logic wrapper
│   │   │   └── HistoryService.java      # History queries and formatting
│   │   ├── util/
│   │   │   ├── MarkdownParser.java      # Parse MD files → questions
│   │   │   ├── AnswerShuffler.java      # Randomize and track answers
│   │   │   └── QuizUtils.java           # Helpers: scoring, time, formatting
│   │   └── cli/
│   │       ├── QuizCLI.java             # User prompts and input handling
│   │       └── ConsoleFormatter.java    # Pretty-print tables, progress
│   ├── resources/
│   │   └── schema.sql                   # SQLite schema definition
│   └── test/java/com/quizengine/
│       ├── dao/
│       │   ├── QuestionDAOTest.java          # CRUD, cycle mechanics, duplicate handling
│       │   └── SessionDAOTest.java           # Session insert, query, export
│       ├── service/
│       │   ├── QuizEngineTest.java           # Load, submit, finalize, scoring
│       │   └── HistoryServiceTest.java       # History queries, formatting
│       └── util/
│           ├── AnswerShufflerTest.java       # Shuffling correctness, answer mapping
│           └── MarkdownParserTest.java       # File parsing, validation, edge cases
├── build.gradle.kts                     # Gradle dependencies and build config
├── gradlew                              # Gradle wrapper (Unix/Mac)
├── gradlew.bat                          # Gradle wrapper (Windows)
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── settings.gradle.kts                  # Gradle project settings
├── README.md                            # Setup, usage, operation docs
├── Dockerfile               # Container image for production deployment
├── docker-compose.yml       # Multi-container orchestration for dev/test
└── .gitignore
```

### Docker & Containerization

#### Dockerfile (Production - Multi-stage)
```dockerfile
# Build stage
FROM gradle:8-jdk17 as builder

WORKDIR /app

COPY . .

RUN gradle build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy built JAR from builder
COPY --from=builder /app/build/libs/*.jar app.jar

# Create non-root user
RUN addgroup -g 1000 javauser && adduser -D -u 1000 -G javauser javauser
RUN chown -R javauser:javauser /app
USER javauser

# JVM optimizations
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java", "-jar", "app.jar"]
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
      - gradle-cache:/root/.gradle  # Cache Gradle downloads
    working_dir: /app
    command: gradle bootRun --no-daemon
    environment:
      - GRADLE_OPTS=-Dorg.gradle.daemon=false
    stdin_open: true
    tty: true

  quiz-engine-test:
    build: .
    container_name: quiz-engine-test
    volumes:
      - .:/app
      - gradle-cache:/root/.gradle
    working_dir: /app
    command: gradle test jacocoTestCoverageVerification --no-daemon
    environment:
      - GRADLE_OPTS=-Dorg.gradle.daemon=false

volumes:
  gradle-cache:
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

3. **Run tests with JaCoCo coverage:**
   ```bash
   docker-compose up quiz-engine-test
   ```

4. **Run interactively:**
   ```bash
   docker run -it quiz-engine:latest quiz --questions 10
   ```

5. **Import questions:**
   ```bash
   docker run -it -v $(pwd):/app quiz-engine:latest java -jar app.jar import --file questions.md
   ```

**Build & Push:**
```bash
# Build multi-arch
docker buildx build --platform linux/amd64,linux/arm64 -t myregistry/quiz-engine:1.0 .

# Push to registry
docker push myregistry/quiz-engine:1.0
```

**Container Configuration:**
- Multi-stage build: Gradle build + minimal runtime JRE
- Eclipse Temurin 17 JRE Alpine for small image size
- Non-root user (javauser) for security
- Gradle cache volume for faster rebuilds
- G1GC garbage collector optimized for containers
- JaCoCo coverage verification integrated in test service

### Database Schema

#### Table: questions
```sql
CREATE TABLE questions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    question_text TEXT NOT NULL,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT NOT NULL,
    option_d TEXT NOT NULL,
    option_e TEXT,
    correct_answer TEXT NOT NULL,
    explanation TEXT,
    section TEXT,
    difficulty TEXT,
    source_file TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usage_cycle INTEGER DEFAULT 1,
    times_used INTEGER DEFAULT 0,
    last_used_at TIMESTAMP,
    UNIQUE(question_text, correct_answer)
);
CREATE INDEX idx_questions_section ON questions(section);
CREATE INDEX idx_questions_difficulty ON questions(difficulty);
CREATE INDEX idx_questions_usage_cycle ON questions(usage_cycle);
```

#### Table: quiz_sessions
```sql
CREATE TABLE quiz_sessions (
    session_id TEXT PRIMARY KEY,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP,
    num_questions INTEGER NOT NULL,
    num_correct INTEGER DEFAULT 0,
    percentage_correct REAL DEFAULT 0.0,
    time_taken_seconds INTEGER,
    UNIQUE(session_id)
);
CREATE INDEX idx_sessions_date ON quiz_sessions(started_at DESC);
```

#### Table: quiz_responses
```sql
CREATE TABLE quiz_responses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    session_id TEXT NOT NULL,
    question_id INTEGER NOT NULL,
    user_answer TEXT NOT NULL,
    is_correct INTEGER DEFAULT 0,
    time_taken_seconds INTEGER,
    FOREIGN KEY (session_id) REFERENCES quiz_sessions(session_id),
    FOREIGN KEY (question_id) REFERENCES questions(id),
    UNIQUE(session_id, question_id)
);
CREATE INDEX idx_responses_session ON quiz_responses(session_id);
```

---

## Implementation Plan

### Phase 1: Foundation (Setup & Database)
**Timeline:** 1.5-2.5 hours

**Objective:** Create Gradle project structure, SQLite schema, entity models, and DAO layer.

**Tasks:**

1. **Initialize Gradle Project:**
   ```bash
   gradle init --type java-application --dsl kotlin
   ```
   - Generates project structure with Gradle wrapper
   - Creates `build.gradle.kts` with standard plugins

2. **Create `build.gradle.kts` Configuration:**
   ```kotlin
   plugins {
       id("java")
       id("application")
   }

   java {
       toolchain {
           languageVersion.set(JavaLanguageVersion.of(17))
       }
   }

   repositories {
       mavenCentral()
   }

   dependencies {
       // SQLite JDBC Driver
       implementation("org.xerial:sqlite-jdbc:3.44.0.0")

       // HikariCP Connection Pooling
       implementation("com.zaxxer:HikariCP:5.1.0")

       // Picocli for CLI
       implementation("info.picocli:picocli:4.7.5")

       // Jackson for JSON handling
       implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")

       // JUnit 5 & Mockito for testing
       testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
       testImplementation("org.mockito:mockito-core:5.6.1")
       testImplementation("org.mockito:mockito-junit-jupiter:5.6.1")
   }

   tasks.test {
       useJUnitPlatform()
   }

   application {
       mainClass.set("com.quizengine.QuizEngineApp")
   }
   ```

3. **Write `src/main/resources/schema.sql`:**
   - All three table definitions with indexes and foreign keys

4. **Write `DatabaseManager.java`:**
   - `initializeDatabase()` → creates 3 tables with indexes
   - Configure HikariCP for connection pooling
   - Handle SQLite-specific settings (foreign keys, WAL mode)

5. **Write Entity Models (`model/` package):**
   - `Question.java` → POJO with id, questionText, optionA-E, correctAnswer, explanation, section, difficulty, usageCycle, timesUsed, lastUsedAt
   - `QuizSession.java` → POJO with sessionId, startedAt, endedAt, numQuestions, numCorrect, percentageCorrect, timeTakenSeconds
   - `QuizResponse.java` → POJO with id, sessionId, questionId, userAnswer, isCorrect, timeTakenSeconds
   - **CRITICAL:** Question model does NOT expose correctAnswer/explanation in toString()

6. **Write DAO Layer (`dao/` package):**
   - `QuestionDAO.java` (interface with methods)
   - `QuestionDAOImpl.java` (implementation):
     - `getCurrentCycle()` → MIN(usage_cycle) from DB
     - `getRandomQuestions(n, difficulty, section)` → cycle-aware SELECT
     - `getQuestionsByIds(list)` → fetch specific questions
     - `markQuestionUsed(questionId)` → increment usage, check cycle exhaustion
     - `advanceQuestionsToCycle()` → increment usage_cycle for exhausted questions
   - `SessionDAO.java` & `SessionDAOImpl.java` for quiz_sessions table CRUD
   - `ResponseDAO.java` & `ResponseDAOImpl.java` for quiz_responses table CRUD

7. **Test database initialization:** `./gradlew test` should pass for DAO layer tests

**Success Criteria:**
- SQLite file created at `quiz-engine/quiz.db`
- Schema initialized with 3 tables, indexes, foreign keys validated
- HikariCP connection pool operational
- DAO tests pass (CRUD operations work)
- No connection leaks

---

### Phase 2: Core Quiz Logic (Question Loading, Timing, Scoring)
**Timeline:** 2.5-3.5 hours

**Objective:** Implement QuizEngine service with randomization, per-question timing, and scoring.

**Tasks:**

1. **Write `util/AnswerShuffler.java`:**
   - `shuffleAnswers(Question)` → randomize A-E, return shuffled list + position mapping
   - `getShuffledDisplay(Question, mapping)` → format shuffled options for display
   - `verifyAnswer(userAnswer, mappedCorrect)` → check if user's shuffled letter is correct

2. **Write `util/MarkdownParser.java`:**
   - `parseFile(Path)` → extract questions from MD files matching pattern
   - `extractQuestions()` → regex or SAX parsing to get question blocks
   - `parseQuestion(block)` → extract text, options A-E, correct answer, explanation

3. **Write `util/QuizUtils.java`:**
   - `calculateScore(correct, total)` → percentage
   - `formatTime(seconds)` → "MM:SS" format
   - `formatTable(data)` → ASCII table for results

4. **Write `service/QuizEngine.java`:**
   - Constructor: `QuizEngine(sessionId, config: QuizConfig)`
   - `loadQuestions(n)` → fetch random questions from current cycle without answers
   - `submitAnswer(questionIdx, userAnswer, timeTaken)` → record response, verify internally
   - `finalize()` → calculate score, mark questions used, persist session, auto-advance cycle
   - `getSessionReview()` → fetch answers WITH correct answers and explanations
   - `getResults()` → return summary (score, time, percentage)

5. **Write `cli/QuizCLI.java` (using Picocli):**
   - `promptConfiguration()` → interactive questions for num_questions, seconds_per, total_minutes
   - `displayQuestion(question, options, num, total)` → formatted display
   - `getAnswerInput()` → validate A-E input with retry
   - `displayFeedback(isCorrect)` → minimal emoji feedback
   - `displayFinalReview(session)` → show answers AFTER quiz complete
   - `promptNewQuiz()` → ask user to retake, review, or exit

6. **Write `QuizEngineApp.java` (Main entry point):**
   - Orchestrate: init DB → config → loop questions → finalize → display → review option → retake
   - Handle Ctrl+C gracefully (save session state)
   - Timer logic: per-question countdown, global time warning

7. **Test quiz flow:**
   - `./gradlew run --args="quiz"`
   - Run 5-question test quiz
   - Verify cycle-aware question selection
   - Test retake flow

**Success Criteria:**
- Quiz loads 100 random questions without showing answers
- NO questions repeat until all exhausted at cycle
- Answers randomized and correctly tracked
- Per-question timer works
- Score calculation accurate
- Session persisted to DB with UUID
- User can retake without duplicate questions

---

### Phase 3: Data Management (Import, History, Clear Operations)
**Timeline:** 1.5-2 hours

**Objective:** Import questions from markdown, query history, export data.

**Tasks:**

1. **Write `MarkdownParser.java` enhancements:**
   - Batch import logic
   - Duplicate detection
   - Validation: 4-5 options, exactly one correct answer

2. **Write Import CLI Command:**
   - `@Command(name="import")` → Picocli subcommand
   - `--file` option for single file
   - `--dir` option for batch import
   - Report: X imported, Y skipped, Z errors

3. **Write `service/HistoryService.java`:**
   - `getAllSessions()` → query with pagination
   - `getSessionDetails(sessionId)` → fetch responses WITH answers
   - `formatSessionHistory()` → table with date, duration, score
   - `formatSessionReview()` → answer key (incorrect first, then correct)
   - `exportToCSV()` / `exportToJSON()` → file export

4. **Write History CLI Commands:**
   - `@Command(name="history")` → View sessions
   - `--session-id` → filter to single session
   - `--review` → show full answer key
   - `--export csv|json` → export format
   - `--include-answers` → flag for detail level

5. **Write Clear Commands:**
   - `@Command(name="clear-questions")` → truncate questions table (with confirmation)
   - `@Command(name="clear-history")` → delete sessions: `--session-id`, `--all`, `--before days`

6. **Test data operations:**
   - Import 100 test questions
   - Take 2 quizzes, view history
   - Export to CSV/JSON
   - Clear operations with confirmation

**Success Criteria:**
- 100+ questions imported without duplicates
- History shows all sessions with stats
- Export works for both formats
- Clear operations safe (confirmation required)
- Incorrect answers listed first in review

---

### Phase 4: Unit Testing & Coverage Enforcement (JUnit 5 + JaCoCo)
**Timeline:** 2-3 hours

**Objective:** Achieve >90% unit test coverage across all non-CLI modules. JaCoCo must fail the build below 90%.

**JaCoCo Configuration (`build.gradle.kts`):**
```kotlin
plugins {
    java
    application
    id("jacoco")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)
        xml.required.set(true)
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.90".toBigDecimal()   // fail build below 90%
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}
```

**Run coverage:**
```bash
./gradlew test jacocoTestReport jacocoTestCoverageVerification
# HTML report: build/reports/jacoco/test/html/index.html
```

**Tasks:**

1. **Write `QuestionDAOTest.java` (target: >92%):**
   ```java
   class QuestionDAOTest {
       private QuestionDAO dao;

       @BeforeEach
       void setUp() throws Exception {
           // In-memory SQLite database for test isolation
           Connection conn = DriverManager.getConnection("jdbc:sqlite::memory:");
           DatabaseManager manager = new DatabaseManager(conn);
           manager.initSchema();
           dao = new QuestionDAO(conn);
       }

       @Test
       void insertAndRetrieve_returnsInsertedQuestion() {
           Question q = new Question("Q1", "A", "B", "C", "D", "A");
           dao.insert(q);
           List<Question> all = dao.getAll();
           assertEquals(1, all.size());
           assertEquals("Q1", all.get(0).getQuestionText());
       }

       @Test
       void getRandomQuestions_omitsCorrectAnswer() {
           dao.insert(new Question("Q1", "A", "B", "C", "D", "A"));
           List<Question> questions = dao.getRandomQuestions(1);
           assertNull(questions.get(0).getCorrectAnswer(),
               "Correct answer must not be returned during quiz");
       }

       @Test
       void advanceCycle_whenAllQuestionsUsed() {
           dao.insert(new Question("Q1", "A", "B", "C", "D", "A"));
           dao.markUsed(1);
           dao.advanceCycleIfExhausted();
           assertEquals(2, dao.getCurrentCycle());
       }

       @Test
       void insert_skipsExactDuplicate() {
           Question q = new Question("Q1", "A", "B", "C", "D", "A");
           dao.insert(q);
           dao.insert(q);
           assertEquals(1, dao.count());
       }

       @Test
       void getRandomQuestions_respectsCurrentCycle() {
           dao.insert(new Question("Q1", "A", "B", "C", "D", "A"));
           dao.markUsed(1);
           dao.advanceCycleIfExhausted();
           List<Question> q2 = dao.getRandomQuestions(1);
           assertEquals(2, q2.get(0).getUsageCycle());
       }
   }
   ```

2. **Write `QuizEngineTest.java` (target: >92%):**
   ```java
   class QuizEngineTest {
       @Test
       void submitCorrectAnswer_incrementsScore() {
           QuestionDAO dao = buildInMemoryDAO();
           dao.insert(sample());
           QuizEngine engine = new QuizEngine(dao, 1);
           engine.loadQuestions();
           engine.submitAnswer(0, "A", 10);
           assertEquals(1, engine.getNumCorrect());
       }

       @Test
       void submitWrongAnswer_doesNotScore() {
           QuestionDAO dao = buildInMemoryDAO();
           dao.insert(sample());
           QuizEngine engine = new QuizEngine(dao, 1);
           engine.loadQuestions();
           engine.submitAnswer(0, "B", 10);
           assertEquals(0, engine.getNumCorrect());
       }

       @Test
       void finalize_persistsSessionToDatabase() {
           QuestionDAO dao = buildInMemoryDAO();
           dao.insert(sample());
           QuizEngine engine = new QuizEngine(dao, 1);
           engine.loadQuestions();
           engine.submitAnswer(0, "A", 5);
           QuizSession session = engine.finalize();
           assertNotNull(session.getSessionId());
           assertNotNull(dao.getSession(session.getSessionId()));
       }

       @Test
       void finalize_calculatesCorrectPercentage() {
           QuestionDAO dao = buildInMemoryDAO();
           dao.insert(sample()); dao.insert(sample2());
           QuizEngine engine = new QuizEngine(dao, 2);
           engine.loadQuestions();
           engine.submitAnswer(0, "A", 5);
           engine.submitAnswer(1, "B", 5);
           QuizSession session = engine.finalize();
           assertEquals(100.0, session.getPercentageCorrect(), 0.01);
       }
   }
   ```

3. **Write `AnswerShufflerTest.java` (target: >95%):**
   ```java
   class AnswerShufflerTest {
       @Test
       void shuffle_preservesAllOptions() {
           Set<String> original = Set.of("Alpha", "Beta", "Gamma", "Delta");
           ShuffleResult result = AnswerShuffler.shuffle(
               List.of("Alpha", "Beta", "Gamma", "Delta"), "A");
           assertEquals(original, new HashSet<>(result.getShuffledOptions()));
       }

       @Test
       void shuffle_mapsCorrectAnswerToNewPosition() {
           ShuffleResult result = AnswerShuffler.shuffle(
               List.of("Alpha", "Beta", "Gamma", "Delta"), "A"); // A = "Alpha"
           String correctText = result.getShuffledOptions()
               .get(result.getCorrectShuffledIndex());
           assertEquals("Alpha", correctText);
       }
   }
   ```

4. **Write `MarkdownParserTest.java` (target: >90%):**
   ```java
   class MarkdownParserTest {
       @Test
       void parseFile_extractsQuestionsCorrectly(@TempDir Path tempDir) throws IOException {
           Path file = tempDir.resolve("test.md");
           Files.writeString(file, """
               ## Q1
               > What is CI?
               - A) Integration\n- B) Delivery\n- C) Deploy\n- D) Build
               **Answer: A**
               """);
           List<Question> questions = MarkdownParser.parseFile(file.toString());
           assertEquals(1, questions.size());
           assertEquals("A", questions.get(0).getCorrectAnswer());
       }

       @Test
       void parseBlock_throwsOnMissingAnswer() {
           assertThrows(IllegalArgumentException.class,
               () -> MarkdownParser.parseBlock("...no answer line..."));
       }
   }
   ```

5. **Coverage target summary:**

| Class | Test File | Target |
|---|---|---|
| `QuestionDAO` | `QuestionDAOTest` | >92% |
| `SessionDAO` | `SessionDAOTest` | >90% |
| `QuizEngine` | `QuizEngineTest` | >92% |
| `HistoryService` | `HistoryServiceTest` | >90% |
| `AnswerShuffler` | `AnswerShufflerTest` | >95% |
| `MarkdownParser` | `MarkdownParserTest` | >90% |

### Phase 5: CLI Polish & README
**Timeline:** 1-2 hours

**Objective:** Enhance UX, implement error handling, document system.

**Tasks:**

1. **Implement Error Handling:**
   - DB connection failures → graceful reconnect with retry
   - Corrupt data → validation on load
   - CLI input validation → retry on invalid input
   - Keyboard interrupt → save session before exit

2. **Create `ConsoleFormatter.java`:**
   - Box drawing for question display
   - Colored output (ANSI codes)
   - Progress bar for timer
   - Table formatting for results

3. **Add Picocli Features:**
   - `@Command` on main class with subcommands
   - `--help` and `--version` flags
   - Tab completion support

4. **Write README.md:**
   - Getting Started, configuration, usage for all commands
   - **Testing section:** `./gradlew test jacocoTestReport` — must show ≥90% coverage
   - Architecture overview

5. **Build fat JAR and end-to-end test:**
   - `./gradlew shadowJar` → creates `build/libs/quiz-engine-all.jar`
   - `java -jar build/libs/quiz-engine-all.jar` → all commands work

**Success Criteria:**
- `./gradlew test jacocoTestCoverageVerification` **passes (>90% coverage enforced)**
- JaCoCo HTML report generated at `build/reports/jacoco/test/html/`
- Fat JAR builds successfully
- All CLI commands work from command line
- Error messages are helpful
- README covers all operations
- No unhandled exceptions in normal usage
- Cross-platform (Windows/Mac/Linux compatible)

---

## Gradle Dependencies Summary
- **sqlite-jdbc** (3.44.0.0) - SQLite driver
- **HikariCP** (5.1.0) - Connection pooling
- **Picocli** (4.7.5) - Modern CLI framework
- **Jackson** (2.16.0) - JSON handling
- **JUnit 5** (5.10.1) - Testing framework
- **Mockito** (5.6.1) - Mocking library

Total JAR size: ~15MB (uncompressed)

**Build with Gradle:**
```bash
./gradlew build                          # Compile and run tests
./gradlew jar                            # Build JAR
./gradlew shadowJar                      # Build fat JAR (requires shadow plugin)
./gradlew run --args="quiz"             # Run quiz command
./gradlew test                           # Run all tests
./gradlew test --info                    # Verbose test output
```

---

## Core Design Decisions

### 1. JDBC + Connection Pooling (HikariCP)
- **Choice:** Manual JDBC instead of ORM (lighter, more control)
- **Pooling:** HikariCP for connection management (high performance)
- **Rationale:** SQLite doesn't need complex ORM; pooling improves throughput

### 2. Picocli for CLI
- **Choice:** Picocli over Spring Shell (lighter, pure Java)
- **Features:** Subcommands, options, auto-completion, help generation
- **Rationale:** Modern, declarative, low-dependency

### 3. Entity Models as Immutable POJOs
- **Immutability:** Reduces bugs, easier testing
- **Builders:** Use builder pattern for construction
- **No JPA annotations:** Keep models pure, decoupled from framework

### 4. DAO Pattern
- **Interface + Implementation:** Loose coupling, testable
- **Separation of Concerns:** Business logic separate from DB access
- **QueryBuilder:** Use StringBuilder for dynamic queries (or QueryDSL for complex)

### 5. Non-Repetition Cycle (Database-Driven)
- **Current Cycle:** Calculated from MIN() in SQL
- **Question Selection:** WHERE usage_cycle = current_cycle filter
- **Auto-Advance:** Happens during finalize()
- **Benefits:** Scalable, no in-memory state needed

---

## CLI Operations & Examples

### 1. Take a Quiz
```bash
java -jar quiz-engine.jar quiz
```
**Output:** Interactive quiz flow with questions, timer, results, answer review

### 2. Import Questions
```bash
java -jar quiz-engine.jar import --file questions.md
java -jar quiz-engine.jar import --dir ./md/
```
**Output:** "Imported 50 questions, 3 skipped (duplicates), 0 errors"

### 3. View Quiz History
```bash
java -jar quiz-engine.jar history
java -jar quiz-engine.jar history --session-id <uuid> --review
java -jar quiz-engine.jar history --export json --start-date 2025-01-01
```
**Output:** Formatted table or full answer key with explanations

### 4. Clear Data
```bash
java -jar quiz-engine.jar clear --questions --confirm
java -jar quiz-engine.jar clear --history --all --confirm
```

---

## Success Criteria

### Functional Requirements
- ✓ Load 100+ random questions (WITHOUT correct answers visible)
- ✓ NEVER repeat question until all exhausted at current cycle
- ✓ Randomize answers per question
- ✓ Per-question and global timers
- ✓ Calculate and display score
- ✓ Persist session and responses to DB
- ✓ Import questions from markdown with explanations
- ✓ View quiz history summaries and full answer reviews
- ✓ Clear questions and history safely
- ✓ All CLI commands functional and documented

### Non-Functional Requirements
- ✓ Performance: Load 100 questions + display first in <1 second
- ✓ Usability: New user completes full workflow in <15 minutes
- ✓ Reliability: Graceful error handling, no crashes
- ✓ Maintainability: Clean code, SOLID principles, testable
- ✓ Compatibility: Java 11+, Windows/Mac/Linux

---

## Implementation Notes

- **Test-driven:** Write DAO tests first, then service layer
- **Defensive coding:** Validate all inputs, handle edge cases
- **Connection pooling:** Never leak connections (use try-with-resources)
- **Documentation:** Javadoc for public APIs
- **Build:** Use Gradle shadow plugin for fat JAR distribution
- **Future:** Add difficulty/section filtering, performance analytics, GUI (JavaFX)
