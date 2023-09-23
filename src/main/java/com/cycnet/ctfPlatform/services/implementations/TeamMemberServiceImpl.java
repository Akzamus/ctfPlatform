package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.teamMember.TeamMemberRequestDto;
import com.cycnet.ctfPlatform.dto.teamMember.TeamMemberResponseDto;
import com.cycnet.ctfPlatform.enums.TeamRole;
import com.cycnet.ctfPlatform.exceptions.entity.EntityAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.mappers.TeamMemberMapper;
import com.cycnet.ctfPlatform.models.Student;
import com.cycnet.ctfPlatform.models.TeamMember;
import com.cycnet.ctfPlatform.models.TeamRegistration;
import com.cycnet.ctfPlatform.repositories.TeamMemberRepository;
import com.cycnet.ctfPlatform.services.StudentService;
import com.cycnet.ctfPlatform.services.TeamMemberService;
import com.cycnet.ctfPlatform.services.TeamRegistrationService;
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
public class TeamMemberServiceImpl implements TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamMemberMapper teamMemberMapper;
    private final StudentService studentService;
    private final TeamRegistrationService teamRegistrationService;

    @Override
    public PageResponseDto<TeamMemberResponseDto> getAll(int pageNumber, int pageSize) {
        log.info("Retrieving TeamMember, page number: {}, page size: {}", pageNumber, pageSize);

        Page<TeamMember> page = teamMemberRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<TeamMemberResponseDto> responseDto = teamMemberMapper.toDto(page);

        log.info("Finished retrieving TeamMember, page number: {}, page size: {}", pageNumber, pageSize);

        return responseDto;
    }

    @Override
    public TeamMemberResponseDto getById(long id) {
        log.info("Retrieving TeamMember with ID: {}", id);

        TeamMember teamMember = getEntityById(id);
        TeamMemberResponseDto responseDto = teamMemberMapper.toDto(teamMember);

        log.info("Finished retrieving TeamMember by ID: {}", teamMember.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public TeamMemberResponseDto create(TeamMemberRequestDto requestDto) {
        log.info(
                "Creating a new TeamMember for TeamRegistration with ID {} and Student with ID {}",
                requestDto.teamRegistrationId(),
                requestDto.studentId()
        );

        TeamRegistration teamRegistration = teamRegistrationService.getEntityById(requestDto.teamRegistrationId());
        Student student = studentService.getEntityById(requestDto.studentId());

        throwExceptionIfTeamMemberExists(student, teamRegistration);

        TeamMember teamMember = teamMemberMapper.toEntity(requestDto);
        teamMember.setStudent(student);
        teamMember.setTeamRegistration(teamRegistration);

        log.info(
                "TeamRegistration with ID {} and Student with ID {} are set for TeamMember",
                teamRegistration.getId(),
                student.getId()
        );

        teamMember = teamMemberRepository.save(teamMember);
        TeamMemberResponseDto responseDto = teamMemberMapper.toDto(teamMember);

        log.info("Created a new TeamMember with ID: {}", teamMember.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public TeamMemberResponseDto update(long id, TeamMemberRequestDto requestDto) {
        log.info("Updating a TeamMember with ID: {}", id);

        TeamMember teamMember = getEntityById(id);

        Student student = teamMember.getStudent();
        TeamRegistration teamRegistration = teamMember.getTeamRegistration();

        long oldStudentId = student.getId();
        long oldTeamRegistrationId = teamRegistration.getId();

        long newStudentId = requestDto.studentId();
        long newTeamRegistrationId = requestDto.teamRegistrationId();

        if (newTeamRegistrationId != oldTeamRegistrationId) {
            teamRegistration = teamRegistrationService.getEntityById(newTeamRegistrationId);
            throwExceptionIfTeamMemberExists(student, teamRegistration);
            teamMember.setTeamRegistration(teamRegistration);

            log.info(
                    "TeamRegistration with ID {} has been set for TeamMember with ID: {}",
                    teamRegistration.getId(),
                    teamMember.getId()
            );
        }

        if (newStudentId != oldStudentId) {
            student = studentService.getEntityById(newStudentId);
            throwExceptionIfTeamMemberExists(student, teamRegistration);
            teamMember.setStudent(student);

            log.info(
                    "Student with ID {} has been set for TeamMember with ID: {}",
                    student.getId(),
                    teamMember.getId()
            );
        }

        teamMember.setTeamRole(
                TeamRole.valueOf(requestDto.teamRole())
        );

        teamMember = teamMemberRepository.save(teamMember);
        TeamMemberResponseDto responseDto = teamMemberMapper.toDto(teamMember);

        log.info("Updated TeamMember with ID: {}", teamMember.getId());

        return responseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Deleting TeamMember with ID: {}", id);

        TeamMember teamMember = getEntityById(id);
        teamMemberRepository.delete(teamMember);

        log.info("Deleted TeamMember with ID: {}", id);
    }

    @Override
    public TeamMember getEntityById(long id) {
        return teamMemberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team member with ID " + id + " does not exists."));
    }

    private void throwExceptionIfTeamMemberExists(Student student, TeamRegistration teamRegistration) {
        teamMemberRepository.findByStudentAndTeamRegistration(student, teamRegistration)
                .ifPresent(foundTeamMember -> {
                    throw new EntityAlreadyExistsException(
                            String.format(
                                    "Student with ID %d is already in the team with registration ID %d",
                                    student.getId(),
                                    teamRegistration.getId()
                            )
                    );
                });
    }
}
