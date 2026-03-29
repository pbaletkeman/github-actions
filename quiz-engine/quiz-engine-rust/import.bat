@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Rust - Import Questions ===
set "PATH=%USERPROFILE%\.cargo\bin;%PATH%"
if "%~1"=="" (
    echo Usage: import.bat [file]
    echo   Example: import.bat questions.md
    echo.
    echo No file specified. Please provide a markdown file path.
    echo Usage: import.bat ^<questions.md^>
    exit /b 1
) else (
    echo Importing from file: %~1
    cargo run --release -- import --file "%~1"
)
