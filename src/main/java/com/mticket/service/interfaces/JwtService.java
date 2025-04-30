package com.mticket.service.interfaces;

import com.mticket.entity.User;

import java.util.UUID;

public interface JwtService {
    String generateToken(User user);
    String extractUsername(String token);
    UUID extractUserId(String token);
    String extractRole(String token);
    boolean isTokenExpired(String token);
}
