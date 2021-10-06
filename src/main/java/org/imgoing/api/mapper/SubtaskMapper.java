package org.imgoing.api.mapper;

import org.imgoing.api.dto.SubtaskDto;
import org.imgoing.api.entity.Subtask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubtaskMapper {
    SubtaskDto toDto(Subtask subtask);

    Subtask toEntity(SubtaskDto dto);
}
