#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Go - View History ==="
if [ ! -f "bin/quiz-engine" ]; then
    echo "Executable not found. Run build.sh first."
    exit 1
fi
echo "Showing quiz history..."
./bin/quiz-engine history
