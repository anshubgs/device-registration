package com.anshu.device_registration.controller;

import com.anshu.device_registration.dto.DeviceDashboardResponse;
import com.anshu.device_registration.service.DeviceDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DeviceDashboardController {

    private final DeviceDashboardService dashboardService;

    public DeviceDashboardController(DeviceDashboardService dashboardService){
        this.dashboardService = dashboardService;
    }
    /**
     * GET /api/v1/devices/dashboard
     * Admin-only API to fetch dashboard data:
     * - total devices
     * - active devices
     * - recent devices
     *
     * Headers required:
     * - X-USER-UUID
     * - X-USER-ROLE (must be ADMIN)
     */

    @GetMapping
    public ResponseEntity<DeviceDashboardResponse> getDashBoard(
            @RequestHeader("X-USER-UUID") UUID uuid,
             @RequestHeader("X-USER-ROLE") String userRole,
            @RequestParam(defaultValue = "5") int recentLimit){

        DeviceDashboardResponse response = dashboardService.getDashBoard(uuid, userRole,recentLimit);

        return  ResponseEntity.ok(response);
    }
}
