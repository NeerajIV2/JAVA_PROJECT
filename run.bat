@echo off
echo ========================================
echo Recipe Management System - Quick Start
echo ========================================
echo.

REM Check for Maven in common locations
set MAVEN_HOME=
if exist "%ProgramFiles%\Apache\maven" set MAVEN_HOME=%ProgramFiles%\Apache\maven
if exist "%ProgramFiles(x86)%\Apache\maven" set MAVEN_HOME=%ProgramFiles(x86)%\Apache\maven
if exist "%USERPROFILE%\maven" set MAVEN_HOME=%USERPROFILE%\maven
if exist "%LOCALAPPDATA%\Programs\maven" set MAVEN_HOME=%LOCALAPPDATA%\Programs\maven

REM Try to use Maven if found
if defined MAVEN_HOME (
    echo Found Maven at: %MAVEN_HOME%
    set PATH=%MAVEN_HOME%\bin;%PATH%
    goto :run
)

REM Check if mvn is in PATH
where mvn >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Maven found in PATH
    goto :run
)

echo.
echo ERROR: Maven not found!
echo.
echo Please install Maven:
echo 1. Download from: https://maven.apache.org/download.cgi
echo 2. Extract to a folder (e.g., C:\Program Files\Apache\maven)
echo 3. Add to PATH: %USERPROFILE%\maven\bin
echo.
echo Or use Chocolatey: choco install maven
echo.
pause
exit /b 1

:run
echo.
echo Building project...
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo Starting Tomcat server...
echo Application will be available at: http://localhost:8080/
echo.
echo Press Ctrl+C to stop the server
echo.
call mvn tomcat7:run

pause

