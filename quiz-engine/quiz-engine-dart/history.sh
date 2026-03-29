#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Dart - View History ==="
if [ -z "$1" ]; then
    echo "Showing all sessions..."
    dart run lib/main.dart history
else
    echo "Showing session: $1"
    dart run lib/main.dart history --session-id "$1"
fi
