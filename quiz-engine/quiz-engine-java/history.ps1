$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Java - View History ===" -ForegroundColor Cyan
if (-not (Test-Path "build\libs\quiz-engine.jar")) {
    Write-Host "JAR not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
Write-Host "Showing quiz history..." -ForegroundColor Yellow
java -jar build\libs\quiz-engine.jar history
