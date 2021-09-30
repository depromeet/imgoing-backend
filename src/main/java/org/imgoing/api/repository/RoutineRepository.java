package org.imgoing.api.repository;

import org.imgoing.api.entity.Routine;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    @Query(value = "SELECT * FROM routine_tb WHERE routine_tb.post_idx = :userId", nativeQuery = true)
    List<Routine> findAllByUserId(Long userId);
}
