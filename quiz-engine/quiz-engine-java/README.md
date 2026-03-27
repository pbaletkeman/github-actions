# Quiz Engine — Java

> Part of the [Quiz Engine multi-language collection](../README.md)

A Java-based interactive quiz engine for studying GitHub Actions, built with Gradle.

## Features
- Interactive CLI quiz interface
- SQLite database for persistence
- Answer shuffling for each quiz session
- Quiz history tracking
- Markdown question file import
- JSON/CSV history export
- Docker support

## Prerequisites
- Java 17+
- Gradle 7.6+ (or use Gradle wrapper)

## Setup

### Install Java 17+

#### Windows
1. Download [JDK 17+](https://www.oracle.com/java/technologies/downloads/) or [Eclipse Temurin](https://adoptium.net/)
2. Run the installer
3. Add to PATH or set JAVA_HOME environment variable
4. Verify: `java -version`

#### macOS
```bash
# Using Homebrew
brew install temurin

# Verify installation
java -version
```

#### Linux (Debian/Ubuntu)
```bash
sudo apt-get update
sudo apt-get install default-jdk

# Or install specific version
sudo apt-get install openjdk-17-jdk

# Verify installation
java -version
```

### Install Gradle

#### Windows
1. Download [Gradle](https://gradle.org/releases/)
2. Extract to folder (e.g., `C:\gradle`)
3. Add `bin` folder to PATH
4. Verify: `gradle --version`

#### macOS
```bash
# Using Homebrew (recommended)
brew install gradle

# Verify installation
gradle --version
```

#### Linux
```bash
# Debian/Ubuntu
sudo apt-get install gradle

# Verify installation
gradle --version
```

## Build

### Build the Project

```bash
# Build and create fat JAR with all dependencies
gradle clean build

# Build without running tests
gradle clean build -x test
```

### Verify Build

The compiled JAR will be located at:
- `build/libs/quiz-engine.jar`

Test the build:
```bash
java -jar build/libs/quiz-engine.jar --help
```

## Run

### Using Gradle (Development)

```bash
# Start a quiz
gradle run --args="quiz"

# Import questions from markdown
gradle run --args="import questions.md"

# View history
gradle run --args="history"

# Export history as JSON
gradle run --args="history --json"

# Export history as CSV
gradle run --args="history --csv"

# Clear history
gradle run --args="clear"

# Show help
gradle run --args="help"
```

### Using Compiled JAR (Production)

### Using Compiled JAR (Production)

```bash
# Start a quiz with 20 questions
java -jar build/libs/quiz-engine.jar quiz

# Import questions from markdown
java -jar build/libs/quiz-engine.jar import questions.md

# View quiz history
java -jar build/libs/quiz-engine.jar history

# Export history as JSON
java -jar build/libs/quiz-engine.jar history --json

# Export history as CSV
java -jar build/libs/quiz-engine.jar history --csv

# Clear all history
java -jar build/libs/quiz-engine.jar clear

# Show help
java -jar build/libs/quiz-engine.jar help
```

## Custom Database Path

```bash
# Using Gradle
gradle run --args="quiz" -Dquiz.db=/path/to/quiz.db

# Using JAR
java -Dquiz.db=/path/to/quiz.db -jar build/libs/quiz-engine.jar quiz
```

## Testing

### Run All Tests

```bash
gradle test
```

### Run Tests with Detailed Output

```bash
gradle test --info
```

### Run Specific Test

```bash
# Run a single test class
gradle test --tests com.quizengine.service.QuizServiceTest

# Run a specific test method
gradle test --tests com.quizengine.service.QuizServiceTest.testQuizGeneration
```

### Code Coverage

```bash
# Run tests and generate coverage report
gradle test jacocoTestReport

# Verify coverage meets minimum threshold (80%)
gradle jacocoTestCoverageVerification
```

Coverage reports are generated in `build/reports/jacoco/test/html/`

## Docker

```bash
docker-compose up quiz-engine
```

## Project Structure

```
quiz-engine-java/
├── build.gradle                             # Gradle build configuration
├── settings.gradle                          # Gradle settings
├── src/
│   ├── main/java/com/quizengine/
│   │   ├── QuizEngineApp.java              # Entry point
│   │   ├── model/                          # Data models
│   │   ├── dao/                            # Database access objects
│   │   ├── service/                        # Business logic
│   │   ├── util/                           # Utilities
│   │   └── cli/                            # CLI interface
│   └── test/java/com/quizengine/           # Unit tests
├── build/                                   # Build output (generated)
│   └── libs/
│       └── quiz-engine.jar                 # Compiled JAR
├── Dockerfile                               # Docker configuration
└── docker-compose.yml                       # Docker Compose configuration
```

## Build Tasks Reference

```bash
# List all available tasks
gradle tasks

# Clean build artifacts
gradle clean

# Build and run tests
gradle build

# Build without tests
gradle build -x test

# Build fat JAR only
gradle shadowJar

# Run application
gradle run --args="[arguments]"

# Generate test report
gradle test

# Generate coverage report
gradle jacocoTestReport

# Verify coverage thresholds
gradle jacocoTestCoverageVerification
```

## Troubleshooting

### Common Issues

**Error**: `Could not find or load main class com.quizengine.QuizEngineApp`
- **Solution**: Ensure JAR is built: `gradle clean build`

**Error**: `JAVA_HOME is not set`
- **Solution**: Install Java and set JAVA_HOME or add to PATH

**Error**: `gradle: command not found`
- **Solution**: Install Gradle or add to PATH

**Error**: Tests fail with database lock
- **Solution**: Tests use SQLite in-memory DB by default; ensure no other instances are running
