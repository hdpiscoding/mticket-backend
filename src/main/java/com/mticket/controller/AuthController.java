package com.mticket.controller;

import com.mticket.base.BaseController;
import com.mticket.dto.AuthRequestDTO;
import com.mticket.dto.AuthResponseDTO;
import com.mticket.enums.Role;
import com.mticket.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final AuthService authService;

    @PostMapping("/user/register")
    public ResponseEntity<Object> registerUser(@RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.register(request, Role.USER);
        return buildResponse(response, HttpStatus.CREATED, "User registered successfully");
    }

    @PostMapping("/admin/register")
    public ResponseEntity<Object> registerAdmin(@RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.register(request, Role.ADMIN);
        return buildResponse(response, HttpStatus.CREATED, "Admin registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return buildResponse(response, HttpStatus.OK, "User logged in successfully");
    }
}
