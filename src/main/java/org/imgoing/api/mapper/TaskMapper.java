package org.imgoing.api.mapper;

import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.task.TaskRequest;
import org.imgoing.api.dto.task.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "routinetasks", ignore = true)
    Task toEntity(User user, TaskRequest taskRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "routinetasks", ignore = true)
    Task toEntity(Long id, User user, TaskRequest taskRequest);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "routinetasks", ignore = true)
    Task toEntity(TaskDto taskDto);

    TaskDto toDto(Task task);
}
