package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.teamTaskAssignment.TeamTaskAssignmentRequestDto;
import com.cycnet.ctfPlatform.dto.teamTaskAssignment.TeamTaskAssignmentResponseDto;
import com.cycnet.ctfPlatform.exceptions.entity.EntityAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.mappers.TeamTaskAssignmentMapper;
import com.cycnet.ctfPlatform.models.*;
import com.cycnet.ctfPlatform.repositories.TeamTaskAssignmentRepository;
import com.cycnet.ctfPlatform.services.TaskService;
import com.cycnet.ctfPlatform.services.TeamRegistrationService;
import com.cycnet.ctfPlatform.services.TeamTaskAssignmentService;
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
public class TeamTaskAssignmentServiceImpl implements TeamTaskAssignmentService {

    private final TeamTaskAssignmentRepository teamTaskAssignmentRepository;
    private final TeamTaskAssignmentMapper teamTaskAssignmentMapper;
    private final TeamRegistrationService teamRegistrationService;
    private final TaskService taskService;

    @Override
    public PageResponseDto<TeamTaskAssignmentResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving TeamTaskAssignments, page number: {}, page size: {}", pageNumber, pageSize);

        Page<TeamTaskAssignment> teamTaskAssignmentPage = teamTaskAssignmentRepository
                .findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<TeamTaskAssignmentResponseDto> teamTaskAssignmentPageResponseDto = teamTaskAssignmentMapper
                .toDto(teamTaskAssignmentPage);

        log.info("Finished retrieving TeamTaskAssignments, page number: {}, page size: {}", pageNumber, pageSize);

        return teamTaskAssignmentPageResponseDto;
    }

    @Override
    public TeamTaskAssignmentResponseDto getById(long id) {
        log.info("Retrieving TeamTaskAssignment with ID: {}", id);

        TeamTaskAssignment teamTaskAssignment = getEntityById(id);
        TeamTaskAssignmentResponseDto teamTaskAssignmentResponseDto = teamTaskAssignmentMapper
                .toDto(teamTaskAssignment);

        log.info("Finished retrieving TeamTaskAssignment by ID: {}", teamTaskAssignment.getId());

        return teamTaskAssignmentResponseDto;
    }

    @Override
    public TeamTaskAssignmentResponseDto create(TeamTaskAssignmentRequestDto requestDto) {
        log.info(
                "Creating a new TeamTaskAssignment for TeamRegistration with ID {} and Task with ID {}",
                requestDto.teamRegistrationId(),
                requestDto.taskId()
        );

        TeamRegistration teamRegistration = teamRegistrationService.getEntityById(requestDto.teamRegistrationId());
        Task task = taskService.getEntityById(requestDto.taskId());

        throwExceptionIfTeamTaskAssignmentExists(teamRegistration, task);

        log.info(
                "TeamRegistration with ID {} and Task with ID {}  are set for TeamTaskAssignment",
                teamRegistration.getId(),
                task.getId()
        );

        TeamTaskAssignment teamTaskAssignment = teamTaskAssignmentMapper.toEntity(requestDto);
        teamTaskAssignment.setTeamRegistration(teamRegistration);
        teamTaskAssignment.setTask(task);

        teamTaskAssignment = teamTaskAssignmentRepository.save(teamTaskAssignment);
        TeamTaskAssignmentResponseDto teamTaskAssignmentResponseDto = teamTaskAssignmentMapper
                .toDto(teamTaskAssignment);

        log.info("Created a new TeamTaskAssignment with ID: {}", teamTaskAssignment.getId());

        return teamTaskAssignmentResponseDto;
    }

    @Override
    public TeamTaskAssignmentResponseDto update(long id, TeamTaskAssignmentRequestDto requestDto) {
        log.info("Updating a TeamTaskAssignment with ID: {}", id);

        TeamTaskAssignment teamTaskAssignment = getEntityById(id);

        TeamRegistration teamRegistration = teamTaskAssignment.getTeamRegistration();
        Task task = teamTaskAssignment.getTask();

        long teamRegistrationId = teamRegistration.getId();
        long taskId = task.getId();

        if (requestDto.teamRegistrationId() != teamRegistrationId) {
            teamRegistration = teamRegistrationService.getEntityById(requestDto.teamRegistrationId());
            throwExceptionIfTeamTaskAssignmentExists(teamRegistration, task);
            teamTaskAssignment.setTeamRegistration(teamRegistration);

            log.info(
                    "TeamRegistration with ID {} has been set for TeamTaskAssignment with ID: {}",
                    teamRegistration.getId(),
                    teamTaskAssignment.getId())
            ;
        }

        if (requestDto.taskId() != taskId) {
            task = taskService.getEntityById(requestDto.taskId());
            throwExceptionIfTeamTaskAssignmentExists(teamRegistration, task);
            teamTaskAssignment.setTask(task);

            log.info(
                    "Task with ID {} has been set for TeamTaskAssignment with ID: {}",
                    task.getId(),
                    teamTaskAssignment.getId()
            );
        }

        teamTaskAssignment.setCorrectAnswer(requestDto.correctAnswer());

        teamTaskAssignment = teamTaskAssignmentRepository.save(teamTaskAssignment);
        TeamTaskAssignmentResponseDto teamTaskAssignmentResponseDto = teamTaskAssignmentMapper
                .toDto(teamTaskAssignment);

        log.info("Updated TeamTaskAssignment with ID: {}", teamTaskAssignment.getId());

        return teamTaskAssignmentResponseDto;
    }

    @Override
    public void delete(long id) {
        log.info("Deleting TeamTaskAssignment with ID: {}", id);

        TeamTaskAssignment existingTeamTaskAssignment = getEntityById(id);
        teamTaskAssignmentRepository.delete(existingTeamTaskAssignment);

        log.info("Deleted TeamTaskAssignment with ID: {}", id);
    }

    @Override
    public TeamTaskAssignment getEntityById(long id) {
        return teamTaskAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Record of the assignment to the team with the ID " + id + " was not found"
                ));
    }

    private void throwExceptionIfTeamTaskAssignmentExists(
            TeamRegistration teamRegistration,
            Task task
    ) {
        teamTaskAssignmentRepository.findByTeamRegistrationAndTask(teamRegistration, task)
                .ifPresent(foundTeamTaskAssignment-> {
                    throw new EntityAlreadyExistsException(
                            "Team with registration ID " + teamRegistration.getId() +
                                    " has already been given a task with ID " + task.getId() + "."
                    );
                });
    }

}
