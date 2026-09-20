@echo off
setlocal

set "TOOLS_DIR=%USERPROFILE%\tools"

:: Find JDK 17
for /d %%D in ("%TOOLS_DIR%\jdk-17*") do (
    set "JAVA_HOME=%%D"
)

if not defined JAVA_HOME (
    echo JAVA_HOME not found in %TOOLS_DIR%!
    exit /b 1
)

set "PATH=%JAVA_HOME%\bin;%TOOLS_DIR%\apache-maven-3.9.9\bin;%PATH%"

"%TOOLS_DIR%\apache-maven-3.9.9\bin\mvn.cmd" %*
