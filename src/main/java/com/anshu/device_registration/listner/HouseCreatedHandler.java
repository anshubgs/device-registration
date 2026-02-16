package com.anshu.device_registration.listner;

import org.springframework.stereotype.Component;

import com.anshu.device_registration.event.UserSyncedEvent;
import com.anshu.device_registration.model.CachedUser;
import com.anshu.device_registration.repository.CachedUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HouseCreatedHandler implements UserEventHandler {

    private final CachedUserRepository repository;

    @Override
    public String getEventType() {
        return "HOUSE_CREATED";
    }

    @Override
    public void handle(UserSyncedEvent event) {

        repository.findByUuid(event.userUuid())
                .ifPresentOrElse(
                    user -> {
                        user.setHouseUuid(event.houseUuid());
                        repository.save(user);
                        log.info("CachedUser houseUuid updated: {}", event.userUuid());
                    },
                    () -> log.warn("User not found for house assignment: {}", event.userUuid())
                );
    }
}
