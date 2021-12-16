package org.imgoing.api.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.imgoing.api.config.BaseTime;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "plan", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Plantask> plantasks = new ArrayList<>();

    @Column
    private Boolean isUserLate;

    @Column
    private LocalDateTime actualArrivalAt;

    public void modify(Plan newPlan) {
        this.name = newPlan.getName();
        this.departureName = newPlan.getDepartureName();
        this.departureLat = newPlan.getDepartureLat();
        this.departureLng = newPlan.getDepartureLng();
        this.arrivalName = newPlan.getArrivalName();
        this.arrivalLat = newPlan.getArrivalLat();
        this.arrivalLng = newPlan.getArrivalLng();
        this.arrivalAt = newPlan.getArrivalAt();
        this.memo = newPlan.getMemo();
        this.belongings = newPlan.getBelongings();
    }

    public void recordArrivalOfAppointment(LocalDateTime actualArrivalAt) {
        this.isUserLate = actualArrivalAt.isAfter(this.getArrivalAt());
        this.actualArrivalAt = actualArrivalAt;
    }
}