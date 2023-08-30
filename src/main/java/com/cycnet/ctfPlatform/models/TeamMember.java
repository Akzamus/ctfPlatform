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
@Table(name = "team_member")
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_registration_id", referencedColumnName = "id")
    private TeamRegistration teamRegistration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

}
