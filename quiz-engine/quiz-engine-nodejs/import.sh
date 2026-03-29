#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Node.js - Import Questions ==="
if [ ! -f "dist/main.js" ]; then
    echo "dist/main.js not found. Run build.sh first."
    exit 1
fi
if [ -z "$1" ]; then
    echo "No path specified. Usage: ./import.sh <file_or_directory>"
    exit 1
elif [ -d "$1" ]; then
    echo "Importing from directory: $1"
    node dist/main.js import --dir "$1"
else
    echo "Importing from file: $1"
    node dist/main.js import --file "$1"
fi
