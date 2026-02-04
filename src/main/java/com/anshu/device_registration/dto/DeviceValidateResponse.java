package com.anshu.device_registration.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeviceValidateResponse {
    private UUID deviceUuid;
    private DeviceStatus status;
}
