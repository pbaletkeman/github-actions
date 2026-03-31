@echo off
REM Run tests and enforce a minimum 90%% coverage threshold.

echo Running tests with coverage...
set CGO_ENABLED=1
go test ./... -coverprofile=coverage.out -covermode=atomic
if %ERRORLEVEL% neq 0 (
    echo ERROR: Tests failed.
    exit /b 1
)

echo.
echo Coverage summary:
go tool cover -func=coverage.out

REM Use PowerShell to parse total coverage and compare
go tool cover -func=coverage.out > %TEMP%\go_cov_output.txt
powershell -NoProfile -Command ^
    "$out = Get-Content '%TEMP%\go_cov_output.txt'; " ^
    "$line = $out | Where-Object { $_ -match '^total:' } | Select-Object -Last 1; " ^
    "if (-not $line) { Write-Error 'Could not find total: line'; exit 1 }; " ^
    "$pct = [double](($line -split '\s+')[-1].TrimEnd('%%')); " ^
    "Write-Host \"Total coverage: $pct%%\"; " ^
    "if ($pct -lt 90) { Write-Error \"ERROR: Coverage $pct%% is below required 90%%.\"; exit 1 } " ^
    "else { Write-Host \"Coverage check passed ($pct%% >= 90%%).\" }"
exit /b %ERRORLEVEL%
