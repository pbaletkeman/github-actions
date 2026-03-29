@echo off
setlocal
cd /d "%~dp0"
set "JAVA_HOME=C:\Users\Pete\.jdks\corretto-21.0.2"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo === Quiz Engine Spring Boot - Build ===
echo Building JAR with Gradle wrapper...
gradlew.bat build
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b %ERRORLEVEL%
)
echo Build successful! JAR: build\libs\quiz-engine-springboot-0.0.1-SNAPSHOT.jar
