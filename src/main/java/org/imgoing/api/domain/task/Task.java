package org.imgoing.api.domain.task;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.imgoing.api.domain.BaseTimeEntity;

import javax.persistence.*;

@Getter @Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="task_tb")
public class Task extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer time;
}
