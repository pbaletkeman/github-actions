$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
Write-Host "=== Quiz Engine Node.js - Build ===" -ForegroundColor Cyan
Write-Host "Installing dependencies..." -ForegroundColor Yellow
npm install
Write-Host "Compiling TypeScript..." -ForegroundColor Yellow
npm run build
Write-Host "Build successful! Output: dist\" -ForegroundColor Green
