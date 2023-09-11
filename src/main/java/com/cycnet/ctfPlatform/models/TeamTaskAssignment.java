package com.cycnet.ctfPlatform.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "team_task_assignment")
public class TeamTaskAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "correct_answer")
    private String correctAnswer;

    @TimeZoneStorage(TimeZoneStorageType.DEFAULT)
    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "team_registration_id", referencedColumnName ="id")
    private TeamRegistration teamRegistration;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

}
