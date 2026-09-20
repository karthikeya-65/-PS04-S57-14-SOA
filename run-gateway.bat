@echo off
title Starlight Stays - API Gateway & Web App (:8080)
cd /d "%~dp0"
call mvnw.bat -pl api-gateway spring-boot:run
pause
