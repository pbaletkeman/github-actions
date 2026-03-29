$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Spring Boot - View History ===" -ForegroundColor Cyan
if (-not (Test-Path "build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar")) {
    Write-Host "JAR not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
Write-Host "Starting Spring Boot application to view history..." -ForegroundColor Yellow
Write-Host ""
Write-Host "Once the server starts, view history via:" -ForegroundColor Cyan
Write-Host "  REST API: http://localhost:8080/api/history" -ForegroundColor White
Write-Host "  Web UI:   http://localhost:8080" -ForegroundColor White
Write-Host ""
Write-Host "Press Ctrl+C to stop the server when done." -ForegroundColor Yellow
Write-Host ""
java -jar build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar
