#!/bin/bash
set -e
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
pip install -r requirements-dev.txt
mkdir -p quiz_engine
echo "Setup complete. Run: source .venv/bin/activate"
