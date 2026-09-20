@echo off
title Push to GitHub - Starlight Stays
cd /d "%~dp0"

echo ============================================================
echo   Pushing Starlight Stays Platform to GitHub
echo   Repository: https://github.com/pushkar3107/SOA.git
echo ============================================================
echo.

echo Attempting git push...
git push -u origin main

if %ERRORLEVEL% neq 0 (
    echo.
    echo ============================================================
    echo GitHub sign-in required.
    echo Opening GitHub browser sign-in...
    echo ============================================================
    git-credential-manager github login
    echo.
    echo Retrying push after sign-in...
    git push -u origin main
)

if %ERRORLEVEL% equ 0 (
    echo.
    echo ============================================================
    echo   SUCCESS! Pushed to https://github.com/pushkar3107/SOA.git
    echo ============================================================
) else (
    echo.
    echo ============================================================
    echo If browser sign-in fails, you can push using a Personal
    echo Access Token (PAT):
    echo 1. Generate token at https://github.com/settings/tokens (repo scope)
    echo 2. Run: git push https://TOKEN@github.com/pushkar3107/SOA.git main
    echo ============================================================
)

pause
