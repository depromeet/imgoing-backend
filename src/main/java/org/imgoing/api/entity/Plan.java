package org.imgoing.api.entity;

import lombok.*;
import org.imgoing.api.config.BaseTime;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="plan_tb")
public class Plan extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String departure_name;

    @Column(nullable = false)
    private Double departure_lat;

    @Column(nullable = false)
    private Double departure_lng;

    @Column(nullable = false)
    private String arrival_name;

    @Column(nullable = false)
    private Double arrival_lat;

    @Column(nullable = false)
    private Double arrival_lng;

    @Column(nullable = false)
    private LocalDateTime arrival_at;
}
