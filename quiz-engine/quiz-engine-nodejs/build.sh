#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Node.js - Build ==="
echo "Installing dependencies..."
npm install
echo "Compiling TypeScript..."
npm run build
echo "Build successful! Output: dist/"
