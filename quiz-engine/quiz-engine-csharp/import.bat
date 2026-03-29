@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine C# - Import Questions ===
echo Usage: import.bat [file_or_directory]
echo   Example: import.bat questions.md
echo   Example: import.bat .\questions\
echo.
if "%~1"=="" (
    echo No path specified. Importing from current directory...
    dotnet run --project QuizEngine.CLI -- import --dir .
) else (
    if exist "%~1\" (
        dotnet run --project QuizEngine.CLI -- import --dir "%~1"
    ) else (
        dotnet run --project QuizEngine.CLI -- import --file "%~1"
    )
)
