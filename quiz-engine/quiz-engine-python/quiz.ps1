param(
    [int]$Questions = 20
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Python - Start Quiz ===" -ForegroundColor Cyan
if (-not (Test-Path "venv\Scripts\Activate.ps1")) {
    Write-Host "Virtual environment not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
& venv\Scripts\Activate.ps1
Write-Host "Starting quiz with $Questions questions..." -ForegroundColor Yellow
python -m quiz_engine.main --questions $Questions
