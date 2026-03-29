@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Java - View History ===
if not exist "build\libs\quiz-engine.jar" (
    echo JAR not found. Run build.bat first.
    exit /b 1
)
echo Showing quiz history...
java -jar build\libs\quiz-engine.jar history
