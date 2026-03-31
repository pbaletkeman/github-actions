@echo off
REM Run tests and enforce a minimum 90%% coverage threshold.
REM Requires cargo-llvm-cov; falls back to cargo-tarpaulin if unavailable.

set THRESHOLD=90

cargo llvm-cov --version >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo Using cargo-llvm-cov...
    cargo llvm-cov --summary-only > %TEMP%\cov_output.txt 2>&1
    type %TEMP%\cov_output.txt

    for /f "tokens=*" %%L in ('findstr /i "^TOTAL" %TEMP%\cov_output.txt') do set TOTAL_LINE=%%L
    for /f "tokens=*" %%T in ('echo %TOTAL_LINE%') do set LAST=%%T
    set TOTAL=%LAST:%%=%

    echo.
    echo Total coverage: %TOTAL%%%
    echo package main > %TEMP%\cov_check.go
    echo import "fmt" >> %TEMP%\cov_check.go
    echo import "os" >> %TEMP%\cov_check.go
    echo import "strconv" >> %TEMP%\cov_check.go
    echo func main() { v, _ := strconv.ParseFloat(os.Args[1], 64); t := float64(%THRESHOLD%); if v ^< t { fmt.Printf("ERROR: Coverage %%.1f%%%% is below %d%%%%.\n", v, %THRESHOLD%); os.Exit(1) }; fmt.Printf("Coverage check passed (%%.1f%%%% ^>= %d%%%%)\n", v, %THRESHOLD%) } >> %TEMP%\cov_check.go
    go run %TEMP%\cov_check.go %TOTAL%
    exit /b %ERRORLEVEL%
)

cargo tarpaulin --version >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo cargo-llvm-cov not found, using cargo-tarpaulin...
    cargo tarpaulin --fail-under %THRESHOLD%
    exit /b %ERRORLEVEL%
)

echo ERROR: Neither cargo-llvm-cov nor cargo-tarpaulin is installed.
echo Install one with:
echo   cargo install cargo-llvm-cov
echo   cargo install cargo-tarpaulin
exit /b 1
