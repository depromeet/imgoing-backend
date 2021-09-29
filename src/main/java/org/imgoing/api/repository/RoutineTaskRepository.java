package org.imgoing.api.repository;

import org.imgoing.api.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineTaskRepository  extends JpaRepository<Routine, Long> {
}
