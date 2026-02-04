package com.anshu.device_registration.event;

import com.anshu.device_registration.dto.DeviceStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record DeviceStatusUpdatedEvent(
        UUID deviceUuid,
        DeviceStatus oldStatus,
        DeviceStatus newStatus,
        UUID updatedBy,
        Instant updatedAt
) {}