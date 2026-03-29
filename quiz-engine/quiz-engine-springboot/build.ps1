$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Spring Boot - Build ===" -ForegroundColor Cyan
Write-Host "Building JAR with Gradle wrapper..." -ForegroundColor Yellow
.\gradlew.bat build
Write-Host "Build successful! JAR: build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar" -ForegroundColor Green
