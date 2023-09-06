package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.team.TeamRequestDto;
import com.cycnet.ctfPlatform.dto.team.TeamResponseDto;

import java.util.List;

public interface TeamService {

    PageResponseDto<TeamResponseDto> getAllTeams(int page, int size);

    TeamResponseDto getTeamById(long id);

    TeamResponseDto creatTeam(TeamRequestDto teamRequestDto);

    TeamResponseDto updateTeam(long id, TeamRequestDto requestDto);
    void deleteTeam(long id);

}
