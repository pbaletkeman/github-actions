param(
    [int]$Questions = 10
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Node.js - Start Quiz ===" -ForegroundColor Cyan
if (-not (Test-Path "dist\main.js")) {
    Write-Host "dist\main.js not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
Write-Host "Starting quiz with $Questions questions..." -ForegroundColor Yellow
node dist\main.js quiz --questions $Questions
