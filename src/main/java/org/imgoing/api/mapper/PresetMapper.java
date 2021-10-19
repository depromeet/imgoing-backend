package org.imgoing.api.mapper;

<<<<<<< HEAD:src/main/java/org/imgoing/api/mapper/PresetMapper.java
import org.imgoing.api.dto.PresetDto;
import org.imgoing.api.domain.entity.Preset;
=======
import org.imgoing.api.domain.entity.Routine;
import org.imgoing.api.domain.entity.Task;
import org.imgoing.api.dto.RoutineDto;
>>>>>>> 94c44d1 (Fix : 루틴 생성 및 조회 Dto 분리, 속성명 변경):src/main/java/org/imgoing/api/mapper/RoutineMapper.java
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", // 스프링 컨테이너에 객체로 관리
        unmappedTargetPolicy = ReportingPolicy.IGNORE) // 일치하지 않는 필드를 무시

<<<<<<< HEAD:src/main/java/org/imgoing/api/mapper/PresetMapper.java
public interface PresetMapper {
    PresetDto toDto(Preset preset);

    Preset toEntity(PresetDto dto);
=======
public interface RoutineMapper {
    RoutineDto.Read toDto(Routine routine);

    Routine toEntity(RoutineDto dto);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    Routine toEntityForPost(RoutineDto.Create dto, List<Task> tasks);
>>>>>>> 94c44d1 (Fix : 루틴 생성 및 조회 Dto 분리, 속성명 변경):src/main/java/org/imgoing/api/mapper/RoutineMapper.java
}
