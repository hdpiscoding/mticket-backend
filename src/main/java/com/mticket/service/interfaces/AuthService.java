package com.mticket.service.interfaces;

import com.mticket.dto.AuthRequestDTO;
import com.mticket.dto.AuthResponseDTO;
import com.mticket.enums.Role;

public interface AuthService {
    AuthResponseDTO register(AuthRequestDTO request, Role role);
    AuthResponseDTO login(AuthRequestDTO request);
}
