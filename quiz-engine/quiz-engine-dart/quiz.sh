#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Dart - Start Quiz ==="
QUESTIONS=${1:-10}
echo "Starting quiz with $QUESTIONS questions..."
dart run lib/main.dart quiz --questions $QUESTIONS
