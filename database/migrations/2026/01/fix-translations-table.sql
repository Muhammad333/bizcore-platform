-- Fix: Create translations table if it doesn't exist
-- Date: 2026-01-24

-- Check if table exists
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'translations') THEN
        -- Create translations table
        CREATE TABLE translations (
            id BIGSERIAL PRIMARY KEY,
            "key" VARCHAR(255) NOT NULL,
            language VARCHAR(10) NOT NULL,
            value TEXT NOT NULL,
            module VARCHAR(100) NOT NULL,
            created_at TIMESTAMP DEFAULT NOW(),
            updated_at TIMESTAMP DEFAULT NOW(),
            CONSTRAINT uk_translations_key_language UNIQUE ("key", language)
        );

        -- Create indexes
        CREATE INDEX idx_translations_language ON translations(language);
        CREATE INDEX idx_translations_module ON translations(module);

        RAISE NOTICE 'Table translations created successfully';
    ELSE
        RAISE NOTICE 'Table translations already exists';
    END IF;
END $$;
