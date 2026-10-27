package com.khushi.iot.repo;

import com.khushi.iot.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findTop100ByOrderByCreatedAtDesc();
    void deleteByDeviceId(Long deviceId);
}
