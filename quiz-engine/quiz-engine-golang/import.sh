#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Go - Import Questions ==="
if [ ! -f "bin/quiz-engine" ]; then
    echo "Executable not found. Run build.sh first."
    exit 1
fi
if [ -z "$1" ]; then
    echo "No path specified. Usage: ./import.sh <file_or_directory>"
    exit 1
elif [ -d "$1" ]; then
    echo "Importing from directory: $1"
    ./bin/quiz-engine import --dir "$1"
else
    echo "Importing from file: $1"
    ./bin/quiz-engine import --file "$1"
fi
