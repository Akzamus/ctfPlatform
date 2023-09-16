package com.cycnet.ctfPlatform.controllers;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.student.StudentRequestDto;
import com.cycnet.ctfPlatform.dto.student.StudentResponseDto;
import com.cycnet.ctfPlatform.services.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<StudentResponseDto> getAllStudents(
            @RequestParam(defaultValue = "0")
            @Range(min = 0, message = "Page number must be greater than or equal to 0") int page,

            @RequestParam(defaultValue = "10")
            @Range(min = 1, max = 100, message = "Page size must be between 1 and 100") int size
    ) {
        return studentService.getAll(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentResponseDto getStudentById(
            @PathVariable @Positive(message = "Id must be positive") long id
    ) {
        return studentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponseDto createStudent(
            @RequestBody @Valid StudentRequestDto studentRequestDto
    ) {
        return studentService.create(studentRequestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentResponseDto updateStudent(
            @PathVariable @Positive(message = "Id must be positive") long id,
            @RequestBody @Valid StudentRequestDto studentRequestDto
    ) {
        return studentService.update(id, studentRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(
            @PathVariable @Positive(message = "Id must be positive") long id
    ) {
        studentService.delete(id);
    }

}
