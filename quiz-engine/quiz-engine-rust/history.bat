@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Rust - View History ===
echo Showing quiz history...
cargo run --release -- history
