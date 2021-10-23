package org.imgoing.api.domain.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.imgoing.api.config.BaseTime;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "routinetask_tb")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Routinetask extends BaseTime implements Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "routineId", referencedColumnName = "id")
    private Routine routine;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "taskId", referencedColumnName = "id")
    private Task task;

    private Integer priority;

    @Override
    public int compareTo(Object o) {
        Routinetask routinetask = (Routinetask)o;
        return this.priority - routinetask.priority;
    }

    public void addPriority(Integer priority) {
        this.priority = priority;
    }
}