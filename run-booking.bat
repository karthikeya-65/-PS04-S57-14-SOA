@echo off
title Starlight Stays - Booking Service (:8083)
for /f "delims=" %%I in ("%~dp0booking-service") do cd /d "%%~sI"
call ..\mvnw.bat spring-boot:run
pause
