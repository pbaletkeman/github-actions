@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Go - Start Quiz ===
if not exist "bin\quiz-engine.exe" (
    echo Executable not found. Run build.bat first.
    exit /b 1
)
if "%~1"=="" (
    echo Starting quiz with 20 questions ^(default^)...
    bin\quiz-engine.exe quiz --questions 20
) else (
    echo Starting quiz with %~1 questions...
    bin\quiz-engine.exe quiz --questions %~1
)
