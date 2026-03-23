# Java Spring Boot/JPA Quiz Engine for GH-200 Certification

## System Architecture Overview

### Directory Structure
```
quiz-engine/
├── src/
│   ├── main/
│   │   ├── java/com/quizengine/
│   │   │   ├── QuizEngineApplication.java     # Spring Boot entry point
│   │   │   ├── entity/
│   │   │   │   ├── Question.java              # JPA entity
│   │   │   │   ├── QuizSession.java           # JPA entity
│   │   │   │   └── QuizResponse.java          # JPA entity
│   │   │   ├── repository/
│   │   │   │   ├── QuestionRepository.java    # Spring Data JPA
│   │   │   │   ├── SessionRepository.java     # Spring Data JPA
│   │   │   │   └── ResponseRepository.java    # Spring Data JPA
│   │   │   ├── service/
│   │   │   │   ├── QuizEngine.java            # Core quiz logic
│   │   │   │   ├── QuizService.java           # Business logic
│   │   │   │   ├── HistoryService.java        # History queries
│   │   │   │   └── ImportService.java         # Markdown import logic
│   │   │   ├── util/
│   │   │   │   ├── MarkdownParser.java        # MD file parsing
│   │   │   │   ├── AnswerShuffler.java        # Answer randomization
│   │   │   │   └── QuizUtils.java             # Helpers
│   │   │   ├── cli/
│   │   │   │   ├── QuizCommand.java           # Picocli @Command
│   │   │   │   ├── ImportCommand.java         # Picocli @Command
│   │   │   │   ├── HistoryCommand.java        # Picocli @Command
│   │   │   │   ├── ClearCommand.java          # Picocli @Command
│   │   │   │   └── ConsoleFormatter.java      # Pretty printing
│   │   │   ├── config/
│   │   │   │   └── QuizEngineConfig.java      # Spring configuration
│   │   │   └── exception/
│   │   │       └── QuizEngineException.java   # Custom exception
│   │   └── resources/
│   │       ├── application.yml                # Spring Boot config
│   │       ├── application-h2.yml             # H2 profile (testing)
│   │       └── schema.sql                     # SQLite schema
│   └── test/
│       └── java/com/quizengine/
│           ├── service/QuizEngineTest.java
│           ├── repository/QuestionRepositoryTest.java
│           └── util/AnswerShufflerTest.java
├── build.gradle.kts                          # Gradle build configuration
├── settings.gradle.kts                       # Gradle project settings
├── gradlew                                   # Gradle wrapper (Unix/Mac)
├── gradlew.bat                               # Gradle wrapper (Windows)
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── README.md                                 # Setup, usage docs
└── .gitignore
```

### Database Schema (Managed by JPA/Spring)

#### Question Entity
```java
@Entity
@Table(name = "questions", indexes = {
    @Index(name = "idx_section", columnList = "section"),
    @Index(name = "idx_difficulty", columnList = "difficulty"),
    @Index(name = "idx_usage_cycle", columnList = "usage_cycle")
})
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String questionText;

    @Column(nullable = false)
    private String optionA;

    @Column(nullable = false)
    private String optionB;

    @Column(nullable = false)
    private String optionC;

    @Column(nullable = false)
    private String optionD;

    @Column
    private String optionE;

    @Column(nullable = false)
    private String correctAnswer;

    @Column
    private String explanation;

    @Column
    private String section;

    @Column
    private String difficulty;

    @Column
    private String sourceFile;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private Integer usageCycle = 1;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer timesUsed = 0;

    @Column
    private LocalDateTime lastUsedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<QuizResponse> responses;
}
```

#### QuizSession Entity
```java
@Entity
@Table(name = "quiz_sessions", indexes = {
    @Index(name = "idx_started_date", columnList = "started_at")
})
public class QuizSession {
    @Id
    @Column(length = 36)
    private String sessionId;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime endedAt;

    @Column(nullable = false)
    private Integer numQuestions;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer numCorrect = 0;

    @Column(columnDefinition = "REAL DEFAULT 0.0")
    private Double percentageCorrect = 0.0;

    @Column
    private Integer timeTakenSeconds;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<QuizResponse> responses;
}
```

#### QuizResponse Entity
```java
@Entity
@Table(name = "quiz_responses")
public class QuizResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", referencedColumnName = "sessionId")
    private QuizSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    @Column(nullable = false)
    private String userAnswer;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer isCorrect = 0;

    @Column
    private Integer timeTakenSeconds;
}
```

---

## Implementation Plan

### Phase 1: Spring Boot Setup & JPA Configuration
**Timeline:** 1.5-2 hours

**Objective:** Initialize Spring Boot project, configure JPA/Hibernate, create entities.

**Tasks:**

1. **Generate Gradle Project:**
   - Run `gradle init --type java-application --dsl kotlin --test-framework junit-jupiter`
   - Configure for Spring Boot 3.2.x with Maven Central repository
   - Java version: 17+

2. **Create `build.gradle.kts` Configuration:**
   ```kotlin
   plugins {
       java
       id("org.springframework.boot") version "3.2.3"
       id("io.spring.dependency-management") version "1.1.4"
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
       // Spring Boot Starters
       implementation("org.springframework.boot:spring-boot-starter-data-jpa")
       implementation("org.springframework.boot:spring-boot-starter-web")

       // SQLite Driver
       implementation("org.xerial:sqlite-jdbc:3.44.0.0")

       // Hibernate Dialect for SQLite
       implementation("com.github.gwenn:sqlite-dialect:0.1.2")

       // Picocli for CLI
       implementation("info.picocli:picocli-spring-boot-starter:4.7.5")

       // Lombok (optional)
       compileOnly("org.projectlombok:lombok")
       annotationProcessor("org.projectlombok:lombok")

       // JUnit 5 & Spring Test
       testImplementation("org.springframework.boot:spring-boot-starter-test")
   }

   tasks.test {
       useJUnitPlatform()
   }
   ```

3. **Create `application.yml`:**
   ```yaml
   spring:
     application:
       name: quiz-engine

     datasource:
       url: jdbc:sqlite:quiz.db
       driver-class-name: org.sqlite.JDBC
       hikari:
         maximum-pool-size: 10

     jpa:
       hibernate:
         ddl-auto: validate
       properties:
         hibernate.dialect: org.hibernate.community.dialect.SQLiteDialect
         hibernate.format_sql: true
         hibernate.use_sql_comments: true

     sql:
       init:
         mode: always
         data-locations: classpath:schema.sql

   logging:
     level:
       root: INFO
       com.quizengine: DEBUG
   ```

4. **Create Entity Models:**
   - `Question.java` with @Entity, @Table, indexes, relationships
   - `QuizSession.java` with @Entity, timestamps, relationship to responses
   - `QuizResponse.java` with @Entity, foreign keys to session and question
   - Use Lombok `@Data`, `@Builder` to reduce boilerplate

5. **Create Spring Data JPA Repositories:**
   - `QuestionRepository extends JpaRepository<Question, Long>`
   - Add custom query methods:
     - `@Query` for cycle-aware question selection
     - `findCurrentCycle()` for MIN(usage_cycle)
     - `findByUsageCycleOrderByRandom()` (with LIMIT)
   - `SessionRepository extends JpaRepository<QuizSession, String>`
   - `ResponseRepository extends JpaRepository<QuizResponse, Long>`

6. **Create `QuizEngineApplication.java`:**
   ```java
   @SpringBootApplication
   public class QuizEngineApplication {
       public static void main(String[] args) {
           SpringApplication.run(QuizEngineApplication.class, args);
       }
   }
   ```

7. **Test JPA mappings:**
   - `./gradlew bootRun`
   - Tables created automatically by Hibernate
   - No errors on startup

**Success Criteria:**
- Spring Boot application starts cleanly
- SQLite database initialized with Hibernate
- All entities properly mapped
- Custom queries resolve correctly
- No lazy loading issues

---

### Phase 2: Service Layer & Quiz Logic
**Timeline:** 2-3 hours

**Objective:** Implement business logic, quiz flow, cycle-aware question selection.

**Tasks:**

1. **Create `service/QuizEngine.java`:**
   - Constructor: accepts SessionId, config, repositories
   - `loadQuestions(int n)` → calls `QuestionRepository.findByUsageCycle(getCurrentCycle()).limit(n)`
   - `submitAnswer()` → verify, persist via `ResponseRepository.save()`
   - `finalize()` → mark questions used (`markQuestionUsed()` → updates timesUsed)
   - `advanceQuestionsCycle()` → increment usage_cycle where timesUsed > 0
   - Transaction support: `@Transactional`

2. **Create `service/QuizService.java`:**
   - Wrapper around QuizEngine
   - `startNewQuiz()` → generate sessionId, create QuizEngine
   - `getQuizSession()` → retrieve from SessionRepository
   - Business logic orchestration

3. **Create `service/HistoryService.java`:**
   - `getAllSessions()` → `SessionRepository.findAll(PageRequest.of())`
   - `getSessionDetails(sessionId)` → fetch with responses loaded
   - `formatReview()` → group answers (incorrect first, then correct)

4. **Create `service/ImportService.java`:**
   - `importQuestions(File)` → MarkdownParser + batch save
   - `@Transactional` for batch operations
   - Handles UNIQUE constraint violations gracefully

5. **Write Repositories with Custom Queries:**
   ```java
   public interface QuestionRepository extends JpaRepository<Question, Long> {
       @Query("SELECT MIN(q.usageCycle) FROM Question q")
       Integer findCurrentCycle();

       @Query(value = "SELECT * FROM questions WHERE usage_cycle = ? ORDER BY RANDOM() LIMIT ?", nativeQuery = true)
       List<Question> findByUsageCycleRandom(Integer cycle, Integer limit);

       @Modifying
       @Query("UPDATE Question SET timesUsed = timesUsed + 1, lastUsedAt = CURRENT_TIMESTAMP WHERE id = ?")
       void markQuestionUsed(Long id);
   }
   ```

6. **Test service layer:**
   - Unit tests with `@DataJpaTest`
   - Mock repositories with Mockito
   - Verify cycle-aware selection
   - Verify score calculation

**Success Criteria:**
- QuizEngine orchestrates full flow
- Questions selected only from current cycle
- Usage stats updated correctly
- Cycle auto-advances
- Transactional integrity maintained
- All service tests passing

---

### Phase 3: CLI Layer with Picocli + Spring
**Timeline:** 1.5-2 hours

**Objective:** Implement interactive CLI using Picocli + Spring integration.

**Tasks:**

1. **Create CLI Commands (Picocli + Spring):**
   - `@Command` annotated classes that are also `@Component`
   - `@Autowired` to inject services
   - Subcommands for quiz, import, history, clear

2. **Write `cli/QuizCommand.java`:**
   - `@Command(name = "quiz", description = "Take a quiz")`
   - Interactive prompts for num_questions, seconds_per
   - Call `QuizService.startNewQuiz()`
   - Loop through questions with timing
   - Display results + offer review

3. **Write `cli/ImportCommand.java`:**
   - `@Command(name = "import")`
   - `@Option(names = "--file")` for file path
   - `@Option(names = "--dir")` for directory
   - Call `ImportService.importQuestions()`
   - Report import stats

4. **Write `cli/HistoryCommand.java`:**
   - `@Command(name = "history")`
   - `@Option(names = "--session-id")`, `--review`, `--export`
   - Call `HistoryService` methods
   - Format and display results

5. **Write `cli/ClearCommand.java`:**
   - `@Command(name = "clear")`
   - `@Option(names = "--questions")`, `--history`, `--confirm`
   - Confirmation prompts
   - Delete via repositories

6. **Create `QuizEngineConfig.java`:**
   - Spring `@Configuration` class
   - Define `@Bean` for Picocli integration
   - Wire Picocli into Spring Boot

7. **Write `ConsoleFormatter.java`:**
   - Static methods for colored output
   - Box drawing for questions
   - Table formatting for results
   - Progress bars for timer

8. **Test CLI commands:**
   - `./gradlew bootRun --args='quiz'`
   - `./gradlew bootRun --args='import --file questions.md'`
   - All commands work interactively

**Success Criteria:**
- All CLI commands execute via Spring Boot
- Picocli integration seamless
- Tab completion works
- Error messages helpful
- Interactive flow smooth

---

### Phase 4: Testing, Polish & Documentation
**Timeline:** 1.5-2 hours

**Objective:** Comprehensive testing, error handling, README.

**Tasks:**

1. **Write Integration Tests:**
   - `@SpringBootTest` with embedded database
   - Test full quiz flow: load → submit → finalize
   - Verify cycle-aware question selection
   - Test non-repetition across multiple quizzes

2. **Write Repository Tests:**
   - `@DataJpaTest` for each repository
   - Test custom query methods
   - Verify cycle calculations

3. **Implement Error Handling:**
   - Custom `QuizEngineException`
   - `@ControllerAdvice` (or global exception handler)
   - Graceful handling of DB failures, validation errors

4. **Add Application Properties:**
   - `application-dev.yml` for development
   - `application-prod.yml` for production
   - Enable/disable debug logging

5. **Create Executable JAR:**
   - Configure Gradle build to produce executable JAR
   - Enable Spring Boot executable JAR format in `build.gradle.kts`
   - Build with `./gradlew build`
   - Creates `build/libs/quiz-engine-0.0.1-SNAPSHOT.jar`

6. **Write Comprehensive README:**
   - **Getting Started:** Java 17+ requirement, download/build steps
   - **Configuration:** application.yml options
   - **Running Quizzes:** `java -jar build/libs/quiz-engine-0.0.1-SNAPSHOT.jar quiz`
   - **CLI Commands:** quiz, import, history, clear with examples
   - **Architecture:** Spring Data JPA, Picocli integration
   - **Testing:** How to run tests
   - **Troubleshooting:** Common issues

7. **Test end-to-end:**
   - `./gradlew build`
   - `java -jar build/libs/quiz-engine-0.0.1-SNAPSHOT.jar` → works
   - `java -jar build/libs/quiz-engine-0.0.1-SNAPSHOT.jar --help` → shows commands
   - All workflows function correctly

**Success Criteria:**
- All tests passing with >80% coverage
- Executable JAR runs standalone
- README complete and clear
- No unhandled exceptions
- Cross-platform compatibility

---

## Dependencies Summary
- **Spring Boot 3.2** - Application framework
- **Spring Data JPA** - ORM abstraction
- **Hibernate SQLite Dialect** - SQLite support
- **Picocli Spring Boot Starter** - CLI framework
- **Lombok** (optional) - Boilerplate reduction
- **JUnit 5, Mockito** - Testing frameworks

Total JAR size: ~45MB (Spring Boot overhead)

---

## Core Design Decisions

### 1. Spring Data JPA over Plain JDBC
- **ORM:** Simplified data access, less boilerplate
- **Queries:** Custom `@Query` for complex queries
- **Lifecycle:** Automatic session/transaction management
- **Benefit:** Productivity over raw control

### 2. Picocli + Spring Integration
- **CLI:** Modern, declarative command structure
- **Spring:** Full DI, service layer access
- **Result:** CLI commands are Spring beans

### 3. Transactional Integrity
- `@Transactional` on service methods
- Batch operations (import) atomicity
- Quiz finalization as single transaction

### 4. Lazy vs Eager Loading
- `QuizResponse` → EAGER fetch relationships (needed in review)
- `Question` → LAZY for performance (not always needed)
- Explicit control via fetch strategy

### 5. Non-Repetition with JPA Queries
- Native SQL for cycle-aware RANDOM() selection (SQLite-specific)
- JPA `@Query` wrapper for type safety
- Hibernate manages parameter binding

---

## CLI Operations & Examples

### 1. Take a Quiz
```bash
java -jar quiz-engine.jar quiz --questions 100
```

### 2. Import Questions
```bash
java -jar quiz-engine.jar import --file questions.md
java -jar quiz-engine.jar import --dir ./md/
```

### 3. View History
```bash
java -jar quiz-engine.jar history
java -jar quiz-engine.jar history --session-id <uuid> --review
java -jar quiz-engine.jar history --export json
```

### 4. Clear Data
```bash
java -jar quiz-engine.jar clear --questions --confirm
java -jar quiz-engine.jar clear --history --all --confirm
```

---

## Success Criteria

### Functional Requirements
- ✓ Spring Boot application boots cleanly
- ✓ JPA entities auto-create SQLite schema
- ✓ Load 100+ random questions from current cycle
- ✓ NEVER repeat question until all exhausted
- ✓ Answers randomized and tracked
- ✓ Session persisted with full integrity
- ✓ Import, history, clear operations work
- ✓ All CLI commands functional

### Non-Functional Requirements
- ✓ Performance: Question loading <1 second (JPA query)
- ✓ Usability: Full workflow in <15 minutes
- ✓ Reliability: Transactional integrity, error handling
- ✓ Maintainability: Clean service layer, testable
- ✓ Compatibility: Java 17+, Windows/Mac/Linux

---

## Implementation Notes

- **Test-Driven:** Write repository tests first, then services
- **Lazy vs Eager:** Review fetch strategies to avoid N+1 queries
- **Batch Operations:** Use `saveAll()` for imports
- **Transaction Scope:** Keep `@Transactional` blocks minimal
- **Profiles:** Use `@Profile` for dev/test/prod configs
- **Future:** Add REST API with `spring-boot-starter-web`, persistence layer caching
