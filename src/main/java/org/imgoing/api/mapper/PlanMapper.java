package org.imgoing.api.mapper;

import org.imgoing.api.dto.PlanDto;
import org.imgoing.api.domain.entity.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanMapper {
    PlanDto toDto(Plan plan);

    Plan toEntity(PlanDto planDto);
}
