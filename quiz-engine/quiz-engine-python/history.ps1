$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Python - View History ===" -ForegroundColor Cyan
if (-not (Test-Path "venv\Scripts\Activate.ps1")) {
    Write-Host "Virtual environment not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
& venv\Scripts\Activate.ps1
Write-Host "Showing quiz history summary..." -ForegroundColor Yellow
python scripts\view_history.py --summary
