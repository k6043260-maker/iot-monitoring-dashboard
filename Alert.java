package com.khushi.iot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Getter @Setter @NoArgsConstructor
public class Alert {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "device_id")
    private Device device;
    @Column(name = "alert_value")
    private Double value;
    private String message;
    private boolean acknowledged = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}
