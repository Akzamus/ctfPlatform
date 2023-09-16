package com.cycnet.ctfPlatform.dto.file;

import lombok.Builder;

@Builder
public record FileResponseDto (
        long id,
        String path,
        long taskId
) { }
