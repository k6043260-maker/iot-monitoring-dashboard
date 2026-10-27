package com.khushi.iot.service;

import com.khushi.iot.entity.Alert;
import com.khushi.iot.entity.Device;
import com.khushi.iot.entity.SensorReading;
import com.khushi.iot.repo.AlertRepository;
import com.khushi.iot.repo.SensorReadingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReadingService {
    private final SensorReadingRepository readings;
    private final AlertRepository alerts;

    public ReadingService(SensorReadingRepository readings, AlertRepository alerts) {
        this.readings = readings;
        this.alerts = alerts;
    }

    /** Saves a reading and raises an alert if it crosses the device threshold. */
    @Transactional
    public boolean ingest(Device device, double value) {
        SensorReading r = new SensorReading();
        r.setDevice(device);
        r.setValue(value);
        readings.save(r);

        if (device.getThreshold() != null && value > device.getThreshold()) {
            Alert a = new Alert();
            a.setDevice(device);
            a.setValue(value);
            a.setMessage(device.getSensorType() + " reading " + value
                    + " exceeded threshold " + device.getThreshold());
            alerts.save(a);
            return true;
        }
        return false;
    }
}
