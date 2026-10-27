package com.khushi.iot;

import com.khushi.iot.entity.User;
import com.khushi.iot.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class IotApplication {
    public static void main(String[] args) {
        SpringApplication.run(IotApplication.class, args);
    }

    // Seeds a default admin on first run: admin / admin123 (change it!)
    @Bean
    CommandLineRunner seedAdmin(UserRepository users, PasswordEncoder enc) {
        return args -> {
            if (users.findByUsername("admin").isEmpty()) {
                User u = new User();
                u.setUsername("admin");
                u.setPassword(enc.encode("admin123"));
                u.setRole("ROLE_ADMIN");
                users.save(u);
            }
        };
    }
}
