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
@Table(name = "team_task_assignment")
public class TeamTaskAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_registration_id", referencedColumnName ="id")
    private TeamRegistration teamRegistration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id",referencedColumnName = "id")
    private Task task;

}
