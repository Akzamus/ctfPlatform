package com.cycnet.ctfPlatform.controllers;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.teamMember.TeamMemberRequestDto;
import com.cycnet.ctfPlatform.dto.teamMember.TeamMemberResponseDto;
import com.cycnet.ctfPlatform.services.TeamMemberService;
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
@RequestMapping("/api/v1/team-member")
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<TeamMemberResponseDto> getAllTeamMembers(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number must be greater than or equal to 0") int page,

            @RequestParam(defaultValue = "10")
            @Range(min = 1, max = 100, message = "Page size must be between 1 and 100") int size
    ) {
        return teamMemberService.getAll(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeamMemberResponseDto getTeamMemberById(
            @PathVariable @Positive(message = "Id must be greater than zero") long id
    ) {
        return teamMemberService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamMemberResponseDto creatTeamMember(
            @RequestBody @Valid TeamMemberRequestDto requestDto
    ) {
        return teamMemberService.create(requestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeamMemberResponseDto updateTeamMember(
            @PathVariable @Positive(message = "Id must be positive") long id,
            @RequestBody @Valid TeamMemberRequestDto teamMemberRequestDto
    ) {
        return teamMemberService.update(id, teamMemberRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeamMember(
            @PathVariable @Positive(message = "Id must be greater than zero") long id
    ) {
        teamMemberService.delete(id);
    }
}
