package org.imgoing.api.mapper;

import org.imgoing.api.domain.entity.Routine;
import org.imgoing.api.domain.entity.Routinetask;
import org.imgoing.api.domain.entity.User;
import org.imgoing.api.dto.RoutineDto;
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
    Routine toEntityForPost(User user, RoutineDto.Create dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Routine toEntityForPut(User user, RoutineDto.Update dto);

    @Mapping(target = "routinetasks", source = "routinetasks")
    RoutineDto.Read toDto(Routine routine, List<Routinetask> routinetasks);

    RoutineDto.Read toDto(Routine routine);
}
