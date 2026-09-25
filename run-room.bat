@echo off
title Starlight Stays - Room Service (:8082)
for /f "delims=" %%I in ("%~dp0room-service") do cd /d "%%~sI"
call ..\mvnw.bat spring-boot:run
pause
