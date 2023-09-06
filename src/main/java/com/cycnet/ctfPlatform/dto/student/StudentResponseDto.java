package com.cycnet.ctfPlatform.dto.student;

import com.cycnet.ctfPlatform.dto.user.UserResponseDto;
import lombok.Builder;

@Builder
public record StudentResponseDto(
        Long id,
        String firstName,
        String lastName,
        UserResponseDto user
) { }
