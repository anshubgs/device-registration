package com.anshu.device_registration.service;

import com.anshu.device_registration.dto.DeviceDashboardResponse;
import com.anshu.device_registration.dto.DeviceStatus;
import com.anshu.device_registration.dto.RecentDeviceDto;
import com.anshu.device_registration.exception.AccessDeniedException;
import com.anshu.device_registration.exception.RoleMismatchException;
import com.anshu.device_registration.exception.UserNotFoundException;
import com.anshu.device_registration.model.CachedUser;
import com.anshu.device_registration.model.Device;
import com.anshu.device_registration.repository.CachedUserRepository;
import com.anshu.device_registration.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DeviceDashboardServiceImpl implements DeviceDashboardService{

    private final DeviceRepository deviceRepository;
    private final CachedUserRepository cachedUserRepository;

    public DeviceDashboardServiceImpl(
            DeviceRepository deviceRepository,
            CachedUserRepository cachedUserRepository
    ) {
        this.deviceRepository = deviceRepository;
        this.cachedUserRepository = cachedUserRepository;
    }

    /**
     * Fetch dashboard data for admin
     * Validates user from cached_user table
     */
    @Override
    public DeviceDashboardResponse getDashBoard(UUID uuid, String userRole, int recentLimit) {

        log.debug("[VALIDATE ADMIN] userUuid={} role={}", uuid, userRole);

        // 🔹 Step 1: Validate Admin User
        CachedUser user = cachedUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + uuid));

        if(!"ACTIVE".equalsIgnoreCase(user.getStatus())){
            log.warn("[ACCESS DENIED] User inactive: {}", uuid);
            throw new AccessDeniedException("User is inactive");
        }

        if(!user.getRole().equalsIgnoreCase(userRole)){
            log.warn("[ROLE MISMATCH] Expected={} Provided={}", user.getRole(), userRole);
            throw new RoleMismatchException("Role mismatch");
        }

        if(!"ADMIN".equalsIgnoreCase(userRole)){
            log.warn("[ACCESS DENIED] Non-admin user tried to access dashboard: {}", uuid);
            throw new AccessDeniedException("Access denied: ADMIN only");
        }

        log.debug("[ADMIN VALIDATED] userUuid={}", uuid);

        // 🔹 Step 2: Fetch Dashboard Data
        long totalDevices = deviceRepository.count();
        long activeDevices = deviceRepository.countByStatus(DeviceStatus.ACTIVE);

        log.debug("[FETCH DEVICES] total={} active={}", totalDevices, activeDevices);

        List<Device> devices = deviceRepository.findRecentDevice(PageRequest.of(0,recentLimit));

        List<RecentDeviceDto> recentDevices  = devices.stream().map(
                d -> new RecentDeviceDto(
                        d.getUuid(),
                        d.getName(),
                        d.getStatus(),
                        d.getUpdatedAt()
                )
        ).toList();

        log.debug("[RECENT DEVICES] count={}", recentDevices.size());

        return DeviceDashboardResponse.builder()
                .totalDevices(totalDevices)
                .activeDevices(activeDevices)
                .recentDevices(recentDevices)
                .build();
    }

}
