@echo off
title Push to GitHub - Starlight Stays
cd /d "%~dp0"

echo ============================================================
echo   Pushing Starlight Stays Platform to GitHub
echo   Repository: https://github.com/pushkar3107/SOA.git
echo ============================================================
echo.

echo Checking GitHub authentication status...
gh auth status >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo.
    echo ============================================================
    echo Logging in to GitHub...
    echo Your browser will open with a one-time code.
    echo Simply confirm the code in your browser and click Authorize.
    echo ============================================================
    echo.
    gh auth login --web -p https
)

echo.
echo Configuring Git authentication...
gh auth setup-git

echo.
echo Pushing commits to https://github.com/pushkar3107/SOA.git ...
git push -u origin main

if %ERRORLEVEL% equ 0 (
    echo.
    echo ============================================================
    echo   SUCCESS! Pushed to https://github.com/pushkar3107/SOA.git
    echo ============================================================
) else (
    echo.
    echo ============================================================
    echo If needed, you can also push using a GitHub Personal Access Token:
    echo 1. Generate a token at https://github.com/settings/tokens (repo scope)
    echo 2. Run: git push https://TOKEN@github.com/pushkar3107/SOA.git main
    echo ============================================================
)

pause
