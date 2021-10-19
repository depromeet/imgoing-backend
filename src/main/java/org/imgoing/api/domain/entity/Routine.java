package org.imgoing.api.domain.entity;

import lombok.*;
import org.imgoing.api.config.BaseTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "routine_tb")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Routine extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "routine", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Routinetask> routinetasks = new ArrayList<>();

    public void modifyRoutine(Routine newRoutine) {
        this.name = newRoutine.getName();
    }

    public void setRoutinetasks(List<Task> tasks) {
        this.routinetasks = tasks.stream()
                .map(task -> Routinetask.builder()
                        .routine(this)
                        .task(task)
                        .build())
                .collect(Collectors.toList());
    }

    public List<Task> getTaskList() {
        return routinetasks.stream().map(Routinetask::getTask).collect(Collectors.toList());
    }
}
