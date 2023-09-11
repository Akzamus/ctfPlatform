package com.cycnet.ctfPlatform.models;

import com.cycnet.ctfPlatform.enums.TeamResult;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@ToString(exclude = {"teamTaskAssignments", "teamMembers", "winningTeam"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "team_registration")
public class TeamRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_result")
    private TeamResult teamResult;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @OneToMany(mappedBy = "teamRegistration", cascade = CascadeType.ALL)
    private List<TeamTaskAssignment> teamTaskAssignments;

    @OneToMany(mappedBy = "teamRegistration", cascade = CascadeType.ALL)
    private List<TeamMember> teamMembers;

}
