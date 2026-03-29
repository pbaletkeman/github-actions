@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Node.js - Start Quiz ===
if not exist "dist\main.js" (
    echo dist\main.js not found. Run build.bat first.
    exit /b 1
)
if "%~1"=="" (
    echo Starting quiz with 10 questions ^(default^)...
    node dist\main.js quiz
) else (
    echo Starting quiz with %~1 questions...
    node dist\main.js quiz --questions %~1
)
