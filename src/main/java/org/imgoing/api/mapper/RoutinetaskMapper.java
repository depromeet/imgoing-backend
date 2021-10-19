package org.imgoing.api.mapper;

import org.imgoing.api.domain.entity.Routinetask;
import org.imgoing.api.dto.RoutinetaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", // 스프링 컨테이너에 객체로 관리
        unmappedTargetPolicy = ReportingPolicy.IGNORE) // 일치하지 않는 필드를 무시

public interface RoutinetaskMapper {
    RoutinetaskDto toDto(Routinetask routinetask);
}