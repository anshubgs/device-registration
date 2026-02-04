package com.anshu.device_registration.model;

import com.anshu.device_registration.dto.DeviceResponse;

public class DeviceMapper {

    public static DeviceResponse toResponse(Device device){

        return DeviceResponse.builder()
                .deviceUuid(device.getUuid())
                .name(device.getName())
                .deviceType(device.getDeviceType())
                .status(device.getStatus())
                .createdAt(device.getCreatedAt())
                .updatedAt(device.getUpdatedAt())
                .build();
    }
}