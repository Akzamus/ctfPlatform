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

        Page<TeamTaskAssignment> page = teamTaskAssignmentRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<TeamTaskAssignmentResponseDto> pageResponseDto = teamTaskAssignmentMapper.toDto(page);

        log.info("Finished retrieving TeamTaskAssignments, page number: {}, page size: {}", pageNumber, pageSize);

        return pageResponseDto;
    }

    @Override
    public TeamTaskAssignmentResponseDto getById(long id) {
        log.info("Retrieving TeamTaskAssignment with ID: {}", id);

        TeamTaskAssignment teamTaskAssignment = getEntityById(id);
        TeamTaskAssignmentResponseDto responseDto = teamTaskAssignmentMapper.toDto(teamTaskAssignment);

        log.info("Finished retrieving TeamTaskAssignment by ID: {}", teamTaskAssignment.getId());

        return responseDto;
    }

    @Override
    public TeamTaskAssignmentResponseDto create(TeamTaskAssignmentRequestDto requestDto) {
        log.info(
                "Creating new TeamTaskAssignment for TeamRegistration with ID {} and Task with ID {}",
                requestDto.teamRegistrationId(),
                requestDto.taskId()
        );

        TeamRegistration teamRegistration = teamRegistrationService.getEntityById(requestDto.teamRegistrationId());
        Task task = taskService.getEntityById(requestDto.taskId());

        throwExceptionIfTeamTaskAssignmentExists(teamRegistration, task);

        TeamTaskAssignment teamTaskAssignment = teamTaskAssignmentMapper.toEntity(requestDto);
        teamTaskAssignment.setTeamRegistration(teamRegistration);
        teamTaskAssignment.setTask(task);

        log.info(
                "TeamRegistration with ID {} and Task with ID {} are set for TeamTaskAssignment",
                teamRegistration.getId(),
                task.getId()
        );

        teamTaskAssignment = teamTaskAssignmentRepository.save(teamTaskAssignment);
        TeamTaskAssignmentResponseDto responseDto = teamTaskAssignmentMapper.toDto(teamTaskAssignment);

        log.info("Created new TeamTaskAssignment with ID: {}", teamTaskAssignment.getId());

        return responseDto;
    }

    @Override
    public TeamTaskAssignmentResponseDto update(long id, TeamTaskAssignmentRequestDto requestDto) {
        log.info("Updating TeamTaskAssignment with ID: {}", id);

        TeamTaskAssignment teamTaskAssignment = getEntityById(id);

        TeamRegistration teamRegistration = teamTaskAssignment.getTeamRegistration();
        Task task = teamTaskAssignment.getTask();

        long oldTeamRegistrationId = teamRegistration.getId();
        long oldTaskId = task.getId();

        long newTeamRegistrationId = requestDto.teamRegistrationId();
        long newTaskId = requestDto.taskId();

        if (newTeamRegistrationId != oldTeamRegistrationId) {
            teamRegistration = teamRegistrationService.getEntityById(newTeamRegistrationId);
            throwExceptionIfTeamTaskAssignmentExists(teamRegistration, task);
            teamTaskAssignment.setTeamRegistration(teamRegistration);

            log.info(
                    "TeamRegistration with ID {} has been set for TeamTaskAssignment with ID: {}",
                    teamRegistration.getId(),
                    teamTaskAssignment.getId()
            );
        }

        if (newTaskId != oldTaskId) {
            task = taskService.getEntityById(newTaskId);
            throwExceptionIfTeamTaskAssignmentExists(teamRegistration, task);
            teamTaskAssignment.setTask(task);

            log.info(
                    "Task with ID {} has been set for TeamTaskAssignment with ID: {}",
                    task.getId(),
                    teamTaskAssignment.getId()
            );
        }

        teamTaskAssignment.setCorrectAnswer(
                requestDto.correctAnswer()
        );

        teamTaskAssignment = teamTaskAssignmentRepository.save(teamTaskAssignment);
        TeamTaskAssignmentResponseDto responseDto = teamTaskAssignmentMapper.toDto(teamTaskAssignment);

        log.info("Updated TeamTaskAssignment with ID: {}", teamTaskAssignment.getId());

        return responseDto;
    }

    @Override
    public void delete(long id) {
        log.info("Deleting TeamTaskAssignment with ID: {}", id);

        TeamTaskAssignment teamTaskAssignment = getEntityById(id);
        teamTaskAssignmentRepository.delete(teamTaskAssignment);

        log.info("Deleted TeamTaskAssignment with ID: {}", teamTaskAssignment.getId());
    }

    @Override
    public TeamTaskAssignment getEntityById(long id) {
        return teamTaskAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Record of the assignment to the team with the ID " + id + " was not found"
                ));
    }

    private void throwExceptionIfTeamTaskAssignmentExists(TeamRegistration teamRegistration, Task task) {
        teamTaskAssignmentRepository.findByTeamRegistrationAndTask(teamRegistration, task)
                .ifPresent(foundTeamTaskAssignment-> {
                    throw new EntityAlreadyExistsException(
                            String.format(
                                    "Team with registration ID %d has already been given a task with ID %d.",
                                    teamRegistration.getId(),
                                    task.getId()
                            )
                    );
                });
    }

}
