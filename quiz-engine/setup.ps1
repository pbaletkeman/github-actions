python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
pip install -r requirements-dev.txt
New-Item -ItemType Directory -Force -Path quiz_engine
Write-Host "Setup complete."
