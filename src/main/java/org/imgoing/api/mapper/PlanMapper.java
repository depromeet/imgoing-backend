package org.imgoing.api.mapper;

import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.PlanDto;
import org.imgoing.api.domain.entity.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanMapper {
    @Mapping(source = "tasks", target = "task")
    PlanDto toDto(Plan plan, List<Task> tasks);

    Plan toEntity(PlanDto planDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Plan toEntityForSave(User user, PlanDto.Create dto);
}
