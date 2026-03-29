#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Node.js - Start Quiz ==="
if [ ! -f "dist/main.js" ]; then
    echo "dist/main.js not found. Run build.sh first."
    exit 1
fi
QUESTIONS=${1:-10}
echo "Starting quiz with $QUESTIONS questions..."
node dist/main.js quiz --questions $QUESTIONS
