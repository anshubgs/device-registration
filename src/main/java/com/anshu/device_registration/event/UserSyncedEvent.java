package com.anshu.device_registration.event;

import java.util.UUID;

import lombok.Builder;

@Builder
public record UserSyncedEvent(
        String eventType,   // e.g., "USER_CREATED"
        UUID userUuid,
        UUID houseUuid,
        String role,
        String status
) {}
