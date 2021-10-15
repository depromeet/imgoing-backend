package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> getByArrivalAtLessThanOrderByArrivalAtAsc (LocalDateTime now);
}
