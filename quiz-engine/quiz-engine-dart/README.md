# Quiz Engine -- Dart

> Part of the [Quiz Engine multi-language collection](../README.md)

- [Quiz Engine -- Dart](#quiz-engine----dart)
  - [Get Started in 5 Minutes](#get-started-in-5-minutes)
    - [Prerequisites](#prerequisites)
    - [1. Install Dependencies](#1-install-dependencies)
    - [2. Import Questions](#2-import-questions)
    - [3. Take a Quiz](#3-take-a-quiz)
    - [4. View History](#4-view-history)
    - [5. Build Native Executable (optional)](#5-build-native-executable-optional)
    - [6. Docker](#6-docker)


A CLI quiz engine for GitHub Actions (GH-200) certification prep. Built with Dart 3, SQLite, and native executable compilation.

## Get Started in 5 Minutes

### Prerequisites
- [Dart SDK 3.0+](https://dart.dev/get-dart) or Docker

### 1. Install Dependencies
```bash
dart pub get
```

### 2. Import Questions
```bash
dart run lib/main.dart import --file questions.md
# or import a directory
dart run lib/main.dart import --dir ./questions/
```

### 3. Take a Quiz
```bash
dart run lib/main.dart quiz --questions 10
```

### 4. View History
```bash
dart run lib/main.dart history
```

### 5. Build Native Executable (optional)
```bash
dart compile exe lib/main.dart -o bin/quiz_engine
./bin/quiz_engine quiz --questions 10
```

### 6. Docker
```bash
docker build -t quiz-engine-dart:latest .
docker run -it quiz-engine-dart:latest dart run lib/main.dart quiz --questions 10
```

---

Full docs: [docs/README.md](docs/README.md) | Architecture: [docs/architecture.md](docs/architecture.md)
