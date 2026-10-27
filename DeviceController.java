package com.khushi.iot.controller;

import com.khushi.iot.entity.Device;
import com.khushi.iot.entity.SensorReading;
import com.khushi.iot.repo.AlertRepository;
import com.khushi.iot.repo.DeviceRepository;
import com.khushi.iot.repo.SensorReadingRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    public record DeviceRequest(@NotBlank String name, String location, String sensorType, @NotNull Double threshold) {}

    private final DeviceRepository devices;
    private final SensorReadingRepository readings;
    private final AlertRepository alerts;

    public DeviceController(DeviceRepository devices, SensorReadingRepository readings, AlertRepository alerts) {
        this.devices = devices;
        this.readings = readings;
        this.alerts = alerts;
    }

    @GetMapping
    public List<Device> all() { return devices.findAll(); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody DeviceRequest req) {
        Device d = new Device();
        d.setName(req.name());
        d.setLocation(req.location());
        d.setSensorType(req.sensorType());
        d.setThreshold(req.threshold());
        d.setApiKey(UUID.randomUUID().toString());
        devices.save(d);
        // API key is shown only once, at creation
        return ResponseEntity.status(201).body(Map.of("id", d.getId(), "name", d.getName(), "apiKey", d.getApiKey()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody DeviceRequest req) {
        return devices.findById(id).map(d -> {
            d.setName(req.name());
            d.setLocation(req.location());
            d.setSensorType(req.sensorType());
            d.setThreshold(req.threshold());
            return ResponseEntity.ok((Object) devices.save(d));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!devices.existsById(id)) return ResponseEntity.notFound().build();
        readings.deleteByDeviceId(id);
        alerts.deleteByDeviceId(id);
        devices.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/readings")
    public List<SensorReading> readings(@PathVariable Long id) {
        List<SensorReading> list = new ArrayList<>(readings.findTop50ByDeviceIdOrderByRecordedAtDesc(id));
        Collections.reverse(list);   // oldest first, good for charts
        return list;
    }
}
