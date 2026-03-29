#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Go - Start Quiz ==="
if [ ! -f "bin/quiz-engine" ]; then
    echo "Executable not found. Run build.sh first."
    exit 1
fi
QUESTIONS=${1:-20}
echo "Starting quiz with $QUESTIONS questions..."
./bin/quiz-engine quiz --questions $QUESTIONS
