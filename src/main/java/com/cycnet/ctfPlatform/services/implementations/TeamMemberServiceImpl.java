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

        Page<TeamMember> teamMemberPage = teamMemberRepository.findAll(PageRequest.of(pageNumber, pageSize));
        PageResponseDto<TeamMemberResponseDto> teamMemberResponseDtoPageResponseDto = teamMemberMapper.toDto(teamMemberPage);

        return teamMemberResponseDtoPageResponseDto;
    }

    @Override
    public TeamMemberResponseDto getById(long id) {

        TeamMember teamMember = getEntityById(id);
        TeamMemberResponseDto teamMemberResponseDto = teamMemberMapper.toDto(teamMember);

        return teamMemberResponseDto;
    }

    @Override
    @Transactional
    public TeamMemberResponseDto create(TeamMemberRequestDto requestDto) {
        Student student = studentService.getEntityById(requestDto.studentId());
        TeamRegistration teamRegistration = teamRegistrationService.getEntityById(requestDto.teamRegistrationId());

        throwExceptionIfTeamMemberWithStudentAndTeamRegistrationExists(student, teamRegistration);

        TeamMember teamMember = teamMemberMapper.toEntity(requestDto);

        teamMember.setStudent(student);
        teamMember.setTeamRegistration(teamRegistration);

        teamMember = teamMemberRepository.save(teamMember);
        TeamMemberResponseDto teamMemberResponseDto = teamMemberMapper.toDto(teamMember);

        return teamMemberResponseDto;
    }

    @Override
    @Transactional
    public TeamMemberResponseDto update(long id, TeamMemberRequestDto requestDto) {
        TeamMember teamMember = getEntityById(id);

        Student student = teamMember.getStudent();
        TeamRegistration teamRegistration = teamMember.getTeamRegistration();

        long studentId = teamMember.getStudent().getId();
        long teamRegistrationId = teamMember.getTeamRegistration().getId();

        if(requestDto.teamRegistrationId() != teamRegistrationId){
            teamRegistration = teamRegistrationService.getEntityById(requestDto.teamRegistrationId());
        }

        if(requestDto.studentId() != studentId){
            student = studentService.getEntityById(requestDto.studentId());
        }

        if(requestDto.studentId() != studentId || requestDto.teamRegistrationId() != teamRegistrationId){
            throwExceptionIfTeamMemberWithStudentAndTeamRegistrationExists(student, teamRegistration);
        }

        teamMember.setTeamRegistration(teamRegistration);
        teamMember.setStudent(student);
        teamMember.setTeamRole(TeamRole.valueOf(requestDto.teamRole()));

        teamMember = teamMemberRepository.save(teamMember);
        TeamMemberResponseDto teamMemberResponseDto = teamMemberMapper.toDto(teamMember);
        return teamMemberResponseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        TeamMember teamMember = getEntityById(id);
        teamMemberRepository.delete(teamMember);
    }

    @Override
    public TeamMember getEntityById(long id) {
        return teamMemberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team member with ID : " + id + " does not exists."));
    }

    private void throwExceptionIfTeamMemberWithStudentAndTeamRegistrationExists(Student student, TeamRegistration teamRegistration) {
        teamMemberRepository.findByStudentAndTeamRegistration(student, teamRegistration)
                .ifPresent(foundTeamMember -> {
                    throw new EntityAlreadyExistsException(
                            "Student with ID: "+ foundTeamMember.getStudent().getId() + " already registered for this team : "+
                                    foundTeamMember.getTeamRegistration().getTeam().getId()
                    );
                });
    }
}
