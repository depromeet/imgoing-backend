package org.imgoing.api.domain.routine_task;

import org.imgoing.api.domain.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineTaskRepository  extends JpaRepository<Routine, Long> {
}
