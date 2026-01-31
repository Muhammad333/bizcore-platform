#!/bin/bash
# ============================================
# BizCore Database Setup Script (Linux/Mac)
# ============================================
# This script creates the database and runs all setup scripts
# Usage: chmod +x setup.sh && ./setup.sh
# ============================================

echo ""
echo "========================================"
echo "  BizCore Database Setup"
echo "========================================"
echo ""

# Configuration - Adjust these if needed
PG_USER="postgres"
DB_NAME="bizcore"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "Configuration:"
echo "  User: $PG_USER"
echo "  Database: $DB_NAME"
echo ""

# Step 1: Drop existing database (if exists)
echo -e "[Step 1/5] Dropping existing database (if exists)..."
psql -U $PG_USER -c "DROP DATABASE IF EXISTS $DB_NAME;" 2>/dev/null
echo -e "${GREEN}Done.${NC}"
echo ""

# Step 2: Create new database
echo -e "[Step 2/5] Creating new database..."
psql -U $PG_USER -c "CREATE DATABASE $DB_NAME;"
if [ $? -ne 0 ]; then
    echo -e "${RED}ERROR: Failed to create database${NC}"
    echo "Make sure PostgreSQL is running and user '$PG_USER' has CREATE DATABASE permission."
    exit 1
fi
echo -e "${GREEN}Done.${NC}"
echo ""

# Step 3: Create tables
echo -e "[Step 3/5] Creating tables..."
psql -U $PG_USER -d $DB_NAME -f setup/tables.sql
if [ $? -ne 0 ]; then
    echo -e "${RED}ERROR: Failed to create tables${NC}"
    exit 1
fi
echo -e "${GREEN}Done.${NC}"
echo ""

# Step 4: Insert default data
echo -e "[Step 4/5] Inserting default data..."
psql -U $PG_USER -d $DB_NAME -f setup/default-data.sql
if [ $? -ne 0 ]; then
    echo -e "${RED}ERROR: Failed to insert default data${NC}"
    exit 1
fi
echo -e "${GREEN}Done.${NC}"
echo ""

# Step 5: Load translations
echo -e "[Step 5/5] Loading translations..."
psql -U $PG_USER -d $DB_NAME -f setup/translations.sql
if [ $? -ne 0 ]; then
    echo -e "${RED}ERROR: Failed to load translations${NC}"
    exit 1
fi
echo -e "${GREEN}Done.${NC}"
echo ""

echo "========================================"
echo -e "  ${GREEN}Setup Completed Successfully!${NC}"
echo "========================================"
echo ""
echo "Database '$DB_NAME' is ready."
echo ""
echo -e "${YELLOW}LOGIN CREDENTIALS:${NC}"
echo "  Username: admin"
echo "  Password: admin123"
echo ""
echo -e "${RED}IMPORTANT: Change the admin password after first login!${NC}"
echo ""
echo "NEXT STEPS:"
echo "  1. Start backend:  cd bizcore && mvn spring-boot:run"
echo "  2. Start frontend: cd bizcore/webapp && npm run dev"
echo "  3. Open browser:   http://localhost:5173"
echo ""
