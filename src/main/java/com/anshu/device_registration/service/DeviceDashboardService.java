package com.anshu.device_registration.service;

import com.anshu.device_registration.dto.DeviceDashboardResponse;

import java.util.UUID;

public interface DeviceDashboardService {
    DeviceDashboardResponse getDashBoard(UUID uuid, String userRole, int recentLimit);
}
