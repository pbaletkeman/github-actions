@echo off
setlocal
cd /d "%~dp0"
set "JAVA_HOME=C:\Users\Pete\.jdks\corretto-21.0.2"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo === Quiz Engine Spring Boot - Start Quiz ===
if not exist "build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar" (
    echo JAR not found. Run build.bat first.
    exit /b 1
)
echo Starting Spring Boot application for quiz...
echo.
echo Once the server starts:
echo   Web UI:    http://localhost:8080
echo   Start API: POST http://localhost:8080/api/quiz/start
echo              Body: {"numQuestions": 10}
echo.
echo Press Ctrl+C to stop the server when done.
echo.
java -jar build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar
