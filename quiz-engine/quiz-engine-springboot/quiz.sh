#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Spring Boot - Start Quiz ==="
if [ ! -f "build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar" ]; then
    echo "JAR not found. Run build.sh first."
    exit 1
fi
echo "Starting Spring Boot application for quiz..."
echo ""
echo "Once the server starts:"
echo "  Web UI:    http://localhost:8080"
echo "  Start API: POST http://localhost:8080/api/quiz/start"
echo '  Body:      {"numQuestions": 10}'
echo ""
echo "Press Ctrl+C to stop the server when done."
echo ""
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar
