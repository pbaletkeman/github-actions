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

REM Extract total coverage percentage
for /f "tokens=3" %%A in ('go tool cover -func=coverage.out ^| findstr /C:"^total:"') do set TOTAL=%%A
set TOTAL=%TOTAL:%%=%

echo.
echo Total coverage: %TOTAL%%%

REM Delegate floating-point comparison to Go
echo package main > %TEMP%\cov_check.go
echo import "fmt" >> %TEMP%\cov_check.go
echo import "os" >> %TEMP%\cov_check.go
echo import "strconv" >> %TEMP%\cov_check.go
echo func main() { v, _ := strconv.ParseFloat(os.Args[1], 64); if v < 90 { fmt.Printf("ERROR: Coverage %%.1f%%%% is below 90%%%%.\n", v); os.Exit(1) }; fmt.Printf("Coverage check passed (%%.1f%%%% >= 90%%%%)\n", v) } >> %TEMP%\cov_check.go
go run %TEMP%\cov_check.go %TOTAL%
if %ERRORLEVEL% neq 0 exit /b 1
