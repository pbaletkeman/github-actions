@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Node.js - Build ===
echo Installing dependencies...
npm install
if %ERRORLEVEL% NEQ 0 (
    echo npm install failed!
    exit /b %ERRORLEVEL%
)
echo Compiling TypeScript...
npm run build
if %ERRORLEVEL% NEQ 0 (
    echo TypeScript compilation failed!
    exit /b %ERRORLEVEL%
)
echo Build successful! Output: dist\
