@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Dart - Start Quiz ===
if "%~1"=="" (
    echo Starting quiz with 10 questions ^(default^)...
    dart run lib/main.dart quiz
) else (
    echo Starting quiz with %~1 questions...
    dart run lib/main.dart quiz --questions %~1
)
