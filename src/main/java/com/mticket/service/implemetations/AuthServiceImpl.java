package com.mticket.service.implemetations;

import com.mticket.dto.AuthRequestDTO;
import com.mticket.dto.AuthResponseDTO;
import com.mticket.service.interfaces.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponseDTO register(AuthRequestDTO request) {
        return null;
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO request) {
        return null;
    }
}
