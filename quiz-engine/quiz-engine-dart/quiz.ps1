param(
    [int]$Questions = 10
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Dart - Start Quiz ===" -ForegroundColor Cyan
Write-Host "Starting quiz with $Questions questions..." -ForegroundColor Yellow
dart run lib/main.dart quiz --questions $Questions
