package org.imgoing.api.domain.entity;

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
    private String departureName;

    @Column(nullable = false)
    private Double departureLat;

    @Column(nullable = false)
    private Double departureLng;

    @Column(nullable = false)
    private String arrivalName;

    @Column(nullable = false)
    private Double arrivalLat;

    @Column(nullable = false)
    private Double arrivalLng;

    @Column(nullable = false)
    private LocalDateTime arrivalAt;

    @Column
    private String memo;

    @Column
    private String belongings;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    public void addUser(User user) {
        this.user = user;
    }
}