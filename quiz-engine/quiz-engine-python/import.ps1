param(
    [string]$Path = ""
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Python - Import Questions ===" -ForegroundColor Cyan
if (-not (Test-Path "venv\Scripts\Activate.ps1")) {
    Write-Host "Virtual environment not found. Run build.ps1 first." -ForegroundColor Red
    exit 1
}
& venv\Scripts\Activate.ps1
if ($Path -eq "") {
    Write-Host "No path specified. Usage: .\import.ps1 -Path <file_or_directory>" -ForegroundColor Red
    exit 1
} elseif (Test-Path $Path -PathType Container) {
    Write-Host "Importing from directory: $Path" -ForegroundColor Yellow
    python scripts\import_questions.py --dir $Path
} else {
    Write-Host "Importing from file: $Path" -ForegroundColor Yellow
    python scripts\import_questions.py --file $Path
}
