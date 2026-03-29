$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Node.js - View History ===" -ForegroundColor Cyan
if (-not (Test-Path "dist\main.js")) {
    Write-Host "dist\main.js not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
Write-Host "Showing quiz history..." -ForegroundColor Yellow
node dist\main.js history
