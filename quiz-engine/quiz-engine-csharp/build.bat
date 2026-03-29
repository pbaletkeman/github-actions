@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine C# - Build ===
echo Building solution...
dotnet build QuizEngine.sln
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b %ERRORLEVEL%
)
echo Build successful!
