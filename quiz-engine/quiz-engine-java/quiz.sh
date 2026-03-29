#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Java - Start Quiz ==="
if [ ! -f "build/libs/quiz-engine.jar" ]; then
    echo "JAR not found. Run build.sh first."
    exit 1
fi
echo "Starting quiz..."
java -jar build/libs/quiz-engine.jar quiz
