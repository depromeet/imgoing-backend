package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT * FROM routine_tb WHERE routine_tb.post_idx = :userId", nativeQuery = true)
    List<Task> findAllByUserId(Long userId);
}
