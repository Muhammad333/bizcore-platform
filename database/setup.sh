#!/bin/bash
# BizCore Database Setup Script
# This script drops and recreates the database, then runs all migration scripts

echo "========================================"
echo "BizCore Database Setup"
echo "========================================"
echo ""

# Configuration
PG_USER="postgres"
DB_NAME="bizcore"

# Step 1: Drop existing database
echo "Step 1: Dropping existing database..."
psql -U $PG_USER -c "DROP DATABASE IF EXISTS $DB_NAME;"
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to drop database"
    exit 1
fi
echo "Database dropped successfully."
echo ""

# Step 2: Create new database
echo "Step 2: Creating new database..."
psql -U $PG_USER -c "CREATE DATABASE $DB_NAME;"
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to create database"
    exit 1
fi
echo "Database created successfully."
echo ""

# Step 3: Run core tables script
echo "Step 3: Running core tables script..."
psql -U $PG_USER -d $DB_NAME -f core/tables.sql
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to create core tables"
    exit 1
fi
echo "Core tables created successfully."
echo ""

# Step 4: Run references tables script
echo "Step 4: Running references tables script..."
psql -U $PG_USER -d $DB_NAME -f references/tables.sql
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to create reference tables"
    exit 1
fi
echo "Reference tables created successfully."
echo ""

# Step 5: Run initial migration
echo "Step 5: Running initial migration (test data)..."
psql -U $PG_USER -d $DB_NAME -f migrations/2026/01/24.sql
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to load initial data"
    exit 1
fi
echo "Initial data loaded successfully."
echo ""

echo "========================================"
echo "Database setup completed successfully!"
echo "========================================"
echo ""
echo "You can now start the application with: mvn spring-boot:run"
echo ""
echo "Default login credentials:"
echo "  Company: DEMO"
echo "  Username: admin"
echo "  Password: password123"
echo ""
