param(
    [int]$Questions = 20
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Go - Start Quiz ===" -ForegroundColor Cyan
if (-not (Test-Path "bin\quiz-engine.exe")) {
    Write-Host "Executable not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
Write-Host "Starting quiz with $Questions questions..." -ForegroundColor Yellow
.\bin\quiz-engine.exe quiz --questions $Questions
