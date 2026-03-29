param(
    [string]$Path = ""
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine C# - Import Questions ===" -ForegroundColor Cyan
if ($Path -eq "") {
    Write-Host "No path specified. Usage: .\import.ps1 -Path <file_or_directory>" -ForegroundColor Yellow
    Write-Host "Importing from current directory..." -ForegroundColor Yellow
    dotnet run --project QuizEngine.CLI -- import --dir .
} elseif (Test-Path $Path -PathType Container) {
    Write-Host "Importing from directory: $Path" -ForegroundColor Yellow
    dotnet run --project QuizEngine.CLI -- import --dir $Path
} else {
    Write-Host "Importing from file: $Path" -ForegroundColor Yellow
    dotnet run --project QuizEngine.CLI -- import --file $Path
}
