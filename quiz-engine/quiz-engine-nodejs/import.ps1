param(
    [string]$Path = ""
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Node.js - Import Questions ===" -ForegroundColor Cyan
if (-not (Test-Path "dist\main.js")) {
    Write-Host "dist\main.js not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
if ($Path -eq "") {
    Write-Host "No path specified. Usage: .\import.ps1 -Path <file_or_directory>" -ForegroundColor Red
    exit 1
} elseif (Test-Path $Path -PathType Container) {
    Write-Host "Importing from directory: $Path" -ForegroundColor Yellow
    node dist\main.js import --dir $Path
} else {
    Write-Host "Importing from file: $Path" -ForegroundColor Yellow
    node dist\main.js import --file $Path
}
