@echo off
setlocal
cd /d "%~dp0"
set "JAVA_HOME=C:\Users\Pete\.jdks\corretto-21.0.2"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo === Quiz Engine Spring Boot - Import Questions ===
if not exist "build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar" (
    echo JAR not found. Run build.bat first.
    exit /b 1
)
echo Starting Spring Boot application for question import...
echo.
echo Once the server starts, import questions via the REST API:
echo   POST http://localhost:8080/api/import
echo   Content-Type: application/json
echo   Body: {"content": "<markdown content>", "source": "filename.md"}
echo.
echo Or use curl:
echo   curl -X POST http://localhost:8080/api/import ^
echo        -H "Content-Type: application/json" ^
echo        -d "{\"content\": \"...\", \"source\": \"questions.md\"}"
echo.
echo Press Ctrl+C to stop the server when done.
echo.
java -jar build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar
