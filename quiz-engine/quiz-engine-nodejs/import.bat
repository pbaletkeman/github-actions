@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Node.js - Import Questions ===
if not exist "dist\main.js" (
    echo dist\main.js not found. Run build.bat first.
    exit /b 1
)
if "%~1"=="" (
    echo No path specified. Please provide a file or directory path.
    echo Usage: import.bat [file_or_directory]
    echo   Example: import.bat questions.md
    echo   Example: import.bat .\questions\
    exit /b 1
) else (
    if exist "%~1\" (
        echo Importing from directory: %~1
        node dist\main.js import --dir "%~1"
    ) else (
        echo Importing from file: %~1
        node dist\main.js import --file "%~1"
    )
)
