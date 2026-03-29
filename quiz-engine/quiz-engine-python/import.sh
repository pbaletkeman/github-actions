#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Python - Import Questions ==="
if [ ! -f "venv/bin/activate" ]; then
    echo "Virtual environment not found. Run build.sh first."
    exit 1
fi
source venv/bin/activate
if [ -z "$1" ]; then
    echo "No path specified. Usage: ./import.sh <file_or_directory>"
    exit 1
elif [ -d "$1" ]; then
    echo "Importing from directory: $1"
    python scripts/import_questions.py --dir "$1"
else
    echo "Importing from file: $1"
    python scripts/import_questions.py --file "$1"
fi
