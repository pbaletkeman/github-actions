$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Python - Build ===" -ForegroundColor Cyan
Write-Host "Creating virtual environment..." -ForegroundColor Yellow
python -m venv venv
Write-Host "Activating virtual environment..." -ForegroundColor Yellow
& venv\Scripts\Activate.ps1
Write-Host "Installing dependencies..." -ForegroundColor Yellow
pip install -r requirements.txt
Write-Host "Build successful! Virtual environment ready in venv\" -ForegroundColor Green
Write-Host "To activate manually: venv\Scripts\Activate.ps1" -ForegroundColor Cyan
