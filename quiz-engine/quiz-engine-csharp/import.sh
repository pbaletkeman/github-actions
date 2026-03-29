#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine C# - Import Questions ==="
if [ -z "$1" ]; then
    echo "Usage: ./import.sh <file_or_directory>"
    echo "Example: ./import.sh questions.md"
    echo "No path specified. Importing from current directory..."
    dotnet run --project QuizEngine.CLI -- import --dir .
elif [ -d "$1" ]; then
    echo "Importing from directory: $1"
    dotnet run --project QuizEngine.CLI -- import --dir "$1"
else
    echo "Importing from file: $1"
    dotnet run --project QuizEngine.CLI -- import --file "$1"
fi
