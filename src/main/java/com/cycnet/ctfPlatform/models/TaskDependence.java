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
@Table(name = "task_dependence")
public class TaskDependence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dependent_task_id",referencedColumnName = "id")
    private Task dependentTask;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id",referencedColumnName = "id")
    private Task task;
}
