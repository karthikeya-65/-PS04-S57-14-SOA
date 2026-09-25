@echo off
title Starlight Stays - API Gateway & Web App (:8088)
for /f "delims=" %%I in ("%~dp0api-gateway") do cd /d "%%~sI"
call ..\mvnw.bat spring-boot:run
pause
