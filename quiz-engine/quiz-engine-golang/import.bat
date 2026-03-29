@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Go - Import Questions ===
if not exist "bin\quiz-engine.exe" (
    echo Executable not found. Run build.bat first.
    exit /b 1
)
if "%~1"=="" (
    echo Usage: import.bat [file_or_directory]
    echo   Example: import.bat questions.md
    echo   Example: import.bat .\questions\
    echo No path specified. Please provide a file or directory path.
    exit /b 1
) else (
    if exist "%~1\" (
        echo Importing from directory: %~1
        bin\quiz-engine.exe import --dir "%~1"
    ) else (
        echo Importing from file: %~1
        bin\quiz-engine.exe import --file "%~1"
    )
)
