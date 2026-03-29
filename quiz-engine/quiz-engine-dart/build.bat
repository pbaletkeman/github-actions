@echo off
setlocal
cd /d "%~dp0"
echo === Quiz Engine Dart - Build ===
echo Installing dependencies...
dart pub get
if %ERRORLEVEL% NEQ 0 (
    echo Dependency installation failed!
    exit /b %ERRORLEVEL%
)
echo Compiling native executable...
if not exist "bin" mkdir bin
dart compile exe lib/main.dart -o bin\quiz_engine.exe
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b %ERRORLEVEL%
)
echo Build successful! Executable: bin\quiz_engine.exe
