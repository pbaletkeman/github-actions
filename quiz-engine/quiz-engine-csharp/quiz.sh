#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine C# - Start Quiz ==="
QUESTIONS=${1:-10}
echo "Starting quiz with $QUESTIONS questions..."
dotnet run --project QuizEngine.CLI -- quiz --questions $QUESTIONS
