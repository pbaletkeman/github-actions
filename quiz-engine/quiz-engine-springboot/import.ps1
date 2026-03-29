$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Spring Boot - Import Questions ===" -ForegroundColor Cyan
if (-not (Test-Path "build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar")) {
    Write-Host "JAR not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
Write-Host "Starting Spring Boot application for question import..." -ForegroundColor Yellow
Write-Host ""
Write-Host "Once the server starts, import questions via the REST API:" -ForegroundColor Cyan
Write-Host "  POST http://localhost:8080/api/import" -ForegroundColor White
Write-Host '  Body: {"content": "<markdown content>", "source": "filename.md"}' -ForegroundColor White
Write-Host ""
Write-Host "Or visit the web interface at: http://localhost:8080" -ForegroundColor Cyan
Write-Host "Press Ctrl+C to stop the server when done." -ForegroundColor Yellow
Write-Host ""
java -jar build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar
