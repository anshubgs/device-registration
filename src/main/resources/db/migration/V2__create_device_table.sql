-- Create device table
CREATE TABLE IF NOT EXISTS device.device (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    secret VARCHAR(100) NOT NULL,
    name VARCHAR(100),
    device_type VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create index on uuid
CREATE INDEX IF NOT EXISTS idx_device_uuid
ON device.device (uuid);
