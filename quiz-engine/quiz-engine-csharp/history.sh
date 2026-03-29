#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine C# - View History ==="
if [ -z "$1" ]; then
    echo "Showing all sessions..."
    dotnet run --project QuizEngine.CLI -- history
else
    echo "Showing session: $1"
    dotnet run --project QuizEngine.CLI -- history --session-id "$1"
fi
