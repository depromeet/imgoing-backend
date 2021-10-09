package org.imgoing.api.mapper;

import org.imgoing.api.dto.SubtaskDto;
import org.imgoing.api.domain.entity.Subtask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubtaskMapper {
    SubtaskDto toDto(Subtask subtask);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Subtask toEntity(SubtaskDto dto);
}
