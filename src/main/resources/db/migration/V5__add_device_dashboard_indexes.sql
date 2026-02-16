-- Index on uuid (already present but keeping safe)
CREATE INDEX IF NOT EXISTS idx_device_uuid
ON device.device (uuid);

-- ✅ Updated: Index on house_uuid for dashboard queries
CREATE INDEX IF NOT EXISTS idx_device_house_uuid
ON device.device (house_uuid);

-- Index for device status (ACTIVE count)
CREATE INDEX IF NOT EXISTS idx_device_status
ON device.device (status);

-- Index for recent devices (ORDER BY updated_at DESC)
CREATE INDEX IF NOT EXISTS idx_device_updated_at
ON device.device (updated_at DESC);

-- Composite index (status + updated_at)
CREATE INDEX IF NOT EXISTS idx_device_status_updated_at
ON device.device (status, updated_at DESC);

-- Index for created_at sorting
CREATE INDEX IF NOT EXISTS idx_device_created_at
ON device.device (created_at DESC);
