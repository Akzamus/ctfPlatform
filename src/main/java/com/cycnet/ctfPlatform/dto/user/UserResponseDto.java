package com.cycnet.ctfPlatform.dto.user;

import com.cycnet.ctfPlatform.enums.Role;
import lombok.Builder;

@Builder
public record UserResponseDto(
        long id,
        String email,
        Role role
) { }
