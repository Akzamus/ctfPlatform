package com.cycnet.ctfPlatform.repositories;

import com.cycnet.ctfPlatform.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    Optional<TeamMember> findByStudentAndTeamRegistration(Student student, TeamRegistration teamRegistration);

}
