@echo off
title Starlight Stays - User Service (:8081)
for /f "delims=" %%I in ("%~dp0user-service") do cd /d "%%~sI"
call ..\mvnw.bat spring-boot:run
pause
