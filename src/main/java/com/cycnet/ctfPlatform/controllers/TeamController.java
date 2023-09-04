package com.cycnet.ctfPlatform.controllers;


import com.cycnet.ctfPlatform.dto.team.TeamRequestDto;
import com.cycnet.ctfPlatform.dto.team.TeamResponseDto;
import com.cycnet.ctfPlatform.services.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TeamResponseDto>> getAllTeams(){
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDto> getTeamsByID(
            @PathVariable @Positive(message = "Id must be greater than zero") Long id
    ){
        TeamResponseDto responseDto = teamService.getTeamById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<TeamResponseDto> createTeam(
            @RequestBody @Valid TeamRequestDto requestDto
    ){
        TeamResponseDto responseDto = teamService.creatTeam(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDto> updateCategory(
            @PathVariable @Positive(message = "Id must be positive") Long id,
            @RequestBody @Valid TeamRequestDto requestDto
    ) {
        TeamResponseDto responseDto = teamService.updateTeam(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TeamResponseDto> deleteTeam(
            @PathVariable @Positive(message = "Id must be greater than zero") Long id
    ){
       teamService.deleteTeam(id);
       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
