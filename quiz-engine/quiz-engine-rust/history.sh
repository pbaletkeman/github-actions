#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Rust - View History ==="
echo "Showing quiz history..."
cargo run --release -- history
