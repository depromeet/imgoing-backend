package org.imgoing.api.mapper;

import org.imgoing.api.dto.TaskDto;
import org.imgoing.api.domain.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    TaskDto toDto(Task task);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Task toEntity(TaskDto dto);
}
