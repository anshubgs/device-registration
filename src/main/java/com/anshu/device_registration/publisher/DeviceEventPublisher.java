package com.anshu.device_registration.publisher;

import com.anshu.device_registration.event.DeviceRegisteredEvent;
import com.anshu.device_registration.event.DeviceStatusUpdatedEvent;

public interface DeviceEventPublisher {

    void publishDeviceRegistered(DeviceRegisteredEvent event);

    void publishDeviceStatusUpdated(DeviceStatusUpdatedEvent event);
}
