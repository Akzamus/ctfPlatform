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
@Table(name = "point")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @OneToOne
    @JoinColumn(name = "team_task_assignment_id", referencedColumnName = "id")
    private TeamTaskAssignment teamTaskAssignment;

    @TimeZoneStorage(TimeZoneStorageType.DEFAULT)
    @Column(name = "issued_at")
    private ZonedDateTime issuedAt;

}
