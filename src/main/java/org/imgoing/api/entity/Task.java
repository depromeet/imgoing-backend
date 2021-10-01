package org.imgoing.api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.imgoing.api.config.BaseTime;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "task_tb")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;


    @JsonManagedReference
    @OneToOne(mappedBy = "task", fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", referencedColumnName = "id")
    private Routine routine;

    public void modifyRoutine(Task newTask) {
        this.name = newTask.getName();
    }
}
