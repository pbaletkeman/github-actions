param(
    [string]$SessionId = ""
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Dart - View History ===" -ForegroundColor Cyan
if ($SessionId -eq "") {
    Write-Host "Showing all sessions..." -ForegroundColor Yellow
    dart run lib/main.dart history
} else {
    Write-Host "Showing session: $SessionId" -ForegroundColor Yellow
    dart run lib/main.dart history --session-id $SessionId
}
