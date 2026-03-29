@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Dart - View History ===
if "%~1"=="" (
    echo Showing all sessions...
    dart run lib/main.dart history
) else (
    echo Showing session: %~1
    dart run lib/main.dart history --session-id "%~1"
)
