package com.cycnet.ctfPlatform.dto.category;

import lombok.Builder;

@Builder
public record CategoryResponseDto (
    long id,
    String name
) { }
