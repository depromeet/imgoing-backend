package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
}
