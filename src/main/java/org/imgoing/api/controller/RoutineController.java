package org.imgoing.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.imgoing.api.dto.RoutineDto;
import org.imgoing.api.domain.entity.Routine;
import org.imgoing.api.domain.entity.Subtask;
import org.imgoing.api.mapper.RoutineMapper;
import org.imgoing.api.service.RoutineService;
import org.imgoing.api.support.ImgoingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = "루틴 관련 API")
@RequestMapping("/api/v1/routines")
public class RoutineController {
    private final RoutineService routineService;
    private final RoutineMapper routineMapper;

    @ApiOperation(value = "루틴 생성")
    @PostMapping
    public ImgoingResponse<RoutineDto.Read> create(@RequestBody RoutineDto.Create dto) {
        List<Subtask> subtaskList = dto.getSubtaskIdList()
                .stream()
                .map(subtaskId -> Subtask.builder().id(subtaskId).build())
                .collect(Collectors.toList());

        Routine newRoutine = routineMapper.toEntityForPost(0L, subtaskList);

        return new ImgoingResponse<>(routineMapper.toDto(routineService.create(newRoutine)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "루틴 조회")
    @GetMapping("/{routineId}")
    public ImgoingResponse<RoutineDto.Read> getById(@ApiParam(value = "루틴 id", required = true, example = "1")
                                             @PathVariable(value = "routineId") Long id) {
        Routine routine = routineService.getById(id);

        return new ImgoingResponse<>(routineMapper.toDto(routine));
    }

    @ApiOperation(value = "루틴 리스트 전체 조회")
    @GetMapping("/all")
    public ImgoingResponse<List<RoutineDto.Read>> getListAll() {
        List<RoutineDto.Read> routineDtoList = routineService.getListAll().stream()
                .map(routineMapper::toDto)
                .collect(Collectors.toList());

        return new ImgoingResponse<>(routineDtoList);
    }

    @ApiOperation(value = "루틴 수정")
    @PutMapping("/{routineId}")
    public ImgoingResponse<RoutineDto.Read> update(@RequestBody RoutineDto.Read routineDto,
                                                   @ApiParam(value = "루틴 id", required = true, example = "1")
                                                   @PathVariable(value = "routineId") Long id) {
        Routine newRoutine = routineMapper.toEntityForPut(routineDto, id);

        return new ImgoingResponse<>(routineMapper.toDto(routineService.update(newRoutine)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "루틴 삭제")
    @DeleteMapping("/{routineId}")
    public ImgoingResponse<String> delete(@ApiParam(value = "루틴 id", required = true, example = "1")
                                              @PathVariable(value = "routineId") Long id) {
        Routine routine = routineService.getById(id);

        routineService.delete(routine);
        String responseMessage = "id = " + id + "루틴이 삭제되었습니다.";
        return new ImgoingResponse<>(HttpStatus.NO_CONTENT, responseMessage);
    }
}
