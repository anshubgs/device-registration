package com.anshu.device_registration.listner;

import com.anshu.device_registration.event.UserSyncedEvent;

public interface UserEventHandler {
    String getEventType();
    void handle(UserSyncedEvent event);
}
