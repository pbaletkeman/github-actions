$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Rust - View History ===" -ForegroundColor Cyan
Write-Host "Showing quiz history..." -ForegroundColor Yellow
cargo run --release -- history
