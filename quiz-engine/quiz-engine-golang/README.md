# Quiz Engine — Go

> Part of the [Quiz Engine multi-language collection](../README.md)

A CLI quiz engine for GitHub Actions certification practice.

## Usage

### Import questions
```bash
./quiz-engine import --file questions.md
```

### Take a quiz
```bash
./quiz-engine quiz --questions 20
```

### View history
```bash
./quiz-engine history
```

### Clear data
```bash
./quiz-engine clear --confirm
```

## Building
```bash
CGO_ENABLED=1 go build -o bin/quiz-engine .
```

## Testing
```bash
CGO_ENABLED=1 go test ./...
```
