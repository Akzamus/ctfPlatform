package com.cycnet.ctfPlatform.controllers;


import com.cycnet.ctfPlatform.dto.team.TeamRequestDto;
import com.cycnet.ctfPlatform.dto.team.TeamResponseDto;
import com.cycnet.ctfPlatform.services.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TeamResponseDto> getAllTeams(){
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeamResponseDto getTeamById(
            @PathVariable @Positive(message = "Id must be greater than zero") Long id
    ) {
        return teamService.getTeamById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponseDto createTeam(
            @RequestBody @Valid TeamRequestDto requestDto
    ) {
        return teamService.creatTeam(requestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeamResponseDto updateTeam(
            @PathVariable @Positive(message = "Id must be positive") Long id,
            @RequestBody @Valid TeamRequestDto requestDto
    ) {
        return teamService.updateTeam(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(
            @PathVariable @Positive(message = "Id must be greater than zero") Long id
    ) {
       teamService.deleteTeam(id);
    }

}
