# Quiz Engine -- C# / .NET 8

> Part of the [Quiz Engine multi-language collection](../README.md)

- [Quiz Engine -- C# / .NET 8](#quiz-engine----c--net-8)
  - [Get Started in 5 Minutes](#get-started-in-5-minutes)
    - [Prerequisites](#prerequisites)
    - [1. Build](#1-build)
    - [2. Import Questions](#2-import-questions)
    - [3. Take a Quiz](#3-take-a-quiz)
    - [4. View History](#4-view-history)
    - [5. Docker](#5-docker)


A CLI quiz engine for GitHub Actions (GH-200) certification prep. Built with .NET 8, EF Core, and Spectre.Console.

## Get Started in 5 Minutes

### Prerequisites
- [.NET 8 SDK](https://dotnet.microsoft.com/download/dotnet/8) or Docker

### 1. Build
```bash
build.bat          # Windows CMD
.\build.ps1        # PowerShell
./build.sh         # Bash / macOS / Linux
```

### 2. Import Questions
```bash
import.bat questions.md            # Windows CMD
.\import.ps1 -Path questions.md    # PowerShell
./import.sh questions.md           # Bash
```

### 3. Take a Quiz
```bash
quiz.bat 10            # Windows CMD  (10 questions)
.\quiz.ps1 -Questions 10  # PowerShell
./quiz.sh 10           # Bash
```

### 4. View History
```bash
history.bat     # Windows CMD
.\history.ps1   # PowerShell
./history.sh    # Bash
```

### 5. Docker
```bash
docker build -t quiz-engine:latest .
docker run -it -v quiz-data:/data quiz-engine:latest quiz --questions 10
```

---

Full docs: [docs/README.md](docs/README.md) | Architecture: [docs/architecture.md](docs/architecture.md)
