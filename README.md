# IoT Device Monitoring Dashboard

Full stack Java application where IoT devices push sensor readings to a REST API; users monitor
live charts and acknowledge threshold alerts.

**Stack:** Java 17, Spring Boot 3, Spring Security + JWT, Spring Data JPA/Hibernate, MySQL, HTML/CSS/JavaScript, Chart.js

## Features
- JWT login with role-based access (ADMIN manages devices, USER views dashboards)
- Device registration with a generated API key (devices authenticate with `X-API-KEY`)
- Sensor ingestion endpoint with automatic threshold alerts
- Live chart (last 50 readings, auto refresh every 5 s) and alert acknowledgement
- BCrypt password hashing, input validation, stateless security

## Architecture
Controller -> Service -> Repository -> MySQL. Devices -> `POST /api/readings/ingest`; Browser -> JWT-secured `/api/**`.

## Database
users(id, username, password, role) | devices(id, name, location, sensor_type, threshold, api_key)
sensor_readings(id, device_id FK, reading_value, recorded_at) | alerts(id, device_id FK, alert_value, message, acknowledged, created_at)

## API
| Method | Endpoint | Access |
|---|---|---|
| POST | /api/auth/register, /api/auth/login | public |
| GET | /api/devices | logged in |
| POST/PUT/DELETE | /api/devices[/{id}] | ADMIN |
| GET | /api/devices/{id}/readings | logged in |
| POST | /api/readings/ingest (header X-API-KEY) | device |
| GET | /api/alerts | logged in |
| PUT | /api/alerts/{id}/ack | logged in |

## Run
1. Install JDK 17, Maven, MySQL. Set your MySQL password in `src/main/resources/application.properties`.
2. `mvn spring-boot:run`, then open http://localhost:8080
3. Login as `admin` / `admin123` (change this), add a device, copy its API key.
4. `python simulator/simulator.py <API_KEY>` to stream fake readings.

## Ideas to extend
Email/SMS alerts, pagination, Docker Compose, unit tests with JUnit/Mockito, deploy on AWS.
