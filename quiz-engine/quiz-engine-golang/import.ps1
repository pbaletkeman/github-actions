param(
    [string]$Path = ""
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Go - Import Questions ===" -ForegroundColor Cyan
if (-not (Test-Path "bin\quiz-engine.exe")) {
    Write-Host "Executable not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
if ($Path -eq "") {
    Write-Host "No path specified. Usage: .\import.ps1 -Path <file_or_directory>" -ForegroundColor Red
    exit 1
} elseif (Test-Path $Path -PathType Container) {
    Write-Host "Importing from directory: $Path" -ForegroundColor Yellow
    .\bin\quiz-engine.exe import --dir $Path
} else {
    Write-Host "Importing from file: $Path" -ForegroundColor Yellow
    .\bin\quiz-engine.exe import --file $Path
}
