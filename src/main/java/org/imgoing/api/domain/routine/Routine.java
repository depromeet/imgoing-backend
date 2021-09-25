package org.imgoing.api.domain.routine;

import lombok.*;
import org.imgoing.api.domain.BaseTimeEntity;

import javax.persistence.*;

@Getter @Setter
@Entity
@Table(name="routine_tb")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Routine extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;
}
