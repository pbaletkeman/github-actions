$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Java - Build ===" -ForegroundColor Cyan
Write-Host "Building JAR with Gradle..." -ForegroundColor Yellow
gradle clean build
Write-Host "Build successful! JAR: build\libs\quiz-engine.jar" -ForegroundColor Green
