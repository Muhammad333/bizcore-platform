@echo off
REM BizCore Database Setup Script
REM This script drops and recreates the database, then runs all migration scripts

echo ========================================
echo BizCore Database Setup
echo ========================================
echo.

REM Set PostgreSQL bin path (adjust if needed)
set PSQL_PATH="C:\Program Files\PostgreSQL\17\bin\psql.exe"
set PG_USER=postgres
set DB_NAME=bizcore

echo Step 1: Dropping existing database...
%PSQL_PATH% -U %PG_USER% -c "DROP DATABASE IF EXISTS %DB_NAME%;"
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to drop database
    pause
    exit /b 1
)
echo Database dropped successfully.
echo.

echo Step 2: Creating new database...
%PSQL_PATH% -U %PG_USER% -c "CREATE DATABASE %DB_NAME%;"
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to create database
    pause
    exit /b 1
)
echo Database created successfully.
echo.

echo Step 3: Running core tables script...
%PSQL_PATH% -U %PG_USER% -d %DB_NAME% -f core/tables.sql
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to create core tables
    pause
    exit /b 1
)
echo Core tables created successfully.
echo.

echo Step 4: Running references tables script...
%PSQL_PATH% -U %PG_USER% -d %DB_NAME% -f references/tables.sql
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to create reference tables
    pause
    exit /b 1
)
echo Reference tables created successfully.
echo.

echo Step 5: Running initial migration (test data)...
%PSQL_PATH% -U %PG_USER% -d %DB_NAME% -f migrations/2026/01/24.sql
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to load initial data
    pause
    exit /b 1
)
echo Initial data loaded successfully.
echo.

echo ========================================
echo Database setup completed successfully!
echo ========================================
echo.
echo You can now start the application with: mvn spring-boot:run
echo.
echo Default login credentials:
echo   Company: DEMO
echo   Username: admin
echo   Password: password123
echo.
pause
