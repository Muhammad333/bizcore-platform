@echo off
REM ============================================
REM BizCore Database Setup Script (Windows)
REM ============================================
REM This script creates the database and runs all setup scripts
REM Usage: Just double-click setup.bat or run from command line
REM ============================================

echo.
echo ========================================
echo   BizCore Database Setup
echo ========================================
echo.

REM Configuration - Adjust these if needed
set PSQL_PATH="C:\Program Files\PostgreSQL\17\bin\psql.exe"
set PG_USER=postgres
set DB_NAME=bizcore

REM Check if psql exists
if not exist %PSQL_PATH% (
    echo ERROR: PostgreSQL not found at %PSQL_PATH%
    echo Please install PostgreSQL or update PSQL_PATH in this script.
    pause
    exit /b 1
)

echo Configuration:
echo   PostgreSQL: %PSQL_PATH%
echo   User: %PG_USER%
echo   Database: %DB_NAME%
echo.

REM Step 1: Drop existing database (if exists)
echo [Step 1/5] Dropping existing database (if exists)...
%PSQL_PATH% -U %PG_USER% -c "DROP DATABASE IF EXISTS %DB_NAME%;" 2>nul
echo Done.
echo.

REM Step 2: Create new database
echo [Step 2/5] Creating new database...
%PSQL_PATH% -U %PG_USER% -c "CREATE DATABASE %DB_NAME%;"
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to create database
    echo Make sure PostgreSQL is running and user '%PG_USER%' has CREATE DATABASE permission.
    pause
    exit /b 1
)
echo Done.
echo.

REM Step 3: Create tables
echo [Step 3/5] Creating tables...
%PSQL_PATH% -U %PG_USER% -d %DB_NAME% -f setup/tables.sql
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to create tables
    pause
    exit /b 1
)
echo Done.
echo.

REM Step 4: Insert default data
echo [Step 4/5] Inserting default data...
%PSQL_PATH% -U %PG_USER% -d %DB_NAME% -f setup/default-data.sql
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to insert default data
    pause
    exit /b 1
)
echo Done.
echo.

REM Step 5: Load translations
echo [Step 5/5] Loading translations...
%PSQL_PATH% -U %PG_USER% -d %DB_NAME% -f setup/translations.sql
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to load translations
    pause
    exit /b 1
)
echo Done.
echo.

echo ========================================
echo   Setup Completed Successfully!
echo ========================================
echo.
echo Database '%DB_NAME%' is ready.
echo.
echo LOGIN CREDENTIALS:
echo   Username: admin
echo   Password: admin123
echo.
echo IMPORTANT: Change the admin password after first login!
echo.
echo NEXT STEPS:
echo   1. Start backend:  cd bizcore ^&^& mvn spring-boot:run
echo   2. Start frontend: cd bizcore/webapp ^&^& npm run dev
echo   3. Open browser:   http://localhost:5173
echo.
pause
