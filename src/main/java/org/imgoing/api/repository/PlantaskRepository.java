package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.domain.entity.Plantask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantaskRepository extends JpaRepository<Plantask, Long> {
    Long deleteByPlanId(Long planId);
}
