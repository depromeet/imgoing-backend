package org.imgoing.api.mapper;

import org.imgoing.api.domain.entity.Plantask;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.dto.plantask.PlantaskRead;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { RoutineMapper.class, TaskMapper.class })

public interface PlantaskMapper {
    @Mapping(target = "id", ignore = true)
    Plantask toEntityForPost(Long routineId, List<Task> taskList); // routineId는 더미 데이터

    @Mapping(target = "id", source = "routineId")
    Plantask toEntityForPut(PlantaskRead routineDto, Long routineId);
}