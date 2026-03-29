@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Python - Import Questions ===
if not exist "venv\Scripts\activate.bat" (
    echo Virtual environment not found. Run build.bat first.
    exit /b 1
)
call venv\Scripts\activate.bat
echo Usage: import.bat [file_or_directory]
echo   Example: import.bat questions.md
echo   Example: import.bat .\questions\
echo.
if "%~1"=="" (
    echo No path specified. Please provide a file or directory path.
    echo Usage: import.bat <file_or_directory>
    exit /b 1
) else (
    if exist "%~1\" (
        echo Importing from directory: %~1
        python scripts\import_questions.py --dir "%~1"
    ) else (
        echo Importing from file: %~1
        python scripts\import_questions.py --file "%~1"
    )
)
