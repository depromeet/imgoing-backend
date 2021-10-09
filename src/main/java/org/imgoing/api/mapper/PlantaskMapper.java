package org.imgoing.api.mapper;

import org.imgoing.api.dto.PlantaskDto;
import org.imgoing.api.domain.entity.Plantask;
import org.imgoing.api.domain.entity.Subtask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { PresetMapper.class, SubtaskMapper.class })

public interface PlantaskMapper {
    PlantaskDto.Read toDto(Plantask plantask);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    Plantask toEntityForPost(Long routineId, List<Subtask> subtaskList); // routineId는 더미 데이터

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "id", source = "routineId")
    Plantask toEntityForPut(PlantaskDto.Read routineDto, Long routineId);
}
