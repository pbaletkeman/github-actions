@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Rust - View History ===
set "PATH=%USERPROFILE%\.cargo\bin;%PATH%"
echo Showing quiz history...
cargo run --release -- history
