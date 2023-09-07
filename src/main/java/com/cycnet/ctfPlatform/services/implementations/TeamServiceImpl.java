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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    @Override
    public PageResponseDto<TeamResponseDto> getAll(int pageNumber, int pageSize) {
        Page<Team> teamPage = teamRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return teamMapper.toDto(teamPage);
    }

    @Override
    public TeamResponseDto getById(long id) {
        return teamRepository.findById(id)
                .map(teamMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID " + id + " does not exist"));
    }

    @Override
    @Transactional
    public TeamResponseDto create(TeamRequestDto teamRequestDto) {
        teamRepository.findByName(teamRequestDto.name())
                .ifPresent(foundCategory -> {
                    throw new EntityAlreadyExistsException(
                            "Team with the name " + foundCategory.getName() + " already exists."
                    );
                });

        Team team = teamMapper.toEntity(teamRequestDto);
        team = teamRepository.save(team);

        return teamMapper.toDto(team);
    }

    @Override
    public TeamResponseDto update(long id, TeamRequestDto requestDto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID " + id + " does not exist"));

        String newName = requestDto.name();

        if(!team.getName().equals(newName)) {
            teamRepository.findByName(newName)
                    .ifPresent(foundTeam -> {
                        throw new EntityAlreadyExistsException(
                                "Category with the name " + foundTeam.getName() + " already exists."
                        );
                    });

            team.setName(newName);
            team = teamRepository.save(team);
        }

        return teamMapper.toDto(team);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID " + id + " does not exist"));

         teamRepository.delete(existingTeam);
    }
}
