@echo off
title Push to GitHub - Starlight Stays
cd /d "%~dp0"

echo ============================================================
echo   Pushing Starlight Stays Platform to GitHub
echo   Repository: https://github.com/pushkar3107/SOA.git
echo ============================================================
echo.

git push -u origin main

if %ERRORLEVEL% equ 0 (
    echo.
    echo ============================================================
    echo   SUCCESS! Pushed to https://github.com/pushkar3107/SOA.git
    echo ============================================================
) else (
    echo.
    echo ============================================================
    echo If authentication failed, you can generate a Personal Access
    echo Token (PAT) on GitHub (with repo scope):
    echo   https://github.com/settings/tokens
    echo.
    echo And push directly using:
    echo   git push https://TOKEN@github.com/pushkar3107/SOA.git main
    echo ============================================================
)

pause
