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

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Override
    public PageResponseDto<TeamResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving Teams, page number: {}, page size: {}", pageNumber, pageSize);

        Page<Team> page = teamRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<TeamResponseDto> pageResponseDto = teamMapper.toDto(page);

        log.info("Finished retrieving Teams, page number: {}, page size: {}", pageNumber, pageSize);

        return pageResponseDto;
    }

    @Override
    public TeamResponseDto getById(long id) {
        log.info("Retrieving Team by ID: {}", id);

        Team team = getEntityById(id);
        TeamResponseDto responseDto = teamMapper.toDto(team);

        log.info("Finished retrieving Team by ID: {}", team.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public TeamResponseDto create(TeamRequestDto requestDto) {
        log.info("Creating new Team with name: {}", requestDto.name());

        throwExceptionIfTeamExists(requestDto.name());

        Team team = teamMapper.toEntity(requestDto);
        team = teamRepository.save(team);
        TeamResponseDto responseDto = teamMapper.toDto(team);

        log.info("Created new Team with ID: {}", team.getId());

        return responseDto;
    }

    @Override
    public TeamResponseDto update(long id, TeamRequestDto requestDto) {
        log.info("Updating Team with ID: {}", id);

        Team team = getEntityById(id);

        String oldName = team.getName();
        String newName = requestDto.name();

        if (!Objects.equals(oldName, newName)) {
            throwExceptionIfTeamExists(newName);
            team.setName(newName);
            team = teamRepository.save(team);
        }

        TeamResponseDto responseDto = teamMapper.toDto(team);

        log.info("Updated Team with ID: {}", team.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting Team with ID: {}", id);

        Team team = getEntityById(id);
        teamRepository.delete(team);

        log.info("Deleted Team with ID: {}", team.getId());
    }

    @Override
    public Team getEntityById(long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID " + id + " does not exist"));
    }

    private void throwExceptionIfTeamExists(String name) {
        teamRepository.findByName(name)
                .ifPresent(foundTeam -> {
                    throw new EntityAlreadyExistsException(
                            "Team with the name '" + foundTeam.getName() + "' already exists"
                    );
                });
    }

}