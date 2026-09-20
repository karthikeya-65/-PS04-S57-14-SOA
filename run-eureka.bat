@echo off
title Starlight Stays - Eureka Server (:8761)
cd /d "%~dp0"
call mvnw.bat -pl eureka-server spring-boot:run
pause
