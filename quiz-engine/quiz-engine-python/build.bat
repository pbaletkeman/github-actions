@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Python - Build ===
echo Creating virtual environment...
python -m venv venv
if %ERRORLEVEL% NEQ 0 (
    echo Failed to create virtual environment. Ensure Python is installed.
    exit /b %ERRORLEVEL%
)
echo Activating virtual environment...
call venv\Scripts\activate.bat
echo Installing dependencies...
pip install -r requirements.txt
if %ERRORLEVEL% NEQ 0 (
    echo Failed to install dependencies!
    exit /b %ERRORLEVEL%
)
echo Build successful! Virtual environment ready in venv\
echo To activate manually: venv\Scripts\activate
