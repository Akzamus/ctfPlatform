package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.team.TeamRequestDto;
import com.cycnet.ctfPlatform.dto.team.TeamResponseDto;
import com.cycnet.ctfPlatform.exceptions.entity.EntityAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.mappers.TeamMapper;
import com.cycnet.ctfPlatform.models.Team;
import com.cycnet.ctfPlatform.repositories.TeamRepository;
import com.cycnet.ctfPlatform.services.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Override
    public PageResponseDto<TeamResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving teams, page number: {}, page size: {}", pageNumber, pageSize);

        Page<Team> teamPage = teamRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<TeamResponseDto> teamPageResponseDto = teamMapper.toDto(teamPage);

        log.info("Finished retrieving teams, page number: {}, page size: {}", pageNumber, pageSize);

        return teamPageResponseDto;
    }

    @Override
    public TeamResponseDto getById(long id) {
        log.info("Retrieving team by ID: {}", id);

        TeamResponseDto teamResponseDto = teamRepository.findById(id)
                .map(teamMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID " + id + " does not exist"));

        log.info("Finished retrieving team by ID: {}", teamResponseDto.id());

        return teamResponseDto;
    }

    @Override
    @Transactional
    public TeamResponseDto create(TeamRequestDto teamRequestDto) {
        log.info("Creating a new team with name: {}", teamRequestDto.name());

        throwExceptionIfTeamExists(teamRequestDto.name());

        Team team = teamMapper.toEntity(teamRequestDto);
        team = teamRepository.save(team);
        TeamResponseDto teamResponseDto = teamMapper.toDto(team);

        log.info("Created a new team with name: {}", team.getName());

        return teamResponseDto;
    }

    @Override
    public TeamResponseDto update(long id, TeamRequestDto requestDto) {
        log.info("Updating team with ID: {}", id);

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID " + id + " does not exist"));

        String newName = requestDto.name();

        if (!team.getName().equals(newName)) {
            throwExceptionIfTeamExists(newName);
            team.setName(newName);
            team = teamRepository.save(team);
        }

        TeamResponseDto teamResponseDto = teamMapper.toDto(team);

        log.info("Updated team with ID: {}", id);

        return teamResponseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting team with ID: {}", id);

        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID " + id + " does not exist"));

        teamRepository.delete(existingTeam);

        log.info("Deleted team with ID: {}", id);
    }

    private void throwExceptionIfTeamExists(String name) {
        teamRepository.findByName(name)
                .ifPresent(foundTeam -> {
                    throw new EntityAlreadyExistsException(
                            "Team with the name " + foundTeam.getName() + " already exists."
                    );
                });
    }

}