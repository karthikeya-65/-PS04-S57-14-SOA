@echo off
title Starlight Stays - Room Service (:8082)
cd /d "%~dp0"
call mvnw.bat -pl room-service spring-boot:run
pause
