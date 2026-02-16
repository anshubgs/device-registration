package com.anshu.device_registration.listner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.anshu.device_registration.event.UserSyncedEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserEventHandlerRegistry {

    private final Map<String, UserEventHandler> handlerMap;

    public UserEventHandlerRegistry(List<UserEventHandler> handlers) {
        handlerMap = handlers.stream()
                .collect(Collectors.toMap(UserEventHandler::getEventType, h -> h));
    }

    public void handle(UserSyncedEvent event) {

        UserEventHandler handler = handlerMap.get(event.eventType());

        if (handler == null) {
            log.warn("⚠️ No handler found for eventType: {}", event.eventType());
            return;
        }

        handler.handle(event);
    }
}
