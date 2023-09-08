package com.cycnet.ctfPlatform.dto.event;

import java.time.ZonedDateTime;

public record EventResponseDto(
    long id,
    String name,
    ZonedDateTime startedAt,
    ZonedDateTime endedAt
) { }
