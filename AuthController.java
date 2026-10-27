package com.khushi.iot.controller;

import com.khushi.iot.entity.User;
import com.khushi.iot.repo.UserRepository;
import com.khushi.iot.security.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public record RegisterRequest(@NotBlank String username, @NotBlank @Size(min = 6) String password) {}
    public record LoginRequest(String username, String password) {}

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtUtil jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (users.findByUsername(req.username()).isPresent())
            return ResponseEntity.status(409).body(Map.of("error", "Username already taken"));
        User u = new User();
        u.setUsername(req.username());
        u.setPassword(encoder.encode(req.password()));
        u.setRole("ROLE_USER");
        users.save(u);
        return ResponseEntity.status(201).body(Map.of("message", "Registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        var user = users.findByUsername(req.username());
        if (user.isEmpty() || !encoder.matches(req.password(), user.get().getPassword()))
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        String role = user.get().getRole();
        return ResponseEntity.ok(Map.of("token", jwt.generate(req.username(), role), "role", role));
    }
}
