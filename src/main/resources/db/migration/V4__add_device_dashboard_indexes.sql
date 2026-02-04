-- Index for device status (ACTIVE count)
CREATE INDEX IF NOT EXISTS idx_device_status
ON device.device (status);

-- Index for recent devices (ORDER BY updated_at DESC)
CREATE INDEX IF NOT EXISTS idx_device_updated_at
ON device.device (updated_at DESC);

-- Composite index (future ready)
CREATE INDEX IF NOT EXISTS idx_device_status_updated_at
ON device.device (status, updated_at DESC);

CREATE INDEX idx_device_created_at
ON device.device(created_at DESC);
