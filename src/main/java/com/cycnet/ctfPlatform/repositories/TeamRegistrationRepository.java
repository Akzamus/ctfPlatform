package com.cycnet.ctfPlatform.repositories;


import com.cycnet.ctfPlatform.models.Event;
import com.cycnet.ctfPlatform.models.Team;
import com.cycnet.ctfPlatform.models.TeamRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRegistrationRepository extends JpaRepository<TeamRegistration, Long> {

    Optional<TeamRegistration> findByTeamAndEvent(Team team, Event event);

}
