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
public class UserRegisteredHandler implements UserEventHandler {

    private final CachedUserRepository repository;

    @Override
    public String getEventType() {
        return "USER_CREATED";  // ✅ must match publisher
    }

    @Override
    public void handle(UserSyncedEvent event) {

        repository.findByUuid(event.userUuid())
                .ifPresentOrElse(

                    existing -> {
                        existing.setRole(event.role());
                        existing.setStatus(event.status());
                        existing.setHouseUuid(event.houseUuid());
                        repository.save(existing);
                        log.info("Existing CachedUser updated: {}", event.userUuid());
                    },

                    () -> {
                        CachedUser user = CachedUser.builder()
                                .uuid(event.userUuid())
                                .role(event.role())
                                .status(event.status())
                                .houseUuid(event.houseUuid())
                                .build();

                        repository.save(user);
                        log.info("New CachedUser saved: {}", event.userUuid());
                    }
                );
    }
}
