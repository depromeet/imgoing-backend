package org.imgoing.api.mapper;

import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.dto.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    TaskDto.Read toDto(Task task);

    Task toEntity(TaskDto dto);
}
