@echo off
title Starlight Stays - Launcher
for /f "delims=" %%I in ("%~dp0.") do cd /d "%%~sI"

echo ============================================================
echo   Launching Starlight Stays & Resorts Microservices
echo ============================================================

echo [1/5] Starting Eureka Discovery Server (:8761)...
start "Starlight - Eureka Server (:8761)" cmd /c "run-eureka.bat"
timeout /t 14 /nobreak >nul

echo [2/5] Starting User Service (:8081)...
start "Starlight - User Service (:8081)" cmd /c "run-user.bat"

echo [3/5] Starting Room Service (:8082)...
start "Starlight - Room Service (:8082)" cmd /c "run-room.bat"
timeout /t 6 /nobreak >nul

echo [4/5] Starting Booking Service (:8083)...
start "Starlight - Booking Service (:8083)" cmd /c "run-booking.bat"
timeout /t 6 /nobreak >nul

echo [5/5] Starting API Gateway & Web App (:8088)...
start "Starlight - API Gateway (:8088)" cmd /c "run-gateway.bat"

echo ============================================================
echo All 5 Spring Boot Microservices launched in separate windows!
echo Web Portal:       http://localhost:8088
echo Eureka Dashboard: http://localhost:8761
echo ============================================================
pause
