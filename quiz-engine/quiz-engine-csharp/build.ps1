$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine C# - Build ===" -ForegroundColor Cyan
Write-Host "Building solution..." -ForegroundColor Yellow
dotnet build QuizEngine.sln
Write-Host "Build successful!" -ForegroundColor Green
