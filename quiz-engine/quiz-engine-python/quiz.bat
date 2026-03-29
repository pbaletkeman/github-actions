@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Python - Start Quiz ===
if not exist "venv\Scripts\activate.bat" (
    echo Virtual environment not found. Run build.bat first.
    exit /b 1
)
call venv\Scripts\activate.bat
if "%~1"=="" (
    echo Starting quiz with 20 questions ^(default^)...
    python -m quiz_engine.main --questions 20
) else (
    echo Starting quiz with %~1 questions...
    python -m quiz_engine.main --questions %~1
)
