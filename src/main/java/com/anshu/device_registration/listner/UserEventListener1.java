package com.anshu.device_registration.listner;

import com.anshu.device_registration.event.UserSyncedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener1 {

    private final ObjectMapper objectMapper;
    private final UserEventHandlerRegistry handlerRegistry;

    @ServiceActivator(inputChannel = "userInputChannel")
    public void receiveMessage(Message<String> message) {

        String payload = message.getPayload();

        try {
            log.info("Received UserSyncedEvent: {}", payload);

            UserSyncedEvent event = objectMapper.readValue(payload, UserSyncedEvent.class);

            log.info("Parsed Event -> eventType: {}, userUuid: {}, houseUuid: {}, role: {}, status: {}",
                    event.eventType(),
                    event.userUuid(),
                    event.houseUuid(),
                    event.role(),
                    event.status()
            );

            handlerRegistry.handle(event);

            log.info("Event processed successfully: {}", event.eventType());

        } catch (Exception e) {
            log.error("Failed to process UserSyncedEvent. Payload: {}", payload, e);
        }
    }
}
