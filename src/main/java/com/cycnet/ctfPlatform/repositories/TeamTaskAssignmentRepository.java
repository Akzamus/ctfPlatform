package com.cycnet.ctfPlatform.repositories;

import com.cycnet.ctfPlatform.models.Task;
import com.cycnet.ctfPlatform.models.TeamRegistration;
import com.cycnet.ctfPlatform.models.TeamTaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamTaskAssignmentRepository extends JpaRepository<TeamTaskAssignment, Long> {

    Optional<TeamTaskAssignment> findByTeamRegistrationAndTask(TeamRegistration teamRegistration, Task task);

}
