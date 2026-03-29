#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Python - Start Quiz ==="
if [ ! -f "venv/bin/activate" ]; then
    echo "Virtual environment not found. Run build.sh first."
    exit 1
fi
source venv/bin/activate
QUESTIONS=${1:-20}
echo "Starting quiz with $QUESTIONS questions..."
python -m quiz_engine.main --questions $QUESTIONS
