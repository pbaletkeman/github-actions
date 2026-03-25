@echo off
python -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
pip install -r requirements-dev.txt
if not exist quiz_engine mkdir quiz_engine
echo Setup complete.
