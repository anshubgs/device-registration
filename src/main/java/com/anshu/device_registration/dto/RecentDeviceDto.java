package com.anshu.device_registration.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record RecentDeviceDto(
        UUID deviceUuid,
        String name,
        DeviceStatus status,
        LocalDateTime lastUpdated
) {
}
