@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Go - Build ===
echo Building executable...
if not exist "bin" mkdir bin
go build -o bin\quiz-engine.exe .
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b %ERRORLEVEL%
)
echo Build successful! Executable: bin\quiz-engine.exe
