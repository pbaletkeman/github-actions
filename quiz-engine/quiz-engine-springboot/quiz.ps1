$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Spring Boot - Start Quiz ===" -ForegroundColor Cyan
if (-not (Test-Path "build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar")) {
    Write-Host "JAR not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
Write-Host "Starting Spring Boot application for quiz..." -ForegroundColor Yellow
Write-Host ""
Write-Host "Once the server starts:" -ForegroundColor Cyan
Write-Host "  Web UI:    http://localhost:8080" -ForegroundColor White
Write-Host "  Start API: POST http://localhost:8080/api/quiz/start" -ForegroundColor White
Write-Host '  Body:      {"numQuestions": 10}' -ForegroundColor White
Write-Host ""
Write-Host "Press Ctrl+C to stop the server when done." -ForegroundColor Yellow
Write-Host ""
java -jar build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar
