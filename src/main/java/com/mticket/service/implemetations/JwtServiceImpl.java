package com.mticket.service.implemetations;

import com.mticket.entity.User;
import com.mticket.service.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {
    private static final String SECRET_KEY = "5bc55edbabc222b20a78d8d0ad352bfc3e03ef9ee67ff6f6f41b6498efff764da0f670295de8dcf5bc0a57597e15bffbc3a87ad5cfef09d09878be26dda18a5b8f1a07c1b84ab471059eb13a4ed797ea0d95749a440bcdf89b56c27b0c786f3100d5fbcdf93851f925c07994d3043299012e75322d0bbab5fbfbdaecf96baa8ad68baec8e510c8e2599041d6ddc1063cedcebe974b2b1c029a53035d9545f51195e9c12ce7edbf03c596b7fea0ed4cd1a2dc7ba8716df87e9a13fb4dbd340939ed3f07ad4ad126fb978385265746c06f9a0577387ae2d20eb4656c967f6f23cc5a12803aa2aa6917e2e2b1dcaef597b2c87187744ac1f871406dc284f2a903d0";

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 3600 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        if (claims != null) {
            Date expiration = claims.getExpiration();
            boolean isExpired = expiration.before(Date.from(Instant.now()));
            if (!isExpired) {
                return claims.getSubject();
            }
            else {
                return null;
            }
        }
        return null;
    }

    @Override
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        if (claims != null) {
            Date expiration = claims.getExpiration();
            boolean isExpired = expiration.before(Date.from(Instant.now()));
            if (!isExpired) {
                return claims.get("userId", Long.class);
            }
            else {
                return null;
            }
        }
        return null;
    }

    @Override
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        if (claims != null) {
            Date expiration = claims.getExpiration();
            boolean isExpired = expiration.before(Date.from(Instant.now()));
            if (!isExpired) {
                return claims.get("role", String.class);
            }
            else {
                return null;
            }
        }
        return null;       }

    @Override
    public boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        if (claims != null) {
            Date expiration = claims.getExpiration();
            return expiration.before(Date.from(Instant.now()));
        }
        return true;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
