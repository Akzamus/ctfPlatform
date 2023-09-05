package com.cycnet.ctfPlatform.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TeamTaskAssignment")
public class TeamTaskAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_registration_id", referencedColumnName ="id")
    private TeamRegistration teamRegistration;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    @OneToOne(mappedBy = "teamTaskAssignment", cascade = CascadeType.ALL)
    private Point point;

    @OneToOne(mappedBy = "teamTaskAssignment", cascade = CascadeType.ALL)
    private TeamCorrectAnswer teamCorrectAnswer;

}
