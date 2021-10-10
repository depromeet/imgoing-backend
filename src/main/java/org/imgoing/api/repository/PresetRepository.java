package org.imgoing.api.repository;

import org.imgoing.api.domain.entity.Preset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PresetRepository extends JpaRepository<Preset, Long> {
    @Query(value = "SELECT * FROM preset_tb WHERE preset_tb.user_id = :userId", nativeQuery = true)
    List<Preset> findAllByUserId(Long userId);
}
