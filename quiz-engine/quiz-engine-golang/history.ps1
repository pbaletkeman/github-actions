$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Go - View History ===" -ForegroundColor Cyan
if (-not (Test-Path "bin\quiz-engine.exe")) {
    Write-Host "Executable not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
Write-Host "Showing quiz history..." -ForegroundColor Yellow
.\bin\quiz-engine.exe history
