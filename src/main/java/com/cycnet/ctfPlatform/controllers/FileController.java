package com.cycnet.ctfPlatform.controllers;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.file.FileRequestDto;
import com.cycnet.ctfPlatform.dto.file.FileResponseDto;
import com.cycnet.ctfPlatform.services.FileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<FileResponseDto> getAllFiles(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number must be greater than or equal to 0") int page,

            @RequestParam(defaultValue = "10")
            @Range(min = 1, max = 100, message = "Page size must be between 1 and 100") int size
    ){
        return fileService.getAll(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FileResponseDto getFileById(
            @PathVariable @Positive(message = "Id must be greater than zero") long id
    ) {
        return fileService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FileResponseDto createFile(
            @ModelAttribute @Valid FileRequestDto fileRequestDto
    ) {
        return fileService.create(fileRequestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FileResponseDto updateFile(
            @PathVariable @Positive(message = "Id must be positive") long id,
            @ModelAttribute @Valid FileRequestDto fileRequestDto
    ) {
        return fileService.update(id, fileRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(
            @PathVariable @Positive(message = "Id must be greater than zero") long id
    ) {
        fileService.delete(id);
    }

}
