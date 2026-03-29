#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Go - Build ==="
echo "Building executable (CGO_ENABLED=1 required for SQLite)..."
mkdir -p bin
CGO_ENABLED=1 go build -o bin/quiz-engine .
echo "Build successful! Executable: bin/quiz-engine"
