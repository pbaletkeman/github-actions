@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Rust - Build ===
set "PATH=%USERPROFILE%\.cargo\bin;%PATH%"
echo Building release binary (this may take a few minutes on first run)...
cargo build --release
if %ERRORLEVEL% NEQ 0 (
    echo Build failed! Ensure Rust and a C compiler are installed.
    exit /b %ERRORLEVEL%
)
echo Build successful! Binary: target\release\quiz_engine.exe
