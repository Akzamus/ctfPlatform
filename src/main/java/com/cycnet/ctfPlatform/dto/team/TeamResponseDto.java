package com.cycnet.ctfPlatform.dto.team;

import lombok.Builder;

@Builder
public record TeamResponseDto(
        long id,
        String name
){}
