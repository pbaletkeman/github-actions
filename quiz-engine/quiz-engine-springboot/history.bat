@echo off
setlocal
cd /d "%~dp0"
set "JAVA_HOME=C:\Users\Pete\.jdks\corretto-21.0.2"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo === Quiz Engine Spring Boot - View History ===
if not exist "build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar" (
    echo JAR not found. Run build.bat first.
    exit /b 1
)
echo Starting Spring Boot application to view history...
echo.
echo Once the server starts, view history via:
echo   REST API:   http://localhost:8080/api/history
echo   Web UI:     http://localhost:8080
echo.
echo Press Ctrl+C to stop the server when done.
echo.
java -jar build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar
