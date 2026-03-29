#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Spring Boot - Import Questions ==="
if [ ! -f "build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar" ]; then
    echo "JAR not found. Run build.sh first."
    exit 1
fi
echo "Starting Spring Boot application for question import..."
echo ""
echo "Once the server starts, import questions via the REST API:"
echo "  POST http://localhost:8080/api/import"
echo "  Body: {\"content\": \"<markdown content>\", \"source\": \"filename.md\"}"
echo ""
echo "Or visit the web interface at: http://localhost:8080"
echo "Press Ctrl+C to stop the server when done."
echo ""
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar
