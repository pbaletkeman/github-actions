@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Python - View History ===
if not exist "venv\Scripts\activate.bat" (
    echo Virtual environment not found. Run build.bat first.
    exit /b 1
)
call venv\Scripts\activate.bat
echo Showing quiz history summary...
python scripts\view_history.py --summary
