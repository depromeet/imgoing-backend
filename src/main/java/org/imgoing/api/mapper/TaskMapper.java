package org.imgoing.api.mapper;

import org.imgoing.api.dto.RoutineDto;
import org.imgoing.api.dto.TaskDto;
import org.imgoing.api.entity.Routine;
import org.imgoing.api.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    @Mapping(target = "id", expression = "java(task.getId())")
    @Mapping(target = "name", expression = "java(task.getName())")
    @Mapping(target = "time", expression = "java(task.getTime())")
    TaskDto toDto(Task task);

    Task toEntity(TaskDto dto);
}
