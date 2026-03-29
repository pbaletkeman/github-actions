#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Java - View History ==="
if [ ! -f "build/libs/quiz-engine.jar" ]; then
    echo "JAR not found. Run build.sh first."
    exit 1
fi
echo "Showing quiz history..."
java -jar build/libs/quiz-engine.jar history
