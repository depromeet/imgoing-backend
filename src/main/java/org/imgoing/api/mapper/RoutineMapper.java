package org.imgoing.api.mapper;

import org.imgoing.api.domain.entity.Routine;
import org.imgoing.api.domain.entity.Routinetask;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.routine.RoutineRequest;
import org.imgoing.api.dto.routine.RoutineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { RoutinetaskMapper.class }) // 일치하지 않는 필드를 무시

public interface RoutineMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "routinetasks", ignore = true)
    Routine toEntity(User user, RoutineRequest routineCreateRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "routinetasks", ignore = true)
    Routine toEntity(Long id, User user, RoutineRequest routineCreateRequest);

    @Mapping(target = "routinetasks", source = "routinetasks")
    RoutineResponse toDto(Routine routine, List<Routinetask> routinetasks);

    RoutineResponse toDto(Routine routine);
}