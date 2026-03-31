@echo off
REM Run tests and enforce a minimum 90%% coverage threshold.
REM Requires cargo-llvm-cov; falls back to cargo-tarpaulin if unavailable.

set THRESHOLD=90

cargo llvm-cov --version >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo Using cargo-llvm-cov...
    cargo llvm-cov --summary-only > %TEMP%\cov_output.txt 2>&1
    if %ERRORLEVEL% neq 0 (
        echo ERROR: cargo-llvm-cov failed.
        exit /b 1
    )
    type %TEMP%\cov_output.txt

    REM Use PowerShell to parse the TOTAL line and compare
    powershell -NoProfile -Command ^
        "$out = Get-Content '%TEMP%\cov_output.txt'; " ^
        "$line = $out | Where-Object { $_ -match '^TOTAL' } | Select-Object -Last 1; " ^
        "if (-not $line) { Write-Error 'Could not find TOTAL line'; exit 1 }; " ^
        "$pct = [double]($line -replace '.*\s+(\d+\.?\d*)%%.*', '$1'); " ^
        "Write-Host \"Total coverage: $pct%%\"; " ^
        "if ($pct -lt %THRESHOLD%) { Write-Error \"ERROR: Coverage $pct%% is below required %THRESHOLD%%.\"; exit 1 } " ^
        "else { Write-Host \"Coverage check passed ($pct%% >= %THRESHOLD%%%.\" }"
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
