@echo off
REM ParkSmart Docker Startup Script (Windows)
REM Usage: docker-start.bat [dev|prod]

setlocal enabledelayedexpansion

set PROFILE=%1
if "%PROFILE%"=="" set PROFILE=dev

echo ===================================
echo ParkSmart Docker Startup Script
echo ===================================
echo Profile: %PROFILE%
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo Error: Docker is not installed or not in PATH
    exit /b 1
)

REM Check if .env file exists
if not exist ".env" (
    echo Warning: .env file not found. Creating from .env.example...
    copy .env.example .env
    echo Created .env file. Please edit it with your configuration.
    echo.
)

REM Build image
echo Building Docker image...
docker-compose build --no-cache
if errorlevel 1 (
    echo Error: Docker build failed
    exit /b 1
)

REM Start services
echo.
echo Pushing services...

if /i "%PROFILE%"=="dev" (
    echo Starting in DEVELOPMENT mode (with pgAdmin and Redis Commander^)...
    docker-compose --profile dev up -d
) else (
    echo Starting in PRODUCTION mode...
    docker-compose up -d
)

if errorlevel 1 (
    echo Error: Failed to start services
    exit /b 1
)

REM Wait for services
echo.
echo Waiting for services to be healthy...
timeout /t 10 /nobreak

REM Check services
echo.
echo Checking service health...

REM Note: For Windows, we'll just show the startup info
echo.
echo ===================================
echo Services are starting!
echo ===================================
echo.
echo Service URLs:
echo   - ParkSmart App: http://localhost:8082
echo   - Health Check: http://localhost:8082/actuator/health
echo.

if /i "%PROFILE%"=="dev" (
    echo   - pgAdmin: http://localhost:5050 ^(admin@parksmart.local / admin^)
    echo   - Redis Commander: http://localhost:8081
)

echo.
echo Database Access:
echo   - Host: localhost
echo   - Port: 5434
echo   - User: postgres
echo   - Password: postgres
echo   - Database: ParkingLot
echo.
echo Commands:
echo   - Stop services: docker-compose down
echo   - View logs: docker-compose logs -f app
echo   - Restart: docker-compose restart
echo.

