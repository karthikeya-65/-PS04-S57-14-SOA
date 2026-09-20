package com.starlight.user;

import com.starlight.user.dto.AuthResponse;
import com.starlight.user.dto.LoginRequest;
import com.starlight.user.dto.RegisterRequest;
import com.starlight.user.entity.Role;
import com.starlight.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class UserServiceApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void testRegisterAndLoginFlow() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("testguest")
                .email("guest@starlight.com")
                .password("Password123!")
                .firstName("John")
                .lastName("Doe")
                .role(Role.ROLE_GUEST)
                .build();

        AuthResponse registerResponse = userService.register(registerRequest);
        assertNotNull(registerResponse);
        assertNotNull(registerResponse.getToken());
        assertEquals("testguest", registerResponse.getUsername());
        assertEquals(Role.ROLE_GUEST, registerResponse.getRole());

        LoginRequest loginRequest = LoginRequest.builder()
                .username("testguest")
                .password("Password123!")
                .build();

        AuthResponse loginResponse = userService.login(loginRequest);
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getToken());
        assertEquals(registerResponse.getId(), loginResponse.getId());
    }
}
