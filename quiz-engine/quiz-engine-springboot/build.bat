@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Spring Boot - Build ===
echo Building JAR with Gradle wrapper...
gradlew.bat build
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b %ERRORLEVEL%
)
echo Build successful! JAR: build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar
