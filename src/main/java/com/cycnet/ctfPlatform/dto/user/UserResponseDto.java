package com.cycnet.ctfPlatform.dto.user;

import com.cycnet.ctfPlatform.enums.Role;
import lombok.Builder;

@Builder
public record UserResponseDto(
        Long id,
        String email,
        Role role
) { }
