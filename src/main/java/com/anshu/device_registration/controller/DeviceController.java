package com.anshu.device_registration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anshu.device_registration.dto.DeviceRegisterRequest;
import com.anshu.device_registration.dto.DeviceRegisterResponse;
import com.anshu.device_registration.dto.DeviceValidateRequest;
import com.anshu.device_registration.dto.DeviceValidateResponse;
import com.anshu.device_registration.service.DeviceService;

@RestController
@RequestMapping("api/v1/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/register")
    public ResponseEntity<DeviceRegisterResponse> register(@RequestBody DeviceRegisterRequest request) {
        DeviceRegisterResponse response = deviceService.registerDevice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 🔹 Device Validate (Image Service call)
    @PostMapping("/validate")
    public ResponseEntity<DeviceValidateResponse> validate(@RequestBody DeviceValidateRequest request) {

        DeviceValidateResponse response = deviceService.validateDevice(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
