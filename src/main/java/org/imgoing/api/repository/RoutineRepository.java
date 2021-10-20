package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    @Query(value = "SELECT * FROM routine_tb WHERE routine_tb.user_id = :userId", nativeQuery = true)
    List<Routine> findAllByUserId(Long userId);
}