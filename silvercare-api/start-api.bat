@echo off
echo ========================================
echo Starting SilverCare REST API (Backend)
echo ========================================
echo Port: 8080
echo Context: /api
echo Full URL: http://localhost:8080/api
echo ========================================
echo.

cd /d "%~dp0"
mvn spring-boot:run

pause
