@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine C# - Start Quiz ===
if "%~1"=="" (
    echo Starting quiz with 10 questions ^(default^)...
    dotnet run --project QuizEngine.CLI -- quiz
) else (
    echo Starting quiz with %~1 questions...
    dotnet run --project QuizEngine.CLI -- quiz --questions %~1
)
