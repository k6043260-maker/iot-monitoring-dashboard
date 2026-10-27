package com.khushi.iot.controller;

import com.khushi.iot.entity.Alert;
import com.khushi.iot.repo.AlertRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    private final AlertRepository alerts;

    public AlertController(AlertRepository alerts) { this.alerts = alerts; }

    @GetMapping
    public List<Alert> latest() { return alerts.findTop100ByOrderByCreatedAtDesc(); }

    @PutMapping("/{id}/ack")
    public ResponseEntity<?> acknowledge(@PathVariable Long id) {
        return alerts.findById(id).map(a -> {
            a.setAcknowledged(true);
            return ResponseEntity.ok((Object) alerts.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }
}
