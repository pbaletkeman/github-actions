param(
    [string]$File = ""
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Rust - Import Questions ===" -ForegroundColor Cyan
if ($File -eq "") {
    Write-Host "No file specified. Usage: .\import.ps1 -File <questions.md>" -ForegroundColor Red
    exit 1
}
Write-Host "Importing from file: $File" -ForegroundColor Yellow
cargo run --release -- import --file $File
