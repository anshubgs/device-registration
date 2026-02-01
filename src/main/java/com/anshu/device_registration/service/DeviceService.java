package com.anshu.device_registration.service;

import com.anshu.device_registration.dto.DeviceRegisterRequest;
import com.anshu.device_registration.dto.DeviceRegisterResponse;
import com.anshu.device_registration.dto.DeviceValidateRequest;
import com.anshu.device_registration.dto.DeviceValidateResponse;

public interface DeviceService {

    DeviceRegisterResponse registerDevice(DeviceRegisterRequest request);

    DeviceValidateResponse validateDevice(DeviceValidateRequest request);
}
