package org.imgoing.api.mapper;

import org.imgoing.api.dto.RoutineDto;
import org.imgoing.api.entity.Routine;
import org.imgoing.api.entity.Subtask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { TaskMapper.class, SubtaskMapper.class })

public interface RoutineMapper {
    RoutineDto.Read toDto(Routine routine);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    Routine toEntityForPost(Long routineId, List<Subtask> subtaskList); // routineId는 더미 데이터

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "id", source = "routineId")
    Routine toEntityForPut(RoutineDto.Read routineDto, Long routineId);
}
