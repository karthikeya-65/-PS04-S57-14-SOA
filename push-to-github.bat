@echo off
title Push to GitHub - Starlight Stays
cd /d "%~dp0"

set "GIT_DIR=%LOCALAPPDATA%\Microsoft\WinGet\Packages\Git.MinGit_Microsoft.Winget.Source_8wekyb3d8bbwe\cmd"
set "GCM_DIR=%LOCALAPPDATA%\Programs\Git Credential Manager"
set "PATH=%GIT_DIR%;%GCM_DIR%;%PATH%"

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
    echo If GitHub authentication is required, you can also push
    echo using a Personal Access Token (PAT):
    echo.
    echo 1. Generate a token at https://github.com/settings/tokens
    echo    (Check the 'repo' scope)
    echo 2. Run:
    echo    git push https://<YOUR_TOKEN>@github.com/pushkar3107/SOA.git main
    echo ============================================================
)

pause
