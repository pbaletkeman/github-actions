@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Dart - Import Questions ===
echo Usage: import.bat [file_or_directory]
echo   Example: import.bat questions.md
echo   Example: import.bat .\questions\
echo.
if "%~1"=="" (
    echo No path specified. Importing from current directory...
    dart run lib/main.dart import --dir .
) else (
    if exist "%~1\" (
        echo Importing from directory: %~1
        dart run lib/main.dart import --dir "%~1"
    ) else (
        echo Importing from file: %~1
        dart run lib/main.dart import --file "%~1"
    )
)
