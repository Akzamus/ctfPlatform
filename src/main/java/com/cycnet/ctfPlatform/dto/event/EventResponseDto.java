package com.cycnet.ctfPlatform.dto.event;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record EventResponseDto(
    long id,
    String name,
    ZonedDateTime startedAt,
    ZonedDateTime endedAt
) { }
