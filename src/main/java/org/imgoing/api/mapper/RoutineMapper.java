package org.imgoing.api.mapper;

import org.imgoing.api.domain.entity.Routine;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.dto.RoutineDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", // 스프링 컨테이너에 객체로 관리
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { TaskMapper.class }) // 일치하지 않는 필드를 무시

public interface RoutineMapper {
    Routine toEntityForPut(RoutineDto.Update dto);

    RoutineDto.Read toDto(Routine routine, List<Task> tasks);

    RoutineDto.Read toDto(Routine routine);
}
