package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.domain.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByUserIdAndArrivalAtGreaterThanEqualOrderByArrivalAtAsc(Long userId, LocalDateTime now);

    Optional<Plan> findTopByUserAndArrivalAtGreaterThanOrderByArrivalAtAsc (User user, LocalDateTime now);
}
