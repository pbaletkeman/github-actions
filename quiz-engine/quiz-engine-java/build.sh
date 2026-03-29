#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Java - Build ==="
echo "Building JAR with Gradle..."
gradle clean build
echo "Build successful! JAR: build/libs/quiz-engine.jar"
