@echo off
title Starlight Stays - User Service (:8081)
cd /d "%~dp0"
call mvnw.bat -pl user-service spring-boot:run
pause
