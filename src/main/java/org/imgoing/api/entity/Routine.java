package org.imgoing.api.entity;

import lombok.*;

import javax.persistence.*;

@Getter @Setter
@Entity
@Table(name="routine_tb")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Routine extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;
}
