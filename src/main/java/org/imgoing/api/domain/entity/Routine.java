package org.imgoing.api.domain.entity;

import lombok.*;
import org.imgoing.api.config.BaseTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public List<Routinetask> getRoutinetasks() {
        Collections.sort(routinetasks);
        return routinetasks;
    }

    public List<Routinetask> makeRoutinetasks(List<Task> tasks) {
        List<Routinetask> rt = new ArrayList<>();
        for(int i = 0; i < tasks.size(); ++i) {
            rt.add(Routinetask.builder()
                    .routine(this)
                    .task(tasks.get(i))
                    .priority(i)
                    .build());
        }
        return rt;
    }
}
