-- ============================================
-- BizCore - Reference Tables
-- Created: 2026-01-24
-- Description: Shared lookup/reference data (countries, currencies, units, categories)
-- ============================================

-- Countries
CREATE TABLE ref_countries (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(3) UNIQUE NOT NULL,      -- ISO 3166-1 alpha-3 (USA, UZB, RUS)
    code2 VARCHAR(2) UNIQUE NOT NULL,     -- ISO 3166-1 alpha-2 (US, UZ, RU)
    name VARCHAR(255) NOT NULL,
    local_name VARCHAR(255),
    phone_code VARCHAR(10),
    currency_code VARCHAR(3),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_ref_countries_code ON ref_countries(code);
CREATE INDEX idx_ref_countries_code2 ON ref_countries(code2);
CREATE INDEX idx_ref_countries_active ON ref_countries(active);

-- Currencies
CREATE TABLE ref_currencies (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(3) UNIQUE NOT NULL,      -- ISO 4217 (USD, UZS, RUB)
    name VARCHAR(255) NOT NULL,
    symbol VARCHAR(10),
    decimal_places INT DEFAULT 2,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_ref_currencies_code ON ref_currencies(code);
CREATE INDEX idx_ref_currencies_active ON ref_currencies(active);

-- Measurement Units
CREATE TABLE ref_units (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50),                  -- weight, volume, length, area, piece, etc.
    symbol VARCHAR(20),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_ref_units_code ON ref_units(code);
CREATE INDEX idx_ref_units_category ON ref_units(category);
CREATE INDEX idx_ref_units_active ON ref_units(active);

-- Categories (Generic category system)
CREATE TABLE ref_categories (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT REFERENCES ref_categories(id) ON DELETE CASCADE,
    code VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    type VARCHAR(50),                      -- product, service, article, etc.
    level INT DEFAULT 0,
    display_order INT DEFAULT 0,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_ref_categories_parent ON ref_categories(parent_id);
CREATE INDEX idx_ref_categories_code ON ref_categories(code);
CREATE INDEX idx_ref_categories_type ON ref_categories(type);
CREATE INDEX idx_ref_categories_active ON ref_categories(active);
