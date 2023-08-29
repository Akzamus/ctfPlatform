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
    private Long id;

    @Column(name = "correct_answer")
    private String correctAnswer;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_task_assignment_id", referencedColumnName = "id")
    private TeamTaskAssignment teamTaskAssignment;

}
