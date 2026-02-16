ALTER TABLE device.cached_user
ADD COLUMN IF NOT EXISTS house_uuid UUID;

CREATE INDEX IF NOT EXISTS idx_cached_user_house_uuid
    ON device.cached_user (house_uuid);
