#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Spring Boot - View History ==="
if [ ! -f "build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar" ]; then
    echo "JAR not found. Run build.sh first."
    exit 1
fi
echo "Starting Spring Boot application to view history..."
echo ""
echo "Once the server starts, view history via:"
echo "  REST API: http://localhost:8080/api/history"
echo "  Web UI:   http://localhost:8080"
echo ""
echo "Press Ctrl+C to stop the server when done."
echo ""
java -jar build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar
