#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Rust - Start Quiz ==="
QUESTIONS=${1:-10}
echo "Starting quiz with $QUESTIONS questions..."
cargo run --release -- quiz --questions $QUESTIONS
