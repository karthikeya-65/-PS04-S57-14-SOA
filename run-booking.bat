@echo off
title Starlight Stays - Booking Service (:8083)
cd /d "%~dp0"
call mvnw.bat -pl booking-service spring-boot:run
pause
