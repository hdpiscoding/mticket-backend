package com.mticket.service.interfaces;

import com.mticket.dto.AuthRequestDTO;
import com.mticket.dto.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO register(AuthRequestDTO request);
    AuthResponseDTO login(AuthRequestDTO request);
}
