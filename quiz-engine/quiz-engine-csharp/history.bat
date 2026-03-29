@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine C# - View History ===
if "%~1"=="" (
    echo Showing all sessions...
    dotnet run --project QuizEngine.CLI -- history
) else (
    echo Showing session: %~1
    dotnet run --project QuizEngine.CLI -- history --session-id "%~1"
)
