package com.cycnet.ctfPlatform.controllers;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.task.TaskRequestDto;
import com.cycnet.ctfPlatform.dto.task.TaskResponseDto;
import com.cycnet.ctfPlatform.services.TaskService;
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
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<TaskResponseDto> getAllTasks(
            @RequestParam(defaultValue = "0")
            @Range(min = 0, message = "Page number must be greater than or equal to 0") int page,

            @RequestParam(defaultValue = "10")
            @Range(min = 1, max = 100, message = "Page size must be between 1 and 100") int size
    ) {
        return taskService.getAll(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto getTaskById(
            @PathVariable @Positive(message = "Id must be positive") long id
    ) {
        return taskService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto createTask(
            @RequestBody @Valid TaskRequestDto taskRequestDto
    ) {
        return taskService.create(taskRequestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto updateTask(
            @PathVariable @Positive(message = "Id must be positive") long id,
            @RequestBody @Valid TaskRequestDto taskRequestDto
    ) {
        return taskService.update(id, taskRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
            @PathVariable @Positive(message = "Id must be positive") long id
    ) {
        taskService.delete(id);
    }

}
