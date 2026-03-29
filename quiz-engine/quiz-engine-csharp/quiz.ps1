param(
    [int]$Questions = 10
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine C# - Start Quiz ===" -ForegroundColor Cyan
Write-Host "Starting quiz with $Questions questions..." -ForegroundColor Yellow
dotnet run --project QuizEngine.CLI -- quiz --questions $Questions
