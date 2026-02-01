package com.anshu.device_registration.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.anshu.device_registration.publisher.DeviceEventPublisher;
import com.anshu.device_registration.event.DeviceRegisteredEvent;
import com.anshu.device_registration.dto.DeviceRegisterRequest;
import com.anshu.device_registration.dto.DeviceRegisterResponse;
import com.anshu.device_registration.dto.DeviceValidateRequest;
import com.anshu.device_registration.dto.DeviceValidateResponse;
import com.anshu.device_registration.exception.DeviceAlreadyRegisteredException;
import com.anshu.device_registration.exception.DeviceInactiveException;
import com.anshu.device_registration.exception.DeviceNotFoundException;
import com.anshu.device_registration.exception.InvalidDeviceSecretException;
import com.anshu.device_registration.model.Device;
import com.anshu.device_registration.repository.DeviceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceEventPublisher eventPublisher;

    public DeviceServiceImpl(DeviceRepository deviceRepository, DeviceEventPublisher eventPublisher) {
        this.deviceRepository = deviceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public DeviceRegisterResponse registerDevice(DeviceRegisterRequest request) {

        if (deviceRepository.existsByUuid(request.uuid())) {
            throw new DeviceAlreadyRegisteredException("Device already registered with UUID: " + request.uuid());
        }

        Device device = Device.builder()
                .uuid(UUID.randomUUID())
                .secret(UUID.randomUUID().toString().replace("-", "").substring(0, 8))
                .name(request.name())
                .deviceType(request.deviceType())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();
        deviceRepository.save(device);

        log.info("[DEVICE REGISTERED] uuid={}", device.getUuid());

        // EVENT PUBLISH (NEW)
        DeviceRegisteredEvent event = new DeviceRegisteredEvent(
                device.getUuid(),
                device.getName(),
                device.getDeviceType(),
                device.getSecret(),
                device.getStatus(),
                device.getCreatedAt().toInstant(ZoneOffset.UTC));

        eventPublisher.publishEvent(event);

        return DeviceRegisterResponse.builder()
                .deviceUuid(device.getUuid())
                .deviceSecret(device.getSecret())
                .status(device.getStatus())
                .build();
    }

    @Override
    @Cacheable(value = "device:validate", key = "#deviceUuid", unless = "#result == null")
    public DeviceValidateResponse validateDevice(DeviceValidateRequest request) {

        log.info("[CACHE MISS] validating device={}", request.deviceUuid());

        Device device = deviceRepository.findByUuid(request.deviceUuid())
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with UUID: " + request.deviceUuid()));

        log.info("[DEVICE VALIDATED] uuid={}", device.getUuid());
        if (!device.getSecret().equals(request.deviceSecret())) {
            throw new InvalidDeviceSecretException("Invalid device secret for UUID: " + request.deviceUuid());

        }

        if (!"ACTIVE".equalsIgnoreCase(device.getStatus())) {
            throw new DeviceInactiveException("Device is not active with UUID: " + request.deviceUuid());
        }

        log.info("[DEVICE VALIDATED & CACHED] uuid={}", device.getUuid());

        return DeviceValidateResponse.builder()
                .deviceUuid(device.getUuid())
                .status(device.getStatus())
                .build();
    }
}
