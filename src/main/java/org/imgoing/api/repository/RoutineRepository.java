package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    @Query("SELECT r FROM Routine r WHERE r.user.id = :userId")
    List<Routine> findAllByUserId(Long userId);
}