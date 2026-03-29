#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Rust - Import Questions ==="
if [ -z "$1" ]; then
    echo "No file specified. Usage: ./import.sh <questions.md>"
    exit 1
fi
echo "Importing from file: $1"
cargo run --release -- import --file "$1"
