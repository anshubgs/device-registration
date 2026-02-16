-- =========================================
-- Create cached_house table
-- =========================================
CREATE TABLE IF NOT EXISTS device.cached_house (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    owner_uuid UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for cached_house
CREATE INDEX IF NOT EXISTS idx_cached_house_uuid
ON device.cached_house (uuid);

CREATE INDEX IF NOT EXISTS idx_cached_house_owner
ON device.cached_house (owner_uuid);
