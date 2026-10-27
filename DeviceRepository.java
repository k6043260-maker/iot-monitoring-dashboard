package com.khushi.iot.repo;

import com.khushi.iot.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByApiKey(String apiKey);
}
