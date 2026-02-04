package com.anshu.device_registration.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class DeviceResponse {

    private UUID deviceUuid;
    private String name;
    private String deviceType;
    private DeviceStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
