@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Java - Build ===
echo Building JAR with Gradle...
gradle clean build
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b %ERRORLEVEL%
)
echo Build successful! JAR: build\libs\quiz-engine.jar
