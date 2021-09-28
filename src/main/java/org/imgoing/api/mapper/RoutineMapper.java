package org.imgoing.api.mapper;

import org.imgoing.api.dto.RoutineDto;
import org.imgoing.api.entity.Routine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", // 스프링 컨테이너에 객체로 관리
        unmappedTargetPolicy = ReportingPolicy.IGNORE) // 일치하지 않는 필드를 무시

public interface RoutineMapper {
    @Mapping(target = "id", expression = "java(routine.getId())")
    @Mapping(target = "name", expression = "java(routine.getName())")
    RoutineDto toDto(Routine routine);

    Routine toEntity(RoutineDto dto);
}
