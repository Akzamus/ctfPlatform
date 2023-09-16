package com.cycnet.ctfPlatform.dto.file;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

public record FileRequestDto (

        @NotNull(message = "File cannot be null")
        MultipartFile file,

        @NotNull(message = "File id cannot be null")
        @Positive(message = "File id must be positive")
        long taskId

) { }
