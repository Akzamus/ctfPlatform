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
@Table(name = "TaskDependence")
public class TaskDependence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dependent_task_id", referencedColumnName = "id")
    private Task dependentTask;

    @ManyToOne
    @JoinColumn(name = "parent_task_id", referencedColumnName = "id")
    private Task parentTask;

}
