param(
    [string]$File = ""
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Java - Import Questions ===" -ForegroundColor Cyan
if (-not (Test-Path "build\libs\quiz-engine.jar")) {
    Write-Host "JAR not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
if ($File -eq "") {
    Write-Host "No file specified. Usage: .\import.ps1 -File <questions.md>" -ForegroundColor Red
    exit 1
}
Write-Host "Importing from file: $File" -ForegroundColor Yellow
java -jar build\libs\quiz-engine.jar import $File
