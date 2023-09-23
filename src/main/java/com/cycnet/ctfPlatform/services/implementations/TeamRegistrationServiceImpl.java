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
        log.info("Retrieving TeamRegistration, page number: {}, page size: {}", pageNumber, pageSize);

        Page<TeamRegistration> page = teamRegistrationRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<TeamRegistrationResponseDto> pageResponseDto = teamRegistrationMapper.toDto(page);

        log.info("Finished retrieving TeamRegistration, page number: {}, page size: {}", pageNumber, pageSize);

        return pageResponseDto;
    }

    @Override
    public TeamRegistrationResponseDto getById(long id) {
        log.info("Retrieving TeamRegistration with ID: {}", id);

        TeamRegistration teamRegistration = getEntityById(id);
        TeamRegistrationResponseDto responseDto = teamRegistrationMapper.toDto(teamRegistration);

        log.info("Finished retrieving TeamRegistration by ID: {}", teamRegistration.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public TeamRegistrationResponseDto create(TeamRegistrationRequestDto requestDto) {
        log.info(
                "Creating new TeamRegistration for Team with ID {} and Event with ID {}",
                requestDto.teamId(),
                requestDto.eventId()
        );

        Team team = teamService.getEntityById(requestDto.teamId());
        Event event = eventService.getEntityById(requestDto.eventId());

        throwExceptionIfTeamRegistrationExists(team, event);

        TeamRegistration teamRegistration = teamRegistrationMapper.toEntity(requestDto);
        teamRegistration.setTeam(team);
        teamRegistration.setEvent(event);

        log.info(
                "Team with ID {} and Event with ID {} are set for TeamRegistration",
                team.getId(),
                event.getId()
        );

        teamRegistration = teamRegistrationRepository.save(teamRegistration);
        TeamRegistrationResponseDto responseDto = teamRegistrationMapper.toDto(teamRegistration);

        log.info("Created new TeamRegistration with ID: {}", teamRegistration.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public TeamRegistrationResponseDto update(long id, TeamRegistrationRequestDto requestDto) {
        log.info("Updating TeamRegistration with ID: {}", id);

        TeamRegistration teamRegistration = getEntityById(id);

        Team team = teamRegistration.getTeam();
        Event event = teamRegistration.getEvent();

        long oldTeamId = team.getId();
        long oldEventId = event.getId();

        long newTeamId = requestDto.teamId();
        long newEventId = requestDto.eventId();

        if (newTeamId != oldTeamId) {
            team = teamService.getEntityById(newTeamId);
            throwExceptionIfTeamRegistrationExists(team, event);
            teamRegistration.setTeam(team);

            log.info(
                    "Team with ID {} has been set for TeamRegistration with ID: {}",
                    team.getId(),
                    teamRegistration.getId()
            );
        }

        if (newEventId != oldEventId) {
            event = eventService.getEntityById(newEventId);
            throwExceptionIfTeamRegistrationExists(team, event);
            teamRegistration.setEvent(event);

            log.info(
                    "Event with ID {} has been set for TeamRegistration with ID: {}",
                    event.getId(),
                    teamRegistration.getId()
            );
        }

        teamRegistration.setTeamResult(
                TeamResult.valueOf(requestDto.teamResult())
        );

        teamRegistration = teamRegistrationRepository.save(teamRegistration);
        TeamRegistrationResponseDto responseDto = teamRegistrationMapper.toDto(teamRegistration);

        log.info("Updated TeamRegistration with ID: {}", teamRegistration.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting TeamRegistration with ID: {}", id);

        TeamRegistration teamRegistration = getEntityById(id);
        teamRegistrationRepository.delete(teamRegistration);

        log.info("Deleted TeamRegistration with ID: {}", teamRegistration.getId());
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
                            String.format(
                                    "Team with ID %d already registered for the event with ID: %d",
                                    team.getId(),
                                    event.getId()
                            )
                    );
                });
    }

}
