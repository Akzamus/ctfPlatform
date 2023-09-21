package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.teamRegistration.TeamRegistrationRequestDto;
import com.cycnet.ctfPlatform.dto.teamRegistration.TeamRegistrationResponseDto;
import com.cycnet.ctfPlatform.enums.TeamResult;
import com.cycnet.ctfPlatform.exceptions.entity.EntityAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.mappers.TeamRegistrationMapper;
import com.cycnet.ctfPlatform.models.Event;
import com.cycnet.ctfPlatform.models.Team;
import com.cycnet.ctfPlatform.models.TeamRegistration;
import com.cycnet.ctfPlatform.repositories.TeamRegistrationRepository;
import com.cycnet.ctfPlatform.services.EventService;
import com.cycnet.ctfPlatform.services.TeamRegistrationService;
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
public class TeamRegistrationServiceImpl implements TeamRegistrationService {

    private final TeamRegistrationRepository teamRegistrationRepository;
    private final TeamRegistrationMapper teamRegistrationMapper;
    private final TeamService teamService;
    private final EventService eventService;

    @Override
    public PageResponseDto<TeamRegistrationResponseDto> getAll(int pageNumber, int pageSize) {

        Page<TeamRegistration> teamRegistrationPage = teamRegistrationRepository.findAll(
                PageRequest.of(pageNumber, pageSize)
        );
        PageResponseDto<TeamRegistrationResponseDto> teamRegistrationResponseDtoPageResponseDto =
                teamRegistrationMapper.toDto(teamRegistrationPage);

        return teamRegistrationResponseDtoPageResponseDto;
    }

    @Override
    public TeamRegistrationResponseDto getById(long id) {
        TeamRegistration teamRegistration = getEntityById(id);
        TeamRegistrationResponseDto teamRegistrationResponseDto = teamRegistrationMapper.toDto(teamRegistration);

        return teamRegistrationResponseDto;
    }

    @Override
    @Transactional
    public TeamRegistrationResponseDto create(TeamRegistrationRequestDto requestDto) {

        Team team = teamService.getEntityById(requestDto.teamId());
        Event event = eventService.getEntityById(requestDto.eventId());

        throwExceptionIfTeamRegistrationExists(team, event);

        TeamRegistration teamRegistration = teamRegistrationMapper.toEntity(requestDto);

        teamRegistration.setTeamResult(TeamResult.valueOf(requestDto.teamResult()));
        teamRegistration.setTeam(team);
        teamRegistration.setEvent(event);

        teamRegistration = teamRegistrationRepository.save(teamRegistration);
        TeamRegistrationResponseDto teamRegistrationResponseDto = teamRegistrationMapper.toDto(teamRegistration);

        return teamRegistrationResponseDto;
    }

    @Override
    @Transactional
    public TeamRegistrationResponseDto update(long id, TeamRegistrationRequestDto requestDto) {
        TeamRegistration teamRegistration = getEntityById(id);

        Team team = teamRegistration.getTeam();
        Event event = teamRegistration.getEvent();

        long teamId = team.getId();
        long eventId = event.getId();

        if (requestDto.teamId() != teamId) {
            team = teamService.getEntityById(requestDto.teamId());
        }

        if (requestDto.eventId() != eventId) {
            event = eventService.getEntityById(requestDto.eventId());
        }

        if (
                requestDto.eventId() != eventId ||
                requestDto.teamId() != teamId
        ) {
            throwExceptionIfTeamRegistrationExists(team, event);
        }

        teamRegistration.setTeam(team);
        teamRegistration.setEvent(event);
        teamRegistration.setTeamResult(TeamResult.valueOf(requestDto.teamResult()));

        teamRegistration = teamRegistrationRepository.save(teamRegistration);
        TeamRegistrationResponseDto teamResponseDto = teamRegistrationMapper.toDto(teamRegistration);

        return teamResponseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        TeamRegistration existingTeam = getEntityById(id);
        teamRegistrationRepository.delete(existingTeam);
    }

    @Override
    public TeamRegistration getEntityById(long id) {
        return teamRegistrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registration with id " + id  +" does not exists"));
    }

    private void throwExceptionIfTeamRegistrationExists(Team team, Event event) {
        teamRegistrationRepository.findByTeamAndEvent(team, event)
                .ifPresent(foundTeamRegistration -> {
                    throw new EntityAlreadyExistsException(
                            "Team with ID " + foundTeamRegistration.getId() +
                                    " already registered for the event with ID: " +
                                    event.getId() + "."
                    );
                });
    }

}
