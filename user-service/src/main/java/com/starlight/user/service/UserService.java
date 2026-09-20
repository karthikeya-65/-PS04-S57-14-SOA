package com.starlight.user.service;

import com.starlight.user.dto.AuthResponse;
import com.starlight.user.dto.LoginRequest;
import com.starlight.user.dto.RegisterRequest;
import com.starlight.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserResponse getCurrentUser(String username);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();
}
