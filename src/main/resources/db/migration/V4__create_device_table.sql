-- =========================================
-- Create device table
-- =========================================
CREATE TABLE IF NOT EXISTS device.device (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    house_uuid UUID NOT NULL,  -- FK target
    secret VARCHAR(100) NOT NULL,
    name VARCHAR(100),
    device_type VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    last_seen TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_device_status
        CHECK (status IN ('ACTIVE','INACTIVE','DISABLED')),

    CONSTRAINT fk_device_house
        FOREIGN KEY (house_uuid)
        REFERENCES device.cached_house(uuid)  -- FK points to correct column
        ON DELETE CASCADE
);

-- Indexes for device table
CREATE INDEX IF NOT EXISTS idx_device_uuid
ON device.device (uuid);

CREATE INDEX IF NOT EXISTS idx_device_house_uuid
ON device.device (house_uuid);

CREATE INDEX IF NOT EXISTS idx_device_status
ON device.device (status);
