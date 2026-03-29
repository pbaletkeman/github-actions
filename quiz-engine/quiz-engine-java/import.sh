#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Java - Import Questions ==="
if [ ! -f "build/libs/quiz-engine.jar" ]; then
    echo "JAR not found. Run build.sh first."
    exit 1
fi
if [ -z "$1" ]; then
    echo "No file specified. Usage: ./import.sh <questions.md>"
    exit 1
fi
echo "Importing from file: $1"
java -jar build/libs/quiz-engine.jar import "$1"
