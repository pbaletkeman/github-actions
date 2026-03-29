$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Rust - Build ===" -ForegroundColor Cyan
Write-Host "Building release binary (this may take a few minutes on first run)..." -ForegroundColor Yellow
cargo build --release
Write-Host "Build successful! Binary: target\release\quiz_engine.exe" -ForegroundColor Green
