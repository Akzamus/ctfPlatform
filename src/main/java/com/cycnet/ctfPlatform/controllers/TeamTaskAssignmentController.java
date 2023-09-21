package com.cycnet.ctfPlatform.controllers;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.teamTaskAssignment.TeamTaskAssignmentRequestDto;
import com.cycnet.ctfPlatform.dto.teamTaskAssignment.TeamTaskAssignmentResponseDto;
import com.cycnet.ctfPlatform.services.TeamTaskAssignmentService;
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
@RequestMapping("/api/v1/team-task-assignments")
public class TeamTaskAssignmentController {

    private final TeamTaskAssignmentService teamTaskAssignmentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<TeamTaskAssignmentResponseDto> getAllTeamTaskAssignments(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number must be greater than or equal to 0") int page,

            @RequestParam(defaultValue = "10")
            @Range(min = 1, max = 100, message = "Page size must be between 1 and 100") int size
    ){
        return teamTaskAssignmentService.getAll(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeamTaskAssignmentResponseDto getTeamTaskAssignmentByID(
            @PathVariable @Positive(message = "Id must be positive") long id
    ){
        return teamTaskAssignmentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public TeamTaskAssignmentResponseDto createTeamTaskAssignment(
            @RequestBody @Valid TeamTaskAssignmentRequestDto teamTaskAssignmentRequestDto
    ){
        return teamTaskAssignmentService.create(teamTaskAssignmentRequestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeamTaskAssignmentResponseDto updateTeamTaskAssignment(
            @PathVariable @Positive(message = "Id must be positive") long id,
            @RequestBody @Valid TeamTaskAssignmentRequestDto teamTaskAssignmentRequestDto
    ) {
        return teamTaskAssignmentService.update(id , teamTaskAssignmentRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeamTaskAssignment(
            @PathVariable @Positive(message = "Id must be must positive" ) long id
    ){
        teamTaskAssignmentService.delete(id);
    }

}
