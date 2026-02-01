package com.anshu.device_registration.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeviceRegisterResponse {

    private UUID deviceUuid;
    private String deviceSecret;
    private String status;

}
