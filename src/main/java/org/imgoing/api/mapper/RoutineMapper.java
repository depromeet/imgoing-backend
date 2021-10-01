package org.imgoing.api.mapper;

import org.imgoing.api.dto.RoutineDto;
import org.imgoing.api.entity.Routine;
import org.imgoing.api.entity.Task;
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

    @Mapping(target = "id", ignore = true)
    Routine toEntityForPost(Task task, List<Subtask> subtaskList);
}
