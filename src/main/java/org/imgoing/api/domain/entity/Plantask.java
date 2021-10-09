package org.imgoing.api.domain.entity;

import lombok.*;
import org.imgoing.api.config.BaseTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "taskId", referencedColumnName = "id")
    private List<Task> taskList = new ArrayList<>();

    public void modifyRoutine(List<Task> taskList) {
        this.taskList = taskList;
    }
}
