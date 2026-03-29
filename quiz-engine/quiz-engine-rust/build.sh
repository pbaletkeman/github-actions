#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Rust - Build ==="
echo "Building release binary (this may take a few minutes on first run)..."
cargo build --release
echo "Build successful! Binary: target/release/quiz_engine"
