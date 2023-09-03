package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.team.TeamRequestDto;
import com.cycnet.ctfPlatform.dto.team.TeamResponseDto;

import java.util.List;

public interface TeamService {

    List<TeamResponseDto> getAllTeams();

    TeamResponseDto getTeamById(Long id);

    TeamResponseDto creatTeam(TeamRequestDto teamRequestDto);

    TeamResponseDto updateTeam(Long id, TeamRequestDto requestDto);
    void deleteTeam(Long id);

}
