package com.cycnet.ctfPlatform.services.implementations;

import com.cycnet.ctfPlatform.dto.PageResponseDto;
import com.cycnet.ctfPlatform.dto.team.TeamRequestDto;
import com.cycnet.ctfPlatform.dto.team.TeamResponseDto;
import com.cycnet.ctfPlatform.exceptions.entity.EntityAlreadyExistsException;
import com.cycnet.ctfPlatform.exceptions.entity.EntityNotFoundException;
import com.cycnet.ctfPlatform.mappers.TeamMapper;
import com.cycnet.ctfPlatform.models.Team;
import com.cycnet.ctfPlatform.models.TeamRegistration;
import com.cycnet.ctfPlatform.repositories.TeamRepository;
import com.cycnet.ctfPlatform.services.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    private TeamService underTest;

    @BeforeEach
    void setUp() {
        underTest = new TeamServiceImpl(teamRepository, teamMapper);
    }

    @Test
    void getAll_WhenTeamExists_ThenReturnPageResponseDto() {
        // give
        int pageNumber = 0;
        int pageSize = 10;

        long id = 1L;
        String teamName = "TeamName";
        List<TeamRegistration> teamRegistrations = Collections.emptyList();

        Team team = new Team(id, teamName, teamRegistrations);
        TeamResponseDto responseDto = new TeamResponseDto(id, teamName);

        Page<Team> page = new PageImpl<>(Collections.singletonList(team));
        PageResponseDto<TeamResponseDto> pageResponseDto = new PageResponseDto<>(
                pageNumber, pageSize, 1, 1, true, true, false,
                Collections.singletonList(responseDto)
        );

        when(teamRepository.findAll(PageRequest.of(pageNumber, pageSize))).thenReturn(page);
        when(teamMapper.toDto(page)).thenReturn(pageResponseDto);

        // when
        PageResponseDto<TeamResponseDto> result = underTest.getAll(pageNumber, pageSize);

        // then
        verify(teamRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize));
        verify(teamMapper, times(1)).toDto(page);

        assertEquals(result, pageResponseDto);
    }

    @Test
    void getById_WhenTeamExists_ThenReturnTeamResponseDto() {
        // give
        long id = 1L;
        String teamName = "TeamName";
        List<TeamRegistration> teamRegistrations = Collections.emptyList();

        Team team = new Team(id, teamName, teamRegistrations);
        TeamResponseDto responseDto = new TeamResponseDto(id, teamName);

        when(teamRepository.findById(id)).thenReturn(Optional.of(team));
        when(teamMapper.toDto(team)).thenReturn(responseDto);

        // when
        TeamResponseDto result = underTest.getById(id);

        // then
        verify(teamRepository).findById(id);
        verify(teamMapper).toDto(team);

        assertEquals(result, responseDto);
    }

    @Test
    void getById_WhenTeamDoesNotExist_ThenThrowEntityNotFoundException() {
        // give
        long id = 1L;

        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        // when and then
        assertThrows(
                EntityNotFoundException.class,
                () -> underTest.getById(id),
                "Team with ID " + id + " does not exist"
        );

        verify(teamRepository).findById(id);
    }

    @Test
    public void create_WhenTeamAlreadyExists_ThenThrowEntityAlreadyExistsException() {
        // give
        long id = 1L;
        String teamName = "TeamName";
        List<TeamRegistration> teamRegistrations = Collections.emptyList();

        TeamRequestDto requestDto = new TeamRequestDto(teamName);
        Team team = new Team(id, teamName, teamRegistrations);

        when(teamRepository.findByName(teamName)).thenReturn(Optional.of(team));

        // when and then
        assertThrows(
                EntityAlreadyExistsException.class,
                () -> underTest.create(requestDto),
                "Team with the name " + teamName + " already exists"
        );

        verify(teamRepository).findByName(teamName);
    }

    @Test
    void create_WhenTeamDoesNotExist_ThenReturnTeamResponseDto() {
        // give
        long id = 1L;
        String teamName = "TeamName";
        List<TeamRegistration> teamRegistrations = Collections.emptyList();

        Team team = new Team(null, teamName, teamRegistrations);
        Team savedTeam = new Team(id, teamName, teamRegistrations);

        TeamRequestDto requestDto = new TeamRequestDto(teamName);
        TeamResponseDto responseDto = new TeamResponseDto(id, teamName);

        when(teamRepository.findByName(teamName)).thenReturn(Optional.empty());
        when(teamMapper.toEntity(requestDto)).thenReturn(team);
        when(teamRepository.save(team)).thenReturn(savedTeam);
        when(teamMapper.toDto(savedTeam)).thenReturn(responseDto);

        // when
        TeamResponseDto result = underTest.create(requestDto);

        // then
        verify(teamRepository).findByName(teamName);
        verify(teamRepository).save(team);

        assertEquals(result, responseDto);
    }

    @Test
    void update_WhenTeamExistsAndOldAndNewNamesEqual_ThenReturnTeamResponseDto() {
        // give
        long id = 1L;
        String oldTeamName = "OldTeamName";
        String newTeamName = "OldTeamName";
        List<TeamRegistration> teamRegistrations = Collections.emptyList();

        TeamRequestDto requestDto = new TeamRequestDto(newTeamName);
        Team oldTeam = new Team(id, oldTeamName, teamRegistrations);
        TeamResponseDto responseDto = new TeamResponseDto(id, oldTeamName);

        when(teamRepository.findById(id)).thenReturn(Optional.of(oldTeam));
        when(teamMapper.toDto(oldTeam)).thenReturn(responseDto);

        // when
        TeamResponseDto result = underTest.update(id, requestDto);

        // then
        verify(teamRepository).findById(id);
        verify(teamRepository, never()).findByName(newTeamName);

        assertEquals(result, responseDto);
    }

    @Test
    void update_WhenTeamExistsAndNewNameOccupied_ThenThrowEntityAlreadyExistsException() {
        // give
        long id = 1L;
        String oldTeamName = "OldTeamName";
        String newTeamName = "NewTeamName";
        List<TeamRegistration> teamRegistrations = Collections.emptyList();

        TeamRequestDto requestDto = new TeamRequestDto(newTeamName);
        Team oldTeam = new Team(id, oldTeamName, teamRegistrations);
        Team founTeam = new Team(id, newTeamName, teamRegistrations);

        when(teamRepository.findById(id)).thenReturn(Optional.of(oldTeam));
        when(teamRepository.findByName(newTeamName)).thenReturn(Optional.of(founTeam));

        // when and then
        assertThrows(
                EntityAlreadyExistsException.class,
                () -> underTest.update(id, requestDto),
                "Team with the name " + newTeamName + " already exists"
        );

        verify(teamRepository).findById(id);
        verify(teamRepository).findByName(newTeamName);
    }

    @Test
    void update_WhenTeamExistsAndNewNameFree_ThenReturnTeamResponseDto() {
        // give
        long id = 1L;
        String oldTeamName = "OldTeamName";
        String newTeamName = "NewTeamName";
        List<TeamRegistration> teamRegistrations = Collections.emptyList();

        TeamRequestDto requestDto = new TeamRequestDto(newTeamName);
        Team oldTeam = new Team(id, oldTeamName, teamRegistrations);
        Team newTeam = new Team(id, newTeamName, teamRegistrations);
        TeamResponseDto responseDto = new TeamResponseDto(id, newTeamName);

        when(teamRepository.findById(id)).thenReturn(Optional.of(oldTeam));
        when(teamRepository.findByName(newTeamName)).thenReturn(Optional.empty());
        when(teamRepository.save(oldTeam)).thenReturn(newTeam);
        when(teamMapper.toDto(newTeam)).thenReturn(responseDto);

        // when
        TeamResponseDto result = underTest.update(id, requestDto);

        // then
        verify(teamRepository).findById(id);
        verify(teamRepository).findByName(newTeamName);
        verify(teamRepository).save(oldTeam);

        assertEquals(result, responseDto);
    }

    @Test
    void update_WhenTeamDoesNotExist_ThenThrowEntityNotFoundException() {
        // give
        long id = 1L;
        String newTeamName = "NewTeamName";

        TeamRequestDto requestDto = new TeamRequestDto(newTeamName);

        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        // when and then
        assertThrows(
                EntityNotFoundException.class,
                () -> underTest.update(id, requestDto),
                "Team with ID " + id + " does not exist"
        );

        verify(teamRepository).findById(id);
    }

    @Test
    void delete_WhenTeamExists_ThenDeleteTeam() {
        // given
        long id = 1L;
        String teamName = "TeamName";
        List<TeamRegistration> teamRegistrations = Collections.emptyList();

        Team team = new Team(id, teamName, teamRegistrations);

        when(teamRepository.findById(id)).thenReturn(Optional.of(team));

        // when
        underTest.delete(id);

        // then
        verify(teamRepository).findById(id);
        verify(teamRepository).delete(team);
    }

    @Test
    void delete_WhenTeamDoesNotExist_ThenThrowEntityNotFoundException() {
        // given
        long id = 1L;

        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        // when and then
        assertThrows(
                EntityNotFoundException.class,
                () -> underTest.delete(id),
                "Team with ID " + id + " does not exist"
        );

        verify(teamRepository).findById(id);
    }

    @Test
    void getEntityById_WhenTeamExists_ThenReturnTeam() {
        // given
        long id = 1L;
        String teamName = "TeamName";
        List<TeamRegistration> teamRegistrations = Collections.emptyList();

        Team team = new Team(id, teamName, teamRegistrations);

        when(teamRepository.findById(id)).thenReturn(Optional.of(team));

        // when
        Team result = underTest.getEntityById(id);

        // then
        verify(teamRepository).findById(id);

        assertEquals(result, team);
    }

    @Test
    void getEntityById_WhenTeamDoesNotExist_ThenThrowEntityNotFoundException() {
        // given
        long id = 1L;

        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        // when and then
        assertThrows(
                EntityNotFoundException.class,
                () -> underTest.getEntityById(id),
                "Team with ID " + id + " does not exist"
        );

        verify(teamRepository).findById(id);
    }

}