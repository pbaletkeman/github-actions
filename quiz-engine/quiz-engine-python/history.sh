#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
echo "=== Quiz Engine Python - View History ==="
if [ ! -f "venv/bin/activate" ]; then
    echo "Virtual environment not found. Run build.sh first."
    exit 1
fi
source venv/bin/activate
echo "Showing quiz history summary..."
python scripts/view_history.py --summary
