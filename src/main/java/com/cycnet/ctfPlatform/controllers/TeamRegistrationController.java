package com.cycnet.ctfPlatform.controllers;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.teamRegistration.TeamRegistrationRequestDto;
import com.cycnet.ctfPlatform.dto.teamRegistration.TeamRegistrationResponseDto;
import com.cycnet.ctfPlatform.services.TeamRegistrationService;
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
@RequestMapping("/api/v1/team-registration")
public class TeamRegistrationController {

    private final TeamRegistrationService teamRegistrationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<TeamRegistrationResponseDto> getAllTeams(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number must be greater than or equal to 0") int page,

            @RequestParam(defaultValue = "10")
            @Range(min = 1, max = 100, message = "Page size must be between 1 and 100") int size
    ){
        return teamRegistrationService.getAll(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeamRegistrationResponseDto getTeamByID(
           @PathVariable @Positive(message = "Id must be positive") long id
    ){
      return teamRegistrationService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public TeamRegistrationResponseDto createTeam(
            @RequestBody @Valid TeamRegistrationRequestDto teamRegistrationRequestDto
            ){
        return teamRegistrationService.create(teamRegistrationRequestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeamRegistrationResponseDto updateTeam(
            @PathVariable @Positive(message = "Id must be positive") long id,
            @RequestBody @Valid TeamRegistrationRequestDto teamRegistrationRequestDto
    ) {
        return teamRegistrationService.update(id , teamRegistrationRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(
            @PathVariable @Positive(message = "Id must be must positive" ) long id
    ){
        teamRegistrationService.delete(id);
    }

}
