package com.khushi.iot.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "devices")
@Getter @Setter @NoArgsConstructor
public class Device {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String location;
    private String sensorType;      // temperature, gas, humidity ...
    private Double threshold;       // alert when value > threshold
    @Column(unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // never sent back in JSON lists
    private String apiKey;
}
