package org.imgoing.api.mapper;

import org.imgoing.api.dto.SubtaskDto;
import org.imgoing.api.entity.Subtask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubtaskMapper {
    @Mapping(target = "id", expression = "java(subtask.getId())")
    @Mapping(target = "name", expression = "java(subtask.getName())")
    @Mapping(target = "time", expression = "java(subtask.getTime())")
    SubtaskDto toDto(Subtask subtask);

    @Mapping(target = "name", expression = "java(dto.getName())")
    @Mapping(target = "time", expression = "java(dto.getTime())")
    Subtask toEntity(SubtaskDto dto);
}
