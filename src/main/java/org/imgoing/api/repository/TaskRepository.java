package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT t FROM Task t WHERE t.user.id = :userId")
    List<Task> findAllByUserId(Long userId);

    List<Task> findAllByIdIn(List<Long> taskIds);
}