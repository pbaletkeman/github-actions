@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Go - View History ===
if not exist "bin\quiz-engine.exe" (
    echo Executable not found. Run build.bat first.
    exit /b 1
)
bin\quiz-engine.exe history
