package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByUserIdAndArrivalAtGreaterThanEqualOrderByArrivalAtAsc(Long userId, LocalDateTime now);

    Optional<Plan> findTopByUserAndArrivalAtGreaterThanOrderByArrivalAtAsc (User user, LocalDateTime now);

    List<Plan> findByUserAndArrivalAtGreaterThanEqualOrderByArrivalAtAsc(User user, LocalDateTime date);

    List<Plan> findAllByUserAndActualArrivalAtGreaterThanEqual(User user, LocalDateTime date);
}
