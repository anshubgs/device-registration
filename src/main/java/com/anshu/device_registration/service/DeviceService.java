package com.anshu.device_registration.service;

import com.anshu.device_registration.dto.*;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface DeviceService {

    DeviceRegisterResponse registerDevice(DeviceRegisterRequest request,UUID uuid, String userRole);

    DeviceValidateResponse validateDevice(DeviceValidateRequest request);

    DeviceStatusUpdateResponse updateDeviceStatus(UUID deviceUuid, UUID uuid, String userRole, DeviceStatusUpdateRequest request);

    Page<DeviceResponse> getDevices(int page, int size, DeviceStatus status,  UUID userUuid,
                                    String role);

    DeviceResponse getDeviceByUuid(UUID uuid, UUID userUuid, String role);
}
