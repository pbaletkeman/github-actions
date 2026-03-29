param(
    [int]$Questions = 10
)
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Rust - Start Quiz ===" -ForegroundColor Cyan
Write-Host "Starting quiz with $Questions questions..." -ForegroundColor Yellow
cargo run --release -- quiz --questions $Questions
