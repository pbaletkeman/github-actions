# Quiz Engine

A Python/SQLite quiz engine for GH-200 GitHub Actions Certification preparation.

## Setup

```bash
pip install -r requirements.txt
pip install -r requirements-dev.txt
```

## Import Questions

```bash
python scripts/import_questions.py --dir /path/to/markdown/files
```

## Run Quiz

```bash
python -m quiz_engine.main --questions 20
```

## Run Tests

```bash
cd quiz-engine
python -m pytest tests/ -v
```

## View History

```bash
python scripts/view_history.py --summary
python scripts/view_history.py --export csv --output history.csv
```
