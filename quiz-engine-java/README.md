# Quiz Engine Java

A Java-based interactive quiz engine for studying GitHub Actions, built with Maven.

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
- Maven 3.8+

## Build

```bash
mvn clean package
```

## Run

```bash
# Start a quiz
java -jar target/quiz-engine.jar quiz

# Import questions from markdown
java -jar target/quiz-engine.jar import questions.md

# View history
java -jar target/quiz-engine.jar history

# Export history as JSON
java -jar target/quiz-engine.jar history --json

# Export history as CSV
java -jar target/quiz-engine.jar history --csv

# Clear history
java -jar target/quiz-engine.jar clear

# Show help
java -jar target/quiz-engine.jar help
```

## Custom DB Path

```bash
java -Dquiz.db=/path/to/quiz.db -jar target/quiz-engine.jar
```

## Docker

```bash
docker-compose up quiz-engine
```

## Testing

```bash
mvn clean test
```

## Project Structure

```
src/
├── main/java/com/quizengine/
│   ├── QuizEngineApp.java       # Entry point
│   ├── model/                   # Data models
│   ├── dao/                     # Database access objects
│   ├── service/                 # Business logic
│   ├── util/                    # Utilities
│   └── cli/                     # CLI interface
└── test/java/com/quizengine/    # Unit tests
```
