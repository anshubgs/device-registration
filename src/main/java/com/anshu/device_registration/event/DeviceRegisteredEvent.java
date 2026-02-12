package com.anshu.device_registration.event;

import java.time.Instant;
import java.util.UUID;

import com.anshu.device_registration.dto.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRegisteredEvent {
    private UUID deviceUuid;
    private String deviceName;
    private String deviceType;
    private String deviceSecret;
    private DeviceStatus status;
    private Instant registeredAt;
    private UUID userUuid;

}
