#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Dart - Build ==="
echo "Installing dependencies..."
dart pub get
echo "Compiling native executable..."
mkdir -p bin
dart compile exe lib/main.dart -o bin/quiz_engine
echo "Build successful! Executable: bin/quiz_engine"
