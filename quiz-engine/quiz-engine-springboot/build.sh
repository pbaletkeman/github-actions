#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Spring Boot - Build ==="
echo "Building JAR with Gradle wrapper..."
chmod +x gradlew
./gradlew build
echo "Build successful! JAR: build/libs/quiz-engine-springboot-0.0.1-SNAPSHOT.jar"
