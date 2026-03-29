@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Rust - Start Quiz ===
set "PATH=%USERPROFILE%\.cargo\bin;%PATH%"
if "%~1"=="" (
    echo Starting quiz with 10 questions ^(default^)...
    cargo run --release -- quiz
) else (
    echo Starting quiz with %~1 questions...
    cargo run --release -- quiz --questions %~1
)
