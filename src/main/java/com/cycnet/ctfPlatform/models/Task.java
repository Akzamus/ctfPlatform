package com.cycnet.ctfPlatform.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_points")
    private Integer numberOfPoints;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TeamTaskAssignment> teamTaskAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "dependentTask", cascade = CascadeType.ALL)
    private List<TaskDependence> incomingDependencies = new ArrayList<>();

    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL)
    private List<TaskDependence> outgoingDependencies = new ArrayList<>();

}
