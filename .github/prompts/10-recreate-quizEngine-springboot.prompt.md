# Recreate: quiz-engine-springboot

> Self-contained specification for recreating the `quiz-engine-springboot` project from scratch.
> No prior context is required — every file, decision, and configuration detail is documented below.

---

## Table of Contents

1. [Project Structure](#1-project-structure)
2. [Language, Runtime, and Dependencies](#2-language-runtime-and-dependencies)
3. [Database Schema](#3-database-schema)
4. [CLI Commands](#4-cli-commands)
5. [Documentation](#5-documentation)
6. [Question File Formats](#6-question-file-formats)
7. [Unit Test Coverage](#7-unit-test-coverage)
8. [Scripts](#8-scripts)
9. [Docker Setup](#9-docker-setup)
10. [Architecture Decisions](#10-architecture-decisions)

---

## 1. Project Structure

```
quiz-engine-springboot/
├── src/
│   ├── main/
│   │   ├── java/com/quizengine/
│   │   │   ├── QuizEngineApplication.java        # @SpringBootApplication entry point; delegates to SpringApplication.run()
│   │   │   ├── cli/
│   │   │   │   ├── ClearCommand.java             # Picocli "clear" command — deletes questions/history/all from DB
│   │   │   │   ├── ConsoleFormatter.java         # ANSI-colored output helpers; holds ReviewItem record
│   │   │   │   ├── HistoryCommand.java           # Picocli "history" command — list, detail, export json/csv
│   │   │   │   ├── ImportCommand.java            # Picocli "import" command — --file or --dir flags
│   │   │   │   └── QuizCommand.java              # Picocli "quiz" command — interactive CLI quiz session
│   │   │   ├── config/
│   │   │   │   └── QuizEngineConfig.java         # @Configuration; registers CommonsRequestLoggingFilter bean
│   │   │   ├── controller/
│   │   │   │   └── QuizController.java           # Combined Thymeleaf MVC + REST controller (@Controller)
│   │   │   │                                     #   GET /  → index.html
│   │   │   │                                     #   GET /quiz → quiz.html
│   │   │   │                                     #   GET /history → history.html (paginated)
│   │   │   │                                     #   POST /api/quiz/start
│   │   │   │                                     #   POST /api/quiz/{sessionId}/answer
│   │   │   │                                     #   POST /api/quiz/{sessionId}/finalize
│   │   │   │                                     #   GET  /api/history (paginated)
│   │   │   │                                     #   GET  /api/history/{sessionId}
│   │   │   │                                     #   POST /api/import
│   │   │   │                                     #   GET  /api/questions/count
│   │   │   ├── entity/
│   │   │   │   ├── Question.java                 # @Entity "questions" table with usage-cycle tracking
│   │   │   │   ├── QuizResponse.java             # @Entity "quiz_responses" table; FK to session + question
│   │   │   │   └── QuizSession.java              # @Entity "quiz_sessions" table; one-to-many responses
│   │   │   ├── exception/
│   │   │   │   └── QuizEngineException.java      # Unchecked RuntimeException with message + cause constructors
│   │   │   ├── repository/
│   │   │   │   ├── QuestionRepository.java       # JpaRepository<Question, Long> with custom JPQL queries
│   │   │   │   ├── ResponseRepository.java       # JpaRepository<QuizResponse, Long>
│   │   │   │   └── SessionRepository.java        # JpaRepository<QuizSession, String> (PK is UUID string)
│   │   │   ├── service/
│   │   │   │   ├── HistoryService.java           # List/page sessions; export to JSON/CSV; SessionSummary record
│   │   │   │   ├── ImportService.java            # Parse + persist .md files (file, content string, or directory)
│   │   │   │   ├── QuizEngine.java               # Core engine: startNewSession, loadQuestions, checkAnswer,
│   │   │   │   │                                 #   recordAnswer, finalizeSession, cycle advancement
│   │   │   │   └── QuizService.java              # Orchestrator; holds in-memory ActiveQuiz map;
│   │   │   │                                     #   startQuiz, submitAnswer, finalizeQuiz, getSessionQuestions
│   │   │   └── util/
│   │   │       ├── AnswerShuffler.java           # Randomly reassigns A/B/C/D/E labels; finds correct letter
│   │   │       ├── MarkdownParser.java           # Parses both legacy and gh-200 (Answer Key table) formats
│   │   │       └── QuizUtils.java               # generateSessionId (UUID), calculatePercentage, formatDuration,
│   │   │                                         #   gradeResult (EXCELLENT/PASS/BORDERLINE/FAIL)
│   │   └── resources/
│   │       ├── application.yml                   # Default (dev) profile: SQLite quiz.db, create-drop DDL
│   │       ├── application-prod.yml              # Prod profile: SQLite /data/quiz.db, update DDL, Thymeleaf cache on
│   │       └── templates/
│   │           ├── index.html                    # Thymeleaf: stats dashboard (question count, sessions, best score)
│   │           ├── quiz.html                     # Thymeleaf + inline JS: full quiz flow via REST API calls
│   │           └── history.html                  # Thymeleaf: paginated session history table
├── src/
│   └── test/
│       ├── java/com/quizengine/
│       │   ├── repository/
│       │   │   └── QuestionRepositoryTest.java   # @SpringBootTest + H2; tests JPQL queries and cycle logic
│       │   ├── service/
│       │   │   ├── QuizEngineTest.java           # @ExtendWith(MockitoExtension) unit tests for QuizEngine
│       │   │   └── QuizServiceIntegrationTest.java # @SpringBootTest + H2; end-to-end service integration
│       │   └── util/
│       │       ├── AnswerShufflerTest.java        # Pure unit tests for shuffle and correct-letter detection
│       │       ├── MarkdownParserTest.java        # Tests both legacy and gh-200 formats, edge cases
│       │       └── QuizUtilsTest.java             # Tests percentage, duration formatting, grading
│       └── resources/
│           └── application-test.properties       # H2 in-memory config; overrides application.yml for tests
├── bin/                                          # Gradle-compiled class output (not committed; generated)
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar                    # Gradle wrapper executable JAR
│       └── gradle-wrapper.properties             # Wrapper config: Gradle 8.9-bin distribution URL
├── docs/
│   ├── README.md                                 # Full user-facing documentation (see Section 5)
│   └── architecture.md                           # Mermaid diagrams: sequence, class, ER, data flow
├── build.gradle.kts                              # Gradle build file (Kotlin DSL); plugins, deps, JaCoCo config
├── settings.gradle.kts                          # Root project name: "quiz-engine-springboot"
├── gradlew                                       # Unix Gradle wrapper script
├── gradlew.bat                                   # Windows Gradle wrapper script
├── Dockerfile                                    # Multi-stage: gradle:8-jdk17 builder → temurin:17-jre-alpine
├── docker-compose.yml                            # Services: quiz-engine-dev and quiz-engine-test
├── build.sh                                      # Bash: ./gradlew build
├── build.bat                                     # Windows CMD: gradlew.bat build
├── build.ps1                                     # PowerShell: .\gradlew.bat build
├── quiz.sh                                       # Bash: starts Spring Boot JAR (web server + CLI)
├── quiz.bat                                      # Windows CMD: starts Spring Boot JAR
├── quiz.ps1                                      # PowerShell: starts Spring Boot JAR
├── import.sh                                     # Bash: starts JAR and prints REST API import instructions
├── import.bat                                    # Windows CMD: same as import.sh
├── import.ps1                                    # PowerShell: same as import.sh
├── history.sh                                    # Bash: starts JAR and prints history URL
├── history.bat                                   # Windows CMD: same as history.sh
├── history.ps1                                   # PowerShell: same as history.sh
├── README.md                                     # Project root README (brief pointer to docs/README.md)
├── architecture.md                               # Symlink/copy of docs/architecture.md at project root
└── .gitignore                                    # Standard Gradle/.idea/build ignores
```

---

## 2. Language, Runtime, and Dependencies

### Runtime

| Property | Value |
|---|---|
| Language | Java 21 |
| Build System | Gradle 8.9 (wrapper included) |
| Build Script DSL | Kotlin (`build.gradle.kts`) |
| Root project name | `quiz-engine-springboot` |
| Group | `com.quizengine` |
| Version | `0.0.1-SNAPSHOT` |

### Gradle Plugins (`build.gradle.kts`)

```kotlin
plugins {
    java
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    jacoco
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
```

### Dependencies

```kotlin
dependencies {
    // Spring Boot starters (versions managed by Spring BOM via dependency-management plugin)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // Database
    implementation("org.xerial:sqlite-jdbc:3.44.1.0")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.4.4.Final")

    // CLI
    implementation("info.picocli:picocli-spring-boot-starter:4.7.5")

    // Code generation
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")  // JUnit 5 + Mockito + AssertJ
    testImplementation("com.h2database:h2")  // In-memory DB for tests
}
```

### Gradle Wrapper

```properties
# gradle/wrapper/gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.9-bin.zip
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
networkTimeout=10000
validateDistributionUrl=true
```

---

## 3. Database Schema

Schema is managed entirely by Hibernate (`ddl-auto: create-drop` in dev, `update` in prod). There are **no SQL migration files** — the schema is derived from JPA entity annotations.

### Table: `questions`

| Column | Type | Nullable | Notes |
|---|---|---|---|
| `id` | BIGINT | NOT NULL | PK, auto-generated identity |
| `question_text` | TEXT | NOT NULL | Full question body |
| `option_a` | TEXT | NOT NULL | Answer choice A |
| `option_b` | TEXT | NOT NULL | Answer choice B |
| `option_c` | TEXT | NOT NULL | Answer choice C |
| `option_d` | TEXT | NOT NULL | Answer choice D |
| `option_e` | TEXT | NULL | Optional fifth answer choice |
| `correct_answer` | VARCHAR(255) | NOT NULL | Single letter: A, B, C, D, or E |
| `explanation` | TEXT | NULL | Explanation shown after answer |
| `section` | VARCHAR(255) | NULL | Topic/domain tag (from gh-200 format header) |
| `difficulty` | VARCHAR(255) | NULL | e.g., "EASY", "MEDIUM", "HARD" |
| `source_file` | VARCHAR(255) | NULL | Filename the question was imported from |
| `usage_cycle` | INTEGER | NOT NULL | Default: 1. Incremented by `advanceCycle()` after all questions in cycle are used |
| `times_used` | INTEGER | NOT NULL | Default: 0. Incremented by `markQuestionUsed()` |
| `last_used_at` | TIMESTAMP | NULL | Set by `markQuestionUsed()` |
| `created_at` | TIMESTAMP | NOT NULL | Auto-set on insert (`@CreationTimestamp`), not updatable |

**Indexes:**
- `idx_section` on `(section)`
- `idx_difficulty` on `(difficulty)`
- `idx_usage_cycle` on `(usage_cycle)`

**Relationships:**
- One-to-many with `quiz_responses` via `question_id` FK (cascade REMOVE)

### Table: `quiz_sessions`

| Column | Type | Nullable | Notes |
|---|---|---|---|
| `session_id` | VARCHAR(36) | NOT NULL | PK; UUID string generated by `QuizUtils.generateSessionId()` |
| `started_at` | TIMESTAMP | NOT NULL | Auto-set on insert (`@CreationTimestamp`) |
| `ended_at` | TIMESTAMP | NULL | Set when `finalizeSession()` is called |
| `num_questions` | INTEGER | NOT NULL | Total questions in this session |
| `num_correct` | INTEGER | NULL | Default: 0. Updated on finalize |
| `percentage_correct` | DOUBLE | NULL | Default: 0.0. Updated on finalize |
| `time_taken_seconds` | INTEGER | NULL | Elapsed seconds from `started_at` to finalize |

**Indexes:**
- `idx_started_date` on `(started_at)`

**Relationships:**
- One-to-many with `quiz_responses` via FK `session_id` (cascade ALL, fetch EAGER)

### Table: `quiz_responses`

| Column | Type | Nullable | Notes |
|---|---|---|---|
| `id` | BIGINT | NOT NULL | PK, auto-generated identity |
| `session_id` | VARCHAR(36) | NULL | FK → `quiz_sessions.session_id` (LAZY fetch) |
| `question_id` | BIGINT | NULL | FK → `questions.id` (LAZY fetch) |
| `user_answer` | VARCHAR(255) | NOT NULL | User's letter choice, uppercased |
| `is_correct` | INTEGER | NULL | Default: 0. 1 = correct, 0 = incorrect |
| `time_taken_seconds` | INTEGER | NULL | Per-question elapsed time |

### Key JPQL Queries (`QuestionRepository`)

```java
// Returns the lowest usageCycle value across all questions (the "current cycle")
@Query("SELECT MIN(q.usageCycle) FROM Question q")
Integer findCurrentCycle();

// Returns all questions in the given cycle (shuffled in service layer)
@Query("SELECT q FROM Question q WHERE q.usageCycle = :cycle")
List<Question> findByUsageCycle(@Param("cycle") Integer cycle);

// Increments timesUsed and sets lastUsedAt for a single question
@Modifying(clearAutomatically = true)
@Query("UPDATE Question q SET q.timesUsed = q.timesUsed + 1, q.lastUsedAt = CURRENT_TIMESTAMP WHERE q.id = :id")
void markQuestionUsed(@Param("id") Long id);

// Advances cycle: increments usageCycle for all used questions in the current cycle
@Modifying
@Query("UPDATE Question q SET q.usageCycle = q.usageCycle + 1 WHERE q.timesUsed > 0 AND q.usageCycle = (SELECT MIN(q2.usageCycle) FROM Question q2)")
void advanceCycle();

// Counts questions in current cycle that have timesUsed = 0 (not yet presented)
@Query("SELECT COUNT(q) FROM Question q WHERE q.usageCycle = (SELECT MIN(q2.usageCycle) FROM Question q2) AND q.timesUsed = 0")
long countUnusedInCurrentCycle();
```

### Cycle Advancement Logic

After every `finalizeSession()` call, `QuizEngine` checks `countUnusedInCurrentCycle()`. If it returns 0 (all questions in the current cycle have been used at least once), `advanceCycle()` is called, bumping the `usage_cycle` of all used questions, effectively restarting the pool.

---

## 4. CLI Commands

The application uses **Picocli** integrated with Spring Boot via `picocli-spring-boot-starter:4.7.5`. Commands are Spring `@Component` beans discovered automatically.

The JAR is `build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar`.

> **Note:** In practice the `.sh`/`.bat`/`.ps1` scripts simply start the Spring Boot web server — the Picocli CLI and the web server share the same JAR. The CLI commands below are invoked by passing arguments to the JAR.

---

### `import` — Import questions from Markdown files

```
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar import [OPTIONS]
```

| Option | Short | Description | Default |
|---|---|---|---|
| `--file` | `-f` | Path to a single Markdown file | — |
| `--dir` | `-d` | Path to a directory of `.md` files (non-recursive) | — |
| `--help` | `-h` | Print help | — |

**Behavior:**
- Exactly one of `--file` or `--dir` must be provided; otherwise prints an error.
- Calls `MarkdownParser.parseFile()` or `MarkdownParser.parseContent()` then `QuestionRepository.save()`.
- Prints `✓ Imported N questions, skipped M` on success (green), or `✗ ...` on failure (red).

**Example invocations:**

```bash
# Import a single file
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar import --file ./questions.md

# Import a whole directory
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar import --dir ./quiz-source-material/

# Expected output (success)
✓ Imported 42 questions, skipped 0
```

---

### `quiz` — Take an interactive CLI quiz session

```
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar quiz [OPTIONS]
```

| Option | Short | Default | Description |
|---|---|---|---|
| `--questions` | `-n` | `10` | Number of questions in the session |
| `--help` | `-h` | — | Print help |

**Behavior:**
1. Calls `QuizService.startQuiz(numQuestions)` which selects questions from the current usage cycle, shuffled randomly.
2. For each question, shuffles the answer options via `AnswerShuffler.shuffleAnswers()` so labels A/B/C/D are randomized.
3. Prompts `Your answer (A/B/C/D):` and reads from stdin.
4. After all questions, prints score and full answer review with explanations.

**Example invocations:**

```bash
# Default 10-question quiz
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar quiz

# 5-question quiz
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar quiz --questions 5
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar quiz -n 5
```

**Example output:**

```
════════════════════════════════════════════════════════════
                  GH-200 CERTIFICATION QUIZ
════════════════════════════════════════════════════════════

Question 1/5
Which trigger event runs a workflow on a schedule?

  A) on: timer
  B) on: schedule
  C) on: cron
  D) on: workflow_dispatch

Your answer (A/B/C/D): B

...

════════════════════════════════════════════════════════════
                       QUIZ RESULTS
════════════════════════════════════════════════════════════
Score: 4/5 (80.0%)
Grade: PASS

════════════════════════════════════════════════════════════
                      ANSWER REVIEW
════════════════════════════════════════════════════════════
Q1: Which trigger event runs a workflow on a schedule?
  Your answer: B — on: schedule
```

**Grade thresholds (in `QuizUtils.gradeResult`):**

| Score | Grade |
|---|---|
| ≥ 90% | EXCELLENT |
| ≥ 75% | PASS |
| ≥ 60% | BORDERLINE |
| < 60% | FAIL |

---

### `history` — View or export quiz history

```
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar history [OPTIONS]
```

| Option | Short | Description |
|---|---|---|
| `--session-id` | `-s` | Show details for a specific session UUID |
| `--review` | `-r` | Show detailed answer review for the session |
| `--export` | — | Export all sessions: `json` or `csv` |
| `--help` | `-h` | Print help |

**Behavior (no flags):** Prints a summary table of all sessions (Total Sessions, Avg Score, Best Score), then lists each session with Session ID, Date, Q count, Correct count, Score%.

**Example invocations:**

```bash
# List all sessions
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar history

# Session detail
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar history --session-id 550e8400-e29b-41d4-a716-446655440000

# Export to JSON (file: quiz-history-<timestamp>.json)
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar history --export json

# Export to CSV (file: quiz-history-<timestamp>.csv)
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar history --export csv
```

**Example output (list):**

```
════════════════════════════════════════════════════════════
                       QUIZ HISTORY
════════════════════════════════════════════════════════════
Total Sessions: 3 | Avg Score: 78.3% | Best: 90.0%

Session ID                              Date                     Q    Cor   Score
────────────────────────────────────────────────────────────────────────────────
550e8400-e29b-41d4-a716-446655440000    2024-03-01T10:15:00     10      9   90.0%
...
```

**JSON export format:**

```json
[
  {
    "session_id": "550e8400-e29b-41d4-a716-446655440000",
    "date": "2024-03-01T10:15:00",
    "score": 9,
    "total_questions": 10,
    "percentage_correct": 90.00
  }
]
```

**CSV export format:**

```
session_id,date,score,total_questions,percentage_correct
550e8400-e29b-41d4-a716-446655440000,2024-03-01T10:15:00,9,10,90.00
```

---

### `clear` — Delete stored data

```
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar clear [OPTIONS]
```

| Option | Description |
|---|---|
| `--questions` | Delete all imported questions |
| `--history` | Delete all quiz session history (responses + sessions) |
| `--all` | Delete everything (questions AND history) |
| `--confirm` | Skip the interactive "Are you sure? (yes/no)" prompt |

**Behavior:** Without `--confirm`, prompts `Are you sure? This cannot be undone. (yes/no):` and requires literal `yes` to proceed. At least one of `--questions`, `--history`, or `--all` must be provided.

**Example invocations:**

```bash
# Clear questions with confirmation prompt
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar clear --questions

# Clear everything without prompt
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar clear --all --confirm

# Expected output
✓ Quiz history cleared.
✓ All questions cleared.
```

---

### Web Interface Routes

| Route | Method | Description |
|---|---|---|
| `GET /` | Web | Home page: stats dashboard (question count, sessions taken, best score) |
| `GET /quiz` | Web | Quiz setup page; starts a quiz via REST |
| `GET /history` | Web | Paginated history table (`?page=0&size=10`) |
| `GET /api/questions/count` | REST | `{ "total": N, "currentCycle": N }` |
| `POST /api/quiz/start` | REST | `{ "numQuestions": 10 }` → `{ "sessionId", "numQuestions", "questions": [...] }` |
| `POST /api/quiz/{sessionId}/answer` | REST | `{ "questionIndex": 0, "answer": "A", "timeTaken": 0 }` → `{ "correct", "correctAnswer", "explanation" }` |
| `POST /api/quiz/{sessionId}/finalize` | REST | → `{ "sessionId", "numCorrect", "numQuestions", "percentageCorrect", "timeTakenSeconds" }` |
| `GET /api/history` | REST | Paginated: `?page=0&size=10` → `{ "sessions", "totalPages", "currentPage", "totalSessions" }` |
| `GET /api/history/{sessionId}` | REST | Session detail object |
| `POST /api/import` | REST | `{ "content": "<markdown>", "source": "filename.md" }` → `{ "imported", "skipped", "message" }` |

---

## 5. Documentation

### `docs/README.md` — Full documentation structure

The `docs/README.md` file is the canonical user documentation. It contains the following top-level sections (with `##` headings):

1. **Overview** — Brief description: "A quiz engine for GH-200 GitHub Actions certification preparation built with Spring Boot 3.2. Provides both a CLI (Picocli) and full Thymeleaf web UI at `http://localhost:8080`. Also exposes a REST API."
   - **Features** subsection (bullet list): Web UI, REST API, CLI, Dual database (H2 test / SQLite prod), Non-repetition cycle tracking, Markdown import, JaCoCo 90% coverage, Gradle + Spring Data JPA

2. **Project Structure** — ASCII tree of all directories and files with inline descriptions

3. **Prerequisites** — Table of Java JDK 17+, Gradle via wrapper, Docker 20.10+ (optional); verification commands

4. **Installation** — `./gradlew build`, `./gradlew bootRun`, `java -jar build/libs/quiz-engine-*.jar`

5. **Script Reference** — Subsections for Build Scripts, Quiz Scripts, Import Scripts, History Scripts; each with platform variants and example invocations

6. **CLI Commands** — `import`, `quiz`, `history`, `clear`, Global Options table

7. **Web Interface** — Table of pages (Home `/`, Quiz `/quiz`, Results `/quiz/results`, History `/history`)

8. **REST API Reference** — Subsections: Import Questions, Start a Quiz Session, Submit an Answer, Finish Session, Get History — each with HTTP verb, path, request/response body examples

9. **Docker Setup** — Building, Running Interactively, Environment Variables table, Docker Compose Services table

10. **Question File Format** — Full example of both formats (see Section 6 below for exact samples)

11. **Configuration** — `application.properties` (production) and `application-test.properties` code blocks

12. **Testing** — `./gradlew test`, `./gradlew jacocoTestReport`, `./gradlew jacocoTestCoverageVerification`

13. **Dependencies** — Table mapping each dependency to its purpose

14. **Architecture** — Pointer to `architecture.md`

### `docs/architecture.md` — Architecture diagrams

Contains four Mermaid diagrams:

1. **Sequence Diagram — Quiz Flow (Web + REST)** — Shows User → Browser/REST Client → Controller → QuizService → Repositories → DB for both web and REST flows

2. **Class Diagram** — Full class diagram of all entities, repositories, services, controllers, and CLI with relationships

3. **Entity Relationship Diagram** — ER diagram for QUESTIONS, QUIZ_SESSIONS, QUIZ_RESPONSES tables

4. **Data Flow Diagram** — Flowchart showing all input sources (Browser, REST Client, CLI stdin, Markdown file) flowing through Controller → Service → Repository → DB layers

---

## 6. Question File Formats

The `MarkdownParser` auto-detects the format: if the file contains `## Answer Key`, it uses the **gh-200 format**; otherwise it uses the **legacy format**.

---

### Format 1: Legacy Format

**Detection:** No `## Answer Key` section present.

**Structure:**

```markdown
## <Question Text>

- A. <Option A text>
- B. <Option B text>
- C. <Option C text>
- D. <Option D text>
- E. <Optional fifth option>   ← optional

**Answer:** <A|B|C|D|E>
**Explanation:** <explanation text>
```

**Regex pattern (simplified):** Matches `## <text>` followed by `- A.`, `- B.`, `- C.`, `- D.` (and optional `- E.`), then `**Answer:** <letter>`. Options can use `.` or `)` after the letter.

**Sample Question 1 (from `MarkdownParserTest.LEGACY_SINGLE`):**

```markdown
## What is GitHub Actions?

- A. A CI/CD platform
- B. A version control system
- C. A package manager
- D. A code editor

**Answer:** A
**Explanation:** GitHub Actions is a CI/CD and automation platform.
```

**Sample Question 2 (from `MarkdownParserTest.LEGACY_FIVE_OPTIONS`):**

```markdown
## Question 1: Best practice for secrets?

- A. Hardcode them
- B. Use environment variables
- C. Use GitHub Secrets
- D. Store in a file
- E. None of the above

**Answer:** C
```

**Parsed result:**
- `questionText`: text after `## ` (strips "Question N: " prefix automatically via regex)
- `optionA`–`optionD`: text after `- A.` etc. (whitespace stripped)
- `optionE`: present only if fifth option line exists
- `correctAnswer`: uppercased letter
- `explanation`: extracted from `**Explanation:** ...` up to next `##` or end of file
- `section`: `null`
- `difficulty`: `null`
- `sourceFile`: filename passed to parser

---

### Format 2: gh-200 Format (Answer Key Table)

**Detection:** File contains `## Answer Key` section.

**Structure:**

```markdown
# <Title>

### Question <N> — <Section/Domain Name>

**Difficulty**: <EASY|MEDIUM|HARD>
**Answer Type**: Single Answer

> **Question:**
> <Question text here>

- A) <Option A>
- B) <Option B>
- C) <Option C>
- D) <Option D>

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| N  | <letter>  | <explanation text> | <source> | <difficulty> |
```

**Rules:**
- Questions with multiple answers in the Answer Key (e.g., `A, B, C`), or `many`/`none` answers, are **skipped**.
- Questions with `**Answer Type**` field present but not containing "single" are **skipped**.
- Must have options A, B, C, D to be included. Option E is optional.
- Question text is extracted from `**Question:**` label content, or falls back to first non-blank non-metadata line.

**Sample Question 1 (from `MarkdownParserTest.GH200_CONTENT`):**

```markdown
### Question 1 — Domain: Continuous Integration

**Difficulty**: EASY
**Answer Type**: Single Answer

> **Scenario:**
> A developer needs to automate tests on every commit.
>
> **Question:**
> Which GitHub feature enables CI/CD workflows?

- A) GitHub Actions
- B) GitHub Pages
- C) GitHub Packages
- D) GitHub Copilot

---

## Answer Key

| Q# | Answer(s) | Explanation                            | Source  | Difficulty |
|----|-----------|----------------------------------------|---------|------------|
| 1  | A         | GitHub Actions is the CI/CD platform.  | GH Docs | EASY       |
```

**Parsed result:**
- `questionText`: "Which GitHub feature enables CI/CD workflows?"
- `optionA`: "GitHub Actions"
- `optionB`: "GitHub Pages"
- `optionC`: "GitHub Packages"
- `optionD`: "GitHub Copilot"
- `correctAnswer`: "A"
- `explanation`: "GitHub Actions is the CI/CD platform."
- `section`: "Domain: Continuous Integration"
- `difficulty`: "EASY"

**Sample Question 2 (from `MarkdownParserTest.GH200_CONTENT`):**

```markdown
### Question 2 — Domain: Security

**Difficulty**: MEDIUM
**Answer Type**: Single Answer

> **Question:**
> Where should you store sensitive credentials in GitHub Actions?

- A) In the workflow YAML file
- B) In a public repository
- C) In GitHub Secrets
- D) In a README file

---

## Answer Key

| Q# | Answer(s) | Explanation                                              | Source  | Difficulty |
|----|-----------|----------------------------------------------------------|---------|------------|
| 2  | C         | Secrets are stored in repo settings under Secrets.       | GH Docs | MEDIUM     |
```

**Parsed result:**
- `questionText`: "Where should you store sensitive credentials in GitHub Actions?"
- `correctAnswer`: "C"
- `optionC`: "In GitHub Secrets"
- `difficulty`: "MEDIUM"
- `section`: "Domain: Security"

**Example skipped (multi-answer):**

```markdown
### Question 3 — Domain: Multiple

**Difficulty**: HARD
**Answer Type**: Multiple Answer

> **Question:**
> Which of the following are valid trigger events?

- A) push
- B) pull_request
- C) schedule
- D) deploy

## Answer Key
| 3  | A, B, C   | Multiple valid triggers. | GH Docs | HARD |
```

This question is **skipped** because the answer key contains `A, B, C` (multiple answers) AND the `**Answer Type**` is "Multiple Answer" (does not contain "single").

---

## 7. Unit Test Coverage

### Enforcement Tool

**JaCoCo** (Java Code Coverage Library), integrated as a Gradle plugin.

### Threshold Configuration

**File:** `build.gradle.kts`

**Task:** `jacocoTestCoverageVerification`

**Property:** `minimum` (set on the `limit` block inside a `rule`)

**Value: `0.90`** (90% minimum instruction coverage)

```kotlin
tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            excludes = listOf(
                "com.quizengine.QuizEngineApplication",   // entry point excluded
                "com.quizengine.cli.*",                   // all CLI classes excluded
                "com.quizengine.config.*"                 // config classes excluded
            )
            limit {
                minimum = "0.90".toBigDecimal()           // ← 90% threshold
            }
        }
    }
}
```

The threshold applies to **instruction coverage** (the JaCoCo default metric) across all non-excluded classes.

### Test Execution

```kotlin
tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)   // report always generated after tests
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)           // build/reports/jacoco/test/html/index.html
        xml.required.set(true)            // build/reports/jacoco/test/jacocoTestReport.xml
    }
}
```

### Test Suites

| Test Class | Type | What it covers |
|---|---|---|
| `MarkdownParserTest` | Unit (no Spring context) | Legacy format parsing, gh-200 format parsing, Answer Key table parsing, multi-answer skipping, format dispatch |
| `AnswerShufflerTest` | Unit (no Spring context) | `shuffleAnswers()` preserves all options, handles option E, `findCorrectLetter()` works after shuffle |
| `QuizUtilsTest` | Unit (no Spring context) | `calculatePercentage`, `generateSessionId`, `gradeResult`, `formatDuration` all edge cases |
| `QuizEngineTest` | Unit (Mockito) | `checkAnswer` case-insensitive, null handling; `loadQuestions` repo delegation; `startNewSession` empty/happy path; `finalizeSession` not-found exception |
| `QuizServiceIntegrationTest` | Integration (`@SpringBootTest` + H2) | Full service flow: startQuiz, submitAnswer, finalizeQuiz, isSessionActive |
| `QuestionRepositoryTest` | Integration (`@SpringBootTest` + H2 + `@Transactional`) | `findByUsageCycle`, `findCurrentCycle`, `markQuestionUsed`, `countUnusedInCurrentCycle` |

### Running Tests

```bash
# Run tests only
./gradlew test

# Run tests + generate JaCoCo HTML report
./gradlew test jacocoTestReport

# Enforce 90% threshold (fails build if below)
./gradlew jacocoTestCoverageVerification

# All in one
./gradlew build   # includes test + jacocoTestReport
```

---

## 8. Scripts

All scripts are in the **project root** directory. The `.bat` scripts hard-code a `JAVA_HOME` path (`C:\Users\Pete\.jdks\corretto-21.0.2`) which should be updated for the target machine. All scripts change directory to `$PSScriptRoot` / `%~dp0` / `$(dirname "$0")` before running — they can be invoked from any working directory.

The JAR filename is always: `build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar`

---

### Build Scripts

#### `build.sh` — Bash (macOS / Linux)

**Location:** `quiz-engine-springboot/build.sh`
**Purpose:** Compile, test, and package the JAR using `./gradlew build`
**Invocation:**

```bash
./build.sh
```

**Full content:**

```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Spring Boot - Build ==="
echo "Building JAR with Gradle wrapper..."
chmod +x gradlew
./gradlew build
echo "Build successful! JAR: build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar"
```

---

#### `build.bat` — Windows CMD

**Location:** `quiz-engine-springboot/build.bat`
**Purpose:** Same as `build.sh` for Windows CMD
**Invocation:**

```bat
build.bat
```

**Full content:**

```bat
@echo off
setlocal
cd /d "%~dp0"
set "JAVA_HOME=C:\Users\Pete\.jdks\corretto-21.0.2"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo === Quiz Engine Spring Boot - Build ===
echo Building JAR with Gradle wrapper...
gradlew.bat build
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b %ERRORLEVEL%
)
echo Build successful! JAR: build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar
```

---

#### `build.ps1` — PowerShell

**Location:** `quiz-engine-springboot/build.ps1`
**Purpose:** Same as `build.sh` for PowerShell
**Invocation:**

```powershell
.\build.ps1
```

**Full content:**

```powershell
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Spring Boot - Build ===" -ForegroundColor Cyan
Write-Host "Building JAR with Gradle wrapper..." -ForegroundColor Yellow
.\gradlew.bat build
Write-Host "Build successful! JAR: build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar" -ForegroundColor Green
```

---

### Quiz Scripts

#### `quiz.sh` — Bash

**Location:** `quiz-engine-springboot/quiz.sh`
**Purpose:** Start the Spring Boot server (web + CLI mode)
**Invocation:**

```bash
./quiz.sh
```

**Full content:**

```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Spring Boot - Start Quiz ==="
if [ ! -f "build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar" ]; then
    echo "JAR not found. Run build.sh first."
    exit 1
fi
echo "Starting Spring Boot application for quiz..."
echo ""
echo "Once the server starts:"
echo "  Web UI:    http://localhost:8080"
echo "  Start API: POST http://localhost:8080/api/quiz/start"
echo '  Body:      {"numQuestions": 10}'
echo ""
echo "Press Ctrl+C to stop the server when done."
echo ""
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar
```

#### `quiz.bat` / `quiz.ps1`

Same purpose; `quiz.bat` is CMD variant, `quiz.ps1` is PowerShell variant. Both check for JAR existence and print the same startup instructions before launching the JAR.

---

### Import Scripts

#### `import.sh` — Bash

**Location:** `quiz-engine-springboot/import.sh`
**Purpose:** Start the Spring Boot server and print REST API instructions for importing questions
**Invocation:**

```bash
./import.sh
```

**Full content:**

```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Spring Boot - Import Questions ==="
if [ ! -f "build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar" ]; then
    echo "JAR not found. Run build.sh first."
    exit 1
fi
echo "Starting Spring Boot application for question import..."
echo ""
echo "Once the server starts, import questions via the REST API:"
echo "  POST http://localhost:8080/api/import"
echo "  Body: {\"content\": \"<markdown content>\", \"source\": \"filename.md\"}"
echo ""
echo "Or visit the web interface at: http://localhost:8080"
echo "Press Ctrl+C to stop the server when done."
echo ""
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar
```

#### `import.bat` / `import.ps1`

Same purpose; CMD and PowerShell variants. `import.bat` also shows a `curl` example:

```bat
echo   curl -X POST http://localhost:8080/api/import ^
echo        -H "Content-Type: application/json" ^
echo        -d "{\"content\": \"...\", \"source\": \"questions.md\"}"
```

---

### History Scripts

#### `history.sh` — Bash

**Location:** `quiz-engine-springboot/history.sh`
**Purpose:** Start the Spring Boot server and print history API/Web URLs
**Invocation:**

```bash
./history.sh
```

**Full content:**

```bash
#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Spring Boot - View History ==="
if [ ! -f "build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar" ]; then
    echo "JAR not found. Run build.sh first."
    exit 1
fi
echo "Starting Spring Boot application to view history..."
echo ""
echo "Once the server starts, view history via:"
echo "  REST API: http://localhost:8080/api/history"
echo "  Web UI:   http://localhost:8080"
echo ""
echo "Press Ctrl+C to stop the server when done."
echo ""
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar
```

#### `history.bat` / `history.ps1`

Same purpose; CMD and PowerShell variants.

---

## 9. Docker Setup

### `Dockerfile` — Multi-Stage Build

**Full content:**

```dockerfile
# Build stage
FROM gradle:8-jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN addgroup -g 1000 springuser && adduser -D -u 1000 -G springuser springuser
RUN chown -R springuser:springuser /app
USER springuser

ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -Dserver.port=8080"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Build stage details:**
- Base image: `gradle:8-jdk17`
- Copies entire project context into `/app`
- Runs `gradle build -x test --no-daemon` (skips tests for faster image build)

**Runtime stage details:**
- Base image: `eclipse-temurin:17-jre-alpine` (minimal JRE)
- Copies built JAR as `app.jar`
- Creates non-root user `springuser` (UID/GID 1000) for security
- `JAVA_OPTS`: G1GC, 75% RAM limit, port 8080
- Exposes port 8080

**Commands:**

```bash
# Build image
docker build -t quiz-engine-spring:latest .

# Run with volume for persistent SQLite DB
docker run -it \
  -p 8080:8080 \
  -v quiz-spring-data:/data \
  -e SPRING_PROFILES_ACTIVE=prod \
  quiz-engine-spring:latest
```

---

### `docker-compose.yml` — Service Definitions

**Full content:**

```yaml
version: '3.8'

services:
  quiz-engine:
    build: .
    container_name: quiz-engine-dev
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
    volumes:
      - quiz-data:/data

  quiz-engine-test:
    build: .
    container_name: quiz-engine-test
    volumes:
      - .:/app
      - gradle-cache:/root/.gradle
    working_dir: /app
    command: gradle test --no-daemon
    environment:
      - GRADLE_OPTS=-Dorg.gradle.daemon=false
      - SPRING_PROFILES_ACTIVE=test

volumes:
  quiz-data:
  gradle-cache:
```

**Service: `quiz-engine`**
- Builds from local Dockerfile
- Container name: `quiz-engine-dev`
- Port: `8080:8080`
- Environment: `SPRING_PROFILES_ACTIVE=dev`
- Volume: `quiz-data` named volume → `/data` (persists SQLite DB across container restarts)

**Service: `quiz-engine-test`**
- Builds from local Dockerfile
- Container name: `quiz-engine-test`
- Mounts project root as `/app` (for source access)
- Mounts `gradle-cache` for faster repeated builds
- Command overridden to: `gradle test --no-daemon`
- Environment: `GRADLE_OPTS=-Dorg.gradle.daemon=false`, `SPRING_PROFILES_ACTIVE=test`

**Named Volumes:**
- `quiz-data` — persists the SQLite database file at `/data/quiz.db` (prod profile)
- `gradle-cache` — caches Gradle dependencies between test runs

**Environment Variables Reference:**

| Variable | Default | Description |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | (none) | `dev` uses `quiz.db` in CWD; `prod` uses `/data/quiz.db`; `test` uses H2 in-memory |
| `JAVA_OPTS` | `-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -Dserver.port=8080` | JVM tuning; set in Dockerfile ENV |
| `GRADLE_OPTS` | (none) | Used in test service to disable daemon |

**Docker Compose commands:**

```bash
# Start web server
docker-compose up quiz-engine

# Run tests in container
docker-compose up quiz-engine-test

# Build and start in background
docker-compose up -d quiz-engine

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

---

## 10. Architecture Decisions

### 1. Repository Pattern via Spring Data JPA

All database access is routed through three `JpaRepository` interfaces:
- `QuestionRepository` — extends `JpaRepository<Question, Long>`
- `SessionRepository` — extends `JpaRepository<QuizSession, String>` (String PK = UUID)
- `ResponseRepository` — extends `JpaRepository<QuizResponse, Long>`

Custom JPQL queries are annotated with `@Query`. No raw SQL is used. Hibernate generates DDL automatically — there are no SQL migration files (Flyway/Liquibase not used).

### 2. Two-Layer Service Architecture

**`QuizEngine`** is the low-level engine: it owns the core logic of starting sessions, loading questions, checking answers, recording responses, and advancing the usage cycle. It works directly with repositories.

**`QuizService`** is the orchestrator: it holds an in-memory `ConcurrentHashMap<String, ActiveQuiz>` of active (in-progress) quiz sessions and delegates to `QuizEngine`. This separation allows the controller to call a simple API (`startQuiz`, `submitAnswer`, `finalizeQuiz`) without knowing quiz state management details.

### 3. Dual-Interface (Web + CLI) via Single JAR

The same Spring Boot JAR provides both a Thymeleaf web UI (port 8080) and a Picocli CLI. This is achieved via `picocli-spring-boot-starter` which integrates Picocli commands as Spring `@Component` beans. The `QuizEngineApplication` entry point runs `SpringApplication.run()` which starts the embedded Tomcat server — CLI commands are only invoked if passed as command-line arguments.

### 4. ORM-Based Schema (No Migrations)

Hibernate manages the schema via `ddl-auto`:
- `create-drop` in dev (schema created on startup, dropped on shutdown)
- `update` in prod (schema updated to match entities on startup)
- `create-drop` in test (clean state for every test run)

This means schema changes are made by editing entity classes — no migration scripts to maintain.

### 5. Non-Repetition Cycle Tracking

Questions avoid repetition using two integer columns (`usage_cycle`, `times_used`) on the `Question` entity:
- Questions are loaded by fetching only those in the **minimum** `usage_cycle` value (`findCurrentCycle()` returns `MIN(usageCycle)`).
- After each answer, `markQuestionUsed()` increments `times_used`.
- After `finalizeSession()`, if `countUnusedInCurrentCycle()` returns 0, `advanceCycle()` increments `usage_cycle` for all used questions in the current cycle, effectively creating a new "round" of questions.

This ensures the user sees all questions before any repeats.

### 6. Answer Shuffling

`AnswerShuffler.shuffleAnswers()` randomly reassigns A/B/C/D/E labels to answer text at display time. The correct answer is stored as a letter in the DB, but `findCorrectLetter()` maps the stored letter back to the answer text and then finds the new letter in the shuffled map. This prevents users from memorizing letter positions.

### 7. Dual Database Strategy (SQLite + H2)

- **Production/Dev:** SQLite via `org.xerial:sqlite-jdbc` with `org.hibernate.community.dialect.SQLiteDialect`
- **Tests:** H2 in-memory via `spring.profiles.test` activated by `@TestPropertySource`

Tests never touch the SQLite file — they use a fresh H2 database created per test class (`ddl-auto: create-drop`).

### 8. Constructor Injection (No Field Injection)

All Spring beans use **constructor injection** (not `@Autowired` field injection). This is enforced consistently across all controllers, services, repositories, and CLI commands. Benefits: immutable fields, easy unit testing with `new MyService(mockRepo)`.

### 9. Lombok for Entity Boilerplate

All three entities (`Question`, `QuizSession`, `QuizResponse`) use Lombok:
- `@Data` — getters, setters, toString, equals, hashCode
- `@Builder` — builder pattern with `@Builder.Default` for default values (`usageCycle = 1`, `timesUsed = 0`, etc.)
- `@NoArgsConstructor` / `@AllArgsConstructor` — required by JPA and builder respectively

### 10. Single Controller with Mixed Responsibilities

`QuizController` is annotated `@Controller` (not `@RestController`) and handles both:
- Thymeleaf MVC routes (returning view names like `"index"`, `"quiz"`, `"history"`)
- REST endpoints (returning `ResponseEntity<Map<String, Object>>` with `@ResponseBody`)

This consolidates all HTTP handling into one class. The REST endpoints return plain `Map<String, Object>` DTOs (no dedicated DTO classes) to minimize boilerplate.

### 11. Thymeleaf Templates with Inline JavaScript

The `quiz.html` template is essentially a single-page application: it uses Thymeleaf only for the initial server-rendered setup view (question count, max questions input), then uses inline JavaScript `fetch()` calls to the REST API for the entire quiz flow (`/api/quiz/start`, `/api/quiz/{id}/answer`, `/api/quiz/{id}/finalize`). This hybrid approach avoids full-page reloads during the quiz while keeping dependencies minimal (no frontend build system).
