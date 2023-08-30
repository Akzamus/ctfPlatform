package com.cycnet.ctfPlatform.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="team_registration")
public class TeamRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @OneToMany(mappedBy = "teamRegistration", cascade = CascadeType.ALL)
    private List<TeamTaskAssignment> teamTaskAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "teamRegistration", cascade = CascadeType.ALL)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @OneToOne(mappedBy = "teamRegistration", cascade = CascadeType.ALL)
    private WinningTeam winningTeam;

}
