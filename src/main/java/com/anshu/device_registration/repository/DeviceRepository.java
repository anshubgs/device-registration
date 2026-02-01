package com.anshu.device_registration.repository;

import com.anshu.device_registration.model.Device;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    boolean existsByUuid(UUID uuid);

    Optional<Device> findByUuid(UUID devicUuid);
}
