package com.anshu.device_registration.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import com.anshu.device_registration.dto.*;
import com.anshu.device_registration.event.DeviceStatusUpdatedEvent;
import com.anshu.device_registration.exception.*;
import com.anshu.device_registration.model.CachedUser;
import com.anshu.device_registration.model.DeviceMapper;
import com.anshu.device_registration.publisher.DeviceEventPublisher;
import com.anshu.device_registration.repository.CachedUserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.anshu.device_registration.event.DeviceRegisteredEvent;
import com.anshu.device_registration.model.Device;
import com.anshu.device_registration.repository.DeviceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceEventPublisher eventPublisher;
    private final CachedUserRepository cachedUserRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository, DeviceEventPublisher eventPublisher,CachedUserRepository cachedUserRepository) {
        this.deviceRepository = deviceRepository;
        this.eventPublisher = eventPublisher;
        this.cachedUserRepository = cachedUserRepository;
    }

    @Override
    public DeviceRegisterResponse registerDevice(DeviceRegisterRequest request,UUID uuid, String userRole) {

        if (deviceRepository.existsByUuid(request.uuid())) {
            throw new DeviceAlreadyRegisteredException("Device already registered with UUID: " + request.uuid());
        }

        Device device = Device.builder()
                .uuid(UUID.randomUUID())
                .secret(UUID.randomUUID().toString().replace("-", "").substring(0, 8))
                .name(request.name())
                .deviceType(request.deviceType())
                .status(DeviceStatus.ACTIVE)
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

        eventPublisher.publishDeviceRegistered(event);

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

        if (device.getStatus() != DeviceStatus.ACTIVE) {
            throw new DeviceInactiveException("Device is not active with UUID: " + request.deviceUuid());
        }

        log.info("[DEVICE VALIDATED & CACHED] uuid={}", device.getUuid());

        return DeviceValidateResponse.builder()
                .deviceUuid(device.getUuid())
                .status(device.getStatus())
                .build();
    }

    @Override
    public DeviceStatusUpdateResponse updateDeviceStatus(UUID deviceUuid, UUID uuid, String userRole, DeviceStatusUpdateRequest request) {

        CachedUser user = cachedUserRepository.findByUuid(uuid).orElseThrow(
                () -> new UserNotFoundException("User Not Found"));

        if(!user.getStatus().equals("ACTIVE")){
            throw new AccessDeniedException("User InActive");
        }

        if(!user.getRole().equalsIgnoreCase(userRole)){
            throw new RoleMismatchException("Role MisMatch");
        }

        if(!userRole.equalsIgnoreCase("ADMIN")){
            throw new AccessDeniedException("Access Denied");
        }

        Device device = deviceRepository.findByUuid(deviceUuid).orElseThrow(
                () -> new DeviceNotFoundException("Device not found: " + deviceUuid));

        DeviceStatus oldStatus = device.getStatus();
        device.setStatus(request.status());
        device.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));

        deviceRepository.save(device);

        //EVENT PUBLISH
        DeviceStatusUpdatedEvent event = DeviceStatusUpdatedEvent.builder()
                .deviceUuid(device.getUuid())
                .oldStatus(oldStatus)
                .newStatus(device.getStatus())
                .updatedBy(uuid)
                .updatedAt(device.getUpdatedAt().toInstant(ZoneOffset.UTC))
                .build();

        eventPublisher.publishDeviceStatusUpdated(event);

        log.info(
                "[DEVICE STATUS UPDATED] {} -> {} device={} by user={}",
                oldStatus, device.getStatus(), deviceUuid, uuid
        );

        // BUILDER RESPONSE
        return DeviceStatusUpdateResponse.builder()
                .deviceUuid(deviceUuid)
                .oldStatus(oldStatus)
                .newStatus(device.getStatus())
                .updatedAt(device.getUpdatedAt())
                .updatedBy(uuid)
                .build();
    }

    @Override
    public Page<DeviceResponse> getDevices(
            int page,
            int size,
            DeviceStatus status,
            UUID userUuid,
            String role
    ) {

        CachedUser user = cachedUserRepository.findByUuid(userUuid)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with uuid: " + userUuid)
                );

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new AccessDeniedException("User is inactive");
        }

        if (!user.getRole().equalsIgnoreCase(role)) {
            throw new RoleMismatchException(
                    "Role mismatch: header=" + role + ", actual=" + user.getRole()
            );
        }

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new AccessDeniedException("Only ADMIN can access devices");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Device> devicePage;

        if (status != null) {
            devicePage = deviceRepository.findByStatus(status, pageable);
        } else {
            devicePage = deviceRepository.findAll(pageable);
        }

        return devicePage.map(DeviceMapper::toResponse);
    }

    @Override
    public DeviceResponse getDeviceByUuid(
            UUID uuid,
            UUID userUuid,
            String role
    ) {

        validateAdminAccess(userUuid, role);

        Device device = deviceRepository
                .findByUuid(uuid)
                .orElseThrow(() ->
                        new DeviceNotFoundException("Device not found"));

        return DeviceMapper.toResponse(device);
    }

    private void validateAdminAccess(UUID userUuid, String role){

        CachedUser user = cachedUserRepository.findByUuid(userUuid)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if(!"ACTIVE".equalsIgnoreCase(user.getStatus())){
            throw new AccessDeniedException("User is inactive");
        }

        if(!user.getRole().equalsIgnoreCase(role)){
            throw new RoleMismatchException("Role mismatch");
        }

        if(!"ADMIN".equalsIgnoreCase(role)){
            throw new AccessDeniedException("Only ADMIN can access device details");
        }
    }



}
