param(
    [string]$SessionId = ""
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine C# - View History ===" -ForegroundColor Cyan
if ($SessionId -eq "") {
    Write-Host "Showing all sessions..." -ForegroundColor Yellow
    dotnet run --project QuizEngine.CLI -- history
} else {
    Write-Host "Showing session: $SessionId" -ForegroundColor Yellow
    dotnet run --project QuizEngine.CLI -- history --session-id $SessionId
}
