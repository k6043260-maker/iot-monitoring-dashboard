package com.khushi.iot.controller;

import com.khushi.iot.repo.DeviceRepository;
import com.khushi.iot.service.ReadingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/readings")
public class ReadingController {
    public record ReadingRequest(@NotNull Double value) {}

    private final DeviceRepository devices;
    private final ReadingService service;

    public ReadingController(DeviceRepository devices, ReadingService service) {
        this.devices = devices;
        this.service = service;
    }

    // Called by the IoT device (or simulator). Authenticated by the device API key, not JWT.
    @PostMapping("/ingest")
    public ResponseEntity<?> ingest(@RequestHeader("X-API-KEY") String apiKey,
                                    @Valid @RequestBody ReadingRequest req) {
        return devices.findByApiKey(apiKey)
                .map(d -> ResponseEntity.ok((Object) Map.of("alertRaised", service.ingest(d, req.value()))))
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid API key")));
    }
}
