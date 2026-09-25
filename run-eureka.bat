@echo off
title Starlight Stays - Eureka Server (:8761)
for /f "delims=" %%I in ("%~dp0eureka-server") do cd /d "%%~sI"
call ..\mvnw.bat spring-boot:run
pause
