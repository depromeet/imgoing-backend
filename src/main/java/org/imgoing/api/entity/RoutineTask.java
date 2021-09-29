package org.imgoing.api.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="routine_task_tb")
public class RoutineTask extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "routine_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Routine routine;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "routine_task_id")
    private List<Task> taskList = new ArrayList<>();
}
