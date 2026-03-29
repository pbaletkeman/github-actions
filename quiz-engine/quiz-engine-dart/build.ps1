$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Dart - Build ===" -ForegroundColor Cyan
Write-Host "Installing dependencies..." -ForegroundColor Yellow
dart pub get
Write-Host "Compiling native executable..." -ForegroundColor Yellow
if (-not (Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
dart compile exe lib/main.dart -o bin\quiz_engine.exe
Write-Host "Build successful! Executable: bin\quiz_engine.exe" -ForegroundColor Green
