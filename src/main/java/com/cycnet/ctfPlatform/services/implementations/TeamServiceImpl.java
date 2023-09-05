package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.team.TeamRequestDto;
import com.cycnet.ctfPlatform.dto.team.TeamResponseDto;
import com.cycnet.ctfPlatform.exceptions.entity.EntityAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.mappers.TeamMapper;
import com.cycnet.ctfPlatform.models.Team;
import com.cycnet.ctfPlatform.repositories.TeamRepository;
import com.cycnet.ctfPlatform.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    @Override
    public List<TeamResponseDto> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teamMapper.toDto(teams);
    }

    @Override
    public TeamResponseDto getTeamById(Long id) {
        return teamRepository.findById(id)
                .map(teamMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID " +id + " does not exist"));
    }

    @Override
    @Transactional
    public TeamResponseDto creatTeam(TeamRequestDto teamRequestDto) {
        teamRepository.findByName(teamRequestDto.name())
                .ifPresent(foundCategory -> {
                    throw new EntityAlreadyExistsException(
                            "Team with the name " + foundCategory.getName() + " already exists."
                    );
                });

        Team team= teamMapper.toEntity(teamRequestDto);
        team = teamRepository.save(team);

        return teamMapper.toDto(team);
    }

    @Override
    public TeamResponseDto updateTeam(Long id, TeamRequestDto requestDto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID +" + id + " does not exist"));

        String teamName = requestDto.name();

        teamRepository.findByName(teamName)
                .ifPresent(foundTeam -> {
                    throw new EntityAlreadyExistsException(
                            "Category with the name " + foundTeam.getName() + " already exists."
                    );
                });

        team.setName(teamName);
        team = teamRepository.save(team);

        return teamMapper.toDto(team);
    }

    @Override
    @Transactional
    public void deleteTeam(Long id) {
        Team alreadExistsTeam = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID + " + id + " does not exist"));

         teamRepository.delete(alreadExistsTeam);
    }
}
