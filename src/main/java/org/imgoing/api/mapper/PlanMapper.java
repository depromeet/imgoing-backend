package org.imgoing.api.mapper;

import org.imgoing.api.dto.PlanDto;
import org.imgoing.api.domain.entity.Plan;
import org.imgoing.api.dto.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanMapper {
    PlanDto toDto(Plan plan, List<TaskDto> taskDtos);

    Plan toEntity(PlanDto planDto);
}
