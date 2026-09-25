@echo off
setlocal enabledelayedexpansion

:: Convert current directory to 8.3 short path to eliminate non-ASCII (e.g. Korean) characters and spaces
for %%I in ("%CD%") do cd /d "%%~sI"

:: Prefer existing JAVA_HOME if valid, else resolve from java in PATH
if defined JAVA_HOME (
    set "JH=%JAVA_HOME%"
    for /f "tokens=*" %%a in ("!JH!") do set "JH=%%a"
    if exist "!JH!\bin\java.exe" (
        set "JAVA_HOME=!JH!"
        goto find_mvn
    )
)

:: Auto-detect JAVA_HOME from java.exe in PATH
for /f "delims=" %%I in ('where java.exe 2^>nul') do (
    for %%P in ("%%~dpI..") do set "JAVA_HOME=%%~fsP"
    goto find_mvn
)

:: Check tools dir
if exist "%USERPROFILE%\tools" (
    for /d %%D in ("%USERPROFILE%\tools\jdk*") do (
        if exist "%%D\bin\java.exe" (
            set "JAVA_HOME=%%D"
            goto find_mvn
        )
    )
)

echo [ERROR] Neither JAVA_HOME nor java.exe was found!
exit /b 1

:find_mvn
:: Find mvn.cmd on PATH or in tools dir
set "MVN_CMD="
for /f "delims=" %%I in ('where mvn.cmd 2^>nul') do (
    set "MVN_CMD=%%I"
    goto exec_mvn
)
if exist "%USERPROFILE%\tools\apache-maven-3.9.9\bin\mvn.cmd" (
    set "MVN_CMD=%USERPROFILE%\tools\apache-maven-3.9.9\bin\mvn.cmd"
    goto exec_mvn
)
where mvn >nul 2>&1
if !ERRORLEVEL! equ 0 (
    set "MVN_CMD=mvn"
    goto exec_mvn
)

echo [ERROR] Maven (mvn.cmd) was not found on PATH!
exit /b 1

:exec_mvn
:: Check if run with no arguments
if "%~1"=="" (
    echo ============================================================
    echo Starlight Stays - Maven Wrapper
    echo Java Home: !JAVA_HOME!
    echo Maven:     !MVN_CMD!
    echo ============================================================
    echo Running reactor validation...
    call "!MVN_CMD!" validate
    exit /b !ERRORLEVEL!
)

:: If invoked with -pl <module> spring-boot:run, enter that module directory first
:: to prevent JVM argfile encoding errors on non-ASCII paths.
if /i "%~1"=="-pl" (
    if not "%~2"=="" (
        set "MODULE=%~2"
        set "MODULE=!MODULE::=!"
        if exist "!MODULE!\pom.xml" (
            shift
            shift
            cd /d "!MODULE!"
            call "!MVN_CMD!" %1 %2 %3 %4 %5 %6 %7 %8 %9
            exit /b !ERRORLEVEL!
        )
    )
)

call "!MVN_CMD!" %*
exit /b !ERRORLEVEL!
