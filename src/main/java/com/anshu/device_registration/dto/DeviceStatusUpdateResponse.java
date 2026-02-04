package com.anshu.device_registration.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DeviceStatusUpdateResponse(UUID deviceUuid,
                                         DeviceStatus oldStatus,
                                         DeviceStatus newStatus,
                                         LocalDateTime updatedAt,
                                         UUID updatedBy) {
}
