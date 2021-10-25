package org.imgoing.api.domain.entity;

import lombok.*;
import org.imgoing.api.config.BaseTime;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plantask_tb")
public class Plantask extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "planId", referencedColumnName = "id")
    private Plan plan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "taskId", referencedColumnName = "id")
    private Task task;

    public void setPlanTask(Plan plan, Task task) {
        this.plan = plan;
        this.task = task;
    }
}
