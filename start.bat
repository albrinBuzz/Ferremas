@echo off

setlocal enabledelayedexpansion



REM Ejecutar Maven Wrapper y redirigir salida a consola y log

.\mvnw.cmd spring-boot:run 

pause
endlocal
