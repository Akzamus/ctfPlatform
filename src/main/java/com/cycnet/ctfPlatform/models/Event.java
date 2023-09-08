package com.cycnet.ctfPlatform.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @TimeZoneStorage(TimeZoneStorageType.DEFAULT)
    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    @TimeZoneStorage(TimeZoneStorageType.DEFAULT)
    @Column(name = "ended_at")
    private ZonedDateTime endedAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<TeamRegistration> teamRegistrations;

}
