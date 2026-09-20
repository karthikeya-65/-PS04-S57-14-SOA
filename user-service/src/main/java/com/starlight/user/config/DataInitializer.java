package com.starlight.user.config;

import com.starlight.user.entity.Role;
import com.starlight.user.entity.User;
import com.starlight.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Seeding default Spring Boot user accounts...");

            User admin = User.builder()
                    .username("admin")
                    .email("admin@starlight.com")
                    .password(passwordEncoder.encode("Admin123!"))
                    .firstName("Starlight")
                    .lastName("Administrator")
                    .phoneNumber("+1-800-555-0100")
                    .role(Role.ROLE_ADMIN)
                    .build();

            User guest = User.builder()
                    .username("john_doe")
                    .email("john@starlight.com")
                    .password(passwordEncoder.encode("Guest123!"))
                    .firstName("John")
                    .lastName("Doe")
                    .phoneNumber("+1-555-0199")
                    .role(Role.ROLE_GUEST)
                    .build();

            User manager = User.builder()
                    .username("manager")
                    .email("manager@starlight.com")
                    .password(passwordEncoder.encode("Manager123!"))
                    .firstName("Sarah")
                    .lastName("Connor")
                    .phoneNumber("+1-555-0144")
                    .role(Role.ROLE_HOTEL_MANAGER)
                    .build();

            userRepository.save(admin);
            userRepository.save(guest);
            userRepository.save(manager);

            log.info("Default Spring Boot users seeded successfully: admin, john_doe, manager");
        }
    }
}
