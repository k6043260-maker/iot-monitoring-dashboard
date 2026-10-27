package com.khushi.iot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_readings", indexes = @Index(columnList = "device_id, recordedAt"))
@Getter @Setter @NoArgsConstructor
public class SensorReading {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id")
    @JsonIgnore
    private Device device;
    @Column(name = "reading_value", nullable = false)
    private Double value;
    private LocalDateTime recordedAt = LocalDateTime.now();
}
