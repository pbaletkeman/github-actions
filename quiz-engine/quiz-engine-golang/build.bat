@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Go - Build ===
echo Building executable (CGO_ENABLED=1 required for SQLite)...
if not exist "bin" mkdir bin
set CGO_ENABLED=1
go build -o bin\quiz-engine.exe .
if %ERRORLEVEL% NEQ 0 (
    echo Build failed! Ensure a C compiler (MinGW or MSVC) is installed.
    exit /b %ERRORLEVEL%
)
echo Build successful! Executable: bin\quiz-engine.exe
