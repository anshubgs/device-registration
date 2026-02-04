-- V3__create_cached_user_table.sql

CREATE TABLE IF NOT EXISTS device.cached_user (
    id BIGSERIAL PRIMARY KEY,
    user_uuid UUID NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_cached_user_user_uuid
    ON device.cached_user (user_uuid);

-- Insert dummy admin user
INSERT INTO device.cached_user (user_uuid, role, status)
VALUES (
    '111e8400-e29b-41d4-a716-446655440000',
    'ADMIN',
    'ACTIVE'
)
ON CONFLICT (user_uuid) DO NOTHING;
