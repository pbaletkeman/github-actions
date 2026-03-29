$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Go - Build ===" -ForegroundColor Cyan
Write-Host "Building executable (CGO_ENABLED=1 required for SQLite)..." -ForegroundColor Yellow
if (-not (Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
$env:CGO_ENABLED = "1"
go build -o bin\quiz-engine.exe .
Write-Host "Build successful! Executable: bin\quiz-engine.exe" -ForegroundColor Green
