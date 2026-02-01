package com.anshu.device_registration.dto;

import java.util.UUID;

public record DeviceValidateRequest(UUID deviceUuid, String deviceSecret/* , String status */) {
}