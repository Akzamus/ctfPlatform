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
@Table(name = "score")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_of_points")
    private int number_of_point;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_registration_id", referencedColumnName = "id")
    private TeamRegistration teamRegistration;



}
