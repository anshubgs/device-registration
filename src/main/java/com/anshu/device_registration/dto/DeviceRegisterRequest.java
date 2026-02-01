package com.anshu.device_registration.dto;

import java.util.UUID;

public record DeviceRegisterRequest(UUID uuid, String name, String deviceType) {

}
