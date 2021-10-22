package org.imgoing.api.mapper;

import org.imgoing.api.domain.entity.Routine;
import org.imgoing.api.domain.entity.Routinetask;
import org.imgoing.api.dto.RoutineDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", // 스프링 컨테이너에 객체로 관리
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { RoutinetaskMapper.class }) // 일치하지 않는 필드를 무시

public interface RoutineMapper {
    Routine toEntityForPut(RoutineDto.Update dto);

    @Mapping(target = "id", ignore = true)
    Routine toEntityForPost(RoutineDto.Create dto);

    RoutineDto.Read toDto(Routine routine, List<Routinetask> routinetasks);

    RoutineDto.Read toDto(Routine routine);
}
