package com.anshu.device_registration.listner;

import com.anshu.device_registration.event.HouseCreatedEvent;
import com.anshu.device_registration.model.CachedHouse;
import com.anshu.device_registration.model.CachedUser;
import com.anshu.device_registration.repository.CachedHouseRepository;
import com.anshu.device_registration.repository.CachedUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class HouseEventListener {

    private final ObjectMapper objectMapper;
    private final CachedHouseRepository cachedHouseRepository;
    private final CachedUserRepository cachedUserRepository; // 👈 Add this

    @ServiceActivator(inputChannel = "houseInputChannel")
    public void receiveMessage(Message<String> message) {

        try {
            String payload = message.getPayload();
            log.info("Received HouseCreatedEvent: {}", payload);

            HouseCreatedEvent event =
                    objectMapper.readValue(payload, HouseCreatedEvent.class);

            // Save house in cached_house table if not exists
            cachedHouseRepository.findByUuid(event.houseUuid())
                    .orElseGet(() -> {
                        CachedHouse cachedHouse = CachedHouse.builder()
                                .uuid(event.houseUuid())
                                .ownerUuid(event.ownerUuid())
                                .name(event.name())
                                .createdAt(LocalDateTime.now())
                                .build();
                        cachedHouseRepository.save(cachedHouse);
                        log.info("CachedHouse saved successfully: {}", event.houseUuid());
                        return cachedHouse;
                    });

            // Update admin's houseUuid in cached_user table
            cachedUserRepository.findByUuid(event.ownerUuid())
                    .ifPresent(user -> {
                        user.setHouseUuid(event.houseUuid());
                        cachedUserRepository.save(user);
                        log.info("Admin CachedUser updated with houseUuid: {}", event.houseUuid());
                    });

        } catch (Exception e) {
            log.error("Error processing HouseCreatedEvent", e);
        }
    }
}
