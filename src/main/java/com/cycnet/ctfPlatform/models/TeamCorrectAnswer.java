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
@Table(name = "team_correct_answer")
public class TeamCorrectAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "correct_answer")
    private String correctAnswer;


    @OneToOne
    @JoinColumn(name = "team_task_assignment_id", referencedColumnName = "id")
    private TeamTaskAssignment teamTaskAssignment;

}
