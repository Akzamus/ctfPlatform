package com.cycnet.ctfPlatform.dto.task;

import com.cycnet.ctfPlatform.dto.category.CategoryResponseDto;
import com.cycnet.ctfPlatform.dto.event.EventResponseDto;

public record TaskResponseDto (
        long id,
        String name,
        String question,
        String description,
        int numberOfPoints,
        EventResponseDto event,
        CategoryResponseDto category
) { }
