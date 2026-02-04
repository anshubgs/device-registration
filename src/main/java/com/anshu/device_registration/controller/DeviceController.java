package com.anshu.device_registration.controller;

import com.anshu.device_registration.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anshu.device_registration.service.DeviceService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/register")
    public ResponseEntity<DeviceRegisterResponse> register(
            @RequestBody DeviceRegisterRequest request,
            @RequestHeader("X-USER-UUID") UUID uuid,
            @RequestHeader("X-USER-ROLE") String userRole) {
        DeviceRegisterResponse response = deviceService.registerDevice(request,uuid,userRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 🔹 Device Validate (Image Service call)
    @PostMapping("/validate")
    public ResponseEntity<DeviceValidateResponse> validate(@RequestBody DeviceValidateRequest request) {

        DeviceValidateResponse response = deviceService.validateDevice(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{deviceUuid}/status")
    public ResponseEntity<DeviceStatusUpdateResponse> updateStatus(
            @PathVariable UUID deviceUuid,
            @RequestHeader("X-USER-UUID") UUID uuid,
            @RequestHeader("X-USER-ROLE") String userRole,
            @RequestBody DeviceStatusUpdateRequest request
            ){

        DeviceStatusUpdateResponse response = deviceService.updateDeviceStatus(deviceUuid,
                uuid,userRole,request);

        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<Page<DeviceResponse>> getDevices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) DeviceStatus status,
            @RequestHeader("X-USER-UUID") UUID userUuid,
            @RequestHeader("X-USER-ROLE") String role
    ) {

        Page<DeviceResponse> devices =
                deviceService.getDevices(page, size, status, userUuid, role);

        return ResponseEntity.ok(devices);
    }


    @GetMapping("/{uuid}")
    public ResponseEntity<DeviceResponse> getDeviceByUuid(

            @PathVariable UUID uuid,
            @RequestHeader("X-USER-UUID") UUID userUuid,
            @RequestHeader("X-USER-ROLE") String role
    ) {

        DeviceResponse response =
                deviceService.getDeviceByUuid(uuid, userUuid, role);

        return ResponseEntity.ok(response);
    }

}
