package com.anshu.device_registration.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DeviceDashboardResponse(
        long totalDevices,
        long activeDevices,
        List<RecentDeviceDto> recentDevices
) {
}
