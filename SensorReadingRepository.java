package com.khushi.iot.repo;

import com.khushi.iot.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    List<SensorReading> findTop50ByDeviceIdOrderByRecordedAtDesc(Long deviceId);
    void deleteByDeviceId(Long deviceId);
}
