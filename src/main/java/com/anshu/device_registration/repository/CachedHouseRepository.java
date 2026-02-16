package com.anshu.device_registration.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anshu.device_registration.model.CachedHouse;


public interface CachedHouseRepository extends JpaRepository<CachedHouse, Long> {

    Optional<CachedHouse> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);
}
