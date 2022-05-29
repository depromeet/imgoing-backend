package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.Plantask;
import org.imgoing.api.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantaskRepository extends JpaRepository<Plantask, Long> {
    void deleteAllByPlan(Plan plan);

    void deleteAllByPlanId(Long planId);

    Plantask findOneByPlanIdAndTaskId(Long planId, Long taskId);

    List<Plantask> getByPlanIdOrderBySequenceAsc(Long planId);
}
