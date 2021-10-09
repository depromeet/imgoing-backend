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
@Table(name = "task_tb")
public class Task extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer time;

    @Column(nullable = false)
    private Boolean isBookmarked;

//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "user_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private User user;

    public void modifyTask(Task newTask) {
        this.name = newTask.getName();
        this.time = newTask.getTime();
        this.isBookmarked = newTask.getIsBookmarked();
    }
}
