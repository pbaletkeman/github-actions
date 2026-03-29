@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Node.js - View History ===
if not exist "dist\main.js" (
    echo dist\main.js not found. Run build.bat first.
    exit /b 1
)
echo Showing quiz history...
node dist\main.js history
