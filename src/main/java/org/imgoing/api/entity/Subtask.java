package org.imgoing.api.entity;

import lombok.*;
import org.imgoing.api.config.BaseTime;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subtask_tb")
public class Subtask extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer time;

//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "user_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private User user;

    public void modifyTask(Subtask newSubtask) {
        this.name = newSubtask.getName();
        this.time = newSubtask.getTime();
    }
}
