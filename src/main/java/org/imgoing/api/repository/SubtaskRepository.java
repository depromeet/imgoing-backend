package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    @Query(value = "SELECT * FROM task_tb WHERE task_tb.user_id = :userId", nativeQuery = true)
    List<Subtask> findAllByUserId(Long userId);
}