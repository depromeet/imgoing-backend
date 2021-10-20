package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    @Query(value = "SELECT * FROM plan_tb WHERE plan_tb.user_id = :userId", nativeQuery = true)
    List<Task> findAllByUserId(Long userId);
}