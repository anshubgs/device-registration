package com.anshu.device_registration.repository;

import com.anshu.device_registration.dto.DeviceStatus;
import com.anshu.device_registration.model.Device;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    boolean existsByUuid(UUID uuid);

    Optional<Device> findByUuid(UUID devicUuid);

    long countByStatus(DeviceStatus deviceStatus);

    /**
     * Fetch recent devices sorted by updatedAt desc
     * Uses pagination to limit results
     */
  //  List<Device> findRecentDevice(PageRequest pageRequest);
    // Recent devices, ordered by updated_at DESC
    @Query("SELECT d FROM Device d ORDER BY d.updatedAt DESC")
    List<Device> findRecentDevice(Pageable pageable);

    Page<Device> findByStatus(DeviceStatus status, Pageable pageable);
  //  Optional<Device> findByUuid(UUID uuid);

}
