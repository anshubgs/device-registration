package com.anshu.device_registration.dto;

import lombok.Builder;

@Builder
public record DeviceStatusUpdateRequest(

        DeviceStatus status) {
}
