@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Java - Import Questions ===
if not exist "build\libs\quiz-engine.jar" (
    echo JAR not found. Run build.bat first.
    exit /b 1
)
if "%~1"=="" (
    echo No file specified. Please provide a markdown file path.
    echo Usage: import.bat ^<questions.md^>
    echo   Example: import.bat questions.md
    exit /b 1
) else (
    echo Importing from file: %~1
    java -jar build\libs\quiz-engine.jar import "%~1"
)
