@echo off
REM Script to run Maven from the embedded tools directory
REM This allows 'mvn' to be executed from anywhere

setlocal enabledelayedexpansion

REM Detecta la ruta del script actual
set MAVEN_HOME=%~dp0..\tools\apache-maven-3.9.6

REM Ejecuta mvn.cmd con los argumentos pasados
"%MAVEN_HOME%\bin\mvn.cmd" %*

endlocal

