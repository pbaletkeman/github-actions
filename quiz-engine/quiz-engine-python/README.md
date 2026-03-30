# Quiz Engine -- Python

> Part of the [Quiz Engine multi-language collection](../README.md)

- [Quiz Engine -- Python](#quiz-engine----python)
  - [Get Started in 5 Minutes](#get-started-in-5-minutes)
    - [Prerequisites](#prerequisites)
    - [1. Build (create virtual environment)](#1-build-create-virtual-environment)
    - [2. Import Questions](#2-import-questions)
    - [3. Take a Quiz](#3-take-a-quiz)
    - [4. View History](#4-view-history)
    - [5. Docker](#5-docker)


A CLI quiz engine for GitHub Actions (GH-200) certification prep. Built with Python 3.8+, Typer, Rich, and SQLite.

## Get Started in 5 Minutes

### Prerequisites
- [Python 3.8+](https://www.python.org/downloads/)

### 1. Build (create virtual environment)
```bash
build.bat          # Windows CMD
.\build.ps1        # PowerShell
./build.sh         # Bash / macOS / Linux
```

### 2. Import Questions
```bash
import.bat questions.md            # Windows CMD (activates venv automatically)
.\import.ps1 -Path questions.md    # PowerShell
./import.sh questions.md           # Bash
# or manually (after activating venv)
python scripts/import_questions.py --file questions.md
```

### 3. Take a Quiz
```bash
quiz.bat 20          # Windows CMD
.\quiz.ps1 -Q 20     # PowerShell
./quiz.sh 20         # Bash
# or manually
python -m quiz_engine.main --questions 20
```

### 4. View History
```bash
python scripts/view_history.py --summary
```

### 5. Docker
```bash
docker-compose up quiz
```

---

Full docs: [docs/README.md](docs/README.md) | Architecture: [docs/architecture.md](docs/architecture.md)
