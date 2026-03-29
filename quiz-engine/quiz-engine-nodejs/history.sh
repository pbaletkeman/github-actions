#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Node.js - View History ==="
if [ ! -f "dist/main.js" ]; then
    echo "dist/main.js not found. Run build.sh first."
    exit 1
fi
echo "Showing quiz history..."
node dist/main.js history
