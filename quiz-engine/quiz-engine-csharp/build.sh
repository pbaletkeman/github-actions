#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine C# - Build ==="
echo "Building solution..."
dotnet build QuizEngine.sln
echo "Build successful!"
